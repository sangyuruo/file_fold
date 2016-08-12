package com.billionav.navi.download;

import com.billionav.jni.UIOfflinePackageControlJNI;
import com.billionav.navi.naviscreen.base.UIOflLog;


public class OfflinePackageInfo{
	
	public final static int W3_US_NO_PACKAGE				= 0x0;	
	public final static int W3_US_CAN_UPDATE				= 0x1;	
	public final static int W3_US_WAITING_FOR_DOWNLOAD		= 0x2; 
	public final static int W3_US_DOWNLOADING       		= 0x3;  
	public final static int W3_US_PAUSE             		= 0x4;  
	public final static int W3_US_COMPLETED          		= 0x5;  
	public final static int W3_US_UNPACK             		= 0x6; 
	
	public final static int OFFLINE_MAP_TYPE                = UIOfflinePackageControlJNI.UI_OLPC_DDT_MAP_DATA;
	public final static int OFFLINE_SEARCH_TYPE             = UIOfflinePackageControlJNI.UI_OLPC_DDT_SEARCH_DATA;
	//default is search type.
	private int curDownloadingType = OFFLINE_SEARCH_TYPE;
	public int			offlinemap_AreaCode; 
	public String		offlinemap_Name;
	public long			offlinemap_FileTotalSize;
	public long			offlinemap_CurDownloadSize;       
	public int			offlinemap_ePackageUpdateState;	
	
	public int			offlinesearch_AreaCode; 
	public String		offlinesearch_Name;
	public long			offlinesearch_FileTotalSize;
	public long			offlinesearch_CurDownloadSize;       
	public int			offlinesearch_ePackageUpdateState;
	
    	
	public void setOflMapData(int offlinemap_AreaCode,String offlinemap_FileName,long offlinemap_FileTotalSize
			,long offlinemap_CurDownloadSize,int offlinemap_ePackageUpdateState){
		this.offlinemap_AreaCode = offlinemap_AreaCode;
		this.offlinemap_Name = offlinemap_FileName;
		this.offlinemap_FileTotalSize = offlinemap_FileTotalSize;
		this.offlinemap_CurDownloadSize = offlinemap_CurDownloadSize;
//		this.offlinemap_ePackageUpdateState = offlinemap_ePackageUpdateState;
		setMapOflState(offlinemap_ePackageUpdateState);
	}
	
	
	
	public void setOflSrchData(int offlinesearch_AreaCode,String offlinesearch_FileName,long offlinesearch_FileTotalSize
			,long offlinesearch_CurDownloadSize,int	offlinesearch_ePackageUpdateState){
		this.offlinesearch_AreaCode = offlinesearch_AreaCode;
		this.offlinesearch_Name = offlinesearch_FileName;
		this.offlinesearch_FileTotalSize = offlinesearch_FileTotalSize;
		this.offlinesearch_CurDownloadSize = offlinesearch_CurDownloadSize;
//		this.offlinesearch_ePackageUpdateState = offlinesearch_ePackageUpdateState;
		setSearchOflState(offlinesearch_ePackageUpdateState);
	}
	
	
	 public int getUpdateState() {
		 //because 1500000000 package just only has the map package.
		 if(this.getMapAreaCode() == 1500000000){
			 return offlinemap_ePackageUpdateState;
		 }
	    	if(offlinemap_ePackageUpdateState == W3_US_NO_PACKAGE && 
	    			offlinesearch_ePackageUpdateState == W3_US_NO_PACKAGE){
	    		return W3_US_NO_PACKAGE;
	    	}
	    	if(offlinemap_ePackageUpdateState == W3_US_COMPLETED && 
	    			offlinesearch_ePackageUpdateState == W3_US_COMPLETED){
	    		return W3_US_COMPLETED;
	    	}
	    	if(offlinemap_ePackageUpdateState == W3_US_DOWNLOADING || 
	    			offlinesearch_ePackageUpdateState == W3_US_DOWNLOADING||
	    			(offlinemap_ePackageUpdateState ==  W3_US_NO_PACKAGE &&
	    					offlinesearch_ePackageUpdateState == W3_US_COMPLETED)) {
	    		return W3_US_DOWNLOADING;
	    	}
	    	if(offlinemap_ePackageUpdateState == W3_US_UNPACK) {
	    		return W3_US_UNPACK;
	    	}
	    	if(offlinesearch_ePackageUpdateState == W3_US_CAN_UPDATE){
	    		return W3_US_CAN_UPDATE;
	    	}
	    	if(offlinemap_ePackageUpdateState == W3_US_WAITING_FOR_DOWNLOAD || 
	    			offlinesearch_ePackageUpdateState == W3_US_WAITING_FOR_DOWNLOAD) {
	    		return W3_US_WAITING_FOR_DOWNLOAD;
	    	}
	    	if(offlinemap_ePackageUpdateState == W3_US_PAUSE || 
	    			offlinesearch_ePackageUpdateState == W3_US_PAUSE) {
	    		return W3_US_PAUSE;
	    	}
	    	return  -1;
	    	
	    }
	 public void setMapOflState(int currentstate){
		 offlinemap_ePackageUpdateState = currentstate;
//		 switch(currentstate){
//		 //not need dispose
//		 	case W3_US_NO_PACKAGE:
//		 		break;
//		 	case W3_US_CAN_UPDATE:
//		 		if(offlinemap_ePackageUpdateState == W3_US_COMPLETED
//		 		|| offlinemap_ePackageUpdateState == W3_US_WAITING_FOR_DOWNLOAD
//		 		|| offlinemap_ePackageUpdateState == W3_US_PAUSE){
//		 			offlinemap_ePackageUpdateState = W3_US_CAN_UPDATE;
//		 		}
//		 		break;
//		 	case W3_US_WAITING_FOR_DOWNLOAD:
//		 		if(offlinemap_ePackageUpdateState == W3_US_NO_PACKAGE
//		 		|| offlinemap_ePackageUpdateState == W3_US_CAN_UPDATE
//		 		|| offlinemap_ePackageUpdateState == W3_US_PAUSE){
//		 			offlinemap_ePackageUpdateState = W3_US_WAITING_FOR_DOWNLOAD;
//		 		}
//		 		break;
//		 	case W3_US_DOWNLOADING:
//		 		if(offlinemap_ePackageUpdateState == W3_US_NO_PACKAGE
//		 		|| offlinemap_ePackageUpdateState == W3_US_WAITING_FOR_DOWNLOAD
//		 		|| offlinemap_ePackageUpdateState == W3_US_CAN_UPDATE
//		 		|| offlinemap_ePackageUpdateState == W3_US_PAUSE){
//		 			offlinemap_ePackageUpdateState = W3_US_DOWNLOADING;
//		 		}
//		 		break;
//		 	case W3_US_PAUSE:
//		 		if(offlinemap_ePackageUpdateState == W3_US_DOWNLOADING
//		 		|| offlinemap_ePackageUpdateState == W3_US_WAITING_FOR_DOWNLOAD
//		 		){
//		 			offlinemap_ePackageUpdateState = W3_US_PAUSE;
//		 		}
//		 		break;
//		 	case W3_US_UNPACK:
//		 		if(offlinemap_ePackageUpdateState == W3_US_COMPLETED){
//		 			offlinemap_ePackageUpdateState = W3_US_UNPACK;
//		 		}
//		 		break;
//		 	case W3_US_COMPLETED:
//		 		if(offlinemap_ePackageUpdateState == W3_US_DOWNLOADING){
//		 			offlinemap_ePackageUpdateState = W3_US_COMPLETED;
//		 		}
//		 		break;
//		 	
//		 }
	 }
	 public void setSearchOflState(int currentstate){
		 if(this.getMapAreaCode() == 1500000000){
			 offlinesearch_ePackageUpdateState = W3_US_COMPLETED;
			 return;
		 }
		 offlinesearch_ePackageUpdateState =  currentstate;
//		 switch(currentstate){
//		 //not need dispose
//		 	case W3_US_NO_PACKAGE:
//		 		break;
//		 	case W3_US_CAN_UPDATE:
//		 		if(offlinesearch_ePackageUpdateState == W3_US_COMPLETED
//		 		|| offlinesearch_ePackageUpdateState == W3_US_WAITING_FOR_DOWNLOAD
//		 		|| offlinesearch_ePackageUpdateState == W3_US_PAUSE){
//		 			offlinesearch_ePackageUpdateState = W3_US_CAN_UPDATE;
//		 		}
//		 		break;
//		 	case W3_US_WAITING_FOR_DOWNLOAD:
//		 		if(offlinesearch_ePackageUpdateState == W3_US_NO_PACKAGE
//		 		|| offlinesearch_ePackageUpdateState == W3_US_CAN_UPDATE
//		 		|| offlinesearch_ePackageUpdateState == W3_US_PAUSE){
//		 			offlinesearch_ePackageUpdateState = W3_US_WAITING_FOR_DOWNLOAD;
//		 		}
//		 		break;
//		 	case W3_US_DOWNLOADING:
//		 		if(offlinesearch_ePackageUpdateState == W3_US_NO_PACKAGE
//		 		|| offlinesearch_ePackageUpdateState == W3_US_WAITING_FOR_DOWNLOAD
//		 		|| offlinesearch_ePackageUpdateState == W3_US_CAN_UPDATE
//		 		|| offlinesearch_ePackageUpdateState == W3_US_PAUSE){
//		 			offlinesearch_ePackageUpdateState = W3_US_DOWNLOADING;
//		 		}
//		 		break;
//		 	case W3_US_PAUSE:
//		 		if(offlinesearch_ePackageUpdateState == W3_US_DOWNLOADING
//		 		|| offlinesearch_ePackageUpdateState == W3_US_WAITING_FOR_DOWNLOAD
//		 		){
//		 			offlinesearch_ePackageUpdateState = W3_US_PAUSE;
//		 		}
//		 		break;
//		 	case W3_US_UNPACK:
//		 		if(offlinesearch_ePackageUpdateState == W3_US_COMPLETED){
//		 			offlinesearch_ePackageUpdateState = W3_US_UNPACK;
//		 		}
//		 		break;
//		 	case W3_US_COMPLETED:
//		 		if(offlinesearch_ePackageUpdateState == W3_US_DOWNLOADING){
//		 			offlinesearch_ePackageUpdateState = W3_US_COMPLETED;
//		 		}
//		 		break;
//		 	
//		 }
	 }
	 public void setAreaName(String name){
		 offlinemap_Name = name;
	 }
	 /*
	  * 
	  * default return map's areacode.beacuse map list contain all of search package.
	  */
	 public  int getAreaCode(){
		 return offlinemap_AreaCode;
	 }
	 public String getAreaName(){
		 return offlinemap_Name;
	 }
	 public int getMapAreaCode(){
		 return offlinemap_AreaCode;
	 }
	 public int getSrchAreaCode(){
		 return offlinesearch_AreaCode;
	 }
	 public void setDownloadSize(long downsize,int type){
		 if(type == OFFLINE_MAP_TYPE){
			 offlinemap_CurDownloadSize = downsize;
		 }else if(type == OFFLINE_SEARCH_TYPE){
			 offlinesearch_CurDownloadSize = downsize;
		 }else{
			 
		 }
	 }
	 public int getPackageSize(){
		 return (int) (offlinemap_FileTotalSize+offlinesearch_FileTotalSize);
	 }
	 public int getDownloadSize(){
		 return (int) (offlinemap_CurDownloadSize+offlinesearch_CurDownloadSize);
	 }
	 public void setPackageSize(int type,int size) throws Exception{
		 if(type == OFFLINE_MAP_TYPE){
			 offlinemap_CurDownloadSize = size;
		 }else if(type == OFFLINE_SEARCH_TYPE){
			 offlinesearch_CurDownloadSize = size;
		 }else{
			 throw new Exception("error type");
		 }
	 }
	 public int getOfflinemapState(){
		 return offlinemap_ePackageUpdateState;
	 }
	 public int getOfflineSrchState(){
		 return offlinesearch_ePackageUpdateState;
	 }



	public int getCurDownloadingType() {
		return curDownloadingType;
	}



	public void setCurDownloadingType(int curDownloadingType) {
		this.curDownloadingType = curDownloadingType;
	}
}