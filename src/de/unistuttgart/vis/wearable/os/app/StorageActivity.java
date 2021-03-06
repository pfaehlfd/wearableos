package de.unistuttgart.vis.wearable.os.app;

/**
 * Created by Lucas on 09.02.2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import de.unistuttgart.vis.wearable.os.R;
import de.unistuttgart.vis.wearable.os.cloud.dropBox.Dropbox;
import de.unistuttgart.vis.wearable.os.cloud.googleDrive.GoogleDrive;
import de.unistuttgart.vis.wearable.os.cloud.oneDrive.OneDrive;

public class StorageActivity extends Activity {
    private Switch mySwitch;
    private boolean encrypted;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        mySwitch = (Switch) findViewById(R.id.switch_encrypt_data);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AlertDialog.Builder alert = new AlertDialog.Builder(StorageActivity.this);

                    alert.setTitle("Please enter password:");
                    final EditText input = new EditText(StorageActivity.this);
                    encrypted = true;
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            key = input.getText().toString();
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            encrypted =false;
                            buttonView.setChecked(false);
                            return;
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                }
            }
        });
    }

    public void changeToDropBox(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(StorageActivity.this);
        alert.setTitle("Please choose");
        alert.setView(null);

        alert.setPositiveButton("Export", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(getBaseContext(), Dropbox.class);
                intent.putExtra("isExport",true);
                if (encrypted){
                    intent.putExtra("key", key);
                    intent.putExtra("encrypted",encrypted);
                }
                startActivity(intent);
            }
        });

        alert.setNegativeButton("Import", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(getBaseContext(), Dropbox.class);
                intent.putExtra("isExport", false);
                startActivity(intent);
            }
        });
        alert.show();



    }

   public void changeToOneDrive(View view) {
       AlertDialog.Builder alert = new AlertDialog.Builder(StorageActivity.this);
       alert.setTitle("Please choose");
       alert.setView(null);

       alert.setPositiveButton("Export", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int whichButton) {
               Intent intent = new Intent(getBaseContext(), OneDrive.class);
               intent.putExtra("isExport",true);
               if (encrypted){
                   intent.putExtra("key", key);
                   intent.putExtra("encrypted",encrypted);
               }
               startActivity(intent);
           }
       });

       alert.setNegativeButton("Import", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int whichButton) {
               Intent intent = new Intent(getBaseContext(),OneDrive.class);
               intent.putExtra("isExport", false);
               startActivity(intent);
           }
       });
       alert.show();

  }

    public void changeToGoogleDrive(View view) {
    AlertDialog.Builder alert = new AlertDialog.Builder(StorageActivity.this);
    alert.setTitle("Please choose");
    alert.setView(null);

    alert.setPositiveButton("Export", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            Intent intent = new Intent(getBaseContext(), GoogleDrive.class);
            intent.putExtra("isExport",true);
            if (encrypted){
                intent.putExtra("key", key);
                intent.putExtra("encrypted",encrypted);
            }
            startActivity(intent);
        }
    });

    alert.setNegativeButton("Import", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            Intent intent = new Intent(getBaseContext(), GoogleDrive.class);
            intent.putExtra("isExport", false);
            startActivity(intent);
        }
    });
    alert.show();

   }

    public void export(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(StorageActivity.this);
        alert.setTitle("Please choose");
        alert.setView(null);

        alert.setPositiveButton("Export", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(getBaseContext(), ImportExportArchiveActivity.class);
                intent.putExtra("isExport",true);
                if (encrypted){
                    intent.putExtra("key", key);
                    intent.putExtra("encrypted",encrypted);
                }
                startActivity(intent);
            }
        });

        alert.setNegativeButton("Import", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(getBaseContext(), ImportExportArchiveActivity.class);
                intent.putExtra("isExport",false);
                startActivity(intent);
            }
        });
        alert.show();
     }
}
