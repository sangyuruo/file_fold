package com.billionav.DRIR.jni;

public class jniDRIR_ImgDataServer {
	
	/**
	 * camera data format
	 */
	public static final int IMGDS_YUY2 = 0;
	public static final int IMGDS_NV16 = 1;
	public static final int IMGDS_NV21 = 2;
	public static final int IMGDS_UNKNOWN = 4;
	  
	/**
	 * send camera data to DRIR.
	 * when got the camera data , send it to DRIR by this function.
	 * @param   buff   : the buffer of the camera data
	 * @param   iDatasz: camera data size
	 * @return  void
	 */	
	public native static void CameraDataInput(byte[] buff, int iDatasz);
	
	//this function used to initialize  the ImageDataServer module of IDRD
	//must called before start the IRDR
	/**
	 * initialize  the ImageDataServer module of IDRD.
	 * must called before start the IRDR module.
	 * @param   iwidth   : the buffer of the camera data
	 * @param   iheight  : camera data size
	 * @param   Imageformat: camera data format
	 * 					IMGDS_YUY2, IMGDS_NV16, IMGDS_NV21, or IMGDS_UNKNOWN
	 * @return  long :  
	 * 			SUCCESS : 0  FAILED : not 0	
	 */	
	public native static long SetCameraParameter(int iwidth, int iheight, int iCameraformat);

}
