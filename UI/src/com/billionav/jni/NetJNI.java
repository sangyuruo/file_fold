package com.billionav.jni;

import com.billionav.navi.net.PRequest;
import com.billionav.navi.net.PostData;


public class NetJNI {

	public static native void setNativeLog(String log);
	
	public static native void addCommonHttpHeader(String name, String value);

	
}
