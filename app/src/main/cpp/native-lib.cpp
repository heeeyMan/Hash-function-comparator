#include <jni.h>
#include <aidl/com/example/hfc/ITimeMeterHashFunctionCppInterface.h>
#include <android/binder_ibinder_jni.h>

std::shared_ptr<ITimeMeterHashFunctionCppInterface> g_spMyService;

class SpAIBinder;

extern "C" JNIEXPORT void JNICALL
Java_com_example_hfc_MainActivity_onServiceConnected(
        JNIEnv* env,
        jobject binder) {
    AIBinder* pBinder = AIBinder_fromJavaBinder(env, binder);
    const ::ndk::SpAIBinder spBinder(pBinder);
    g_spMyService = ITimeMeterHashFunctionCppInterface::fromBinder(spBinder);
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_hfc_MainActivity_onServiceDisconnected(
        JNIEnv* env) {
    g_spMyService = nullptr;
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_hfc_MainActivity_talkToService(
        JNIEnv* env) {
    //ScopedAStatus returnComplexTypeResult = g_spMyService->returnComplexType(2021,65535000, true, 3.14f, 3.141592653589793238"Hello, World!", &returnedComplexObject);
}