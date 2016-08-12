package com.billionav.navi.naviscreen.route;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.billionav.jni.UIPathControlJNI;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.sync.AppLinkService;
import com.billionav.navi.uicommon.UIC_IntentCommon;
import com.billionav.navi.uicommon.UIC_RouteCommon;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.ui.R;


public class ADT_Route_Calculating extends ActivityBase implements MenuControlIF.MapScreen, UIC_IntentCommon.IntentCall{
	
	
	private static final int ROUTE_FINISH_FAILED = 1;
	
	private CProgressDialog dialog;
	
	private UIPathControlJNI pathControl = new UIPathControlJNI();
	
	private final Handler timeupHandler = new Handler();
	private final Runnable timeupAction = new Runnable() {
		public void run() {
			cancelCalculating();
			CustomToast.showToast(ADT_Route_Calculating.this, R.string.STR_MM_01_01_04_30, 3000);
		}
	};
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		
		getBundleNavi().putBoolean("route", false);
		dialog = CProgressDialog.makeProgressDialog(this, R.string.MSG_03_01_02_02_05);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
				if(MenuControlIF.Instance().IsWinChangeLocked()){
					return true;
				}
				
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					cancelCalculating();
					return true;
				case KeyEvent.KEYCODE_MENU:
					return true;
				case KeyEvent.KEYCODE_HOME:
				case KeyEvent.KEYCODE_SEARCH:
				case KeyEvent.KEYCODE_POWER:
					MenuControlIF.Instance().setWinchangeWithoutAnimation();
					if(!MenuControlIF.Instance().BackWinChange()) {
						MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Main_Map_Navigation.class);
					}
					return true;
				default:
					return false;
				}
			}
		});
		dialog.show();
//		startTimeup();
	}
	
	 protected int onConnectedScreenId() {
		  return SCRMapID.ADT_ID_RouteResult;
	 }
	
	
	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
	}
	
	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
	}
	
	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
//		endTimeup();
		super.OnDestroy();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {		
		int iTriggerID = cTriggerInfo.m_iTriggerID;
		switch (iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED: 
			Log.d("HybridUS", "UIC_MN_TRG_POINT_SEARCH_FINISHED ");
			if(!RouteCalcController.instance().parkSearching){
				return false;
			}
			RouteCalcController.instance().clearTimeout();
			if(cTriggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) {
				UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
				long count = searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
				Log.d("HybridUS", "count = " + count);
				if(count != 0) {
					SearchPointBean bean = SearchPointBean.createSearchPointBean(searchResult, 0);
					Log.d("HybridUS", "bean.name = " + bean.getName());
//					pathControl.setRouteCondition(UIPathControlJNI.PathFinderRequest_Weight_Param_WEIGHT_WALK);
					RouteCalcController.instance().rapidGetRouteRequest(bean.getName(),bean.getLongitude(),bean.getLatitude());
				}else{
//					pathControl.setRouteCondition(UIPathControlJNI.PathFinderRequest_Weight_Param_WEIGHT_DEFAULT);
					pathControl.StartUIPathFinding(UIPathControlJNI.UIC_PT_GO_HERE_FINDING);
				}
			}
			return true;
		// Current Road Change
		case NSTriggerID.UIC_MN_TRG_PATH_FIND_FINISH_RESULT_FAIL: {
			Log.i("HybridUS", "UIC_MN_TRG_PATH_FIND_FINISH_RESULT_FAIL");
//			endTimeup();
			syncRouteOptions();
			RouteCalcController.instance().finishRouteCalculate();
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
			}
			getBundleNavi().putBoolean("Navigation", true);
			CustomToast.showToast(this, R.string.STR_MM_01_01_04_30, 3000);
			MenuControlIF.Instance().setWinchangeWithoutAnimation();
			if(!MenuControlIF.Instance().BackWinChange()) {
				MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Main_Map_Navigation.class);
			}
			if (AppLinkService.getInstance() != null) {
				AppLinkService.getInstance().routeCalculateFailed();
			}

		}
			return true;
		case NSTriggerID.UIC_MN_TRG_ROUTE_SET_DONE: {
			Log.i(NaviConstant.TAG, "UIC_MN_TRG_ROUTE_SET_DONE ");
//			endTimeup();
			syncRouteOptions();
			
			RouteCalcController.instance().finishRouteCalculate();
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
			}
			MenuControlIF.Instance().setWinchangeWithoutAnimation();
			if(!MenuControlIF.Instance().BackSearchWinChange(ADT_Route_Profile.class)){
				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Route_Profile.class);
			}
			new UIPathControlJNI().GuideStart();
			if (AppLinkService.getInstance() != null) {
				//AppLinkService.getInstance().routeCalculateSuccessed();
			}
		}
			return true;
		default:
			super.OnTrigger(cTriggerInfo);
			return false;
		}
	}
	
	private void startTimeup(){
		timeupHandler.postDelayed(timeupAction, 30000);
	}
	
	private void endTimeup(){
		timeupHandler.removeCallbacks(timeupAction);
	}
	
	private void syncRouteOptions() {
		if (UIC_RouteCommon.Instance().isRouteOptionModified()) {
			int status = UIC_RouteCommon.Instance().GetRouteOptionAttr(
					UIC_RouteCommon.ROUTE_CONDITION);
//			setupControl.SetInitialStatus(
//					jniSetupControl.STUPDM_ROUTE_CONDITION, status);
//			status = UIC_RouteCommon.Instance().GetRouteOptionAttr(
//					UIC_RouteCommon.HIGHWAY);
//			setupControl.SetInitialStatus(jniSetupControl.STUPDM_HIGHWAY,
//					status);
//			status = UIC_RouteCommon.Instance().GetRouteOptionAttr(
//					UIC_RouteCommon.FERRY);
//			setupControl.SetInitialStatus(jniSetupControl.STUPDM_FERRY, status);
			UIC_RouteCommon.Instance().setRouteOptionModifiedFlag(false);
		}
	}
	
	private void cancelCalculating(){
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
		new UIPathControlJNI().AbortUIFinding();
		MenuControlIF.Instance().setWinchangeWithoutAnimation();
		if(!MenuControlIF.Instance().BackWinChange()){
			MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Main_Map_Navigation.class);
		}
	}

	@Override
	public boolean onIntentCall(int type) {
		return false;
	}

}
