package com.billionav.DRIR.jni;

public class jniDRIR_AudioData {
	public native static void AudioDataInput(byte[] buff, int iDatasz);
	public native static void StopRecord();
}
