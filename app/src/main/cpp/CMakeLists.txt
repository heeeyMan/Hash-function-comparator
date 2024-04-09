cmake_minimum_required(VERSION 3.6.0)

add_library (
        native-lib
        SHARED
        native-lib.cpp
        aidl/com/example/IMyService.cpp
)

target_include_directories (
        native-lib
        PRIVATE
        includes
)

target_link_libraries (
        native-lib
        binder_ndk
)