package com.billionav.navi.naviscreen.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.billionav.jni.FileSystemJNI;
import com.billionav.jni.UIOfflinePackageControlJNI;
import com.billionav.jni.UIOfflinePackageControlJNI.UI_OLPC_OfflinePackageInfo;
import com.billionav.jni.UIOfflinePackageControlJNI.UI_OLPC_OfflinePackageInfoList;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.dataupdate.DataUpdateManager;
import com.billionav.navi.download.OfflinePackageInfo;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.net.PConnectReceiver.NetConnectTypeListener;
import com.billionav.navi.system.PLog;
import com.billionav.navi.update.DataUpdate;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseBase;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseGetSrchTopDataVersion;
import com.billionav.ui.R;


public class OfflineMapDataManager implements TriggerListener{
	//there are three status for all world package 
	public static int IN_UNDOWNLOAD_LIST_STATUS = 1;
	public static int IN_DOWNLOADING_LIST_STATUS = 2;
	public static int IN_DOWNLOADED_LIST_STATUS = 3;
	
	private String oflmapareacodePath = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_PATH)+"/OfflineData/areacode.xml";
	private String oflsrchareacodePath = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_PATH)+"/OfflineData/oflsrchareacode.xml";
	private int PACKAGE_NUM = 36;
	private int OfflineDlStatus = PConnectReceiver.CONNECT_TYPE_NONE;
	private ArrayList<OfflinePackageInfo> mapDataList = null;
//	private ArrayList<downloadDataItem> layersItemList;
	private ArrayList<OfflinePackageInfo> downloadedList;
	private ArrayList<OfflinePackageInfo> downloadingList;
	private SparseArray<OfflinePackageInfo> undownloadList;
	private static OfflineMapDataManager instance = null;
	private OfflinePackageListener  offlineListener = null;
	private int recoryIndex;
	private boolean isSearchState = false;
	private String searchFiler = ""; 
	private boolean isRequsetlistOK = false;
	
//  trigger parameter
	public static int UI_OLPC_RLRC_NETWORK_ERROR = UIOfflinePackageControlJNI.UI_OLPC_RLRC_NETWORK_ERROR;
	
	public ArrayList<OfflinePackageInfo> getUndownloadArrayList(){
			//open xml file
			File xmlFile = new File(oflmapareacodePath);
			//parse xml 
			ArrayList<OfflinePackageInfo> orderUndownloadList = new ArrayList<OfflinePackageInfo>();
			try {
			if (xmlFile.exists()) {
					InputStream slideInputStream = new FileInputStream(oflmapareacodePath);
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			        factory.setNamespaceAware(true); 
			        XmlPullParser xpp = factory.newPullParser();
			        xpp.setInput(slideInputStream, "UTF-8");
			        int eventType = xpp.getEventType(); 
			        while (eventType != XmlPullParser.END_DOCUMENT) { 
			        	if(eventType == XmlPullParser.START_TAG) { 
			        		String area_code = xpp.getAttributeValue(null,"area_code");
			        		if(area_code != null){
				        		for(int i=0; i<undownloadList.size(); i++){				   
									if(Integer.parseInt(area_code) == undownloadList.get(undownloadList.keyAt(i)).getAreaCode()){
										orderUndownloadList.add(undownloadList.get(undownloadList.keyAt(i)));
										break;
									}
								}
			        		}
			        	} 
			        	//next element
			        	eventType = xpp.next(); 
			        } 
			        
			        //close stream
			        if (slideInputStream != null) {
			        	slideInputStream.close();
			        	slideInputStream = null;
					}
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			return orderUndownloadList;
	}
	public int getRecoryIndex() {
		return recoryIndex;
	}
	
	public int GetStatus(){
		return OfflineDlStatus;
	}
	
	public void requestOfflineData(){
		if(DataUpdateManager.getSearchDataVersion() < 0) {
			DataUpdate.requestLatestSrchDataVersionOnServer();
		}else {
			if(mapDataList.size() == 0){
	//			PackageDownloadManager.predownloadOfflinePackageInfoList();
				UIOfflinePackageControlJNI.RequestOfflinePackageInfoList(UIOfflinePackageControlJNI.UI_OLPC_DDT_MAP_DATA);
			}
		}
	}
	
	public int judgeisthereallworldpackage(){
		for(OfflinePackageInfo d : downloadedList){
			//1200000000 is nationwide package of areacode.
			if(d.getAreaCode() == 1200000000){
				return IN_DOWNLOADED_LIST_STATUS;
			}
		}
		for(OfflinePackageInfo d : downloadingList){
			//1200000000 is nationwide package of areacode.
			if(d.getAreaCode() == 1200000000){
				return IN_DOWNLOADING_LIST_STATUS;
			}
		}
		return IN_UNDOWNLOAD_LIST_STATUS;
	}
	
	
	public static boolean isExistNeedDownloadPackage(){
		return false;
	}
	
	private void TriggerForWinscape(NSTriggerInfo cTriggerInfo)
	{			
		switch(cTriggerInfo.m_iTriggerID){
		case NSTriggerID.UIC_MN_TRG_OFFLINE_PACKAGE_INFO_LIST:
			/* 
			 * this variable is used to judge whether invoke fetchDataAndLayersItemList()
			 * because improve efficient fetchDataAndLayersItemList() only is invoked once.
			*/
			boolean isInvokefetch = false;
			
			UI_OLPC_OfflinePackageInfoList uiInfoslist = UIOfflinePackageControlJNI.GetOfflinePackageInfoList();
			
			if(uiInfoslist.eRequestListReturnCode == UIOfflinePackageControlJNI.UI_OLPC_RLRC_NETWORK_ERROR){
				UIOflLog.TAG_STATE("send net error trigger when full list...");
				NSTriggerInfo info = new NSTriggerInfo();
				info.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DL_MAP_PACK_INITIALIZED;
				info.m_lParam1 = UI_OLPC_RLRC_NETWORK_ERROR;
				MenuControlIF.Instance().TriggerForScreen(info);
				return;
			}
			
			if(mapDataList.size() > 0){
				isInvokefetch = true;
			}else{
				//when first receive trigger send request package info list. 
				UIOfflinePackageControlJNI.RequestOfflinePackageInfoList(UIOfflinePackageControlJNI.UI_OLPC_DDT_SEARCH_DATA);
			}
			Log.i("icon","-----------------------------------------------------");
			for(UI_OLPC_OfflinePackageInfo ofl : uiInfoslist.OfflinePackageInfoItems){
				Log.i("icon", "ofl areacode "+ofl.iAreaCode + "ofl state "+ofl.ePackageUpdateState);
			}
			fullMapDataList(uiInfoslist);
			if(isInvokefetch){
				fetchDataAndLayersItemList();
				if(uiInfoslist.eDownloadDataType != UIOfflinePackageControlJNI.UI_OLPC_RLRC_NETWORK_ERROR){
					startListofDownloading();
				}
				//send trigger to screen to notify update.
				NSTriggerInfo info = new NSTriggerInfo();
				info.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DL_MAP_PACK_INITIALIZED;
				MenuControlIF.Instance().TriggerForScreen(info);
				isInvokefetch = false;
				// request offline package list succeed
				isRequsetlistOK = true;
			}
			break;
		case NSTriggerID.UIC_MN_TRG_OFFLINE_PACKAGE_OP_STATUS:
			break;
		case NSTriggerID.UIC_MN_TRG_OFFLINE_PACKAGE_DOWNLOAD_STATUS:
			UIOfflinePackageControlJNI.UI_OLPC_DownloadStatus downloadstatus = UIOfflinePackageControlJNI.GetDownloadStatus();
				//finding need to update where offline package.
				int areacode = (int) cTriggerInfo.m_lParam2;
				int datatype = (int) cTriggerInfo.m_lParam1;
				int statusCode = (int) cTriggerInfo.m_lParam3;
				OfflinePackageInfo ofl = null;
				if(datatype == UIOfflinePackageControlJNI.UI_OLPC_DDT_SEARCH_DATA){
					for(OfflinePackageInfo d:downloadingList){
						if(d.getSrchAreaCode() == areacode){
							ofl = d;
						}
					}
				}else{
					for(OfflinePackageInfo d:downloadingList){
						if(d.getMapAreaCode() == areacode){
							ofl = d;
						}
					}
				}
				
				if(ofl == null){
					return;
				}
				switch(statusCode){
					case UIOfflinePackageControlJNI.UI_OLPC_DDST_DOWNLOADING:
						UIOflLog.TAG_STATE("UI_OLPC_DDST_DOWNLOADING");
						UIOflLog.TAG_STATE("ofl areacode: "+ofl.getAreaCode()+ "datatype: "+datatype+"CurDownloadSize: "+downloadstatus.lCurDownloadSize);
						if(datatype == UIOfflinePackageControlJNI.UI_OLPC_DDT_SEARCH_DATA){
							ofl.setCurDownloadingType(OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
							ofl.setDownloadSize(downloadstatus.lCurDownloadSize, OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
						}else{
							ofl.setCurDownloadingType(OfflinePackageInfo.OFFLINE_MAP_TYPE);
							ofl.setDownloadSize(downloadstatus.lCurDownloadSize, OfflinePackageInfo.OFFLINE_MAP_TYPE);
						}
						break;
					case UIOfflinePackageControlJNI.UI_OLPC_DDST_DOWNLOAD_COMPLETED:
						if(datatype == UIOfflinePackageControlJNI.UI_OLPC_DDT_SEARCH_DATA){
							UIOflLog.TAG_STATE("UI_OLPC_DDT_SEARCH_DATA UI_OLPC_DDST_DOWNLOAD_COMPLETED size"+downloadstatus.lCurDownloadSize);
							ofl.setDownloadSize(downloadstatus.lCurDownloadSize, OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
							ofl.setSearchOflState(OfflinePackageInfo.W3_US_COMPLETED);
							//if search data completed download then to download map data package.
							ofl.setMapOflState(OfflinePackageInfo.W3_US_DOWNLOADING);
							startDownloadOfflinePackage(ofl.getMapAreaCode(), UIOfflinePackageControlJNI.UI_OLPC_DDT_MAP_DATA);
						}else{
							UIOflLog.TAG_STATE("UI_OLPC_DDT_MAP_DATA UI_OLPC_DDST_DOWNLOAD_COMPLETED");
							ofl.setDownloadSize(downloadstatus.lCurDownloadSize, OfflinePackageInfo.OFFLINE_MAP_TYPE);
						}
						break;
					case UIOfflinePackageControlJNI.UI_OLPC_DDST_ALL_COMPLETED:
						UIOflLog.TAG_STATE("UI_OLPC_DDST_ALL_COMPLETED");
						downloadingList.remove(ofl);
						downloadedList.add(ofl);
						ofl.setMapOflState(OfflinePackageInfo.W3_US_COMPLETED);
						//start next waiting for downloadMap .
						CustomToast.showToast(NSViewManager.GetViewManager(), ofl.getAreaName()+" "+NSViewManager.GetViewManager().getString(R.string.STR_MM_01_04_01_103), 3000);
						startWaitForDownloadPackage();
						//in order to download again.
						ofl.setCurDownloadingType(OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
						break;
					//no processing
					case UIOfflinePackageControlJNI.UI_OLPC_DDST_CANCEL:
						UIOflLog.TAG_STATE("UI_OLPC_DDST_CANCEL");
						break;
					case UIOfflinePackageControlJNI.UI_OLPC_DDST_UNZIPING:
						UIOflLog.TAG_STATE("UI_OLPC_DDST_UNZIPING");
						ofl.setMapOflState(OfflinePackageInfo.W3_US_UNPACK);
						break;
					//no processing
					case UIOfflinePackageControlJNI.UI_OLPC_DDST_UNZIP_COMPLETED:
						UIOflLog.TAG_STATE("UI_OLPC_DDST_UNZIP_COMPLETED");
						break;
					case UIOfflinePackageControlJNI.UI_OLPC_DDST_NET_ERROR:
						UIOflLog.TAG_STATE("UI_OLPC_DDST_NET_ERROR");
						if(datatype == UIOfflinePackageControlJNI.UI_OLPC_DDT_SEARCH_DATA){
							pauseDownloadOfflinePackage(ofl.getSrchAreaCode(), datatype);
							ofl.setMapOflState(OfflinePackageInfo.W3_US_PAUSE);
						}else{
							pauseDownloadOfflinePackage(ofl.getMapAreaCode(), datatype);
							ofl.setSearchOflState(OfflinePackageInfo.W3_US_PAUSE);
						}
						break;
					case UIOfflinePackageControlJNI.UI_OLPC_DDST_WRITE_FILE_ERROR:
						UIOflLog.TAG_STATE("UI_OLPC_DDST_WRITE_FILE_ERROR");
						if(datatype == UIOfflinePackageControlJNI.UI_OLPC_DDT_SEARCH_DATA){
							pauseDownloadOfflinePackage(ofl.getSrchAreaCode(), datatype);
							ofl.setMapOflState(OfflinePackageInfo.W3_US_PAUSE);
						}else{
							pauseDownloadOfflinePackage(ofl.getMapAreaCode(), datatype);
							ofl.setSearchOflState(OfflinePackageInfo.W3_US_PAUSE);
						}
						break;	
					default:
						break;
				}
			break;
			
		case NSTriggerID.UIC_MN_TAG_UC_GET_TOP_DATA_VERSION:
			if(VersionControl_ResponseGetSrchTopDataVersion.TAG.equals(cTriggerInfo.m_String1)) {
				if(VersionControl_ResponseBase.UC_RESPONES_SUC == cTriggerInfo.GetlParam1()) {
					requestOfflineData();
				} else {
					UIOflLog.TAG_STATE("send net error trigger when request data version...");
					onNetError();
				}
			}
			break;
		
		case NSTriggerID.UIC_MN_TRG_DL_MAP_PACKLIST_RECEIVED:
//			fullMapDataList();
//			if(PackageDownloadManager.getReturnCode() == PackageDownloadManager.W3_GET_FROM_NETWORK || PackageDownloadManager.getReturnCode() == PackageDownloadManager.W3_GET_FROM_DB){
//				startListofDownloading();
//			}
//			NSTriggerInfo info = new NSTriggerInfo();
//			info.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DL_MAP_PACK_INITIALIZED;
//			MenuControlIF.Instance().TriggerForScreen(info);
			break;
		case NSTriggerID.UIC_MN_TRG_DL_MAP_PACK_UPDATESIZE:
//				if((int)cTriggerInfo.m_lParam3 < downloadingList.size()){
//				downloadDataItem dataItem = downloadingList.get((int)cTriggerInfo.m_lParam3);
//				OfflinePackageInfo ofl = dataItem.getOfflinePackageInfo();
//				switch((int)cTriggerInfo.m_lParam1)
//				{
//				case PackageDownloadListener.W3_PD_DOWNLOADING:
//					if(ofl.getAreaCode() == (int)cTriggerInfo.m_lParam4){
//						ofl.setDownloadSize((int)cTriggerInfo.m_lParam2);
//					}
//					break;
//				case PackageDownloadListener.W3_PD_COMPLETED:
//					ofl.setDownloadSize((int)cTriggerInfo.m_lParam2);
//					ofl.setUpdateState(OfflinePackageInfo.W3_US_COMPLETED);
//					downloadingList.remove(dataItem);
//					downloadedList.add(dataItem);
//					//start next waiting for downloadMap .
//					startWaitForDownloadPackage();
//					CustomToast.showToast(NSViewManager.GetViewManager(), ofl.getAreaName()+" "+NSViewManager.GetViewManager().getString(R.string.STR_MM_01_04_01_103), 3000);
//					break;
//				case PackageDownloadListener.W3_PD_CANCEL:
//					break;
//				case PackageDownloadListener.W3_PD_NETWORK_ERROR:
//					pauseDownloadOfflinePackage(ofl);
//					ofl.setUpdateState(OfflinePackageInfo.W3_US_PAUSE);
//					break;
//				case PackageDownloadListener.W3_PD_FILE_IO_ERROR:
//					pauseDownloadOfflinePackage(ofl);
//					ofl.setUpdateState(OfflinePackageInfo.W3_US_PAUSE);
//				default:
//					break;
//				}
//				}
				//updata_ADT_DOWNLOAD((int)cTriggerInfo.m_lParam3);
				break;
		default:
			break;
			}
	}
	
	private void onNetError() {
		// TODO Auto-generated method stub
		//send trigger to screen to notify update.
		NSTriggerInfo info = new NSTriggerInfo();
		info.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DL_MAP_PACK_INITIALIZED;
		info.m_lParam1 = UI_OLPC_RLRC_NETWORK_ERROR;
		MenuControlIF.Instance().TriggerForScreen(info);
	}
	/**
	 * you cant't create instance 2 times
	 * 
	 * @throws RuntimeException
	 * */
	public static OfflineMapDataManager createInstance(Context context) {
		if (instance == null) {
			PLog.i("icon", "getInstance");
			instance = new OfflineMapDataManager(context);
		} else {
//			pLog.("you cant't create instance 2 times");
		}
		return instance;
	}
	public void cancelAllnotification()
	{
//		notificationManager.cancelAll();
	}
	public static OfflineMapDataManager getInstance() {
		return instance;
	}
	private OfflineMapDataManager(Context context) {
		downloadedList = new ArrayList<OfflinePackageInfo>();
		downloadingList = new ArrayList<OfflinePackageInfo>();
		undownloadList = new SparseArray<OfflinePackageInfo>();
//		layersItemList = new ArrayList<downloadDataItem>();
		mapDataList = new ArrayList<OfflinePackageInfo>();
		GlobalTrigger.getInstance().addTriggerListener(this);
		PConnectReceiver.addListener(new NetConnectTypeListener() {
			
			@Override
			public void onReceive(int type) {
				switch(type)
				{
				case PConnectReceiver.CONNECT_TYPE_MOBILE:
					OfflineDlStatus = PConnectReceiver.CONNECT_TYPE_MOBILE;
					PLog.i("type", "PConnectReceiver.CONNECT_TYPE_MOBILE:");
					recoryIndex = -1;
					break;
				case PConnectReceiver.CONNECT_TYPE_NONE:
					OfflineDlStatus = PConnectReceiver.CONNECT_TYPE_NONE;
					PLog.i("type", "PConnectReceiver.CONNECT_TYPE_NONE:");
					recoryIndex = -1;
					break;
				case PConnectReceiver.CONNECT_TYPE_WIFI:
					OfflineDlStatus = PConnectReceiver.CONNECT_TYPE_WIFI;
					requestOfflineData();
					PLog.i("type", "PConnectReceiver.CONNECT_TYPE_WIFI:");
//					recoryIndex = startListofDownloading();
					break;
				}
			}
		});
	}
	/*
     * when you deteled some offlinepackage,you need updata download list.
     * 
     * @param List<Integer>
     *            deleted offlinepackage areacode list.
     * @return boolean 
     * 		   true is have need delete data.
     * 		   false is don't need delete data.
     */
	public boolean disposeDelDataList(List<Integer> dellist){
		if(dellist != null){
			ArrayList<OfflinePackageInfo> downloadingNeedRemoveData = new ArrayList<OfflinePackageInfo>();
			ArrayList<OfflinePackageInfo> downloadedListNeedRemoveData = new ArrayList<OfflinePackageInfo>();
			for(OfflinePackageInfo d:downloadingList){
				for(Integer i:dellist){
					if(i.equals(d.getAreaCode())){
						downloadingNeedRemoveData.add(d);
					}
				}
			}
			for(OfflinePackageInfo d:downloadedList){
				for(Integer i:dellist){
					if(i.equals(d.getAreaCode())){
						downloadedListNeedRemoveData.add(d);
					}
				}
			}
			for(OfflinePackageInfo d:downloadingNeedRemoveData){
				analysisPackageToList(d, "");
				downloadingList.remove(d);
			}
			for(OfflinePackageInfo d:downloadedListNeedRemoveData){
				analysisPackageToList(d, "");
				downloadedList.remove(d);
			}
			startWaitForDownloadPackage();
			return true;
		}
		return false;
	}
	/*
     * when you deteled a offlinepackage,you need updata download list.
     * 
     * @param List<Integer>
     *            deleted offlinepackage.
     */
	public void disposeDelPackage(OfflinePackageInfo  ofl){
		analysisPackageToList(ofl, "");
		downloadedList.remove(ofl);
		downloadingList.remove(ofl);
		startWaitForDownloadPackage();
	}
	/*
     * classify offlinePackage.
     * 
     * @param OfflinePackageInfo
     *            classify OfflinePackageInfo.
     *        String
     *            Condition:must contain this string param .because when search need Filer.
     */
	private void analysisPackageToList(OfflinePackageInfo info,String searchFiler){
		if(info.getAreaName() != null){
			if(info.getAreaName().contains(searchFiler)){
				int updateState = info.getUpdateState();
				switch(updateState){
					case OfflinePackageInfo.W3_US_CAN_UPDATE:
					case OfflinePackageInfo.W3_US_COMPLETED:		
						downloadedList.add(info);
						break;
					case OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD:
					case OfflinePackageInfo.W3_US_PAUSE:
					case OfflinePackageInfo.W3_US_DOWNLOADING:
					case OfflinePackageInfo.W3_US_UNPACK:
						downloadingList.add(info);			
					break;
					default:
						undownloadList.append(info.getAreaCode(), info);
						break;
				}
				}
		}
	}
	public void fetchDataAndLayersItemList(String searchFiler){
		int recordParentCode = -1;
		this.searchFiler = searchFiler;                   //save search string
		if(mapDataList != null){
			if(searchFiler.length() > 0){
				isSearchState = true;
			}
			else{
				isSearchState = false;
			}
			if(isSearchState){
				fetchDataAndLayersItemList();
				ArrayList<OfflinePackageInfo> downloadedList = new ArrayList<OfflinePackageInfo>();
				ArrayList<OfflinePackageInfo> downloadingList = new ArrayList<OfflinePackageInfo>();
				for(OfflinePackageInfo d :this.downloadedList){
					if(d.getAreaName().contains(searchFiler)){
						downloadedList.add(d);
					}
				}
				for(OfflinePackageInfo d :this.downloadingList){
					if(d.getAreaName().contains(searchFiler)){
						downloadingList.add(d);
					}
				}
				SparseArray<OfflinePackageInfo> undownloadList = new SparseArray<OfflinePackageInfo>();
				
				for(OfflinePackageInfo info : mapDataList){
					OfflinePackageInfo item = this.undownloadList.get(info.getAreaCode(),null);
					if(item != null && item.getAreaName().contains(searchFiler)){
						undownloadList.append(item.getAreaCode(), item);
					}
				}
				this.undownloadList = undownloadList;
				this.downloadedList = downloadedList;
				this.downloadingList = downloadingList;
				
			}
			else{
				fetchDataAndLayersItemList();
			}
		
			
//			for(OfflinePackageInfo info:mapDataList){
//				if(recordParentCode == info.getParentCode() && info.getUpdateState() == OfflinePackageInfo.W3_US_NO_PACKAGE){
//					downloadDataItem a = undownloadList.get(recordParentCode);
//					if(a != null){
//						if(a.getChildGroup().size() == 0){
//							OfflinePackageInfo ofl = new OfflinePackageInfo();
//							ofl.setParentCode(a.getAreaCode());
//							ofl.setUpdateState(OfflinePackageInfo.W3_US_NO_PACKAGE);
//							ofl.setAreaName(NSViewManager.GetViewManager().getString(R.string.STR_MM_01_04_01_99));
//							ofl.setPackageSize(a.getPackageSize());
//							a.addChildItem(new downloadDataItem(ofl,4));
//						}
//						a.addChildItem(new downloadDataItem(info, 3));
//					}else{
//						//now is searching,city can as group.
//						undownloadList.append(info.getAreaCode(), new downloadDataItem(info, 3));
//					}
//				}
//				else if(info.getAreaName().contains(searchFiler) && info.getUpdateState() == OfflinePackageInfo.W3_US_NO_PACKAGE){
//					int parentCode = info.getParentCode();
//					//0 is province or municipality directly under the Central Government.
//					//otherwise is city 
//					if(parentCode != 0){
//						downloadDataItem a = undownloadList.get(parentCode);
//						if(a != null){
//							if(a.getChildGroup().size() == 0){
//								OfflinePackageInfo ofl = new OfflinePackageInfo();
//								ofl.setParentCode(a.getAreaCode());
//								ofl.setUpdateState(OfflinePackageInfo.W3_US_NO_PACKAGE);
//								ofl.setAreaName(NSViewManager.GetViewManager().getString(R.string.STR_MM_01_04_01_99));
//								ofl.setPackageSize(a.getPackageSize());
//								a.addChildItem(new downloadDataItem(ofl,4));
//							}
//							a.addChildItem(new downloadDataItem(info, 3));
//						}else{
//							//now is searching,city can as group.
//							undownloadList.append(info.getAreaCode(), new downloadDataItem(info, 3));
//						}
//					}else{
//						//if now is searching,then don't add province to undownloadList.
//						//TypeCode:1 is province , 2 is city, 3 is municipality directly under the Central Government.
////						if(!isSearchState || info.getTypeCode() == 3){
//							undownloadList.append(info.getAreaCode(), new downloadDataItem(info, 3));
//							recordParentCode = info.getAreaCode();
////						}
//					}
//				} 
//			}
		}
	}
	//this function only used once.
	private void fetchDataAndLayersItemList(){
		if(mapDataList != null){
			downloadedList.clear();
			downloadingList.clear();
			undownloadList.clear();
			for(OfflinePackageInfo info: mapDataList){
				analysisPackageToList(info, "");
			}
		}
		startWaitForDownloadPackage();
	}

	private void startWaitForDownloadPackage() {
		//init downloading state .
		if(!isHasDownLoading()){
			for( OfflinePackageInfo ofl :downloadingList) {
				if (ofl.getUpdateState() == OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD) {
					if(ofl.getOfflineSrchState() == OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD){
						startDownloadOfflinePackage(ofl.getSrchAreaCode(), OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
						ofl.setSearchOflState(OfflinePackageInfo.W3_US_DOWNLOADING);
					}else{
						startDownloadOfflinePackage(ofl.getAreaCode(), OfflinePackageInfo.OFFLINE_MAP_TYPE);
						ofl.setMapOflState(OfflinePackageInfo.W3_US_DOWNLOADING);
					}
					break;
				}
			}
		}
	}
	
	/**
	 * To full a list from UIOfflinePackageControlJNI returned.
	 * @param   offline package number size.
	 * @param	offline package type.
	 */
	private void fullMapDataList(UI_OLPC_OfflinePackageInfoList uiInfoslist){
		/*
		    RequestListReturnCode
			public static final int UI_OLPC_RLRC_GET_FROM_NETWORK	= 1;
			public static final int UI_OLPC_RLRC_GET_FROM_DB		= 2;
			public static final int UI_OLPC_RLRC_NETWORK_ERROR		= 3;
		 * 
		 */
			if(mapDataList.size() == 0){
				mapDataList = new ArrayList<OfflinePackageInfo>();
				for(int i = 0; i < PACKAGE_NUM ; i++){
					OfflinePackageInfo ofl = new OfflinePackageInfo();
					mapDataList.add(ofl);
				}
			}
			if(mapDataList != null){
				//open xml file
				File xmlFile;
				if(uiInfoslist.eDownloadDataType == UIOfflinePackageControlJNI.UI_OLPC_DDT_MAP_DATA){
					xmlFile = new File(oflmapareacodePath);
				}else{
					xmlFile = new File(oflsrchareacodePath);
				}
				
				//parse xml 
				try {
				if (xmlFile.exists()) {							
						InputStream slideInputStream = new FileInputStream(xmlFile);						
						XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
				        factory.setNamespaceAware(true); 
				        XmlPullParser xpp = factory.newPullParser();
				        xpp.setInput(slideInputStream, "UTF-8");
				        int eventType = xpp.getEventType(); 
				        //this variable is mapDataList's index
				        int oflindex = 0 ;						    
				        while (eventType != XmlPullParser.END_DOCUMENT) { 
				        	if(eventType == XmlPullParser.START_TAG) { 
				        		String area_code = xpp.getAttributeValue(null,"area_code");
				        		//compositor
				        		if(area_code != null){
					        		for(UIOfflinePackageControlJNI.UI_OLPC_OfflinePackageInfo ofl: uiInfoslist.OfflinePackageInfoItems){					      
					        			
					        			if(ofl.iAreaCode == Integer.parseInt(area_code)){		
					        				
					        				int resid = NaviViewManager.GetViewManager().getResources().getIdentifier("STR_"+area_code, "string", NaviViewManager.GetViewManager().getPackageName());					       					        	
					        				
					        				//if no resources, will throw an exception
					        				String area_name = NaviViewManager.GetViewManager().getResources().getString(resid);
					        				if(uiInfoslist.eDownloadDataType == UIOfflinePackageControlJNI.UI_OLPC_DDT_MAP_DATA){
					        					mapDataList.get(oflindex).setOflMapData(ofl.iAreaCode, area_name, ofl.lFileTotalSize, ofl.lCurDownloadSize, ofl.ePackageUpdateState);					        					
					        					oflindex++;
					        					break;
					        				}else if(uiInfoslist.eDownloadDataType == UIOfflinePackageControlJNI.UI_OLPC_DDT_SEARCH_DATA){	
					        					//if this package is electronic police package.
					        					if(1500000000 == mapDataList.get(oflindex).getMapAreaCode()){
					        						mapDataList.get(oflindex).setOflSrchData(0, "", 0,0, UIOfflinePackageControlJNI.UI_OLPC_PUS_COMPLETED);					        					
					        						oflindex++;
					        					}					        	
				        						mapDataList.get(oflindex).setOflSrchData(ofl.iAreaCode, area_name, ofl.lFileTotalSize, ofl.lCurDownloadSize, ofl.ePackageUpdateState);
				        						oflindex++;					        					
					        					break;
					        				}else{
					        					
					        				}
					        				
					        			}
					        		}
				        		}
				        	} 
				        	//next element 				   
				        	eventType = xpp.next(); 			    
				        } 
				        
				        //close stream
				        if (slideInputStream != null) {
				        	slideInputStream.close();
				        	slideInputStream = null;
						}
					}
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					
				}
			}
		}	

	private void startListofDownloading (){
		OfflineDlStatus = PConnectReceiver.getConnectType();
		if(OfflineDlStatus == PConnectReceiver.CONNECT_TYPE_WIFI)
		{
			for(OfflinePackageInfo ofl :downloadingList){
				if(ofl.getUpdateState() == OfflinePackageInfo.W3_US_DOWNLOADING){
					if(ofl.getOfflineSrchState() == OfflinePackageInfo.W3_US_DOWNLOADING){
						startDownloadOfflinePackage(ofl.getAreaCode(),OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
					}else{
						startDownloadOfflinePackage(ofl.getAreaCode(),OfflinePackageInfo.OFFLINE_MAP_TYPE);
					}
				}
			}
		}
	
	}

	public void deleteOfflinePackage(OfflinePackageInfo ofl){
		if(ofl.getSrchAreaCode() != 0){
			pauseDownloadOfflinePackage(ofl.getSrchAreaCode(),OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
		}
		if(ofl.getMapAreaCode() != 0){
			pauseDownloadOfflinePackage(ofl.getMapAreaCode(),OfflinePackageInfo.OFFLINE_MAP_TYPE);
		}
		if(ofl.getSrchAreaCode() != 0){
			UIOfflinePackageControlJNI.RequestOperateOfflinePackage(OfflineOperateIDManager.getInstance().createOperateId(),
					OfflinePackageInfo.OFFLINE_SEARCH_TYPE, ofl.getSrchAreaCode(), UIOfflinePackageControlJNI.UI_OLPC_DOPT_DELETE_PACKAGE);
		}
		if(ofl.getMapAreaCode() != 0){
			UIOfflinePackageControlJNI.RequestOperateOfflinePackage(OfflineOperateIDManager.getInstance().createOperateId(),
					OfflinePackageInfo.OFFLINE_MAP_TYPE, ofl.getMapAreaCode(), UIOfflinePackageControlJNI.UI_OLPC_DOPT_DELETE_PACKAGE);
		}
		ofl.setMapOflState(OfflinePackageInfo.W3_US_NO_PACKAGE);
		ofl.setSearchOflState(OfflinePackageInfo.W3_US_NO_PACKAGE);
		ofl.setDownloadSize(0, OfflinePackageInfo.OFFLINE_MAP_TYPE);
		ofl.setDownloadSize(0, OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
	}
	/*
	 * after press download button use this function.  
	 */
	public boolean DownloadOfflinePackageOnClick(int index,ArrayList<OfflinePackageInfo> currentList){
		//in order to notify UI when occur click event.then update delete button state.
		//--------------------------------------
		NSTriggerInfo nsTriggerInfo = new NSTriggerInfo();
		nsTriggerInfo.SetTriggerID(NSTriggerID.UIC_MN_TRG_DL_MAP_IS_DOWNLOAD_DATA);
		MenuControlIF.Instance().TriggerForScreen(nsTriggerInfo);
		//--------------------------------------
		boolean returnValue = false ;
		OfflinePackageInfo ofl = currentList.get(index);
		UIOflLog.TAG_OPERATE("package map state : "+ofl.getOfflinemapState());
		UIOflLog.TAG_OPERATE("package srch state : "+ofl.getOfflineSrchState());
		switch(ofl.getUpdateState())
		{
		case OfflinePackageInfo.W3_US_COMPLETED:
			break;
		case OfflinePackageInfo.W3_US_DOWNLOADING:
			if(judgeisthereallworldpackage() != IN_DOWNLOADED_LIST_STATUS && ofl.getAreaCode() 
			!= 1200000000){
				return false;
			}
			if(ofl.getCurDownloadingType() == OfflinePackageInfo.OFFLINE_SEARCH_TYPE){
				pauseDownloadOfflinePackage(ofl.getSrchAreaCode(),OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
				ofl.setSearchOflState(OfflinePackageInfo.W3_US_PAUSE);
			}else{
				pauseDownloadOfflinePackage(ofl.getMapAreaCode(),OfflinePackageInfo.OFFLINE_MAP_TYPE);
				ofl.setMapOflState(OfflinePackageInfo.W3_US_PAUSE);
			}
			if(ofl.getAreaCode() != 1200000000){
				startWaitForDownloadPackage();
			}
			break;
		case OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD:
			if(judgeisthereallworldpackage() != IN_DOWNLOADED_LIST_STATUS && ofl.getAreaCode() 
			!= 1200000000){
				return false;
			}
			if(ofl.getOfflineSrchState() == OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD){
				pauseDownloadOfflinePackage(ofl.getSrchAreaCode(),OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
				ofl.setSearchOflState(OfflinePackageInfo.W3_US_PAUSE);
			}else{
				pauseDownloadOfflinePackage(ofl.getMapAreaCode(),OfflinePackageInfo.OFFLINE_MAP_TYPE);
				ofl.setMapOflState(OfflinePackageInfo.W3_US_PAUSE);
			}
			break;
		case OfflinePackageInfo.W3_US_CAN_UPDATE:
			break;
		case OfflinePackageInfo.W3_US_NO_PACKAGE:
//			if(dataItem.getParentCode() == 0 || dataItem.getParentCode() == 1 ||(dataItem.getTypeCode()== 2 && isSearchState)){
//				undownloadList.remove(dataItem.getAreaCode());	
//				returnValue = true;
//			}else{
				
//				// if pressed allcity item .
//				if(dataItem.getITag() == 4){
//					for(downloadDataItem d : item.getChildGroup()){
//						if(d.getITag() != 4){
//							downloadingList.add(d);
//							if (isHasDownLoading()) {
//								d.getOfflinePackageInfo().setUpdateState(OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD);
//								PackageDownloadManager.waitingForDownloadOfflinePackage(d.getOfflinePackageInfo());
//							}
//							else{
//								d.getOfflinePackageInfo().setUpdateState(OfflinePackageInfo.W3_US_DOWNLOADING);
//								startDownloadOfflinePackage(downloadingList.indexOf(d));
//							}
//						}
//					}
//					int size = item.getChildGroup().size();
//					for(int i = size-1; i >= 0; i--){
//						 item.getChildGroup().remove(i);
//						 returnValue = true;
//					}
//					break;
//				}
//			downloadDataItem item = undownloadList.get(dataItem.getParentCode());
//			item.removeChildItem(dataItem);
//			returnValue = true;
//			}
			undownloadList.remove(ofl.getAreaCode());
			returnValue = true;
			downloadingList.add(ofl);
			if (isHasDownLoading()) {
				if(ofl.getOfflineSrchState() == OfflinePackageInfo.W3_US_NO_PACKAGE){
					ofl.setSearchOflState(OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD);
					waitingForDownloadOfflinePackage(ofl.getSrchAreaCode(), OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
				}else{
					ofl.setMapOflState(OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD);
					waitingForDownloadOfflinePackage(ofl.getMapAreaCode(), OfflinePackageInfo.OFFLINE_MAP_TYPE);
				}
			}
			else{
				if(ofl.getOfflineSrchState() == OfflinePackageInfo.W3_US_NO_PACKAGE){
					ofl.setSearchOflState(OfflinePackageInfo.W3_US_DOWNLOADING);
					startDownloadOfflinePackage(ofl.getSrchAreaCode(), OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
				}else{
					ofl.setMapOflState(OfflinePackageInfo.W3_US_DOWNLOADING);
					startDownloadOfflinePackage(ofl.getMapAreaCode(), OfflinePackageInfo.OFFLINE_MAP_TYPE);
				}
			}
			break;
		case OfflinePackageInfo.W3_US_PAUSE:
			if(judgeisthereallworldpackage() != IN_DOWNLOADED_LIST_STATUS && ofl.getAreaCode() 
			!= 1200000000){
				return false;
			}
			if (isHasDownLoading()) {
				if(ofl.getOfflineSrchState() == OfflinePackageInfo.W3_US_PAUSE){
					ofl.setSearchOflState(OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD);
					waitingForDownloadOfflinePackage(ofl.getSrchAreaCode(), OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
				}else{
					ofl.setMapOflState(OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD);
					waitingForDownloadOfflinePackage(ofl.getMapAreaCode(), OfflinePackageInfo.OFFLINE_MAP_TYPE);
				}
			}
			else{
				if(ofl.getOfflineSrchState() == OfflinePackageInfo.W3_US_PAUSE){
					ofl.setSearchOflState(OfflinePackageInfo.W3_US_DOWNLOADING);
					startDownloadOfflinePackage(ofl.getSrchAreaCode(), OfflinePackageInfo.OFFLINE_SEARCH_TYPE);
				}else{
					ofl.setMapOflState(OfflinePackageInfo.W3_US_DOWNLOADING);
					startDownloadOfflinePackage(ofl.getMapAreaCode(), OfflinePackageInfo.OFFLINE_MAP_TYPE);
				}
			}
			break;
			default:
				break;
		}
		return returnValue;
	}
	public ArrayList<OfflinePackageInfo> getDownloadedList() {
		return downloadedList;
	}

	public ArrayList<OfflinePackageInfo> getDownloadingList() {
		return downloadingList;
	}

	public SparseArray<OfflinePackageInfo> getUndownloadList() {
		return undownloadList;
	}

	/**
	 * judge whether has DownLoadingMap.return true is exist ,false is not exist.
	 * @return	Boolean 
	 */
	public boolean isHasDownLoading(){
		for(OfflinePackageInfo d : downloadingList){
			if(d.getUpdateState() == OfflinePackageInfo.W3_US_DOWNLOADING){
				return true;
			}
		}
		return false;
	}
//	//0 is groupPosition,1 is childPosition.
//	private OfflinePackageInfo indexToOfflinePackageInfo(int index,ArrayList<OfflinePackageInfo> currentList){
//		int groupPosition = index>>8;
//		int childPosition = index&255;
//		OfflinePackageInfo ofl;
//		if(childPosition == 255) {
//			ofl = currentList.get(groupPosition);
//		} else {
//			ofl = currentList.get(groupPosition).getChildeAt(childPosition).getOfflinePackageInfo();
//        }
//		return ofl;
//	}
//	
//	private downloadDataItem indexTodownloadDataItem(int index,ArrayList<downloadDataItem> currentList){
//		int groupPosition = index>>8;
//		int childPosition = index&255;
//		downloadDataItem dataItem;
//		if(childPosition == 255) {
//			dataItem = currentList.get(groupPosition);
//		} else {
//			dataItem = currentList.get(groupPosition).getChildeAt(childPosition);
//        }
//		return dataItem;
//	}
	/*
	 * 
	 * return 0
	 */
	public int startDownloadOfflinePackage(int areacode,int datatype){
		int operateid = OfflineOperateIDManager.getInstance().createOperateId();
		UIOflLog.TAG_OPERATE("startDownloadOfflinePackage areacode:"+areacode+"datatype "+datatype);
		UIOfflinePackageControlJNI.RequestOperateOfflinePackage(operateid,
				datatype, areacode, UIOfflinePackageControlJNI.UI_OLPC_DOPT_START_DOWNLOAD);
		return 0;
	}
	public void pauseDownloadOfflinePackage(int areacode,int datatype){
		UIOfflinePackageControlJNI.RequestOperateOfflinePackage(OfflineOperateIDManager.getInstance().createOperateId(),
				datatype, areacode, UIOfflinePackageControlJNI.UI_OLPC_DOPT_PAUSE_DOWNLOAD);
	}
	public void waitingForDownloadOfflinePackage(int areacode,int datatype){
		UIOfflinePackageControlJNI.RequestOperateOfflinePackage(OfflineOperateIDManager.getInstance().createOperateId(),
				datatype, areacode, UIOfflinePackageControlJNI.UI_OLPC_DOPT_WAITING_FOR_DOWNLOADING);
	}
	
	public ArrayList<OfflinePackageInfo> getMapDataList(){
		return mapDataList;
	}

	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		TriggerForWinscape(triggerInfo);
		return false;
	}
	public boolean isRequsetlistOK() {
		return isRequsetlistOK;
	}
	public void setRequsetlistOK(boolean isRequsetlistOK) {
		this.isRequsetlistOK = isRequsetlistOK;
	}
}
