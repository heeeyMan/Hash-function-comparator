#include <jni.h>
#include <string>
#include <chrono>
#include "Sha256.h"
#include "Converter.h"

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_hfc_service_CalculateTimeCppService_getDataSpeedHashFunctionViaCpp(JNIEnv *env,jobject thiz,jstring message,jint number_iterations) {
    Sha256 *sha = new Sha256();
    std::string data = Converter().convertJStringToString(env, message);
    std::string hashResult = "";
    auto start = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < number_iterations; ++i) {
        sha -> update(data);
        hashResult = Sha256::toString(sha -> digest());
    }
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> duration = end - start;
    if(hashResult.empty()) {
        hashResult = "o";
    }
    double timePerIteration = (duration.count() * 1000000000 )/ number_iterations;
    return timePerIteration;
}