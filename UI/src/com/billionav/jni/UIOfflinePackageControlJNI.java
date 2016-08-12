package com.billionav.jni;


public class UIOfflinePackageControlJNI {
	private UIOfflinePackageControlJNI() {
	}
	
	// DownloadDataType
	public static final int UI_OLPC_DDT_MAP_DATA			= 0;
	public static final int UI_OLPC_DDT_SEARCH_DATA			= 1;
	
	// RequestListReturnCode
	public static final int UI_OLPC_RLRC_GET_FROM_NETWORK	= 1;
	public static final int UI_OLPC_RLRC_GET_FROM_DB		= 2;
	public static final int UI_OLPC_RLRC_NETWORK_ERROR		= 3;
	
	// PackageUpdateState
	public static final int UI_OLPC_PUS_NO_PACKAGE				= 0;
	public static final int UI_OLPC_PUS_CAN_UPDATE				= 1;
	public static final int UI_OLPC_PUS_WAITING_FOR_DOWNLOAD	= 2;
	public static final int UI_OLPC_PUS_DOWNLOADING				= 3;
	public static final int UI_OLPC_PUS_PAUSE					= 4;
	public static final int UI_OLPC_PUS_COMPLETED				= 5;
	
	// OperateOfflinePackageType
	public static final int UI_OLPC_DOPT_START_DOWNLOAD				= 0;
	public static final int UI_OLPC_DOPT_WAITING_FOR_DOWNLOADING	= 1;
	public static final int UI_OLPC_DOPT_PAUSE_DOWNLOAD				= 2;	
	public static final int UI_OLPC_DOPT_DELETE_PACKAGE				= 3;
	
	// ErrorCode
	public static final int UI_OLPC_EERR_NO_ERROR		= 0;
	public static final int UI_OLPC_EERR_PARAMETER		= 1;
	public static final int UI_OLPC_EERR_COMMONNET		= 2;
	public static final int UI_OLPC_EERR_TIMEOUT		= 5;
	public static final int UI_OLPC_EERR_NODATA			= 8;
	public static final int UI_OLPC_EERR_SQL			= 10;
	public static final int UI_OLPC_EERR_MODULE_STATUS	= 14;
	
	// DownloadStatusCode
	public static final int UI_OLPC_DDST_DOWNLOADING		= 1;
	public static final int UI_OLPC_DDST_NET_ERROR			= 2;	
	public static final int UI_OLPC_DDST_WRITE_FILE_ERROR	= 3;	
	public static final int UI_OLPC_DDST_DOWNLOAD_COMPLETED	= 4;
	public static final int UI_OLPC_DDST_UNZIPING			= 5;
	public static final int UI_OLPC_DDST_UNZIP_COMPLETED	= 6;
	public static final int UI_OLPC_DDST_CANCEL				= 7;
	public static final int UI_OLPC_DDST_ALL_COMPLETED		= 8;
	
	// OfflinePackageInfo
	public static class UI_OLPC_OfflinePackageInfo {
		public int			iAreaCode; 
		public String		strFileName;
		public long			lFileTotalSize;
		public long			lCurDownloadSize;       
		public int			ePackageUpdateState;	// PackageUpdateState
		//don't delete or rename it, define for jni
		public UI_OLPC_OfflinePackageInfo(int iAreaCode, String strFileName, long lFileTotalSize,
				long lCurDownloadSize, int ePackageUpdateState) {
			this.iAreaCode				= iAreaCode;
			this.strFileName			= strFileName;	// Shallow copy
			this.lFileTotalSize			= lFileTotalSize;
			this.lCurDownloadSize		= lCurDownloadSize;
			this.ePackageUpdateState	= ePackageUpdateState;
		}
	}
	
	// OfflinePackageInfoList
	public static class UI_OLPC_OfflinePackageInfoList {
		public int							eDownloadDataType;			// DownloadDataType
		public int							eRequestListReturnCode;		// RequestListReturnCode
		public UI_OLPC_OfflinePackageInfo	OfflinePackageInfoItems[] = null;
		//don't delete or rename it, define for jni
		public UI_OLPC_OfflinePackageInfoList(int eDownloadDataType, int eRequestListReturnCode,
				UI_OLPC_OfflinePackageInfo[] OfflinePackageInfoItems) {
			this.eDownloadDataType			= eDownloadDataType;
			this.eRequestListReturnCode		= eRequestListReturnCode;
			this.OfflinePackageInfoItems	= new UI_OLPC_OfflinePackageInfo[OfflinePackageInfoItems.length];
			if (null != OfflinePackageInfoItems) {
			    for (int i = 0 ; i < OfflinePackageInfoItems.length ; i++) {
			    	// Shallow copy
			    	this.OfflinePackageInfoItems[i] = OfflinePackageInfoItems[i];
			    }
			}
		}		
	}
	
	// OperateOfflinePackageStatus
	public static class UI_OLPC_OperateOfflinePackageStatus {
		public int iOperateId;
		public int eDownloadDataType;	// DownloadDataType
		public int iAreaCode;
		public int eOperateType;		// OperateOfflinePackageType
		public int eErrorCode;			// ErrorCode
		//don't delete or rename it, define for jni
		public UI_OLPC_OperateOfflinePackageStatus(int iOperateId, int eDownloadDataType, int iAreaCode,
				int eOperateType, int eErrorCode) {
			this.iOperateId			= iOperateId;
			this.eDownloadDataType	= eDownloadDataType;
			this.iAreaCode			= iAreaCode;
			this.eOperateType		= eOperateType;
			this.eErrorCode			= eErrorCode;
		}
	}
	
	public static class UI_OLPC_DownloadStatus {
		public int	iOperateId;
		public int	eDownloadDataType;		// DownloadDataType
		public int	iAreaCode;
		public int  eDownloadStatusCode;	// DownloadStatusCode
		public long	lCurDownloadSize;
		public long	lFileTotalSize;
		//don't delete or rename it, define for jni
		public UI_OLPC_DownloadStatus(int iOperateId, int eDownloadDataType, int iAreaCode,
				int eDownloadStatusCode, long lCurDownloadSize, long lFileTotalSize) {
			this.iOperateId				= iOperateId;
			this.eDownloadDataType		= eDownloadDataType;
			this.iAreaCode				= iAreaCode;
			this.eDownloadStatusCode 	= eDownloadStatusCode;
			this.lCurDownloadSize 		= lCurDownloadSize;
			this.lFileTotalSize			= lFileTotalSize;
		}
	}
	
	// RequestOfflinePackageInfoList
	public static native void RequestOfflinePackageInfoList(int eDownloadDataType);
	
	// GetOfflinePackageInfoList
	public static native UI_OLPC_OfflinePackageInfoList GetOfflinePackageInfoList();
	
	// RequestOperateOfflinePackage
	public static native void RequestOperateOfflinePackage(int iOperateId, int eDownloadDataType, int iAreaCode, int eOperateType);
	
	// GetOperateOfflinePackageStatus
	public static native UI_OLPC_OperateOfflinePackageStatus GetOperateOfflinePackageStatus();
	
	// GetDownloadStatus
	public static native UI_OLPC_DownloadStatus GetDownloadStatus();
}
