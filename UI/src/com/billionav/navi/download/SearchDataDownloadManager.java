package com.billionav.navi.download;

import android.util.Log;

import com.billionav.jni.W3JNI;
import com.billionav.jni.FileSystemJNI;
import com.billionav.navi.net.PNetLog;


class SearchDataDownloadRequestListener implements DownloadRequestListener {
	
	public void onDownloadData( String filePath, long totalSize, long downloadSize, int returnCode )
	{		
	}
}

public class SearchDataDownloadManager {
	public final static int W3_OK                       = 0x0;	
	public final static int W3_GET_FROM_NETWORK         = 0x1;	
	public final static int W3_GET_FROM_DB              = 0x2;
	public final static int W3_NETWORK_ERROR    		= 0x3;
	public final static int W3_PARAMETERS_ERROR    		= 0x4;
	public static int er = 0;
	
	
	/**
	 *  �첽��ȡ�������߰��嵥
	 * @return W3_OK ���ɹ�  else ������
	 */
	public static int predownloadOfflinePackageInfoList()
    {				
		PNetLog.e( "PackageDownloadManager::predownloadOfflinePackageInfoList\n" );
		
		W3JNI.predownloadOfflinePackageInfoList( W3JNI.W3_PACKAGE_INFO_SEARCH );
    	return W3_OK;
    }
	
	public static int getReturnCode()
    {
    	return W3JNI.getReturnCode( W3JNI.W3_PACKAGE_INFO_SEARCH );	
    }
	
	public static int getPackageInfoNumber()
    {
		return W3JNI.getPackageInfoNumber( W3JNI.W3_PACKAGE_INFO_SEARCH );
    }	
	
	/**
	 * get update status of off-line package, return package info list by listener
	 * @param  dataUpdateListener  listener
	 */
	public static boolean getOfflinePackageInfo( int packageInfoIndex, OfflinePackageInfo offlinePackageInfo )
	{			
		
		String PackageInfoString = W3JNI.getOfflinePackageInfoString( W3JNI.W3_PACKAGE_INFO_SEARCH, packageInfoIndex );
		if( PackageInfoString.length() > 0 && null != offlinePackageInfo )
		{
			String StringElem[] = PackageInfoString.split(",");
			if( 12 == StringElem.length )
			{
//				offlinePackageInfo.setAreaCode( Integer.parseInt( StringElem[0] ) );
//				offlinePackageInfo.setParentCode( Integer.parseInt( StringElem[1] ) );
//				offlinePackageInfo.setTypeCode( Integer.parseInt( StringElem[2] ) );
//				offlinePackageInfo.setSequenceNumber( Integer.parseInt( StringElem[3] ) );
//				offlinePackageInfo.setDbVersion( Integer.parseInt( StringElem[4] ) );
//				offlinePackageInfo.setFormatVersion( Integer.parseInt( StringElem[5] ) );
//				offlinePackageInfo.setDataVersion( Integer.parseInt( StringElem[6] ) );
//				offlinePackageInfo.setAreaName( StringElem[7] );
//				offlinePackageInfo.setPackageName( StringElem[8] );
//				//offlinePackageInfo.setdownloadUrl( StringElem[0] );
//				//offlinePackageInfo.setupdateDirectory( StringElem[0] );
//				offlinePackageInfo.setPackageSize( Integer.parseInt( StringElem[9] ) );
//				offlinePackageInfo.setDownloadSize( Integer.parseInt( StringElem[10] ) );
//				offlinePackageInfo.setUpdateState( Integer.parseInt( StringElem[11] ) );
//				//PNetLog.e( "PackageDownloadManager::getOfflinePackageInfo i =" + packageInfoIndex + ",AreaName = " + offlinePackageInfo.getAreaName() + ",PackageName = " + offlinePackageInfo.getPackageName() );
				return true;				
			}	
		}
		return false;
	}
		
	/** 	 
	 * ��ʼ�������߰�
     * @param  offlinePackageInfo     ���߰���Ϣ
     * @param  packageUpdateListener  ��Ϣ֪ͨ������
	 * @return W3_OK ���ɹ�  else ������
	 */
	public static int startDownloadOfflinePackage( OfflinePackageInfo offlinePackageInfo, DownloadRequestListener downloadRequestListener )
	{
		Log.d("DSP","startDownloadOfflinePackage start");
		DownloadAttributes downloadAttributes = new DownloadAttributes();
		
		if( null != downloadAttributes )
		{
			String downloadOfflinePackageURL = getDownloadOfflinePackageURL( offlinePackageInfo );		
			if( downloadOfflinePackageURL.length() > 0 )
			{					
				downloadAttributes.setDownloadRootUrl( downloadOfflinePackageURL.substring( 0, downloadOfflinePackageURL.lastIndexOf("/") + 1 ) );
				downloadAttributes.setFileName( downloadOfflinePackageURL.substring( downloadOfflinePackageURL.lastIndexOf("/") + 1, downloadOfflinePackageURL.length() ) );
				downloadAttributes.setSaveDirectory( getSearchPackageDirectory() );
				
				downloadAttributes.setOnlyUseWifi( true );
				downloadAttributes.setTryTimes( 99 );
				downloadAttributes.setUnzipFile( false );
				downloadAttributes.setDownloadResuming( true );
				downloadAttributes.setDownloadRequestListener( downloadRequestListener );
				DownloadRequest downloadRequest = new DownloadRequest( downloadAttributes );
				DownloadTaskManager.instance(DownloadTaskManager.DownloadDataType.DOWNLOAD_SEARCH_DATA).postDownloadRequest( downloadRequest );			
			}			
		}	
		Log.d("DSP","startDownloadOfflinePackage end");		
		return W3_OK;
	}
	
	public static void waitingForDownloadOfflinePackage( OfflinePackageInfo offlinePackageInfo )
	{
		if( null != offlinePackageInfo )
		{
			W3JNI.waitingForDownloadOfflinePackage( W3JNI.W3_PACKAGE_INFO_SEARCH, offlinePackageInfo.getAreaCode() );
		}		
	}
	
	/** 	 
	 * ��ͣ�������߰�
     * @param  offlinePackageInfo     ���߰���Ϣ	 
	 */	
	public static void pauseDownloadOfflinePackage( OfflinePackageInfo offlinePackageInfo )
	{	
		PNetLog.d( "PackageDownloadManager::pauseDownloadOfflinePackage start\n" );		
		DownloadAttributes downloadAttributes = new DownloadAttributes();
		
		if( null != downloadAttributes )
		{
			String downloadOfflinePackageURL = getDownloadOfflinePackageURL( offlinePackageInfo );		
			if( downloadOfflinePackageURL.length() > 0 )
			{					
				downloadAttributes.setDownloadRootUrl( downloadOfflinePackageURL.substring( 0, downloadOfflinePackageURL.lastIndexOf("/") + 1 ) );
				downloadAttributes.setFileName( downloadOfflinePackageURL.substring( downloadOfflinePackageURL.lastIndexOf("/") + 1, downloadOfflinePackageURL.length() ) );
				downloadAttributes.setSaveDirectory( getSearchPackageDirectory() );
				
				downloadAttributes.setOnlyUseWifi( true );
				downloadAttributes.setTryTimes( 99 );
				downloadAttributes.setUnzipFile( false );
				downloadAttributes.setDownloadResuming( true );			
				DownloadRequest downloadRequest = new DownloadRequest( downloadAttributes );
				DownloadTaskManager.instance(DownloadTaskManager.DownloadDataType.DOWNLOAD_SEARCH_DATA).cancelDownload( downloadRequest );			
			}			
		}			
		PNetLog.d( "PackageDownloadManager::pauseDownloadOfflinePackage end\n" );		
	}			
		
	/**
	 * delete off-line package
	 * @param  areaCode          area code
	 * @param  pakageName        off-line package name	 
	 */
	public static void deleteOfflinePackage( int areaCode, String pakageName )
	{
		W3JNI.deleteOfflinePackage( W3JNI.W3_PACKAGE_INFO_SEARCH, areaCode );
	}
	
	public static void testGetData()
	{
		//if( 1 == getReturnCode() 
		//	|| 2 == getReturnCode() )
		{
			
			int infoNumber = getPackageInfoNumber();
			PNetLog.e( "PackageDownloadManager::testGetData"+"infoNumber="+infoNumber );
			for( int i = 0; i < infoNumber; ++i )
			{
				OfflinePackageInfo offlinePackageInfo = new OfflinePackageInfo();
				if( getOfflinePackageInfo( i, offlinePackageInfo ) )
				{
					PNetLog.e( "PackageDownloadManager::testGetData"+" "+offlinePackageInfo.getAreaCode() );
					PNetLog.e( "PackageDownloadManager::testGetData"+" "+offlinePackageInfo.getAreaName() );
				}				
			}
		}
	
	}
	
	public static String getDownloadOfflinePackageURL( OfflinePackageInfo offlinePackageInfo )
	{
		PNetLog.d( "PackageDownloadManager::GetDownloadOfflinePackageURL start\n" );
	
		String downloadOfflinePackageURL = "";
		String searchPackageUrl = W3JNI.getConfigValue( "SearchPackageUrl" );		
		
		
		if( searchPackageUrl.length() == 0 )
		{
			PNetLog.e( "PackageDownloadManager::getDownloadOfflinePackageURL parameters error searchPackageUrl=["+searchPackageUrl + "]\n" );
		}
		else
		{
//			jniSearchLControl searchLControl = new jniSearchLControl();
//			downloadOfflinePackageURL = String.format( searchPackageUrl, searchLControl.getFormatVersion(), 
//					searchLControl.getCurDataVersion(), offlinePackageInfo.getPackageName() );
		}
		PNetLog.d( "PackageDownloadManager::getDownloadOfflinePackageURL[" + downloadOfflinePackageURL +"] end\n" );
		return downloadOfflinePackageURL;		
	}
	
	public static String getSearchPackageDirectory()
	{
		return FileSystemJNI.instance().getSystemPath( "USER/RW/WebNaviData/search/" );
	}
	
	
}
