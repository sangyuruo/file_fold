package com.billionav.navi.download;

import com.billionav.jni.W3JNI;
import com.billionav.navi.download.OfflinePackageInfo;
import com.billionav.navi.net.PNetLog;


class DSPDownloadTaskListener extends PackageDownloadListener{
    public void onDownloadOfflinePackageSize( String packageName, long receiveCount, int code) {
        /*
        int progess = (int)(event.getReceivedCount()*100.0/event.getTotalCount());
        System.out.println("1:"+progess);
        System.out.println("2:"+event.getRealTimeSpeed());
        System.out.println("3:"+event.getGlobalSpeed());
        if(event.isComplete()){
         System.out.println("-------------end--------");
        }
        */
    	PNetLog.e( "DSPDownloadTaskListener::onDownloadOfflinePackageSize receiveCount=["+receiveCount + "],code=[" + code+"]\n" );
        if(code == DownloadTask.STATUS_FINISH)
        {
        	PNetLog.e( "DSPDownloadTaskListener:: download completed receiveCount=["+receiveCount + "],code=[" + code+"]\n" );
        }
       }
 }

public class PackageDownloadManager {
	public final static int W3_OK                       = 0x0;	
	public final static int W3_GET_FROM_NETWORK         = 0x1;	
	public final static int W3_GET_FROM_DB              = 0x2;
	public final static int W3_NETWORK_ERROR    		= 0x3;
	public final static int W3_PARAMETERS_ERROR    		= 0x4;
	public static int er = 0;
	
	/**
	 *  �Ƿ������Ҫ���ص����߰�
	 * @return true ������  false : ������
	 */
	public static boolean isExistNeedDownloadPackage()
	{
		return false;
	}
	
	/**
	 *  �첽��ȡ�������߰��嵥
	 * @return W3_OK ���ɹ�  else ������
	 */
	public static int predownloadOfflinePackageInfoList()
    {				
		PNetLog.e( "PackageDownloadManager::predownloadOfflinePackageInfoList\n" );
		
		W3JNI.predownloadOfflinePackageInfoList( W3JNI.W3_PACKAGE_INFO_MAP );
    	return W3_OK;
    }
	
	public static int getReturnCode()
    {
    	return W3JNI.getReturnCode( W3JNI.W3_PACKAGE_INFO_MAP );
    }
	
	public static int getPackageInfoNumber()
    {
		return W3JNI.getPackageInfoNumber( W3JNI.W3_PACKAGE_INFO_MAP );
    }	
	
	/**
	 * get update status of off-line package, return package info list by listener
	 * @param  dataUpdateListener  listener
	 */
	public static boolean getOfflinePackageInfo( int packageInfoIndex, OfflinePackageInfo offlinePackageInfo )
	{	
		/*
		PNetLog.e( "PackageDownloadManager::getOfflinePackageInfo start\n" );
		W3JNI.startDownloadOfflinePackage( 30310000 );
		W3JNI.startDownloadOfflinePackage( 30620000 );
		PNetLog.e( "PackageDownloadManager::getOfflinePackageInfo mid 1\n" );
		W3JNI.pauseDownloadOfflinePackage( 30620000 );	
		PNetLog.e( "PackageDownloadManager::getOfflinePackageInfo mid 2\n" );
		
		offlinePackageInfo.setPackageName( "30330000_Zhe_Jiang_Sheng" );
		pauseDownloadOfflinePackage( offlinePackageInfo );
		
		PNetLog.e( "PackageDownloadManager::getOfflinePackageInfo mid 3\n" );
		*/		
		
		String PackageInfoString = W3JNI.getOfflinePackageInfoString( W3JNI.W3_PACKAGE_INFO_MAP, packageInfoIndex );
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
				//PNetLog.e( "PackageDownloadManager::getOfflinePackageInfo i =" + packageInfoIndex + ",AreaName = " + offlinePackageInfo.getAreaName() + ",PackageName = " + offlinePackageInfo.getPackageName() );
				return true;				
			}	
		}
		return false;
	}
	
	public static String GetDownloadOfflinePackageURL( OfflinePackageInfo offlinePackageInfo )
	{
		PNetLog.d( "PackageDownloadManager::GetDownloadOfflinePackageURL start\n" );
		String requestDownloadUrl = new String();
		String requestRootUrl = W3JNI.getConfigValue( "RequestPackageUrl" );		
		
		
		if( requestRootUrl.length() == 0 )
		{
			PNetLog.e( "PackageDownloadManager::GetDownloadOfflinePackageURL parameters error requestRootUrl=["+requestRootUrl + "]\n" );
		}
		else
		{
//			requestDownloadUrl = String.format( requestRootUrl, offlinePackageInfo.getFormatVersion(), 
//					offlinePackageInfo.getDataVersion(), offlinePackageInfo.getPackageName() );
		}
		PNetLog.d( "PackageDownloadManager::GetDownloadOfflinePackageURL[" + requestDownloadUrl +"] end\n" );
		return requestDownloadUrl;		
	}
	
	public static String GetDownloadOfflinePackageLocalPath( OfflinePackageInfo offlinePackageInfo )
	{
		PNetLog.d( "PackageDownloadManager::GetDownloadOfflinePackageLocalPath start\n" );		
		String localPackagePath = new String();
		String localPackageDirectory = W3JNI.getConfigValue( "LocalPackagePath" );		
		
		if( localPackageDirectory.length() == 0 )
		{
			PNetLog.e( "PackageDownloadManager::GetDownloadOfflinePackageLocalPath parameters error localPackagePath=["+localPackageDirectory + "]\n" );
		}		
		else 
		{
//			localPackagePath = localPackageDirectory + offlinePackageInfo.getPackageName();
		}
		PNetLog.d( "PackageDownloadManager::GetDownloadOfflinePackageLocalPath[" + localPackagePath +"]end\n" );
		return localPackagePath;
	}
	
	/** 	 
	 * ��ʼ�������߰�
     * @param  offlinePackageInfo     ���߰���Ϣ
     * @param  packageUpdateListener  ��Ϣ֪ͨ������
	 * @return W3_OK ���ɹ�  else ������
	 */
	public static int startDownloadOfflinePackage( OfflinePackageInfo offlinePackageInfo, PackageDownloadListener packageDownloadListener )
	{
		PNetLog.d( "PackageDownloadManager::startDownloadOfflinePackage start\n" );
		String requestDownloadUrl = GetDownloadOfflinePackageURL( offlinePackageInfo );
		String localPackagePath = GetDownloadOfflinePackageLocalPath( offlinePackageInfo );
		if( null != offlinePackageInfo && requestDownloadUrl.length() > 0 && localPackagePath.length() > 0 )
		{			
			PNetLog.d( "PackageDownloadManager::startDownloadOfflinePackage RequestDownloadUrl=["+requestDownloadUrl + "]\n" );
			PNetLog.d( "PackageDownloadManager::startDownloadOfflinePackage localPackagePath=["+localPackagePath + "]\n" );
			DownloadTask.setDebug(true); //���õ���
			DownloadTask task = new DownloadTask( requestDownloadUrl, localPackagePath );
			packageDownloadListener.setAreaCode( offlinePackageInfo.getAreaCode() );
			task.addTaskListener( packageDownloadListener );
			W3JNI.startDownloadOfflinePackage( W3JNI.W3_PACKAGE_INFO_MAP, offlinePackageInfo.getAreaCode() );
			DownloadManager.instance().postDownloadTask(task);
			PNetLog.d( "PackageDownloadManager::startDownloadOfflinePackage end\n" );
			return W3_OK;
		}	
		else
		{
			PNetLog.e( "PackageDownloadManager::startDownloadOfflinePackage error, offlinePackageInfo=[" 
					+ offlinePackageInfo + "],RequestDownloadUrl=["+requestDownloadUrl 
					+ "],localPackagePath=["+localPackagePath + "]\n" );
			return W3_PARAMETERS_ERROR;	
		}		
	}
	
	public static void waitingForDownloadOfflinePackage( OfflinePackageInfo offlinePackageInfo )
	{
		if( null != offlinePackageInfo )
		{
			W3JNI.waitingForDownloadOfflinePackage( W3JNI.W3_PACKAGE_INFO_MAP, offlinePackageInfo.getAreaCode() );
		}		
	}
	
	/** 	 
	 * ��ͣ�������߰�
     * @param  offlinePackageInfo     ���߰���Ϣ	 
	 */	
	public static void pauseDownloadOfflinePackage( OfflinePackageInfo offlinePackageInfo )
	{	
		PNetLog.d( "PackageDownloadManager::pauseDownloadOfflinePackage start\n" );		
		if( null != offlinePackageInfo )
		{		
			W3JNI.pauseDownloadOfflinePackage( W3JNI.W3_PACKAGE_INFO_MAP, offlinePackageInfo.getAreaCode() );
			String requestDownloadUrl = GetDownloadOfflinePackageURL( offlinePackageInfo );
			String localPackagePath = GetDownloadOfflinePackageLocalPath( offlinePackageInfo );
			if( requestDownloadUrl.length() > 0 && localPackagePath.length() > 0 )
			{
				PNetLog.d( "PackageDownloadManager::pauseDownloadOfflinePackage RequestDownloadUrl=["+requestDownloadUrl + "]\n" );
				PNetLog.d( "PackageDownloadManager::pauseDownloadOfflinePackage localPackagePath=["+localPackagePath + "]\n" );
				DownloadTask.setDebug(true); //���õ���
				DownloadTask task = new DownloadTask( requestDownloadUrl, localPackagePath );			
				DownloadManager.instance().cancelDownload(task);					
			}	
			else
			{
				PNetLog.e( "PackageDownloadManager::pauseDownloadOfflinePackage error, RequestDownloadUrl=["
						+requestDownloadUrl + "],localPackagePath=[" + localPackagePath + "]\n" );			
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
		W3JNI.deleteOfflinePackage( W3JNI.W3_PACKAGE_INFO_MAP, areaCode );
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
}
