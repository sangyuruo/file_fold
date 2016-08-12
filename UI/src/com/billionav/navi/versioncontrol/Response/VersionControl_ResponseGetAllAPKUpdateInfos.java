package com.billionav.navi.versioncontrol.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.versioncontrol.VersionControl_CommonVar;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;
import com.billionav.navi.versioncontrol.VersionControl_RequestType;
import com.billionav.navi.versioncontrol.VersionControl_VersionComparator;
import com.billionav.navi.versioncontrol.VersionControl_VersionDataFormat;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestFactory;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetUpdateLogInfo;

public class VersionControl_ResponseGetAllAPKUpdateInfos extends VersionControl_ResponseBase{
	
	public static String IDENTIFY_KEY_APK_UPDATE_INFOS = "GetAllAPKUpdateInfos";
	//GetAPKUpdateInfo
	public static final int VC_ERROR_CODE_HAVE_UPDATE = 100;
	public static final int VC_ERROR_CODE_NO_UPDATE = 101;
	public static final int VC_ERROR_CODE_MUST_UPDATE = 102;
	public static final int VC_ERROR_CODE_PRASE_METADATA_ERROR = 103;
	public static final int VC_ERROR_CODE_PRASE_GET_UPDATEINFOS_ERROR = 104;
	public static final int VC_ERROR_CODE_NET_ERROR = 105;
	
	private int iStatus = VC_ERROR_CODE_HAVE_UPDATE;
	private int iDetails = 0;
	private int iNetWorkCode = 0;
	private boolean bHasUpdateInfoFile = true;
	
	public VersionControl_ResponseGetAllAPKUpdateInfos(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		int iResCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			try {
				byte[] receiveData = getReceivebuf();
				String strData = new String(receiveData);
				JSONObject jObject;
				jObject = new JSONObject(strData);
				Iterator<String> keyIter = jObject.keys();
				String key = null;
				JSONObject obj = null;
				VersionControl_ManagerIF.Instance().getM_cVersionList().clear();
				
				while (keyIter.hasNext()) {
					VersionControl_VersionDataFormat data = new VersionControl_VersionDataFormat();
					
					key = (String) keyIter.next();
					obj = (JSONObject) jObject.get(key);
					
					data.setStrVerSion(key);
					if (obj.has(VersionControl_VersionDataFormat.APIKEY)) {
						String strAPKName = obj.getString(VersionControl_VersionDataFormat.APIKEY);
						data.setStrAPKName(strAPKName);
						data.setStrAPKDownLoadURL(VersionControl_ManagerIF.Instance().getM_strVersionServerUrl() + strAPKName);
					}
					if (obj.has(VersionControl_VersionDataFormat.METADATAKEY)) {
						
						JSONObject metaDataObj = obj.getJSONObject(VersionControl_VersionDataFormat.METADATAKEY);
						String strMeteDataName = key + "."+VersionControl_VersionDataFormat.METADATAKEY;
						data.setJsonMetaData(metaDataObj);
						data.setStrMetaData(strMeteDataName);
						data.setStrMetaDataDownLoadURL(VersionControl_ManagerIF.Instance().getM_strVersionServerUrl() +strMeteDataName);
					}
					if (obj.has(VersionControl_VersionDataFormat.UPDATEINFOKEY)) {
						String updateInfo = obj.getString(VersionControl_VersionDataFormat.UPDATEINFOKEY);
						data.setStrUpdateInfo(updateInfo);
						data.setStrUpdateInfoURL(VersionControl_ManagerIF.Instance().getM_strVersionServerUrl() + updateInfo);
					}
					VersionControl_ManagerIF.Instance().getM_cVersionList().add(data);
				}
				Comparator comparator = new VersionDataFormatcomparator();
				Collections.sort(VersionControl_ManagerIF.Instance().getM_cVersionList(), comparator);
				DebugSortedListInfo(VersionControl_ManagerIF.Instance().getM_cVersionList());
				
				if (VersionControl_ManagerIF.Instance().getM_cVersionList().size() > 0) {
					VersionControl_VersionDataFormat cLatestVersionInfo = VersionControl_ManagerIF.Instance().getM_cVersionList().get(0);
					int iCompareResult = VersionControl_VersionComparator.VERSION_LOCAL_EQUAL;
					String strLocalVersion = getM_strPassedParam();
					String strLatestVersion = cLatestVersionInfo.getStrVerSion();
					
					if (!"".equals(strLocalVersion) && !VersionControl_VersionDataFormat.INVALIDATE_VALUE.equals(strLatestVersion)) {
						iCompareResult = VersionControl_VersionComparator.VersionCompare(strLocalVersion, strLatestVersion);
						
						if (VersionControl_VersionComparator.VERSION_LOCAL_LOWER == iCompareResult) {
							
							iStatus =  VC_ERROR_CODE_HAVE_UPDATE;
							praseMetaDataInfo(cLatestVersionInfo);
							//Get UpdateInfos
							if (!VersionControl_VersionDataFormat.INVALIDATE_VALUE.equals(cLatestVersionInfo.getStrUpdateInfo())) {
								bHasUpdateInfoFile = true;
								Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos Go to GetRelease Infos File: " + strLatestVersion + VersionControl_VersionDataFormat.RELEASE_UPDATE_LOG_INFO_SUFFIX);
								getReleaseInfos(IDENTIFY_KEY_APK_UPDATE_INFOS, cLatestVersionInfo.getStrUpdateInfo());
							} else {
								Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos bHasUpdateInfoFile = false");
								bHasUpdateInfoFile = false;
							}
							
						}else if (VersionControl_VersionComparator.VERSION_FORMAT_ERROR == iCompareResult) {
							iStatus =  VC_ERROR_CODE_NO_UPDATE;
							iDetails = VersionControl_VersionComparator.VERSION_FORMAT_ERROR;
							Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos VersionFormatError strLocalVersion:" + strLocalVersion + " and strLatestVersion:" + strLatestVersion);
						} else if (VersionControl_VersionComparator.VERSION_LOCAL_EQUAL == iCompareResult) {
							iStatus =  VC_ERROR_CODE_NO_UPDATE;
							iDetails = iCompareResult;
						} else if (VersionControl_VersionComparator.VERSION_LOCAL_HIGHER == iCompareResult ){
							iStatus =  VC_ERROR_CODE_NO_UPDATE;
							iDetails = iCompareResult;
						} else if (VersionControl_VersionComparator.VERSION_LOCAL_EXPIRED == iCompareResult){
							praseMetaDataInfo(cLatestVersionInfo);
							//Get UpdateInfos
							if (!VersionControl_VersionDataFormat.INVALIDATE_VALUE.equals(cLatestVersionInfo.getStrUpdateInfo())) {
								bHasUpdateInfoFile = true;
								Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos Go to GetRelease Infos File: " + strLatestVersion + VersionControl_VersionDataFormat.RELEASE_UPDATE_LOG_INFO_SUFFIX);
								getReleaseInfos(IDENTIFY_KEY_APK_UPDATE_INFOS, cLatestVersionInfo.getStrUpdateInfo());
							} else {
								Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos bHasUpdateInfoFile = false");
								bHasUpdateInfoFile = false;
							}
							iStatus =  VC_ERROR_CODE_MUST_UPDATE;
							iDetails = iCompareResult;
						}
					} else {
						iStatus =  VC_ERROR_CODE_NO_UPDATE;
						iDetails = VersionControl_VersionComparator.VERSION_FORMAT_ERROR;
						
						Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos VersionFormatError strLocalVersion:" + strLocalVersion + " and strLatestVersion:" + strLatestVersion);
					}
					
					Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos Set Version Infos VerionCompare Result :" + iCompareResult);
					VersionControl_ManagerIF.Instance().SetVersionCompareResult(iCompareResult);
					VersionControl_ManagerIF.Instance().setM_cLatestVersionInfo(cLatestVersionInfo);
					
				} else {
					iStatus =  VC_ERROR_CODE_NO_UPDATE;
					iDetails = UC_DETAILS_NO_VERSION;
				}
				VersionControl_ManagerIF.Instance().setApkUpdateType(iStatus);
				
			} catch (JSONException e) {
				iStatus =  VC_ERROR_CODE_NO_UPDATE;
				iDetails = UC_DETAILS_JSON_PRASE_ERROR;
				Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos Response Exception");
				e.printStackTrace();
			} catch (Exception ex) {
				iStatus =  VC_ERROR_CODE_NO_UPDATE;
				iDetails = UC_DETAILS_SERVER_DATA_PRASE_ERROR;
				Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos Response Prase Data Exception");
				ex.printStackTrace();
			}
		} else {
			iStatus = VC_ERROR_CODE_NO_UPDATE;
			iDetails = VC_ERROR_CODE_NET_ERROR;
			iNetWorkCode = iResCode;
		}
		
		if (VC_ERROR_CODE_NO_UPDATE == iStatus || !bHasUpdateInfoFile) {
			Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos Response iStatus = " + iStatus + " iDetails=" +iDetails);
			NSTriggerInfo cInfo = new NSTriggerInfo();
			cInfo.SetTriggerID(VersionControl_CommonVar.UIC_MN_TAG_UC_GET_ALL_APK_UPDATE_INFOS);
			cInfo.SetlParam1(iStatus);
			cInfo.SetlParam2(iDetails);
			cInfo.SetlParam3(iNetWorkCode);
			cInfo.SetString1(IDENTIFY_KEY_APK_UPDATE_INFOS);
			MenuControlIF.Instance().TriggerForScreen(cInfo);
		}
	}
	
	private void praseMetaDataInfo(VersionControl_VersionDataFormat cLatestVersionInfo) {
		JSONObject  metaDataObj = cLatestVersionInfo.getJsonMetaData();
		Map<String,String> metaDataMap = new HashMap<String, String>();
		try{
			if (null != metaDataObj) {
				Iterator<String> keyIter = metaDataObj.keys();
				String key = null;
				String value = "";
				while (keyIter.hasNext()) {
					key = (String) keyIter.next();
					value =  metaDataObj.getString(key);
					metaDataMap.put(key, value);
					Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos add Metadata key:" + key + "  value :"+value );
				}
			}
		}catch (JSONException ex) {
			iDetails = VC_ERROR_CODE_PRASE_METADATA_ERROR;
			Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos Error for metaDatais not Jason Object");
			ex.printStackTrace();
		} catch (Exception exx) {
			exx.printStackTrace();
		}
		cLatestVersionInfo.setcMetaDataMap(metaDataMap);
	}
	
	private void getReleaseInfos(String strIndetityKey, String strLogInfoFileName) {
		VersionControl_RequestGetUpdateLogInfo request = (VersionControl_RequestGetUpdateLogInfo)VersionControl_RequestFactory.Instance().CreateRequest(VersionControl_RequestType.UC_REQ_VC_GET_UPDATE_LOG_INFO);
		request.setStrURL(VersionControl_ManagerIF.Instance().getM_strVersionServerUrl());
		request.setStrIndentifyKey(strIndetityKey);
		request.getUpdateLogInfo(strLogInfoFileName);
	}
	/**
	 * 
	 * @author chenyong
	 * @sample 
	 * 	1.0 -> ��ʼrelease
 	 *	1.1 -> �¹���1release
 	 *	1.1.0.build1234 -> �ڲ�weekly release build, build�汾����ˮ��
 	 *	1.1.0.build1235 -> �ڲ�weekly release build���ⲿ����
 	 *	1.1.1 -> bug�����1.1.0.build1235bump����
 	 *	1.1.2 -> �ٴ�bug����
 	 *	1.2 -> �¹���2release
 	 *	1.2.1 -> bug����
	 *
	 */
	public class VersionDataFormatcomparator implements Comparator{

	    public int compare(Object o1,Object o2) {
	    	
	    	VersionControl_VersionDataFormat p1=(VersionControl_VersionDataFormat)o1;
	    	VersionControl_VersionDataFormat p2=(VersionControl_VersionDataFormat)o2;
	    	String strVersion01 = p1.getStrVerSion();
	    	String strVersion02 = p2.getStrVerSion();
	    	
	    	return VersionControl_VersionComparator.VersionSort(strVersion01, strVersion02);
	       }

	}
	
	public void DebugSortedListInfo(ArrayList<VersionControl_VersionDataFormat> list) {
		for (int index = 0; index < list.size(); index++) {
			VersionControl_VersionDataFormat data = list.get(index);
			Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos SortedListInfo index:" + index + " Version:" + data.getStrVerSion());
		}
	}

}
