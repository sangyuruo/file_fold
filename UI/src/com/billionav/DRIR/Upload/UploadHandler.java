package com.billionav.DRIR.Upload;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import com.billionav.DRIR.jni.jniDRIR_UploadControl;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.net.PConnectReceiver.NetConnectTypeListener;
import com.billionav.navi.net.PSyncRequest;

public class UploadHandler implements NetConnectTypeListener
{
	private static UploadHandler sInstance;
	private boolean m_bIsInit = false;
	private Timer mTimerCheckNet;
	private PSyncRequest m_cRequest = null;  	// for abort
	
	public static String m_deviceID = null;
	public static String m_netWorkStatus = null;
	
	public static final int RES_CODE_NO_ERROR = 200;
	public static final int RES_CODE_CONNECT_EXCEPTION = 1;
	public static final int RES_CODE_REQUEST_ERROR = 2;
	public static final int RES_CODE_GETDATA_EXCEPTION = 3;
	public static final int RES_CODE_WRITEFILE_EXCEPTION = 4;
	public static final int RES_CODE_CANCEL = 5;
	public static final int RES_CODE_CANCEL_FAILED = 6;
	public static final int RES_CODE_NO_FILE = 7;
	public static final int RES_CODE_FILE_TYPE_ERROR = 8;
	
	public static final int UPLOAD_FILE_TYPE_DM = 0;
	public static final int UPLOAD_FILE_TYPE_DR = 1;
	public static final int UPLOAD_FILE_TYPE_PIC = 2;
	public static final int UPLOAD_FILE_TYPE_LOG = 3;
		
	public static final int FILE_NOT_DELETE = 0;
	public static final int FILE_DELETE = 1;
	
	public static final int FILE_NOT_UPLOAD	= 1;		//not in uploading list and not upload
	public static final int FILE_INLIST_NOT_UPLOAD	= 2;//in uploading list but not upload
	public static final int FILE_UPLOADING	= 3;		//uploading
	public static final int FILE_UPLOADED	= 4;		//uploaded
	public static final int FILE_UPLOAD_FAIL= 5;		//upload failure
	public static final int FILE_UPLOAD_CANCEL	= 6;	//cancel upload
	
	//uploading status used for trigger
	public static final int TRIGGER_FILE_STATUA_NOT_UPLOAD 	= 0x0100;		//not upload
	public static final int TRIGGER_FILE_STATUA_UPLOADING	= 0x0101;		//uploading
	public static final int TRIGGER_FILE_STATUA_UPLOADED	= 0x0102;		//uploaded
	public static final int TRIGGER_FILE_STATUA_ABNORMAL	= 0x0103;		//abnormal
	//uploading result used for trigger
	public static final int TRIGGER_FILE_UPLOAD_ERSULT_START	= 0x0104;	//start to upload
	public static final int TRIGGER_FILE_UPLOAD_ERSULT_END	= 0x0105;		//end to upload
	public static final int TRIGGER_FILE_UPLOAD_ERSULT_FAIL = 0x0106;		//upload failure
	//delete file result
	public static final int TRIGGER_FILE_DELETED_OK = 0x0107;		//delete success
	public static final int TRIGGER_FILE_DELETED_NG = 0x0108;		//delete failure

	public static final int UPLOAD_CANCEL_OK				= 0;	//cancel upload
	public static final int UPLOAD_CANCEL_OK_UPLOADING 		= 1;
	public static final int UPLOAD_CANCEL_NG_NOT_IN_LIST	= 2;	//cancel upload NG--not in list
	public static final int UPLOAD_CANCEL_NG_UPLOADED		= 3;	//cancel upload NG--finished upload
	public static final int UPLOAD_CANCEL_NG				= 4;	//cancel upload NG
	
	public static final int ADD_FILE_SUCESS		= 0;
	public static final int ADD_FILE_EXISTED	= 1;
	public static final int ADD_FILE_FAIL		= 2;
	
	public static final int FILE_NOT_INLIST = -1;
	public static final int ABNORMAL = 0xFF;
	
	public static final int UPLOAD_PACKET_SIZE = 256*1024;//256KB
	
	public static final int UPLOAD_NOT_LOG_IN = 401;
	
	public static UploadHandler getInstance()
	{
		if (null == sInstance)
		{
			sInstance = new UploadHandler();
		}
		return sInstance;
	}
	
	public void Init(){
		if(false == m_bIsInit){
		
			m_bIsInit = true;
			PConnectReceiver.addListener(sInstance);
			mTimerCheckNet = new Timer();
			mTimerCheckNet.schedule(new TimerCheckNetStatus(), 20*1000, 10*60*1000);// period 10 minute            
		}
	}
	
	public void DeInit(){

			if(null != m_cRequest)
			{
				Log.i("UPLOAD", "DeInit---------cancel request");
				m_cRequest.cancelRequest();
			} else{
				Log.i("UPLOAD", "DeInit---------request null");
				
			}
			
			m_bIsInit = false;
			Log.i("UPLOAD", "DeInit---------");
			mTimerCheckNet.cancel();
			mTimerCheckNet = null;
	}
	
	public int AddFile(String filePath, int fileType)
	{
		int ret = jniDRIR_UploadControl.AddFile(filePath, fileType);
		 
		if(0 == ret)
		{
			return ADD_FILE_SUCESS;
		}
		else if(1 == ret)
		{
			return ADD_FILE_FAIL;
		}
		else 
		{
			return ABNORMAL;
		} 
	}

	public void sendTigger(String strFilePath, int param1)
	{
		//Log.i("UPLOAD", "sendTigger, strFilePath" + strFilePath + "param1" + param1);
		NSTriggerInfo cTriggerInfo = new NSTriggerInfo();
		cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DRIR_UPLOAD;
		cTriggerInfo.m_lParam1 = param1;
		cTriggerInfo.m_String1 = strFilePath;
		MenuControlIF.Instance().TriggerForScreen(cTriggerInfo);
	}

	public int cancelUpload(String strFilePath, int fileType)
	{
		return jniDRIR_UploadControl.CancelUpload(strFilePath, fileType);
	}
	
 
	public void deleteFile(String strFilePath, int fileType)
	{
		jniDRIR_UploadControl.DeleteFile(strFilePath, fileType);
	}
	

	///upload one file in uploading file list
	public int uploadFile(String strFilePath, int fileType)
	{
		return jniDRIR_UploadControl.uploadFile(strFilePath, fileType);

	}
	 
	public int IsOnServer(String[] strFilePath, int fileType)
	{
		synchronized(this){
			return jniDRIR_UploadControl.IsOnServer(strFilePath, fileType);
		}
	}
	
	public int GetNetWorkStatus()
	{
		return PConnectReceiver.getConnectType();		
	}
	
	
	@Override
	public void onReceive(int type) {
		// TODO Auto-generated method stub
		jniDRIR_UploadControl.SetWifiCnntStatus(GetNetWorkStatus());
	}
	
	private class TimerCheckNetStatus extends TimerTask
	{
		@Override
		public void run() {
			jniDRIR_UploadControl.SetWifiCnntStatus(GetNetWorkStatus());	
		}
		
	}
	
}
