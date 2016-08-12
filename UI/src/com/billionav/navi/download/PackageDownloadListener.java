package com.billionav.navi.download;

import com.billionav.jni.W3JNI;
import com.billionav.navi.net.PNetLog;



public abstract class PackageDownloadListener implements DownloadTaskListener {
	public final static int W3_PD_DOWNLOADING				= 0x0;	
	public final static int W3_PD_COMPLETED				    = 0x1;
	public final static int W3_PD_NETWORK_ERROR				= 0x2;
	public final static int W3_PD_CANCEL     				= 0x3;
	public final static int W3_PD_FILE_IO_ERROR				= 0x4;
	
	
	public static int m_areaCode = 0;
	
	public final void setAreaCode( int areaCode )
	{
		m_areaCode = areaCode;
		
	}	
	
	/**
	 * notify the current off-line package size
	 * @param  areaCode     area code
	 * @param  downloadSize the download size of off-line package
	 * @param  returnCode 0: downloading  1:completed  2: network error		 
	 */
	 abstract protected void onDownloadOfflinePackageSize( String packageName, long downloadSize, int returnCode );
	 
	 public void autoCallback( String packagePath, long downloadSize, int returnCode)
	 {		 
		 String packageName = packagePath.substring( packagePath.lastIndexOf("/") + 1, packagePath.lastIndexOf(".") );
		 String areaCode = packageName.substring( W3JNI.W3_PACKAGE_INFO_MAP, packageName.indexOf("_") );
		 if( returnCode == W3_PD_COMPLETED )
		 {
			 PNetLog.d( "PackageDownloadListener::autoCallback packageName areaCode = " + areaCode );
			 W3JNI.onDownloadOfflinePackageCompleted( W3JNI.W3_PACKAGE_INFO_MAP, Integer.parseInt( areaCode ) );			 
		 }
		 else if( returnCode == W3_PD_NETWORK_ERROR 
				 || returnCode == W3_PD_FILE_IO_ERROR )
		 {
			 W3JNI.waitingForDownloadOfflinePackage( W3JNI.W3_PACKAGE_INFO_MAP, m_areaCode );
		 }	
		 
		 PNetLog.d( "PackageDownloadListener::autoCallback packageName = [" + packageName + "], downloadSize=["+downloadSize + "],returnCode=["+ returnCode +"]\n" );
		 onDownloadOfflinePackageSize( packageName, downloadSize, returnCode );	
	 }	
}

