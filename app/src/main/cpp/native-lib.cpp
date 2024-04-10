#include <jni.h>
#include <string>
#include <chrono>

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_hfc_service_CalculateTimeCppService_getDataSpeedHashFunctionViaCpp(JNIEnv *env,
                                                                                    jobject thiz,
                                                                                    jstring message,
                                                                                    jint number_iterations) {
    //unsigned char hash[SHA256_DIGEST_LENGTH];
    auto start = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < number_iterations; ++i) {
        //SHA256((const unsigned char*)data.c_str(), data.length(), hash);
    }
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> duration = end - start;
    double timePerIteration = (duration.count() * 1000000000 )/ number_iterations;
    return timePerIteration;
}