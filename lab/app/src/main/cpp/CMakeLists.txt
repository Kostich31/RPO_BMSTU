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
        /home/konstantin/AndroidStudioProjects/libs/spdlog/build/${ANDROID_ABI}/libspdlog.a)

add_library(mbedcrypto SHARED IMPORTED)
set_target_properties(mbedcrypto PROPERTIES IMPORTED_LOCATION
        /home/konstantin/AndroidStudioProjects/libs/mbedtls/build/${ANDROID_ABI}/library/libmbedcrypto.so)

target_include_directories(native-lib PUBLIC
        /home/konstantin/AndroidStudioProjects/libs/spdlog/spdlog/include
        /home/konstantin/AndroidStudioProjects/libs/mbedtls/mbedtls/include
        )
target_link_libraries( # Specifies the target library.
        native-lib
        spdlog
        mbedcrypto
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )