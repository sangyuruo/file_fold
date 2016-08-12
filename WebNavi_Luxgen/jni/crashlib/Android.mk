LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

TARGET_ABI	:= android-5-armeabi

LOCAL_LDLIBS 	:= -llog
LOCAL_MODULE    := crash
LOCAL_SRC_FILES := CrashMaker.cpp

include $(BUILD_SHARED_LIBRARY)

