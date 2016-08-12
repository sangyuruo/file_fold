package com.billionav.DRIR.jni;

public class jniDRIR_DRVEventMsgBox {
	
	public static native float	GetForwardG1();
	public static native float	GetForwardG2();
	public static native float	GetCrosswiseG1();
	public static native float	GetCrosswiseG2();
	public static native float	GetHoriG();
	public static native int	GetIsStill();
	public static native float	GetSpeed();
	public static native float	GetDistance();
	public static native float	GetFact();
}

