package com.billionav.jni;

import com.billionav.navi.dataupdate.DataUpdateListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;

/**
 * Provide interface(s) to access W3Lib.
 */
public class W3JNI {
	
	public static DataUpdateListener s_dataUpdateListener = null;
	public final static int W3_PACKAGE_INFO_MAP                     = 0;
	public final static int W3_PACKAGE_INFO_SEARCH                  = 1;
	
	public static native String  getConfigValue( String itemName );
	
	public static native boolean isExistNeedDownloadPackage();
	
	public static native int predownloadOfflinePackageInfoList( int packageInfoType );
	
	public static void onOfflinePackageInfoList( int packageInfoType, int packageInfoNumber, int returnCode )
	{			
		NSTriggerInfo cTriggerInfo = new NSTriggerInfo();	
		if( W3_PACKAGE_INFO_MAP == packageInfoType )
		{
			cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DL_MAP_PACKLIST_RECEIVED;
		}
		else if( W3_PACKAGE_INFO_SEARCH == packageInfoType )
		{
		
			cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DL_SEARCH_PACKLIST_RECEIVED;
		}
		
		cTriggerInfo.m_lParam1 = packageInfoNumber;
		cTriggerInfo.m_lParam2 = returnCode;
		MenuControlIF.Instance().TriggerForScreen(cTriggerInfo);		
	}
	
	public static native int  getReturnCode( int packageInfoType );
	public static native int  getPackageInfoNumber( int packageInfoType );
	
	public static native String  getOfflinePackageInfoString( int packageInfoType, int packageInfoIndex );
	
	public static native int  startDownloadOfflinePackage( int packageInfoType, int areaCode );
	
	public static native void waitingForDownloadOfflinePackage( int packageInfoType, int areaCode );
	
	
	public static native void pauseDownloadOfflinePackage( int packageInfoType, int areaCode );		
	
	
	public static native void onDownloadOfflinePackageCompleted( int packageInfoType, int areaCode );
	
	/**
	 * delete off-line package
	 * @param  areaCode          area code
	 * @param  pakageName        off-line package name	 
	 */
	public static native void deleteOfflinePackage( int packageInfoType, int areaCode );	
	
	public static void setDataUpdateListener( DataUpdateListener dataUpdateListener )
	{
		if( s_dataUpdateListener != dataUpdateListener )
		{
			s_dataUpdateListener = dataUpdateListener;
		}
	}
	
	public static native boolean isFormatVersionSame();
	
	/** 	 
	 * ��ȡ�����еĸ�ʽ�汾�ţ�UI�øø�ʽ�汾���ҵ����µ���ݰ汾��    
	 * @return �����еĸ�ʽ�汾��
	 */	
	public static native int getFormatVersion();
	
	/** 	 
	 * ��ȡ��ݿ��е���ݰ汾�ţ�UI�����Ƚ��Ƿ�����ݸ���    
	 * @return ��ݿ��е���ݰ汾��
	 */	
	public static native int getCurDataVersion();
	
	/** 	 
	 * ����ʱ����Ƿ�������������һ������Ϊ��ݰ汾����ʱ���޷�������գ���Ҫ�´�����ʱɾ��
	 * @return  true ����  false : û��
	 */
	public static native boolean  isExistClearDataRequest();
	
	/** 	 
	 * �������������������
	 * @param  dataUpdateListener    ������Listener
	 * @return  true ���ɹ�  false : ʧ��
	 */
	public static native boolean  clearDataForRequest();
	
	/** 	 
	 * ���ʽ�汾�Ų�ͬ��������   
	 * @param  dataUpdateListener    ������Listener
	 * @return  true ���ɹ�  false : ʧ��
	 */
	public static native boolean clearDataForFormateChanged();
	
	/** 	 
	 * ����ݰ汾�Ų�ͬ�������������������´���������ʱ������ִ�������ݲ���  
	 * @param  dataVersion   ��ӦҪ���µ���ݰ汾��  
	 * @return  true ���ɹ�  false : ʧ��
	 */	
	public static native boolean  requestClearDataForDataVersionChanged( int dataVersion ); 

	public static native int getSearchFormatVersion();
	
	public static native int getSearchDataVersion();

	public static native void setSearchDataVersion(int iSearchDataVersion);
	
	public static void onDeleteFile( String fileName, int fileIndex, int totalFile, boolean isSuccess )
	{
		if( null != s_dataUpdateListener )
		{
			s_dataUpdateListener.onDeleteFile( fileName, fileIndex, totalFile, isSuccess );
		}
	}
	
	public static void onDataClearCompleted()
	{	
		if( null != s_dataUpdateListener )
		{
			s_dataUpdateListener.onDataClearCompleted();
		}
	}	
	
}
