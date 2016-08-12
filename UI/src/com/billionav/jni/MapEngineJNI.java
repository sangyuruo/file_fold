package com.billionav.jni;

public class MapEngineJNI {	
		public static native void initRenderer();
		public static native void setSurfaceSize(int width, int height);
		public static native boolean render(int nativerunmode);
		public static native void releaseRenderResource();
		public static native void ForceUpdate();
		public static native void SetScreenDPI(int iDPIX, int iDPIY);	
}
