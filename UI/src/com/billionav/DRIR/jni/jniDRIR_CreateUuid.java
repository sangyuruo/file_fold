package com.billionav.DRIR.jni;
import java.util.UUID;

public class jniDRIR_CreateUuid {
	public static native void	J2CAttach();
	public static native void	J2CDetach();
	
	public native static void 	CreateUuid(String uuid, int iSize); 
	
	//called by C++
	public static boolean C2JCallCreateUuid() 
	{
		
		//Log.i("DRIR_SYS_UuidProxy", "enter C2JCallCreateUuid");
		UUID uuid = UUID.randomUUID(); 
		CreateUuid(uuid.toString(), uuid.toString().length());
		return true; 
	}
}