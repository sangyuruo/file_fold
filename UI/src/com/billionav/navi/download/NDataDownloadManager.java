package com.billionav.navi.download;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.billionav.jni.FileSystemJNI;
import com.billionav.jni.W3JNI;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PNetLog;


class NDataDownloadListener implements DownloadRequestListener {
	
	public void onDownloadData( String filePath, long totalSize, long downloadSize, int returnCode )
	{

		NSTriggerInfo cTriggerInfo = new NSTriggerInfo();	
		
		if( 0 == returnCode )
		{
			cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DLNDATA_DOWNLOADED_FILE;
		}
		else if( 1 == returnCode )
		{
			cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DLNDATA_DOWNLOAD_FINISHED;
		}		
		else
		{
			Log.d("DSP","NET ERROT Trigger sent");
			cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DLNDATA_NET_ERROR;
		}		
		cTriggerInfo.m_lParam1 = totalSize;
		cTriggerInfo.m_lParam2 = downloadSize;
				
		Log.i("DSP", "NDataDownloadListener filePath=[" + filePath + "], totalSize=[" + totalSize 
				+ "], downloadSize=[" + downloadSize + "], returnCode=[" + returnCode + "], m_iTriggerID=[" 
				+ cTriggerInfo.m_iTriggerID + "]");
		MenuControlIF.Instance().TriggerForScreen(cTriggerInfo);
	}
}


public class NDataDownloadManager {		
	private static NDataDownloadListener s_ndataDownloadListener = new NDataDownloadListener();
	private boolean m_downloadListFile = true;
	private Vector<String> m_downloadFilePath = new Vector<String>();
	
	public String readFileToString( String filePath ) {
		File readFile = new File( filePath );
		PNetLog.e( "readFileToString lastModified=[" + readFile.lastModified() + "]" );
	
		Long fileLength = readFile.length();
		if( fileLength > 0 )
		{
			byte[] fileContent = new byte[fileLength.intValue()];
			try {
				FileInputStream inputStream = new FileInputStream( readFile );
				inputStream.read( fileContent );
				inputStream.close();
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
			return new String( fileContent );			
		}
		else
		{
			return null;
		}
		
	}
	
	public String getNDataDirectory()
	{
		return FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_PATH);
	}
	
	/**
	 * download NData json file and check local file
	 */
	public void checkNDataFile()	{
	   // ��ȡNdata�汾��
		Log.d("DSP","checkNDataFile called---");
		m_downloadFilePath.clear();
		String ndataDirectory = getNDataDirectory();
		String ndataListPath = ndataDirectory + "ndatalist.json";

	   try {		
		PNetLog.e( "checkNDataFile start ndataListPath=[" + ndataListPath + "]" );
	    String strJsonData = readFileToString( ndataListPath );
	    if( null == strJsonData )
	    {
	    	PNetLog.e( "checkNDataFile read file failed ndataListPath = [" + ndataListPath + "]" );
	    	return; 
	    }
	    else
	    {
	    	//PNetLog.e( "checkNDataFile strJsonData = [" + strJsonData + "]" );
	    }

	    JSONObject listObject = new JSONObject( strJsonData );	    
	    String key = null;
	    JSONObject obj = null;	    

	    if( listObject.has( "file" ) )
	    {
	    	JSONArray fileArray = listObject.getJSONArray("file");	    	
	    	JSONObject fileObject=null;
	    	for( int i=0; i< fileArray.length(); i++ )
	    	{	    
	    		fileObject = fileArray.getJSONObject(i);
	    		String filePath = fileObject.getString( "fp" );
	    		long lastModifyTime = fileObject.getLong( "lmt" );
	    		long fileSize = fileObject.getLong( "fs" );
	    		File curFile = new File( ndataDirectory + filePath );
	    		PNetLog.e( "checkNDataFile filePath=[" + ( ndataDirectory + filePath ) + "],lastModifyTime=[" +lastModifyTime + "],fileSize=[" + fileSize +"]");
	    		if( curFile.exists() )
	    		{    	
	    			Calendar calendar = Calendar.getInstance();    
	    			calendar.setTimeInMillis( curFile.lastModified() ); 	    			
	    			long fileModifyTime = calendar.get(Calendar.YEAR) * 10000000000L + ( 1 + calendar.get(Calendar.MONTH) ) * 100000000L + ( 1 + calendar.get(Calendar.DAY_OF_MONTH) ) * 1000000L
	    			                      + calendar.get(Calendar.HOUR_OF_DAY) * 10000L +calendar.get(Calendar.MINUTE) * 100L + calendar.get(Calendar.SECOND);
	    			//PNetLog.e( "checkNDataFile getYear=[" + modifyDate.getYear() + "],getMonth=[" + modifyDate.getMonth() + "],getDay=[" + modifyDate.getDay()+ "],fileModifyTime=[" + fileModifyTime + "," + curFile.lastModified() + "]" );
	    			if( fileSize == curFile.length()
	    				&& lastModifyTime == fileModifyTime )
	    			{
	    				PNetLog.e( "checkNDataFile file not need doanload" );
	    				continue;
	    			}
	    		}	    		
	    		m_downloadFilePath.add( filePath );
	    	} 
	    	PNetLog.e( "checkNDataFile m_downloadFilePath size =[" + m_downloadFilePath.size() + "]" );
	    }	 
	   }catch (JSONException e) {
		   e.printStackTrace();
		   PNetLog.e( "checkNDataFile JSONException" );
		   
	   }		
	}
	
	public static String getNDataVersion()
	{
		return W3JNI.getConfigValue( "NDataVersion" );
	}
	
	/**
	 * download NData file
	 */
	public void downloadNDataFiles()
	{
		
		Log.d("DSP","downloadNDataFiles start");
		DownloadAttributes downloadAttributes = new DownloadAttributes();
		
		if( null != downloadAttributes )
		{
			String requestNDataUrl = W3JNI.getConfigValue( "RequestNDataUrl" );
			String ndataVersion = W3JNI.getConfigValue( "NDataVersion" );
			if( requestNDataUrl.length() > 0 && ndataVersion.length() > 0 )
			{
				String downloadPath = String.format( requestNDataUrl, ndataVersion );	
				downloadAttributes.setDownloadRootUrl( downloadPath.substring( 0, downloadPath.lastIndexOf("/") + 1 ) );
				downloadAttributes.setFileName( downloadPath.substring( downloadPath.lastIndexOf("/") + 1, downloadPath.length() ) );
				downloadAttributes.setSaveDirectory( FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_DOWNLOAD_PATH) );
				//Log.e( "DSP", "NDataDownloadManager config error requestNDataUrl =[" + requestNDataUrl + "], ndataVersion=[" + ndataVersion + "]" );
				//downloadAttributes.setSaveDirectory( "/mnt/sdcard/.kanavijp/USER/RW/WebNaviData/offline/" );
				downloadAttributes.setOnlyUseWifi( false );
				downloadAttributes.setTryTimes( 1 );
				downloadAttributes.setUnzipFile( true );
				downloadAttributes.setDownloadResuming( false );
				downloadAttributes.setDownloadRequestListener( s_ndataDownloadListener );
				DownloadRequest downloadRequest = new DownloadRequest( downloadAttributes );
				DownloadTaskManager.instance(DownloadTaskManager.DownloadDataType.DOWNLOAD_NDATA).postDownloadRequest( downloadRequest );			
			}
			else
			{
				PNetLog.e( "NDataDownloadManager config error requestNDataUrl =[" + requestNDataUrl + "], ndataVersion=[" + ndataVersion + "]" );
				//��triger֪ͨ����
				NSTriggerInfo cTriggerInfo = new NSTriggerInfo();	
				cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DLNDATA_NET_ERROR;						
				cTriggerInfo.m_lParam1 = 1000;
				cTriggerInfo.m_lParam2 = 0;
				MenuControlIF.Instance().TriggerForScreen(cTriggerInfo);	
				Log.e("DSP","data ERROT Trigger sent");
			}
			
		}	
		Log.d("DSP","downloadNDataFiles end");
	}
}


