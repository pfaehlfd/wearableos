/*
 * This file is part of the Garment OS Project. For any details concerning use
 * of this project in source or binary form please refer to the provided license
 * file.
 *
 * (c) 2014-2015 Garment OS
 */
package de.unistuttgart.vis.wearable.os.cloud;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


import de.unistuttgart.vis.wearable.os.properties.Properties;
import de.unistuttgart.vis.wearable.os.utils.Utils;

/**
 * This class is used to create an archive file containing selected sensor data files or
 * the storage file which itself contains information about the sensors or the privacy settings
 * and user applications
 *
 * @author roehrdor
 */
public class Archiver {

    /**
     * Private constructor, shall never be used
     */
    private Archiver() {
        throw new IllegalAccessError("This constructor shall not be used");
    }

    /**
     * Return all files in the given directory and in sub directory
     *
     * @param dir the root directory where to start searching
     * @return a list of files contained in the directory or sub directory
     */
    protected static List<File> getFilesInDirectory(File dir) {
        List<File> files = new ArrayList<File>();
        List<File> queue = new ArrayList<File>();
        queue.add(dir);
        while(!queue.isEmpty()) {
            File current = queue.remove(0);
            File[] filesInCurrent = current.listFiles();
            for(File f : filesInCurrent) {
                if(f.isDirectory())
                    queue.add(f);
                else
                    files.add(f);
            }
        }
        return files;
    }

    /**
     * Create a compressed archive file containing all sensor data and the storage file containing
     * the privacy information and sensor settings.

     * @param outputFile the output file to store the files in
     */
    public static void createArchiveFile(File outputFile) {
        //
        // Wait until no file in the directory is in use and lock these files
        //
        Properties.FILE_STATUS_FIELDS_LOCK.lock();
        Properties.FILE_ARCHIVING.set(true);
        while(Properties.FILES_IN_USE.get() != 0) {
            Utils.sleepUninterrupted(200);
        }
        Properties.FILE_STATUS_FIELDS_LOCK.unlock();

        try {
            // Get all files in the directory and in its sub directories
            List<File> files = getFilesInDirectory(Properties.storageDirectory);

            final int BUFFER_SIZE = 1024;
            byte[] bytes = new byte[BUFFER_SIZE];
            int length;

            // Create a new file output stream and zip output stream to save the files to a zip file
            FileOutputStream fos = new FileOutputStream(outputFile);
            FileInputStream fis;
            ZipOutputStream zos = new ZipOutputStream(fos);

            // For each file we got in the file list
            for (File f : files) {

                // Check whether the file is a directory
                if (!f.isDirectory()) {
                    // and only if not add the file to the zip file
                    // Therefore open the file
                    fis = new FileInputStream(f);

                    // Make the path to the file relative
                    String zippedName = f.getCanonicalPath().substring(Properties.storageDirectory.getCanonicalPath().length() + 1, f.getCanonicalPath().length());

                    // Create a new zip entry with the relative name
                    ZipEntry zipEntry = new ZipEntry(zippedName);

                    // and put it as next entry to the zip file
                    zos.putNextEntry(zipEntry);

                    // copy the file to the zip output stream
                    while((length = fis.read(bytes, 0, BUFFER_SIZE)) >= 0)
                        zos.write(bytes, 0, length);

                    // close the current entry and the now added file
                    zos.closeEntry();
                    fis.close();
                }
            }

            // In the end close the zip file and the according output stream
            zos.finish();
            zos.close();
            fos.close();
        } catch (IOException ioe) {
            Log.e("GarmentOS", "Could not create compressed archive");
            Log.e("orDEBUG", ioe.getMessage());
            Log.e("orDEBUG", ioe.getLocalizedMessage());
        }

        //
        // Unlock the files in the directory again
        //
        Properties.FILE_ARCHIVING.set(false);
    }


    /**
     * Create a compressed and encrypted archive file containing all sensor data and the storage
     * file containing the privacy information and sensor settings. This function call is actually
     * the same as first calling {@link de.unistuttgart.vis.wearable.os.cloud.Archiver#createArchiveFile(java.io.File)}
     * and afterwards calling the {@link de.unistuttgart.vis.wearable.os.cloud.Archiver#encryptFile(java.io.File, String)}
     * method.
     *
     * @param key        the key to encrypt the file with
     * @param outputFile the output file to store the files in
     */
    public static void createEncryptedArchiveFile(String key, File outputFile) {
        createArchiveFile(outputFile);
        encryptFile(outputFile, key);
    }

    //
    // Arbitrarily selected 8-byte salt sequence:
    //
    private static final byte[] salt = { (byte) 0x43, (byte) 0x76, (byte) 0x95,
            (byte) 0xc7, (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17 };

    /**
     * Create a cipher object based on the given password and the decryption
     * mode
     *
     * @param pass
     *            the password to use for de/encryption
     * @param decryptMode
     *            the decrypt mode
     * @return the cipher object
     */
    private static Cipher makeCipher(String pass, Boolean decryptMode) {
        Cipher cipher = null;
        try {
            // Use a KeyFactory to derive the corresponding key from the passphrase:
            PBEKeySpec keySpec = new PBEKeySpec(pass.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            // Create parameters from the salt and an arbitrary number of
            // iterations:
            PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);

            // Set up the cipher:
            cipher = Cipher.getInstance("PBEWithMD5AndDES");

            // Set the cipher mode to decryption or encryption:
            if (decryptMode) {
                cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);
            }
        } catch(GeneralSecurityException gse) {
        }
        return cipher;
    }

    /**
     * Encrypt the given file with the given password
     *
     * @param fileName
     *            the file to encrypt
     * @param pass
     *            the password to use for encryption
     */
    public static void encryptFile(File fileName, String pass) {
        byte[] decData;
        byte[] encData;
        File inFile = fileName;
        try {
            // Generate the cipher using pass:
            Cipher cipher;
            cipher = makeCipher(pass, true);

            // Read in the file:
            FileInputStream inStream = new FileInputStream(inFile);

            int blockSize = 8;
            // Figure out how many bytes are padded
            int paddedCount = blockSize - ((int) inFile.length() % blockSize);

            // Figure out full size including padding
            int padded = (int) inFile.length() + paddedCount;

            decData = new byte[padded];

            inStream.read(decData);

            inStream.close();

            // Write out padding bytes as per PKCS5 algorithm
            for (int i = (int) inFile.length(); i < padded; ++i) {
                decData[i] = (byte) paddedCount;
            }

            // Encrypt the file data:
            encData = cipher.doFinal(decData);

            // Write the encrypted data to a new file:
            FileOutputStream outStream = new FileOutputStream(fileName);
            outStream.write(encData);
            outStream.close();
        } catch (GeneralSecurityException gse) {
        } catch (IOException e) {}
    }

    /**
     * Decrypt the given file with the given password
     *
     * @param fileName
     *            the file to be decrypted
     * @param pass
     *            the password to use for decryption
     */
    public static void decryptFile(File fileName, String pass) {
        byte[] encData;
        byte[] decData;
        File inFile = fileName;
        try {
            // Generate the cipher using pass:
            Cipher cipher = makeCipher(pass, false);

            // Read in the file:
            FileInputStream inStream = new FileInputStream(inFile);
            encData = new byte[(int) inFile.length()];
            inStream.read(encData);
            inStream.close();
            // Decrypt the file data:
            decData = cipher.doFinal(encData);

            // Figure out how much padding to remove

            int padCount = (int) decData[decData.length - 1];

            // Naive check, will fail if plaintext file actually contained
            // this at the end
            // For robust check, check that padCount bytes at the end have same
            // value
            if (padCount >= 1 && padCount <= 8) {
                decData = Arrays.copyOfRange(decData, 0, decData.length - padCount);
            }

            // Write the decrypted data to a new file:
            FileOutputStream target = new FileOutputStream(fileName);
            target.write(decData);
            target.close();
        } catch (GeneralSecurityException gse) {
        } catch (IOException e) {
        }
    }
}
