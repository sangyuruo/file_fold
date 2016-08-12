package com.billionav.DRIR.jni;

public class jniDRIR_FrameManager {
	public native static void Start();
	public native static void Stop();
	public native static void AddFrameVideoData(byte[] in, long insize);	
	public native static void AddFrameAudioData(byte[] in, long insize);
	public native static void AddFrameOptionData(byte[] in, long insize);
	public native static void SetVideoHeight(int iHeight);
	public native static void SetVideoWidth(int iWidth);
	public native static void SetVideoFrameRate(int iRate);
	public native static void SaveFile();
	
	public native static void SetSpsPps(byte[] SPS, int iSpsLen, byte[] PPS, int iPpsLen);
}
