/**
 * 
 */
package com.billionav.navi.download;

public interface DownloadRequestListener {	
	public final static int W3_PD_DOWNLOADING				= 0x0;	
	public final static int W3_PD_COMPLETED				    = 0x1;
	public final static int W3_PD_NETWORK_ERROR				= 0x2;
	public final static int W3_PD_CANCEL     				= 0x3;
	public final static int W3_PD_FILE_IO_ERROR				= 0x4;
	
	public void onDownloadData( String filePath, long totalSize, long downloadSize, int returnCode );
}
