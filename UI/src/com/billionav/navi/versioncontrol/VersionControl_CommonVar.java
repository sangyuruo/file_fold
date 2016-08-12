package com.billionav.navi.versioncontrol;

import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseBase;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseGetAllAPKUpdateInfos;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseGetUpdateLogInfo;

public class VersionControl_CommonVar {
	
	public static final int UIC_MN_TRG_VC_GET_VERSION_LIST = NSTriggerID.UIC_MN_TRG_VC_GET_VERSION_LIST;
	public static final int UIC_MN_TRG_UC_GET_METADATA_INFO = NSTriggerID.UIC_MN_TRG_UC_GET_METADATA_INFO;
	public static final int UIC_MN_TAG_UC_GET_TOP_DATA_VERSION = NSTriggerID.UIC_MN_TAG_UC_GET_TOP_DATA_VERSION;
	public static final int UIC_MN_TAG_UC_GET_ALL_APK_UPDATE_INFOS = NSTriggerID.UIC_MN_TAG_UC_GET_ALL_APK_UPDATE_INFOS;
	public static final int UIC_MN_TAG_UC_GET_UPDATE_LOG_INFO = NSTriggerID.UIC_MN_TAG_UC_GET_UPDATE_LOG_INFO;
	
	//For Get the APK Release Infos
	//error code 
	
	//GetAPKUpdateInfo
	public static final int VC_ERROR_CODE_HAVE_UPDATE = VersionControl_ResponseGetAllAPKUpdateInfos.VC_ERROR_CODE_HAVE_UPDATE;
	public static final int VC_ERROR_CODE_NO_UPDATE = VersionControl_ResponseGetAllAPKUpdateInfos.VC_ERROR_CODE_NO_UPDATE;
	public static final int VC_ERROR_CODE_MUST_UPDATE = VersionControl_ResponseGetAllAPKUpdateInfos.VC_ERROR_CODE_MUST_UPDATE;
	
	public static final int VC_ERROR_CODE_PRASE_METADATA_ERROR = VersionControl_ResponseGetAllAPKUpdateInfos.VC_ERROR_CODE_PRASE_METADATA_ERROR;
	public static final int VC_ERROR_CODE_PRASE_GET_UPDATEINFOS_ERROR = VersionControl_ResponseGetAllAPKUpdateInfos.VC_ERROR_CODE_PRASE_METADATA_ERROR;
	public static final int VC_ERROR_CODE_CP_VERSION_FORMAT_ERROR = VersionControl_VersionComparator.VERSION_FORMAT_ERROR;
	public static final int VC_ERROR_CODE_GET_UPDATE_INFO_SUCCESS = VersionControl_ResponseGetUpdateLogInfo.VC_ERROR_CODE_GET_UPDATE_INFO_SUCCESS;
	public static final int VC_ERROR_CODE_GET_UPDATE_INFO_FAILED = VersionControl_ResponseGetUpdateLogInfo.VC_ERROR_CODE_GET_UPDATE_INFO_FAILED;
	public static final int VC_ERROR_CODE_NO_VERSION= VersionControl_ResponseBase.UC_DETAILS_NO_VERSION;
	public static final int VC_ERROR_CODE_JSON_PRASE_ERROR = VersionControl_ResponseBase.UC_DETAILS_JSON_PRASE_ERROR;
	public static final int VC_ERROR_CODE_SERVER_DATA_PRASE_ERROR = VersionControl_ResponseBase.UC_DETAILS_SERVER_DATA_PRASE_ERROR;
	public static final int VC_ERROR_CODE_NET_ERROR = VersionControl_ResponseGetAllAPKUpdateInfos.VC_ERROR_CODE_NET_ERROR;
	
}
