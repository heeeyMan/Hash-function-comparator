cmake_minimum_required(VERSION 3.6.0)
set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3")
add_library (
        native-lib
        SHARED
        native-lib.cpp
)

add_library (
        sha-lib
        SHARED
        Sha256.cpp
)

add_library (
        converter-lib
        SHARED
        Converter.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        native-lib
        sha-lib
        converter-lib
        ${log-lib}
)