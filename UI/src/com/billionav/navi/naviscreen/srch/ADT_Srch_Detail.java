package com.billionav.navi.naviscreen.srch;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ADT_Detail_Base;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.base.OnScreenBackListener;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.map.ADT_TouchPointList;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.naviscreen.route.ADT_Route_PointMap;
import com.billionav.navi.naviscreen.schedule.ScheduleDataList;
import com.billionav.navi.naviscreen.schedule.ScheduleModel;
import com.billionav.navi.naviscreen.setting.ADT_Schedule_Activity;
import com.billionav.navi.uitools.HybridUSTools;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.ui.R;

public class ADT_Srch_Detail extends ADT_Detail_Base implements OnScreenBackListener{
	private int record = -1;
	private SearchPointBean searchbean;
	private UIPointData point;
//	private PointBean pointbean;
	private int index;
	private int searchType;
	private boolean isLocalSearch;
	private Class previousClass;
	private String uuid;	 	//is used delete photo data.
	private ArrayList<Class> activitylist = null;
	private CustomDialog cdialog ;
	private CProgressDialog cancelingDialog;
	private boolean syncCanceled = false;
	
	private UIPointData homedata = new UIPointData("");
	private UIPointData companydata = new UIPointData("");
	
	private int reqstates = 0;
	
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
		initData();
		
		
	}
	private void initData(){
//			UISearchDetailInfoResultJNI mDetailResult = UISearchControlJNI.Instance().GetDetailInfoResult(searchType);
//			searchbean = SearchPointBean.createSearchPointBean(mDetailResult, index);
		updateDetailInfo(searchbean);
//			requestDetail();
		ischeakmapEnable();
		
//			if(-1 != (record = jniPointControl_new.Instance().GetRecordIDbyNameAndLonLat(searchbean.getName(), (int)searchbean.getLonlat()[0],(int)searchbean.getLonlat()[1]))){
//				updateFavorite(record);
//				checkboxCollect.changeChecked(true);
//				setEditPointEnabled(true);
////				editpoint.setTextColor(Color.BLACK);
//			}
//			else{
//				updateDetailInfo(searchbean);
			checkboxCollect.changeChecked(false);
			setEditPointEnabled(false);
//				editpoint.setTextColor(Color.GRAY);
//			}
		if(MenuControlIF.Instance().GetHierarchyBelowWinscapeClass().equals(ADT_TouchPointList.class)){
			previousClass = (Class) BundleNavi.getInstance().get("previousClass");
		}
		updataRouteAction();
		UIPointControlJNI.Instance().reqGetHome();
		reqstates = reqGetHome;
//		reqFindFavorite();
	}
	
	private void reqFindFavorite() {
		UIPointControlJNI.Instance().reqGetFavorite("");
		reqstates = reqGetFavorite;
//		UIPointControlJNI.Instance().reqFindPoint(searchbean.getName(), searchbean.getLongitude(), searchbean.getLatitude());
	}

	private UIPointData findFavorite() {
		UIPointData[] points = UIPointControlJNI.Instance().getBookmarkData();
		for(UIPointData p: points) {
			if(p.getName().equals(searchbean.getName()) 
					&& p.getLon()==searchbean.getLongitude() 
					&& p.getLat() == searchbean.getLatitude()) {
				return p;
			}
			
		}
		return null;
//		UIPointControlJNI.Instance().reqFindPoint(searchbean.getName(), searchbean.getLongitude(), searchbean.getLatitude());
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
	private void initialize() {
		setDefaultBackground();
		searchType = getBundleNavi().getInt("searchKind");
		searchbean = (SearchPointBean) getBundleNavi().get("SearchPointBean");
	}
	
	private void updateFavorite(){
		if(point == null) {
			record = -1;
		} else {
			record = point.getID();
		}
		
		if(record == -1) {
			return;
		}
		
		checkboxCollect.changeChecked(true);
		final int purpose = RouteCalcController.instance().getRoutePointFindPurpose();
		if(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE == purpose){
			setEditPointEnabled(true);
		}
		//update picture to imageview
		Uri uri = PointTools.getInstance().returnFaveratePhoto(point.getUuid());
		picture.setImageResource(R.drawable.navicloud_and_591a);
		if(null != uri){
			picture.setImageURI(uri);
		}
	}
	
	private void updateDetailInfo(SearchPointBean bean) {
		long[] 	sStartLonLat = new long[2];
		sStartLonLat[0] = UIMapControlJNI.GetCenterLonLat()[0];
		sStartLonLat[1] = UIMapControlJNI.GetCenterLonLat()[1];
		String address  = bean.getAddress();
		long distance = bean.getDistence();
		String telNo = bean.getTelNo();
		String openTime = bean.getOpenTime();
		setTitle(bean.getName());
		this.name.setText(bean.getName());
		this.address.setText(address);
		if(distance <= 0){
			this.distance.setText(RouteTool.getDisplayDistance(PointTools.calcDistance(sStartLonLat, bean.getLonlat())));
		}else{
			this.distance.setText(RouteTool.getDisplayDistance(distance));
		}
		this.telNo.setText(telNo);
		this.openTime.setText(openTime);
	}
	
	private void setBtnsEnabled(boolean enabled){
		checkboxCollect.setEnabled(enabled);
		setEditPointEnabled(enabled);
		isCheakmapEnable = enabled;
		isDestpointEnable = enabled;
		isPassbylocationEnable = enabled;
		isStartpointEnable = enabled;
		ischeakmapEnable();
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
//		if(BundleNavi.getInstance().getBoolean("TouchFlag")){
//			initialize();
//			initData();
//		}
		OnResumeUpdata();
		ischeakmapEnable();
		updataRouteAction();
	}
	
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
	}

	
	@Override
	public final boolean OnTrigger(NSTriggerInfo triggerInfo) {
		int iTriggerID = triggerInfo.GetTriggerID();
		switch (iTriggerID) {
		case NSTriggerID.POINT_RESPONSE_GET_POINT_LIST:
			dismissSyncDialog();
			
			if(triggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				if(reqstates == reqGetHome || reqstates == reqGetCompany){
					reqFindFavorite();
				}else{					
					reqstates = 0;
				}
				return true;
			} else {
				if(reqstates == reqGetFavorite) {
					point = findFavorite();
					updateFavorite();
					reqstates = 0;
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
					reqFindFavorite();
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
		case NSTriggerID.POINT_RESPONSE_TYPE_SYNC_POINT:
//			Log.d("UIPoint", "triggerInfo ID = " + triggerInfo.m_iTriggerID + " ; m_lParam1 = " + triggerInfo.m_lParam1);
			if(triggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
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
			if(triggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
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
					CustomToast.showToast(this, R.string.STR_MM_02_02_02_04, 1000);
					UIPointControlJNI.Instance().reqGetFavorite("");
					reqstates = reqGetFavorite;
				}
			}
			return true;
		case NSTriggerID.POINT_RESPONSE_DELETE_POINT:
			UnlockScreen();
			if(triggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				dismissSyncDialog();
				CustomToast.showToast(this, R.string.MSG_02_02_04_04, 1000);
				reqstates = 0;
				return true;
			} else {
				dismissSyncDialog();
				setEditPointEnabled(false);
				CustomToast.showToast(this, R.string.STR_MM_02_02_04_22, 1000);
				UIPointControlJNI.Instance().reqGetFavorite("");
				reqstates = reqGetFavorite;
			}
			return true;
		}
		return false;
	}
	
	private void refreshName(){
		if(address.getText().equals("")){
			return;
		}
		if(homedata.getAddress().equals(address.getText()) && companydata.getAddress().equals(address.getText())){
			name.setText(searchbean.getName() + "(" + getString(R.string.STR_MM_01_02_01_17) + "/" 
					+ getString(R.string.STR_MM_01_02_01_18) + ")");
		}else if(homedata.getAddress().equals(address.getText())){
			name.setText(searchbean.getName() + "(" + getString(R.string.STR_MM_01_02_01_17) + ")");
		}else if(companydata.getAddress().equals(address.getText())){
			name.setText(searchbean.getName() + "(" + getString(R.string.STR_MM_01_02_01_18) + ")");
		}else{}
	}
	
	@Override
	public void onBack() {
		if (ADT_Srch_Result.class.equals(MenuControlIF.Instance().GetLastWinscapeClass())) {
			getBundleNavi().putBoolean("isCheakMap", true);
			getBundleNavi().putInt("searchType", searchType);
			getBundleNavi().put("SearchPointBean", searchbean);
		}
			BackWinChange();
	}

	private void ensurePoint() {
//		if(point == null) {
			point = new UIPointData(0,searchbean.getName(),searchbean.getAddress(), searchbean.getTelNo(), searchbean.getLongitude(), searchbean.getLatitude());
//		} else {
//			
//		}
	}

	@Override
	protected void setHomeDisposal() {
		ensurePoint();
		point.setName(getResources().getString(R.string.STR_MM_01_02_01_17));
		UIPointControlJNI.Instance().reqAddHome(point);
		reqstates = reqAddHome;
		showSyncDialog();

	}



	@Override
	protected void setCompanyDisposal() {
		ensurePoint();
		point.setName(getResources().getString(R.string.STR_MM_01_02_01_18));
		UIPointControlJNI.Instance().reqAddCompany(point);
		reqstates = reqAddCompany;
		showSyncDialog();
		
	}
	
	private void dismissSyncDialog() {
		if(cdialog != null){
			cdialog.dismiss();
		}
	}

	
	private void showSyncDialog() {
		syncCanceled = false;
		cdialog = new CustomDialog(ADT_Srch_Detail.this);
		cdialog.setTitle(getString(R.string.MSG_02_02_04_13));
		cdialog.setMessage(getString(R.string.MSG_02_02_04_14));
		cdialog.setPositiveButton(getString(R.string.STR_COM_001), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					cancelSync();
				}
			});
		cdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
//				cancelSync();
			}
		});
		cdialog.show();
	}



	protected void cancelSync() {
		// TODO Auto-generated method stub
		syncCanceled = true;
//		DataSyncControl_ManagerIF.Instance().CancelPOISyncRequest();
		showCancelingDialog();
	}
	private void showCancelingDialog() {
		cancelingDialog = new CProgressDialog(this);
		cancelingDialog.show();
		cancelingDialog.setCancelable(false);
		cancelingDialog.setText(getString(R.string.MSG_02_02_04_15));
	}
	private void dismissCancelingDialog() {
		if(cancelingDialog!=null &&cancelingDialog.isShowing()){
			cancelingDialog.dismiss();
		}
	}
	@Override
	protected void setEditDisposal() {
		getBundleNavi().putInt("record", record);
		getBundleNavi().put("pointdata", point);
		ForwardWinChange(ADT_Srch_InfoEdit.class);
	}
	@Override
	protected void setCheckboxCollectDisposal(boolean newState) {
		if (newState) {
			ensurePoint();
			UIPointControlJNI.Instance().reqAddFavorite(point);
			reqstates = AddFavorite;
		} else {
			UIPointControlJNI.Instance().reqDeleteFavorite(record);
			reqstates = DeleteFavorite;
		}
		
	}
	@Override
	protected void setCheakmapDisposal() {
		getBundleNavi().putBoolean("isCheakMap", true);
		getBundleNavi().putInt("searchType", searchType);
//		if(searchType == TouchMapControl.TYPE_LONGPRESS){
//			getBundleNavi().put("PointBean", pointbean);
//		}
//		else{
			getBundleNavi().put("SearchPointBean", searchbean);
//		}
		//if edit path state don't use SearchWinChange().because of editing path state will is destruct. 
		if(RouteCalcController.instance().getRoutePointFindPurpose() != RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE){
			MenuControlIF.Instance().ForwardWinChange(
					(ADT_Srch_Result.class));
		}
		else{
			if (!MenuControlIF.Instance().BackSearchWinChange(ADT_Srch_Result.class)) {
				MenuControlIF.Instance().ForwardWinChange((ADT_Srch_Result.class));
			}
		}
		
	}
	@Override
	protected void setStartpointDisposal() {
		if(addorupdata){
			RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_START);
			RouteCalcController.instance().searchPointToPOI(searchbean);
			MenuControlIF.Instance().ForwardWinChange(ADT_Route_Main.class);
		}
		else{
			RouteCalcController.instance().searchPointToPOI(searchbean);
			BackSearchWinChange(ADT_Route_Main.class);
		}
		
	}
	@Override
	protected void setDestpointDisposal() {
		if (addorupdata) {
			RouteCalcController.instance().setRoutePointFindPurpose(
					RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST);
			RouteCalcController.instance().searchPointToPOI(searchbean);
			MenuControlIF.Instance().ForwardWinChange(ADT_Route_Main.class);
		} else {
			RouteCalcController.instance().searchPointToPOI(searchbean);
			BackSearchWinChange(ADT_Route_Main.class);
		}
	}
	@Override
	protected void setPassbylocationDisposal() {
		if(addorupdata){
			RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA);
			RouteCalcController.instance().searchPointToPOI(searchbean);
			MenuControlIF.Instance().ForwardWinChange(ADT_Route_Main.class);
		}
		else{
			RouteCalcController.instance().searchPointToPOI(searchbean);
			if(!BackSearchWinChange(ADT_Route_Main.class)){
				ForwardWinChange(ADT_Route_Main.class);
			}
		}
		
	}
}
