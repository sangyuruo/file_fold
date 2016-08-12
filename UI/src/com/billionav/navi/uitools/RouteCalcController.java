package com.billionav.navi.uitools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.billionav.jni.UILocationControlJNI;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIPathControlJNI;
import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.intentOpen.IntentOpenCtrl;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.route.ADT_Route_Calculating;
import com.billionav.navi.system.BuleToothMessageQueue;
import com.billionav.navi.uicommon.UIC_RouteCommon;
import com.billionav.navi.uitools.PointTools.PointDetailInfoListener;
import com.billionav.ui.R;
import com.billionav.voicerecog.POIInfo;

public class RouteCalcController {
	
	public class RoutePOIPoint{
		
		public RoutePOIPoint(){
			clear();
		}
		
		public String name;
		public String address;
		public String telNumber;
		public long[] centerLonlat;
		
		public long linkID;
		public byte sideInfo;
		
		public void clear(){
			name = "";
			address = "";
			telNumber = "";
			linkID = -1;
			sideInfo = -1;
			centerLonlat = new long[2];
		}
	}
	
	public interface RouteDetailInfoUpdateListener{
		void onRouteDetailInfoUpdate();
	}
	private RouteDetailInfoUpdateListener routeDetailUpdateListener;
	private RoutePOIPoint routePOIPoint = new RoutePOIPoint();
	
	private static RouteCalcController 	instance = new RouteCalcController();
//	private jniLocationIF				locationIF = new jniLocationIF();
	private UIPathControlJNI				pathControl = new UIPathControlJNI();
	
	public static final int ROUTE_POINT_FIND_PURPOSE_NONE = -1;
	public static final int ROUTE_POINT_FIND_PURPOSE_UPDATE_START = 0;
	public static final int ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA = 1;
	public static final int ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST = 2;
	public static final int ROUTE_POINT_FIND_PURPOSE_ADD_START = 4;
	public static final int ROUTE_POINT_FIND_PURPOSE_ADD_VIA = 5;
	public static final int ROUTE_POINT_FIND_PURPOSE_ADD_DEST = 6;
	public static final int ROUTE_POINT_FIND_PURPOSE_SHOW = 7;
	public static final int ROUTE_POINT_FIND_PURPOSE_NO_ROUTE_EDIT = 8;
	private int routePointFindPurpose = ROUTE_POINT_FIND_PURPOSE_NONE;
	
	public static final int DELETE_RESULT_FAILED = -1;
	public static final int DELETE_RESULT_NORMAL = 0;
	public static final int DELETE_RESULT_WAYPOINT_TO_DEST = 1;
	public static final int DELETE_RESULT_WAYPOINT_TO_START = 2;
	public static final int DELETE_RESULT_DEST_TO_START = 3;
	
	public static final int MAX_ROUTE_POINT_NUM = 12;
	
	public static int selectedIndexInEditMode = -1;
	public static boolean isInShowMode = false;
	
	private boolean isInEditMode = false;
	private boolean isCalRouteEnable;
	public RouteCalcController(){
//		syncRouteInfoFromPath();
	}
	public class RoutePointData extends Object{
		public long[] lonlat;
		public String name;
		public String tel;
		public String address;
		public RoutePointData(){
			lonlat = new long[2];
			name = "";
			tel = "";
			address = "";
		}
	}
	
	public void setRouteDetailUpdateListener(RouteDetailInfoUpdateListener l) {
		this.routeDetailUpdateListener = l;
	}
	public void removeRouteDetailUpdateListener() {
		this.routeDetailUpdateListener = null;
	}
//	private boolean isAllRouteMode = false;
	private ArrayList<RoutePointData> routeDataList = new ArrayList<RoutePointData>();
	private ArrayList<RoutePointData> allRouteDataList = new ArrayList<RoutePointData>();
	
	public static RouteCalcController instance(){
		return instance;
	}

	public void setRoutePointFindPurpose(int purpose){
		if(purpose == ROUTE_POINT_FIND_PURPOSE_SHOW ){
			isInShowMode = true;
		}
		routePointFindPurpose = purpose;
	}
	
	public void finishRouteCalculate(){
		routePointFindPurpose = ROUTE_POINT_FIND_PURPOSE_NONE;
	}
	
	
	/**
	 * For search
	 * 
	 * @return {@link}ROUTE_POINT_FIND_PURPOSE_NONE  need use {@link}setRoutePointFindPurpose(ROUTE_POINT_FIND_PURPOSE_ADD_DEST)
	 * 			
	 */
	public int getRoutePointFindPurpose(){
		return routePointFindPurpose;
	}
	
	/**
	 * Judge if the way point can be added.
	 * @return true : can add way point,false:can't add way point
	 */
	public boolean isEnableToAddWayPoint(){

		if(routePointFindPurpose == ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA){
			return true;
		}
		int num = pathControl.GetNumPathPoint(UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID, UIPathControlJNI.UIC_PT_NUM_POINT_ALL);

		boolean isEditMode = true;//routePointFindPurpose != ROUTE_POINT_FIND_PURPOSE_NONE;
		boolean isRouteExist = routePointFindPurpose != ROUTE_POINT_FIND_PURPOSE_NONE || pathControl.GetRouteStatus() == UIPathControlJNI.UIC_PT_ROUTE_STATUS_COMP;
//		return isRouteExist && isEditMode && (num >= 2 && num <MAX_ROUTE_POINT_NUM);
		return  ( num <MAX_ROUTE_POINT_NUM); 
	}
	
	public int getRoutePointNum(){
		
		return routeDataList.size();
	}
	
	public final ArrayList<RoutePointData> getRoutePointDataList(){
		return routeDataList;
	}
	
	public long[] getCarLonLat(){
		long[] carLonLat = UILocationControlJNI.GetPosition(0);
		return carLonLat;
	}
	
	public void pointCurrentPositionToPOI(){
		 String name = MenuControlIF.Instance().GetCurrentActivity().getString(R.string.STR_MM_03_01_01_06);
		 long[] lonlat = getCarLonLat();
		 pointDataToPOI(name, lonlat);
	}

	public void pointMapCenterToPOI(){
		long[] lonlat = new long[2];
		lonlat[0] = UIMapControlJNI.GetCenterLonLat()[0];
		lonlat[1] = UIMapControlJNI.GetCenterLonLat()[1];
		pointDataToPOI(NSViewManager.GetViewManager().getString(R.string.STR_MM_02_02_04_15), lonlat);
	}
	
	public void pointDataToPOI(RoutePointData data){
		pointDataToPOI(data.name, data.lonlat, data.address, data.tel);
	}

	public void pointDataToPOI(String name, long[] lonlat, String address, String tel){
		pointDataToPOI(name, lonlat[0], lonlat[1], address, tel);
	}
	public void pointDataToPOI(String name, long[] lonlat){
		pointDataToPOI(name, lonlat[0], lonlat[1]);
	}
	public void requestDetailLonLatName(final int index){
		PointTools.getInstance().requestCenterName(new PointDetailInfoListener() {
			public void onPointDetailInfoRequstFinished(String name, String address, String tel, long distance){
				if(TextUtils.isEmpty(name)){
					name = NSViewManager.GetViewManager().getResources().getString(R.string.STR_MM_02_02_04_15);
				}
				setPOIDataInfo(name, address, tel);
//				routeDataList.get(index).name = name;
				updateRoutePointByPurposeAtIndex(index);
				if(null != routeDetailUpdateListener) {
					routeDetailUpdateListener.onRouteDetailInfoUpdate();
				}
				setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE);
			}
		});
	}
	public void searchPointToPOI(SearchPointBean point) {
//		 if(point.getSearchkind() == TouchMapControl.TYPE_LONGPRESS) {
//			 UISearchControlJNI.Instance().PrepareForCalcRoute(UISearchControlJNI.UIC_SCM_SRCH_TYPE_PIN_POINT, point.getIndex());
//		 } else {
//			 UISearchControlJNI.Instance().PrepareForCalcRoute(point.getSearchkind(), point.getIndex());
//		 }
		 
		 pointDataToPOI(point.getName(), point.getLongitude(), point.getLatitude(), point.getAddress(), point.getTelNo());
	}
	
	private void voicePointToPOI(POIInfo info) {
		routePOIPoint.clear();
		routePOIPoint.name = (info.getName()==null ? "" : info.getName());
		routePOIPoint.centerLonlat[0] = Long.parseLong(info.getLon());
		routePOIPoint.centerLonlat[1] = Long.parseLong(info.getLat());
		routePOIPoint.address = (info.getAddr()==null ? "" : info.getAddr());
		routePOIPoint.telNumber = (info.getTel() == null ? "" : info.getTel());
		 
		 try{
			 long linkid = Long.parseLong(info.getLinkid());
			 
			 String sideinfo = info.getLinkside();
			 byte linkside = 0;
			 if("N".equals(sideinfo)){
				 linkside = 0;
			 }else if("R".equals(sideinfo)){
				 linkside = 1;
			 } else if("L".equals(sideinfo)){
				 linkside = 2;
			 }

			 routePOIPoint.linkID = linkid;
			 routePOIPoint.sideInfo = linkside;
		 } catch(Exception e) {}
		 
//		 data.CreateUUID();
	}
	
	/**
	 * For search to route
	 * @param name
	 * @param lon
	 * @param lat
	 */
	public void pointDataToPOI(String name, long lon, long lat){
		routePOIPoint.clear();
		routePOIPoint.name = name;
		routePOIPoint.centerLonlat[0] = lon;
		routePOIPoint.centerLonlat[1] = lat;
//		 data.CreateUUID();
	}
	/**
	 * For search to route
	 * @param name
	 * @param lon
	 * @param lat
	 * @param address
	 * @param tel
	 */
	public void pointDataToPOI(String name, long lon, long lat, String address, String tel){
		routePOIPoint.clear();
		routePOIPoint.name = name;
		routePOIPoint.centerLonlat[0] = lon;
		routePOIPoint.centerLonlat[1] = lat;
		routePOIPoint.address = address;
		routePOIPoint.telNumber = tel;
//		 data.CreateUUID();
	}
	public void setPOIDataName(String r){
		routePOIPoint.name = (r);
	}
	public void setPOIDataInfo(String name, String address, String tel){
		routePOIPoint.name = name;
		routePOIPoint.address = address;
		routePOIPoint.telNumber = tel;
	}
	private RoutePointData getCarData(){
		RoutePointData start = new RoutePointData();
		start.name = MenuControlIF.Instance().GetCurrentActivity().getString(R.string.STR_MM_03_01_01_06);
		long[] lonlat = getCarLonLat();
		start.lonlat = lonlat;
		return start;
	}
	
	private RoutePointData getCurrentRoutePointDataFromMemory(){
		RoutePointData wData = new RoutePointData();
		wData.name = routePOIPoint.name;
		long[] lonlat = new long[2];
		lonlat[0] = routePOIPoint.centerLonlat[0];
		lonlat[1] = routePOIPoint.centerLonlat[1];
		wData.lonlat = lonlat;
		wData.address = routePOIPoint.address;
		wData.tel = routePOIPoint.telNumber;
		return wData;
	}
	
	public void initRouteInfoForNewRoute(){
		isCalRouteEnable = true;
		pathControl.InitRouteInfo();
		routeDataList.clear();
		routeDataList.add(getCarData());
		pointDataToPOI(getCarData());
		setPathPoint(UIPathControlJNI.PDM_DEST_ID_ORG_START);
		setPathPoint(UIPathControlJNI.PDM_DEST_ID_START);
	}
	
	public void initRouteInfoByPurpose(int purpose){
		boolean isRouteExsit = UIC_RouteCommon.Instance().isRouteExistAndGuideOn();
		if(isRouteExsit){
			isCalRouteEnable = true;
			initRouteInfoForEdit();
		}else{
			isCalRouteEnable = false;
			initRouteInfoForBlankRoute();
		}
		
		switch (purpose) {
			case ROUTE_POINT_FIND_PURPOSE_ADD_START:
				updateRoutePointAtIndex(0);
				break;
			case ROUTE_POINT_FIND_PURPOSE_ADD_DEST:
				updateRoutePointAtIndex(getRoutePointNum()-1);
				isCalRouteEnable = true;
				break;
			case ROUTE_POINT_FIND_PURPOSE_ADD_VIA:
				addWayPoint();
				break;
			case ROUTE_POINT_FIND_PURPOSE_UPDATE_START:
			case ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA:
			case ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST:
				isCalRouteEnable = true;
				updateRoutePointAtIndex(selectedIndexInEditMode);
				break;
			case ROUTE_POINT_FIND_PURPOSE_SHOW:
			case ROUTE_POINT_FIND_PURPOSE_NO_ROUTE_EDIT:
			case ROUTE_POINT_FIND_PURPOSE_NONE:
				default:break;
		}
		
		
	}
	private void initRouteInfoForBlankRoute(){
		if(isInEditMode) { return;
        }
		isInEditMode = true;
		pathControl.InitRouteInfo();
		routeDataList.clear();
		RoutePointData aTemp = getCurrentRoutePointDataFromMemory();
		pointCurrentPositionToPOI();
		addStart();
		addEmptyDestination();
		pointDataToPOI(aTemp);
	}
	public void initRouteInfoForEdit(){
		if(isInEditMode) { 
			return;
        }
		isInEditMode = true;
		syncRouteInfoFromPath();
	}
	public void syncRouteInfoFromPath(){
		pathControl.SetRerouteInfo();
		routeDataList.clear();
		int listNum = pathControl.GetNumPathPoint(
				UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID,
				UIPathControlJNI.UIC_PT_NUM_POINT_ALL);
		int passdNum = pathControl.GetNumPathPoint(
				UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID,
				UIPathControlJNI.UIC_PT_NUM_POINT_ALL_VIA)
				- pathControl.GetNumPathPoint(
						UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID,
						UIPathControlJNI.UIC_PT_NUM_POINT_VIA_EXCEPT_PASSED);
		
		for (int num = 0; num < listNum; num++) {
			RoutePointData data =new RoutePointData();
			long[] lonlat = pathControl.GetCenterLonLat(UIPathControlJNI.UIC_PT_FIND_OBJ_UI, num, true);
			data.lonlat = lonlat; 
			data.name = pathControl.GetPointName(UIPathControlJNI.UIC_PT_FIND_OBJ_UI, num, true);
			routeDataList.add(data);
		}
		
//		if(!pathControl.IsStartSpecify()){
//			RoutePointData tempData = getCurrentRoutePointDataFromMemory();
//			pointDataToPOI(getCarData());
//			updateRoutePointAtIndex(0);
//			pointDataToPOI(tempData);
//		}
		
		//clear passed point
		for (int i = 0; i < passdNum; i++) {
			routeDataList.remove(passdNum - i);
			pathControl.DeletePathPoint(1);
		}
	}
	
	public boolean updateRoutePointByPurposeAtIndex(int index){
		switch (routePointFindPurpose) {
		case ROUTE_POINT_FIND_PURPOSE_ADD_START:
			addStart();
			break;
		case ROUTE_POINT_FIND_PURPOSE_ADD_DEST:
			addDestination();
			isCalRouteEnable = true;
			break;
		case ROUTE_POINT_FIND_PURPOSE_ADD_VIA:
			addWayPoint();
			break;
		case ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST:
			isCalRouteEnable = true;
		case ROUTE_POINT_FIND_PURPOSE_UPDATE_START:
		case ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA:
		default:
			return updateRoutePointAtIndex(index);
		}
		
		return true;
	}
	
	public boolean updateRoutePointAtIndex(int index){
		
		if(index < 0 || index >= routeDataList.size()){
			return false;
		}
		
		RoutePointData data = getCurrentRoutePointDataFromMemory();
		routeDataList.set(index, data);
		
		int size = routeDataList.size();
		if(index == 0){
			setPathPoint(UIPathControlJNI.PDM_DEST_ID_ORG_START);
			setPathPoint(UIPathControlJNI.PDM_DEST_ID_START);
		}else {
			if (size >= 2) {
				if(index == size-1){
					setPathPoint(UIPathControlJNI.PDM_DEST_ID_GOAL);
				}else{
					switch(index){
					case 1:
						setPathPoint(UIPathControlJNI.PDM_DEST_ID_VIA_1);
						break;
					case 2:
						setPathPoint(UIPathControlJNI.PDM_DEST_ID_VIA_2);
						break;
					case 3:
						setPathPoint(UIPathControlJNI.PDM_DEST_ID_VIA_3);
						break;
					case 4:
						setPathPoint(UIPathControlJNI.PDM_DEST_ID_VIA_4);
						break;
					case 5:
						setPathPoint(UIPathControlJNI.PDM_DEST_ID_VIA_5);
						break;
					case 6:
						setPathPoint(UIPathControlJNI.PDM_DEST_ID_VIA_6);
						break;
					case 7:
						setPathPoint(UIPathControlJNI.PDM_DEST_ID_VIA_7);
						break;
					case 8:
						setPathPoint(UIPathControlJNI.PDM_DEST_ID_VIA_8);
						break;
					case 9:
						setPathPoint(UIPathControlJNI.PDM_DEST_ID_VIA_9);
						break;
					case 10:
						setPathPoint(UIPathControlJNI.PDM_DEST_ID_VIA_10);
						break;
					}
					
				}
			} 
		}
		return true;
	}
	
	public void exchangeDestAndStart() {
		int listNum = routeDataList.size();
		int[] pointSeq = new int[listNum];
		for (int i = 0; i < listNum; i++) {
			pointSeq[i] = i;
		}
		
		pointSeq[0] = listNum -1;
		pointSeq[listNum -1] = 0;
		
		RoutePointData startData = routeDataList.get(0);
		RoutePointData destData = routeDataList.get(listNum -1);
		
		routeDataList.remove(startData);
		routeDataList.remove(destData);
		
		routeDataList.add(0, destData);
		routeDataList.add(startData);
		
		pathControl.MovePathPoint(listNum, pointSeq);
	}
	
	public int deletePointAtIndex(int index){
		int totalNum = routeDataList.size();
		if(index < totalNum && index >= 0){
			
			if(index == totalNum - 1 && totalNum > 2){
				routeDataList.remove(index);
				pathControl.DeletePathPoint(index - 1);
				return DELETE_RESULT_WAYPOINT_TO_DEST;
			}else if(index == 0 && totalNum == 2){
				int[] seq = {1,0};
				routeDataList.remove(index);
				pathControl.MovePathPoint(totalNum, seq);
				pathControl.DeletePathPoint(1);
				return DELETE_RESULT_DEST_TO_START;
			}else if(index == 0 &&  totalNum > 2){
				int[] seq = new int[totalNum];
				for(int i = 0 ;i < totalNum; i++){
					seq[i] = i;
				}
				seq[0] = 1;
				seq[1] = 0;
				pathControl.MovePathPoint(totalNum, seq);
				routeDataList.remove(0);
				pathControl.DeletePathPoint(1);
				return DELETE_RESULT_WAYPOINT_TO_START;
			}else{
				routeDataList.remove(index);
				pathControl.DeletePathPoint(index);
				return DELETE_RESULT_NORMAL;
			}
		}else{
			return DELETE_RESULT_FAILED;
		}
	}
	
	public boolean switchDataWithIndex(int from, int to){
		if(from < 0 || from >= routeDataList.size() || to < 0 || to >= routeDataList.size()){
			return false;
		}
		
		RoutePointData fromData = routeDataList.get(from);
		routeDataList.remove(from);
		routeDataList.add(to, fromData);
		
		int listNum = pathControl.GetNumPathPoint(
				UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID,
				UIPathControlJNI.UIC_PT_NUM_POINT_ALL);
		int[] pointSeq = new int[listNum];
		for (int i = 0; i < listNum; i++) {
			if(i >= from && i < to){
				pointSeq[i] = i + 1;
			}else if(i == to){
				pointSeq[i] = pointSeq[from] - 1;
			}else{
				pointSeq[i] = i;
			}
		}
		
		pathControl.MovePathPoint(listNum, pointSeq);
		return true;
	}
	
	private void addStart(){
		routeDataList.add(getCurrentRoutePointDataFromMemory());
		setPathPoint(UIPathControlJNI.PDM_DEST_ID_ORG_START);
		setPathPoint(UIPathControlJNI.PDM_DEST_ID_START);
	}
	
	private void addDestination(){
		routeDataList.add(getCurrentRoutePointDataFromMemory());
		setPathPoint(UIPathControlJNI.PDM_DEST_ID_GOAL);
	}
	private void addEmptyDestination(){
		pointDataToPOI(new RoutePointData());
		routeDataList.add(getCurrentRoutePointDataFromMemory());
	}
	private void addWayPoint(){
		routeDataList.add(routeDataList.size() - 1, getCurrentRoutePointDataFromMemory());
		setPathPoint(UIPathControlJNI.PDM_DEST_ID_LAST_DEST);
	}
	
	
	public void startCalculatingAfterEdit(Context context){
		if(isCalRouteEnable){
			isInEditMode = false;
			isInShowMode = false;
			if(!context.getString(R.string.STR_MM_03_01_01_06).equals(routeDataList.get(0).name)){
				pathControl.SetStartSpecify(true);
			}else{
				pathControl.SetStartSpecify(false);
			}
			pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_VIA_EDITED_FINDING);
			MenuControlIF.Instance().setWinchangeWithoutAnimation();
			MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Route_Calculating.class);
		}
	}
	
	public void endEdit(){
		setRoutePointFindPurpose(ROUTE_POINT_FIND_PURPOSE_NONE);
		isInEditMode = false;
	}
	
	/**
	 * Calculate route from car position to the point that user set.
	 * 
	 * @param pointName
	 * @param lon
	 * @param lat
	 */
	public void rapidRouteCalculateWithData(String pointName, long lon, long lat){
		pathControl.InitRouteInfo();
		rapidRouteCalculateWithData(pointName, lon, lat, null, null);
	}
	
	/**
	 * Calculate route from car position to the point that user set sent by other program.
	 * 
	 * @param pointName
	 * @param lon
	 * @param lat
	 * @param address
	 * @param tel
	 */
	public void rapidRouteCalculateWithDataFromOtherProgram(String pointName, long lon, long lat) {
		rapidRouteCalculateWithData(pointName, lon, lat, null, null);
	}
	
	/**
	 * Calculate route from car position to the point that user set.
	 * 
	 * @param pointName
	 * @param lon
	 * @param lat
	 * @param address
	 * @param tel
	 */
	public void rapidRouteCalculateWithData(String pointName, final long lon, final long lat, String address, String tel){
		pathControl.InitRouteInfo();
		pointDataToPOI(getCarData());
		addStart();
		if(address == null || tel == null){
			pointDataToPOI(pointName, lon, lat);
		}else{
			pointDataToPOI(pointName, lon, lat, address, tel);
		}
		addDestination();
		makeSendPNTToCarAudioDialog(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
//				if(BuleToothMessageQueue.getInstance().getBluetoothConnState()) {
					byte[] ret = UISearchControlJNI.getDestNationPointProtoByte(routePOIPoint.name, routePOIPoint.address, 
							routePOIPoint.telNumber, (int)routePOIPoint.centerLonlat[0], (int)routePOIPoint.centerLonlat[1]);
					BroadcastSender.getInstanc().sendPointBroadcast(ret);
					Log.d("test","pnt data sent from bt length="+ret.length);
//				} else if(NetTools.isNetEnable()) {
					UISearchControlJNI.sendDestNationPoint(routePOIPoint.name, routePOIPoint.address, 
							routePOIPoint.telNumber, (int)routePOIPoint.centerLonlat[0], (int)routePOIPoint.centerLonlat[1]);
					Log.d("test", "pnt data sent from WIFI");
//				} else {
//					Log.d("test", "pnt data sent failed ,no bt, no wifi");
//				}
					CustomToast.showToast(NSViewManager.GetViewManager(), "share data succeed!", 3000);
			}
		}, new DialogInterface.OnClickListener() {
			//Calculat Route按钮事件
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
//				UISearchControlJNI.Instance().OnSelectListItemRapidParkNearby(lon,lat);
//				clearTimeout();
//				startTimeout();
				pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_GO_HERE_FINDING);
				BundleNavi.getInstance().putBoolean("route", true);
				MenuControlIF.Instance().setWinchangeWithoutAnimation();
				MenuControlIF.Instance().ForwardWinChange(ADT_Route_Calculating.class);
			}
		});
	}
	public void rapidRouteCalculateWithDataFromNavi(String pointName, final long lon, final long lat, String address, String tel){
		pathControl.InitRouteInfo();
		RoutePointData me = getCarData();
		//me.lonlat[0] = -78325642;
		//me.lonlat[1] = 38442914;
		pointDataToPOI(me);
		Log.i("SDL", "------------------------");
		Log.i("SDL", "Lon:" + me.lonlat[0]);
		Log.i("SDL", "Lat:" + me.lonlat[1]);
		addStart();
		if(address == null || tel == null){
			pointDataToPOI(pointName, lon, lat);
		}else{
			pointDataToPOI(pointName, lon, lat, address, tel);
		}
		Log.i("SDL", "------------------------");
		Log.i("SDL", "Lon:" + lon);
		Log.i("SDL", "Lat:" + lat);
		addDestination();
		pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_GO_HERE_FINDING);
		BundleNavi.getInstance().putBoolean("route", true);
		MenuControlIF.Instance().setWinchangeWithoutAnimation();
		MenuControlIF.Instance().ForwardWinChange(ADT_Route_Calculating.class);
	}
	
	public boolean parkSearching = false;
	
	private Handler searchTimeoutHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_GO_HERE_FINDING);
			parkSearching = false;
		}
	};
	
	private void startTimeout(){
		searchTimeoutHandler.sendEmptyMessageDelayed(0, 8000);
		parkSearching = true;
	}
	
	public void clearTimeout(){
		searchTimeoutHandler.removeMessages(0);
		parkSearching = false;
	}
	
	public void rapidGetRouteRequest(String name, long lon , long lat){
		pointDataToPOI(name, lon, lat);
		setPathPoint(UIPathControlJNI.PDM_DEST_ID_PARKING);
		
		pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_GO_HERE_FINDING);
		BundleNavi.getInstance().putBoolean("route", true);
		MenuControlIF.Instance().setWinchangeWithoutAnimation();
		MenuControlIF.Instance().ForwardWinChange(ADT_Route_Calculating.class);
	}
	
	
	public void rapidRouteCalculateToMapCenterWithoutPopup(){
		long[] lonlat = new long[2];
		lonlat[0] = UIMapControlJNI.GetCenterLonLat()[0];
		lonlat[1] = UIMapControlJNI.GetCenterLonLat()[1];
		pathControl.InitRouteInfo();
		
		pointDataToPOI(getCarData());
		addStart();
		
		pointMapCenterToPOI();
		addDestination();
		
		pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_GO_HERE_FINDING);
		BundleNavi.getInstance().putBoolean("route", true);
		MenuControlIF.Instance().setWinchangeWithoutAnimation();
		MenuControlIF.Instance().ForwardWinChange(ADT_Route_Calculating.class);
	}
	
	public void rapidGetRouteInfoWithData(String pointName, long lon, long lat, String address, String tel){
		pathControl.InitRouteInfo();
		pointDataToPOI(getCarData());
		addStart();
		
		if(address == null || tel == null){
			pointDataToPOI(pointName, lon, lat);
		}else{
			pointDataToPOI(pointName, lon, lat, address, tel);
		}
		addDestination();
		pathControl.StartUIPathFindingForRouteInfo();
	}
	
	/**
	 * Calculate route from car position to home.
	 * 
	 */
	public void rapidRouteCalculateToHome(){
		UIPointData homedata = UIPointControlJNI.Instance().getHome();
		if(homedata == null) {
			return;
		}
		RouteCalcController.instance().rapidRouteCalculateWithData(homedata.getName(), homedata.getLon(), homedata.getLat(), homedata.getAddress(), homedata.getTelNo());
	}
	
	
	/**
	 * Calculate route from car position to company.
	 * 
	 */
	public void rapidRouteCalculateToCompany(){
		UIPointData companydata = UIPointControlJNI.Instance().getCompany();
		if(companydata == null) {
			return;
		}
		RouteCalcController.instance().rapidRouteCalculateWithData(companydata.getName(), companydata.getLon(), companydata.getLat(), companydata.getAddress(), companydata.getTelNo());
	}
	
	
	/**
	 * Calculate route from car position to the point that user set.
	 * 
	 * @param pointName
	 * @param lon
	 * @param lat
	 */
	public void rapidRouteCalculateWithVioce(POIInfo info){
		pathControl.InitRouteInfo();
		pointDataToPOI(getCarData());
		addStart();
		
		voicePointToPOI(info);
		addDestination();
		pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_GO_HERE_FINDING);
		BundleNavi.getInstance().putBoolean("route", true);
		MenuControlIF.Instance().setWinchangeWithoutAnimation();
		MenuControlIF.Instance().ForwardWinChange(ADT_Route_Calculating.class);
	}
	
	/**
	 * Calculate route after condition changed
	 */
	public void rerouteCalculateAfterConditionChanged(){
		pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_COMMON_REROUTE);
		MenuControlIF.Instance().setWinchangeWithoutAnimation();
		MenuControlIF.Instance().ForwardWinChange(ADT_Route_Calculating.class);
	}
	
	/**
	 * Calculate route from car position to the point that user set.
	 * 
	 * @param pointName
	 * @param lon
	 * @param lat
	 */
	public void rapidRouteCalculateWithVioce(POIInfo info, List<POIInfo> wayPoints){
		if(wayPoints == null) {
			rapidRouteCalculateWithVioce(info);
			return;
		}
		pathControl.InitRouteInfo();
		routeDataList.clear();
		
		pointDataToPOI(getCarData());
		addStart();
		
		voicePointToPOI(info);
		addDestination();
		
		for(POIInfo inf: wayPoints) {
			voicePointToPOI(inf);
			addWayPoint();
		}
		pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_VIA_EDITED_FINDING);
		BundleNavi.getInstance().putBoolean("route", true);
		MenuControlIF.Instance().setWinchangeWithoutAnimation();
		MenuControlIF.Instance().ForwardWinChange(ADT_Route_Calculating.class);
	}
	
	/**
	 * Calculate route from car position to the point that user set.
	 * 
	 * @param pointName
	 * @param lon
	 * @param lat
	 */
	public void rapidRouteCalculateForSearch(SearchPointBean point){

		rapidRouteCalculateWithData(point.getName(), point.getLongitude(), point.getLatitude(), point.getAddress(), point.getTelNo());

//		pathControl.InitRouteInfo();
//		pointDataToPOI(getCarData());
//		addStart();
//		
//		searchPointToPOI(point);
//		addDestination();
//		
//		pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_GO_HERE_FINDING);
//		BundleNavi.getInstance().putBoolean("route", true);
//		MenuControlIF.Instance().setWinchangeWithoutAnimation();
//		MenuControlIF.Instance().ForwardWinChange(ADT_Route_Calculating.class);
	}
	
	/**
	 * Calculate route from car position to current map center.
	 */
	public void rapidRouteCalculateToMapCenter(){
		long[] lonlat = new long[2];
		lonlat[0] = UIMapControlJNI.GetCenterLonLat()[0];
		lonlat[1] = UIMapControlJNI.GetCenterLonLat()[1];
		rapidRouteCalculateWithData(NSViewManager.GetViewManager().getString(R.string.STR_MM_02_02_04_15), lonlat[0], lonlat[1], null, null);
//		pathControl.InitRouteInfo();
//		
//		pointDataToPOI(getCarData());
//		addStart();
//		
//		pointMapCenterToPOI();
//		addDestination();
//		
//		pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_GO_HERE_FINDING);
//		BundleNavi.getInstance().putBoolean("route", true);
//		MenuControlIF.Instance().setWinchangeWithoutAnimation();
//		MenuControlIF.Instance().ForwardWinChange(ADT_Route_Calculating.class);
	}
	
	public String getPointNameAtIndex(int index){
		if(index < 0 || index >= routeDataList.size()){
			syncRouteInfoFromPath();
		}
		if(index < 0 || index >= routeDataList.size()){
			return "";
		}
		return routeDataList.get(index).name;
	}
	
	public long[] getPointLonLatAtIndex(int index){
		if(index < 0 || index >= routeDataList.size()){
			syncRouteInfoFromPath();
		}
		if(index < 0 || index >= routeDataList.size()){
			Log.d("UILog","UI routeDatalist size error, index = "+index);
			return new long[2];
		}
		return routeDataList.get(index).lonlat;
	}
	public boolean isCalcRouteEnable(){
		return isCalRouteEnable;
	}

	public void DeleteRoute() {
		routeDataList.clear();
		Log.d("test",routeDataList.size()+"");
		pathControl.DeleteRoute();
		
	}

	public void pointDefaultDestToPOI() {
		pointDataToPOI(getDefaultDest());
		setPathPoint(UIPathControlJNI.PDM_DEST_ID_GOAL);
	}
	private RoutePointData getDefaultDest() {
		RoutePointData dest = new RoutePointData();
		dest.name = MenuControlIF.Instance().GetCurrentActivity().getString(R.string.STR_MM_01_02_01_19);
		long[] lonlat =  new long[2]; 
		lonlat[0] = UIMapControlJNI.GetCenterLonLat()[0];
		lonlat[1] = UIMapControlJNI.GetCenterLonLat()[1];
		dest.lonlat = lonlat;
		return dest;
	}
	
	private void setPathPoint(int destID){
		
		pathControl.SetPathPoint(destID, 0, routePOIPoint.centerLonlat,
				routePOIPoint.centerLonlat, routePOIPoint.centerLonlat,
				routePOIPoint.name, routePOIPoint.address,
				routePOIPoint.telNumber);
	}
	
//	private void setPathPoint(int destID, int pointType){
//		pathControl.SetPathPoint(destID, 0, routePOIPoint.centerLonlat,
//				routePOIPoint.centerLonlat, routePOIPoint.centerLonlat,
//				routePOIPoint.name, routePOIPoint.address,
//				routePOIPoint.telNumber, pointType);
//	}
	
	public void makeSendPNTToCarAudioDialog(DialogInterface.OnClickListener pos, DialogInterface.OnClickListener neg) {
		Context c = NSViewManager.GetViewManager();
		CustomDialog dialog = new CustomDialog(c);
		dialog.setTitle(c.getString(R.string.MSG_DEMO_DIALOG_SYNC_TITLE));
		dialog.setMessage(c.getString(R.string.MSG_DEMO_DIALOG_SYNC_CONTENT));
		//该按钮为share
		dialog.setPositiveButton(R.string.MSG_DEMO_DIALOG_SYNC_BTN_SHARE, pos);
		//该按钮为Calculat route
		dialog.setNegativeButton(R.string.MSG_DEMO_DIALOG_SYNC_BTN_CALC, neg);
		dialog.show();
		dialog.setEnterBackKeyAllowClose(true);
	}
}
