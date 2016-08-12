LOCAL_PATH 		:= $(call my-dir)

include $(CLEAR_VARS)

TARGET_ABI		:= android-5-armeabi

LOCAL_LDLIBS 	:= -llog
LOCAL_MODULE    := supsys
LOCAL_SRC_FILES := jniCErrorRegister.cpp

include $(BUILD_SHARED_LIBRARY)

