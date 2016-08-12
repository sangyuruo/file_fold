package com.billionav.jni;

public class NaviMainJNI {
	public static native void Initialize();
	public static native int DoMain();
	public static native void UnloadNaviMain();
	
	public static void LoadLibrary(){

		try {
			System.loadLibrary("gnustl_shared");
			System.loadLibrary("crypto-navi");
			System.loadLibrary("ssl-navi");
			System.loadLibrary("commonlib-navi");
			System.loadLibrary("aplcommon-navi");
			System.loadLibrary("protobuf-navi");
			System.loadLibrary("sqlite3-navi");
			System.loadLibrary("tiny-navi");
			System.loadLibrary("zeromq-navi");
			System.loadLibrary("naviprotosrc-navi"); 
			System.loadLibrary("protobufhelp-navi"); 
			System.loadLibrary("eventsys-navi"); 
			System.loadLibrary("w3lib-navi");
			System.loadLibrary("Guide-navi");
			System.loadLibrary("mapengine-navi");
			System.loadLibrary("PathLib-navi");
			System.loadLibrary("handler-navi");
//			System.loadLibrary("Location-navi-jp");
			System.loadLibrary("Location-navi-us");
//			System.loadLibrary("Location-navi-cn");
			System.loadLibrary("uuid-navi");
			System.loadLibrary("navicommon-navi");
			System.loadLibrary("7z-navi");
			System.loadLibrary("icuuc-navi");
			System.loadLibrary("Search-navi");
			System.loadLibrary("Point-navi");
			System.loadLibrary("Traffic-navi");
			System.loadLibrary("Sns-navi");
			System.loadLibrary("navimain-navi");
			System.loadLibrary("UserInterface-navi");
			System.loadLibrary("tremolo-navi");
			System.loadLibrary("voicecomm-navi");
			System.loadLibrary("voiceplay-navi");
//			System.loadLibrary("voicerecog-navi");
			System.loadLibrary("developdebug-navi");			
//			System.loadLibrary("Hud-navi");
//			System.loadLibrary("drirdataserver-navi");
			System.loadLibrary("ffmpeg"); 
//			System.loadLibrary("UserReport-navi");
			System.loadLibrary("jni-navi");
//			if(EnvironmentJNI.IsCpuSupportNeon()){
//				System.loadLibrary("drirmainneon-navi");
//			}else {
//				System.loadLibrary("drirmain-navi");
//			}
		}
		catch(java.lang.UnsatisfiedLinkError  e) {
			System.out.println("load library failed.");
			System.out.println(e.toString());
			System.out.println("-----------------------------.");
		}
	}
}
