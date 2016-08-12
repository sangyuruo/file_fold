package com.billionav.navi.datasynccontrol;

import com.billionav.navi.menucontrol.NSTriggerID;

public class DataSyncControl_CommonVar{
	//------------------------------------POI---------------------------------------
	public static final String fileSurfix = ".xml";
	public static final String storedPath = "/mnt/sdcrad/USER/RW/POINT/SYNC/";
	
	public static final String SYNC_PATH_MEHTOD_GETSTATUS	=	"Y";//Y means Query , N means get
	public static final String SYNC_PATH_MEHTOD_GETPATH		=	"N";
	
	public static final int DATASYNC_TRIGGLEID_POI_SYNC_RESULT = NSTriggerID.UIC_MN_TRG_DS_POI_SYNC_RESULT;//TODO
	public static final int DATASYNC_TRIGGLEID_POI_POIFILE_FINISH = NSTriggerID.UIC_MN_TRG_DS_POI_POIFILE_FINISH;//TODO
	public static final int DATASYNC_TRIGGLEID_POI_MERGEFILE_FINISH = NSTriggerID.UIC_MN_TRG_DS_POI_MERGEFILE_FINISH;//TODO
	
	//TriggleID for UI param01
	public static final int SYNCPOISTAYUS_NONEEDSYNC = 0;
	public static final int SYNCPOISTAYUS_DOWNLOAD_ERROR = 1;
	public static final int SYNCPOISTAYUS_MERGE_ERROR = 2;
	public static final int SYNCPOISTAYUS_SUCCESS = 3;
	public static final int SYNCPOISTAYUS_CANCEL = 4;
	
	//poisys request cancel status
	public static final int SYNCPOISTAYUS_NET_NOCANCEL = 0;
	public static final int SYNCPOISTAYUS_NET_CANCELING = 1;
	public static final int SYNCPOISTAYUS_NET_CANCELSUCCESS = 2;
	public static final int SYNCPOISTAYUS_NET_CANCELFAILED = 3;
	public static final int SYNCPOISTAYUS_NET_NO_NEED_REQUEST = 4;
	
	//current status
	public static final int SYNCPOISTAYUS_POIFILE_NO_FINISH = 0;
	public static final int SYNCPOISTAYUS_POIFILE_FINISH = 1;
	public static final int SYNCPOISTAYUS_MERGEFILE_FINISH = 2;
	public static final int SYNCPOISTAYUS_MERGEFILE_START = 3;
	
	
	//Download Status 
	public static final int SYNCDOWNLOADSTATUS_FAILED = 0;
	public static final int SYNCDOWNLOADSTATUS_SUCCESS = 1;
	
	//Merge Status
	public static final int SYNCMERGESTATUS_FAILESD = 0;
	public static final int SYNCMERGESTATUS_SUCCESS = 1;
	public static final int SYNCMERGESTATUS_CANCEL = 2;
	
	//IS NEED SYNC
	public static final long NEED_SYNC = 1;
	public static final long NOTNEED_SYNC = 0;
	public static final long CANCEL = 2;
	
	//------------------------------------Route---------------------------------------
	public static final int DATASYNC_TRIGGLEID_GETROUTE_SYNC_RESULT = NSTriggerID.UIC_MN_TRG_DS_GETROUTE_SYNC_RESULT;
	
	public static final long SYNCGETPATHSTATUS_SUCCESS = 0;
	public static final long SYNCGETPATHSTATUS_FAILED = 1;
	
	public static final int	 SYNCGETPATH_DETAILS_ERROR_SAVEFILE_ERROR = 10;
	
	public static final String DATASYNC_ROUTE_STORE_SUBPATH = "USER/RW/PATH/";
	public static final String DATASYNC_ROUTE_STORE_NAME	= "TEMP_DOWDLOAD_ROUTE.DAT";
	
	//-----------------------------------GetRouteStatus--------------------------------
	public static final int DATASYNC_TRIGGLEID_GETROUTESTATUS	= 	NSTriggerID.UIC_MN_TRG_DS_GETROUTESTATUS;
	
	public static final long SYNCGETROUTESTATUS_SUCCESS = 0;
	public static final long SYNCGETROUTESTATUS_FAILED = 1;
	
	public static final long SYNCROUTESTATUS_NO_ROUTE	=  0;
	public static final long SYNCROUTESTATUS_GETROUTE_FIRST_TIME = 1;
	public static final long SYNCROUTESTATUS_GETROUTE_NOT_FIRST_TIME = 2;  
	
	//----------------------------------GetAmeba----------------------------------------
	
	public static final int DATASYNC_TRIGGLEID_GETAMEBA		= NSTriggerID.UIC_MN_TRG_DS_GETAMEBA;
	
	//----------------------------------POIImage----------------------------------------
	public static final int DATASYNC_TRIGGLEID_DOWNLOAD_POI_IMG	= 9999;
	public static final int DATASYNC_TRIGGLEID_UPLOAD_POI_IMG	= 9998;
	
}