cmake_minimum_required(VERSION 3.6.0)

add_library (
        native-lib
        SHARED
        native-lib.cpp
)

find_library(
        log-lib
        log
)

add_library(ssl STATIC IMPORTED)
set_target_properties(ssl
        PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libssl.a)

target_link_libraries(
        native-lib
        ${log-lib}
)