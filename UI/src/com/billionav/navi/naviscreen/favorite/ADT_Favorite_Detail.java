package com.billionav.navi.naviscreen.favorite;
import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.billionav.jni.UILocationControlJNI;
import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ADT_Detail_Base;
import com.billionav.navi.naviscreen.base.OnScreenBackListener;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.map.ADT_TouchPointList;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.naviscreen.route.ADT_Route_PointMap;
import com.billionav.navi.naviscreen.srch.ADT_Srch_InfoEdit;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.ui.R;

public class ADT_Favorite_Detail extends ADT_Detail_Base implements OnScreenBackListener{
//	private UIPointData pointdata = null;
//	private final jniPOIMemoryControl poiMemControl = new jniPOIMemoryControl();
//	private jniPOIMemoryData poiMemdata = poiMemControl.GetPOIMemoryData(jniPOIMemoryControl.UIC_PM_POIPOINTPOINT);
	private int record = -1;
	private boolean onlyonepoint = false;
	private Class previousClass;
	private String oldname ;
	private String uuid;		//is used delete photo data.
	private ArrayList<Class> activitylist = null;
	private CustomDialog cdialog;
	
	private int reqstates = 0;
	private boolean isDataChanged = false;
	
	private UIPointData homedata = new UIPointData("");
	private UIPointData companydata = new UIPointData("");
	private String initialName = "";
	
	private boolean isFavorite;
	private int sequenceid = -1;
//	private boolean recSyncPoint;
	
	private static final int reqAddHome = 1;
	private static final int reqAddCompany = 2;
	private static final int AddFavorite = 3;
	private static final int DeleteFavorite = 4;
	private static final int reqGetFavorite = 5;
	private static final int reqGetHome = 6;
	private static final int reqGetCompany = 7;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		initialize();
	}
	
	protected void initialize() {
		record = getBundleNavi().getInt("record");
		pointdata = (UIPointData) getBundleNavi().get("pointdata");
		initialName = pointdata.getName();
		isFavorite = getBundleNavi().getBoolean("favorited");
//		pointControl.GetPointDataByRecordID(record).PointToPOIMemory(jniPOIMemoryControl.UIC_PM_POIPOINTPOINT);
		
		setDefaultBackground();
		setTitle(pointdata.getName());
		name.setText(pointdata.getName());
		
		//initialize startpoint . 
		long[] 	sStartLonLat = new long[2];
		sStartLonLat[0] = UILocationControlJNI.GetPosition(0)[0];
		sStartLonLat[1] = UILocationControlJNI.GetPosition(0)[1];
		
		address.setText(pointdata.getAddress());
		distance.setText(RouteTool.getDisplayDistance(PointTools.calcDistance(sStartLonLat, pointdata.getLonlat())));
		telNo.setText(pointdata.getTelNo());

		//update picture
		picture.setImageResource(R.drawable.navicloud_and_591a);
		Uri uri = PointTools.getInstance().returnFaveratePhoto(pointdata.getUuid());
		if(null != uri){
			picture.setImageURI(uri);
		}
		
		if(isFavorite){
			checkboxCollect.changeChecked(true);
			setEditPointEnabled(true);
		}
		ischeakmapEnable();
		oldname = pointdata.getName();
		UIPointControlJNI.Instance().reqGetHome();
		reqstates = reqGetHome;
	}
    
	
	private void ischeakmapEnable() {
		// TODO Auto-generated method stub
		if(activitylist == null){
			activitylist = new ArrayList<Class>(){};
			activitylist.add(ADT_Main_Map_Navigation.class);
			activitylist.add(ADT_Route_PointMap.class);
		}
		if(activitylist.contains(MenuControlIF.Instance().GetHierarchyBelowWinscapeClass()) 
		|| activitylist.contains(previousClass) && MenuControlIF.Instance().GetHierarchyBelowWinscapeClass().equals(ADT_TouchPointList.class)){
			isCheakmapEnable = false;
			return;
		}
	}

	@Override
	protected void OnResume() {
		super.OnResume();
		OnResumeUpdata();
		updataRouteAction();
	}

	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		switch(cTriggerInfo.GetTriggerID())
		{
		case NSTriggerID.POINT_RESPONSE_FIND_POINT:
			final int findPntState = (int)cTriggerInfo.m_lParam1;
			sequenceid = (int)cTriggerInfo.m_lParam2;
			Log.d("UIPoint", "ADT_Favorite_Detail : receive POINT_RESPONSE_FIND_POINT; sequenceid = " + sequenceid);
			if(findPntState == UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS){
				checkboxCollect.changeChecked(true);
				setEditPointEnabled(true);
				isFavorite = true;
			}else{
				checkboxCollect.changeChecked(false);
				setEditPointEnabled(false);
				isFavorite = false;
			}
			break;
		case NSTriggerID.POINT_RESPONSE_DELETE_POINT:
			dismissSyncDialog();
			Log.d("UIPoint", "ADT_Favorite_Detail : receive POINT_RESPONSE_DELETE_POINT; cTriggerInfo.m_lParam1 = " + cTriggerInfo.m_lParam1);
			if(cTriggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				reqstates = 0;
				return true;
			} else {
				reqstates = 0;
				setEditPointEnabled(false);
				CustomToast.showToast(this, R.string.STR_MM_02_02_04_22, 500);
				UIPointControlJNI.Instance().reqGetFavorite("");
				reqstates = reqGetFavorite;
			}
			break;
		case NSTriggerID.POINT_RESPONSE_TYPE_SYNC_POINT:
			if(cTriggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				dismissSyncDialog();
				if(reqstates == reqAddHome){
					CustomToast.showToast(this, R.string.MSG_02_02_04_08, 1000);
				} else if(reqstates == reqAddCompany) {
					CustomToast.showToast(this, R.string.MSG_02_02_04_10, 1000);
				} else{}
				reqstates = 0;
				return true;
			}
		case NSTriggerID.POINT_RESPONSE_ADD_POINT:
			dismissSyncDialog();
			if(cTriggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				if(reqstates == reqAddHome){
					CustomToast.showToast(this, R.string.MSG_02_02_04_11, 1000);
				} else if(reqstates == reqAddCompany) {
					CustomToast.showToast(this, R.string.MSG_02_02_04_12, 1000);
				} else if(reqstates == AddFavorite) {
					CustomToast.showToast(this, R.string.MSG_02_02_04_04, 1000);
				}
				reqstates = 0;
				return true;
			} else {
				if(reqstates == reqAddHome) {
					CustomToast.showToast(this, R.string.MSG_02_02_04_07, 1000);
					UIPointControlJNI.Instance().reqGetHome();
				} else if(reqstates == reqAddCompany) {
					CustomToast.showToast(this, R.string.MSG_02_02_04_09, 1000);
					UIPointControlJNI.Instance().reqGetCompany();
				} else if(reqstates == AddFavorite) {
					sequenceid = (int)cTriggerInfo.m_lParam2;
					CustomToast.showToast(this, R.string.STR_MM_02_02_02_04, 1000);
					UIPointControlJNI.Instance().reqGetFavorite("");
					reqstates = reqGetFavorite;
				}
			}
			break;
		case NSTriggerID.POINT_RESPONSE_GET_POINT_LIST:
			dismissSyncDialog();
			if(cTriggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				CustomToast.showToast(this, R.string.MSG_02_02_04_04, 500);
				reqstates = 0;
				return true;
			} else {
				if(reqstates == reqGetFavorite) {
					UIPointData point = findFavorite();
					if(point == null) {
						record = -1;
					} else {
						record = point.getID();
						pointdata = point;
					}
					updateFavorite();
				}else if(reqstates == reqGetHome){
					if(UIPointControlJNI.Instance().isHomeExist()){
						homedata = UIPointControlJNI.Instance().getHome();
					}
					UIPointControlJNI.Instance().reqGetCompany();
					reqstates = reqGetCompany;
				}else if(reqstates == reqGetCompany){
					if(UIPointControlJNI.Instance().isComapanyExist()){
						companydata = UIPointControlJNI.Instance().getCompany();
					}
					refreshName();
					sequenceid = 0;
				}
				else if(reqstates == reqAddHome){
					if(UIPointControlJNI.Instance().isHomeExist()){
						homedata = UIPointControlJNI.Instance().getHome();
						refreshName();
					}
					reqstates = 0;
				}else if(reqstates == reqAddCompany){
					if(UIPointControlJNI.Instance().isComapanyExist()){
						companydata = UIPointControlJNI.Instance().getCompany();
						refreshName();
					}
					reqstates = 0;
				}else{}
			}
			return true;
		}
		return super.OnTrigger(cTriggerInfo);
	}
	
	private void refreshName(){
		if(address.getText().equals("")){
			return;
		}
		if(homedata.getAddress().equals(address.getText()) && companydata.getAddress().equals(address.getText())){
			name.setText(initialName + "(" + getString(R.string.STR_MM_01_02_01_17) + "/" 
					+ getString(R.string.STR_MM_01_02_01_18) + ")");
		}else if(homedata.getAddress().equals(address.getText())){
			name.setText(initialName + "(" + getString(R.string.STR_MM_01_02_01_17) + ")");
		}else if(companydata.getAddress().equals(address.getText())){
			name.setText(initialName + "(" + getString(R.string.STR_MM_01_02_01_18) + ")");
		}else{}
	}
	
	private void updateFavorite(){
		if(record == -1) {
			checkboxCollect.changeChecked(false);
			setEditPointEnabled(false);
			return;
		}
		
		checkboxCollect.changeChecked(true);
		setEditPointEnabled(true);
	}

	private UIPointData findFavorite() {
		UIPointData[] points = UIPointControlJNI.Instance().getBookmarkData();
		for(UIPointData p: points) {
			if(p.getName().equals(pointdata.getName()) 
					&& p.getLon()==pointdata.getLon() 
					&& p.getLat() == pointdata.getLat()) {
				return p;
			}
			
		}
		return null;
//		UIPointControlJNI.Instance().reqFindPoint(searchbean.getName(), searchbean.getLongitude(), searchbean.getLatitude());
	}


	@Override
	protected void setHomeDisposal() {
		pointdata.setName(getResources().getString(R.string.STR_MM_01_02_01_17));
		UIPointControlJNI.Instance().reqAddHome(pointdata);
		reqstates = reqAddHome;
		showSyncDialog();
		
	}

	@Override
	protected void setCompanyDisposal() {
		pointdata.setName(getResources().getString(R.string.STR_MM_01_02_01_18));
		UIPointControlJNI.Instance().reqAddCompany(pointdata);
		reqstates = reqAddCompany;
		showSyncDialog();
		
	}
	
	private void showSyncDialog() {
		if(cdialog == null) {
			cdialog = new CustomDialog(ADT_Favorite_Detail.this);
			cdialog.setTitle(getString(R.string.MSG_02_02_04_13));
			cdialog.setMessage(getString(R.string.MSG_02_02_04_14));
			cdialog.setCancelable(false);
			cdialog.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK) {
						return true;
					}
					return false;
				}
			});
		}
		cdialog.show();
	}
	
	private void dismissSyncDialog() {
		if(cdialog != null){
			cdialog.cancel();
		}
	}
	
	@Override
	protected void setEditDisposal() {
		getBundleNavi().putInt("record", record);
		getBundleNavi().put("pointdata", pointdata);
		ForwardWinChange(ADT_Srch_InfoEdit.class);
	}

	@Override
	protected void setCheckboxCollectDisposal(boolean newState) {
		Log.d("UIPoint", "callback setCheckboxCollectDisposal: newState = " + newState 
				+ " ; sequenceid = " + sequenceid + " isFavorite = " + isFavorite);
			if (newState) {
				UIPointControlJNI.Instance().reqAddFavorite(pointdata);
				reqstates = AddFavorite;
			} else {
				if(sequenceid == -1){
					UIPointControlJNI.Instance().reqDeleteFavorite(pointdata.getID());
				}else{
					UIPointControlJNI.Instance().reqDeleteFavorite(sequenceid);
				}
				reqstates = DeleteFavorite;
			}
		
	}

	@Override
	protected void setCheakmapDisposal() {
		getBundleNavi().put("pointdata", pointdata);
		//if edit path state don't use SearchWinChange().because of editing path state will is destruct. 
		if(RouteCalcController.instance().getRoutePointFindPurpose() != RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE){
			MenuControlIF.Instance().ForwardWinChange(
					(ADT_Favorite_CheakMap.class));
		}
		else{
			if (!MenuControlIF.Instance().BackSearchWinChange(ADT_Favorite_CheakMap.class)) {
				MenuControlIF.Instance().ForwardWinChange((ADT_Favorite_CheakMap.class));
			}
		}
	}

	@Override
	protected void setStartpointDisposal() {
		RouteCalcController.instance().pointDataToPOI(pointdata.getName(),pointdata.getLonlat()[0],pointdata.getLonlat()[1],pointdata.getAddress(),pointdata.getTelNo());
		if(addorupdata)
		{
			RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_START);
//			RouteCalcController.instance().pointDataToPOI(pointdata.getName(),pointdata.GetLonLat());
			MenuControlIF.Instance().ForwardWinChange(ADT_Route_Main.class);
		}
		else
		{
//			RouteCalcController.instance().pointDataToPOI(pointdata.getName(),pointdata.GetLonLat());
			if(!BackSearchWinChange(ADT_Route_Main.class)){
				ForwardKeepDepthWinChange(ADT_Route_Main.class);
			}
		}
	}
	
	@Override
	protected void setDestpointDisposal() {
		RouteCalcController.instance().pointDataToPOI(pointdata.getName(),pointdata.getLonlat()[0],pointdata.getLonlat()[1],pointdata.getAddress(),pointdata.getTelNo());
		if(addorupdata){
			RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST);
//			RouteCalcController.instance().pointDataToPOI(pointdata.getName(),pointdata.GetLonLat());
			MenuControlIF.Instance().ForwardWinChange(ADT_Route_Main.class);
		}
		else {
//			RouteCalcController.instance().pointDataToPOI(pointdata.getName(),pointdata.GetLonLat());
			if(!BackSearchWinChange(ADT_Route_Main.class)){
				ForwardKeepDepthWinChange(ADT_Route_Main.class);
			}
		}
		
	}

	@Override
	protected void setPassbylocationDisposal() {
		RouteCalcController.instance().pointDataToPOI(pointdata.getName(),pointdata.getLonlat()[0],pointdata.getLonlat()[1],pointdata.getAddress(),pointdata.getTelNo());
		if(addorupdata){
			RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA);
			MenuControlIF.Instance().ForwardWinChange(ADT_Route_Main.class);
		}
		else{
			if(!BackSearchWinChange(ADT_Route_Main.class)){
				ForwardKeepDepthWinChange(ADT_Route_Main.class);
			}
		}
	}

	@Override
	public void onBack() {
		if(MenuControlIF.Instance().GetHierarchyBelowWinscapeClass() == ADT_TouchPointList.class){
			if(onlyonepoint){
				if(!checkboxCollect.isChecked()||!oldname.equals(pointdata.getName())){
					BackSearchWinChange(previousClass);
					return;
				}
			}
			BackWinChange();
		}
		else{
			getBundleNavi().putBoolean("favoriteDetailBack", true);
//			BundleNavi.getInstance().putBoolean("recSyncPoint", recSyncPoint);
//			if (!isDateChanged) {
//			boolean isChanged = (checkboxCollect.isChecked() != isFavorite);
			getBundleNavi().putBoolean("isChanged", isDataChanged);
//			}
			BackWinChange();
		}
	}

	
	
}
