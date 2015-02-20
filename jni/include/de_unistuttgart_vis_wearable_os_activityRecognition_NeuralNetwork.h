/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork */

#ifndef _Included_de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork
#define _Included_de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork
 * Method:    j_new_neural_net
 * Signature: ([II)I
 */
JNIEXPORT jint JNICALL Java_de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork_j_1new_1neural_1net
  (JNIEnv *, jobject, jintArray, jint);

/*
 * Class:     de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork
 * Method:    j_new_neural_net_from_file
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork_j_1new_1neural_1net_1from_1file
  (JNIEnv *, jobject, jstring);

/*
 * Class:     de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork
 * Method:    j_delete_neural_net
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork_j_1delete_1neural_1net
  (JNIEnv *, jobject);

/*
 * Class:     de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork
 * Method:    j_neural_net_feed_forward
 * Signature: ([DI)V
 */
JNIEXPORT void JNICALL Java_de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork_j_1neural_1net_1feed_1forward
  (JNIEnv *, jobject, jdoubleArray, jint);

/*
 * Class:     de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork
 * Method:    j_neural_net_back_prop
 * Signature: ([DI)V
 */
JNIEXPORT void JNICALL Java_de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork_j_1neural_1net_1back_1prop
  (JNIEnv *, jobject, jdoubleArray, jint);

/*
 * Class:     de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork
 * Method:    j_neural_net_get_results
 * Signature: ([DI)V
 */
JNIEXPORT void JNICALL Java_de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork_j_1neural_1net_1get_1results
  (JNIEnv *, jobject, jdoubleArray, jint);

/*
 * Class:     de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork
 * Method:    j_neural_net_get_recent_average_error
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork_j_1neural_1net_1get_1recent_1average_1error
  (JNIEnv *, jobject);

/*
 * Class:     de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork
 * Method:    j_neural_net_save
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_de_unistuttgart_vis_wearable_os_activityRecognition_NeuralNetwork_j_1neural_1net_1save
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif
