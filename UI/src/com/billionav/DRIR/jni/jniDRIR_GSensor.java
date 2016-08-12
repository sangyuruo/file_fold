package com.billionav.DRIR.jni;

public class jniDRIR_GSensor {
	public static native void setGSLevelFromMemory();
	public static native void setGSLevelFromXML(int iIndex, int iN1, int iN2, double dA, double dB);
}
