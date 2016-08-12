package com.billionav.navi.dataupdate;

import com.billionav.jni.W3JNI;
import com.billionav.navi.dataupdate.DataUpdateListener;
import com.billionav.navi.dataupdate.DataVersionInfo;
import com.billionav.navi.net.PNetLog;

public class DataUpdateManager {
	/** 	 
	 * �жϳ����еĸ�ʽ�汾�ź���ݿ��еĸ�ʽ�汾���Ƿ���ͬ������ͬ���һ�㷢����APK���º�    
	 * @return true ����ͬ  false : ����ͬ
	 */
	public static boolean isFormatVersionSame() 
	{
		return W3JNI.isFormatVersionSame();
	}
	
	/** 	 
	 * ��ȡ�����еĸ�ʽ�汾�ţ�UI�øø�ʽ�汾���ҵ����µ���ݰ汾��    
	 * @return �����еĸ�ʽ�汾��
	 */	
	public static int getFormatVersion() 
	{
		return W3JNI.getFormatVersion();
	}
	
	/** 	 
	 * ��ȡ��ݿ��е���ݰ汾�ţ�UI�����Ƚ��Ƿ�����ݸ���    
	 * @return ��ݿ��е���ݰ汾��
	 */	
	public static int getCurDataVersion() 
	{
		return W3JNI.getCurDataVersion();
	}	
	/** 	 
	 * ����ʱ����Ƿ�������������һ������Ϊ��ݰ汾����ʱ���޷�������գ���Ҫ�´�����ʱɾ��
	 * @return  true ����  false : û��
	 */
	public static boolean  isExistClearDataRequest() 
	{
		return W3JNI.isExistClearDataRequest();
	}
	
	/** 	 
	 * �������������������
	 * @param  dataUpdateListener    ������Listener
	 * @return  true ���ɹ�  false : ʧ��
	 */
	public static boolean  clearDataForRequest( DataUpdateListener dataUpdateListener ) 
	{
		W3JNI.setDataUpdateListener(dataUpdateListener);
		return W3JNI.clearDataForRequest();
	}
	
	/** 	 
	 * ���ʽ�汾�Ų�ͬ��������   
	 * @param  dataUpdateListener    ������Listener
	 * @return  true ���ɹ�  false : ʧ��
	 */
	public static boolean clearDataForFormateChanged( DataUpdateListener dataUpdateListener ) 
	{
		W3JNI.setDataUpdateListener(dataUpdateListener);
		return W3JNI.clearDataForFormateChanged();
	}
	
	/** 	 
	 * ����ݰ汾�Ų�ͬ�������������������´���������ʱ������ִ�������ݲ���  
	 * @param  dataVersion   ��ӦҪ���µ���ݰ汾��  
	 * @return  true ���ɹ�  false : ʧ��
	 */	
	public static boolean  requestClearDataForDataVersionChanged( int dataVersion ) 
	{
		return W3JNI.requestClearDataForDataVersionChanged( dataVersion );
	}

	public static int getSearchFormatVersion()
	{
		return W3JNI.getSearchFormatVersion();
	}
	public static int getSearchDataVersion()
	{
		return W3JNI.getSearchDataVersion();
	}
	
	public static void setSearchDataVersion(int iSearchDataVersion)
	{
		W3JNI.setSearchDataVersion(iSearchDataVersion);
	}
}
