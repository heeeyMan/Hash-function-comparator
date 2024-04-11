//
// Created by alexander on 4/11/24.
//

#ifndef HFC_CONVERTER_H
#define HFC_CONVERTER_H

#include <jni.h>
#include <string>

class Converter {
public:
    Converter();
    std::string convertJStringToString(JNIEnv *env, jstring jStr);
};

#endif //HFC_CONVERTER_H
