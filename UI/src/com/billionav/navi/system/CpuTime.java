package com.billionav.navi.system;

import android.os.Debug;

public class CpuTime {
	
private static long lThreadTimeS,lThreadTimeE;
private static long lTickTimeS,lTickTimeE;

	
	static public void start()
	{
		lThreadTimeS = Debug.threadCpuTimeNanos(); 
    	lTickTimeS = System.currentTimeMillis();
	
	};
	
	static public void stop()
	{
    	lThreadTimeE = Debug.threadCpuTimeNanos();
    	lTickTimeE = System.currentTimeMillis();

       	System.out.println("TreadTime:" + (lThreadTimeE-lThreadTimeS)/1000000 + "[" + lThreadTimeE + "," + lThreadTimeS + "]"
				+ " TickTime:" +(lTickTimeE-lTickTimeS) + "[" + lTickTimeE + "," + lTickTimeS + "]" );

	};
	
	

}
