/**
 * 
 */
package com.billionav.navi.download;

public interface DownloadTaskListener {
	
	public void autoCallback( String packagePath, long receiveCount, int status);
}
