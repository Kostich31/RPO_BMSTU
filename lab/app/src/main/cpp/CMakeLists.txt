cmake_minimum_required(VERSION 3.18.1)

project("lab")

add_library( # Sets the name of the library.
        native-lib
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        native-lib.cpp)

find_library(log-lib log)

add_library(spdlog STATIC IMPORTED)
set_target_properties(spdlog PROPERTIES IMPORTED_LOCATION
        /home/konstantin/AndroidStudioProjects/RPO_BMSU/libs/spdlog/build/arm64-v8a/libspdlog.a)

add_library(mbedcrypto STATIC IMPORTED)
set_target_properties(mbedcrypto PROPERTIES IMPORTED_LOCATION
        /home/konstantin/AndroidStudioProjects/RPO_BMSU/libs/mbedtls/build/arm64-v8a/library/libmbedcrypto.so)

target_include_directories(native-lib PUBLIC
        /home/konstantin/AndroidStudioProjects/RPO_BMSU/libs/spdlog/spdlog/include
        /home/konstantin/AndroidStudioProjects/RPO_BMSU/libs/mbedtls/mbedtls/include
        )
target_link_libraries( # Specifies the target library.
        native-lib
        spdlog
        mbedcrypto
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )