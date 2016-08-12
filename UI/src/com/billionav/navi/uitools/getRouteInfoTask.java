package com.billionav.navi.uitools;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.naviscreen.schedule.ScheduleDataList;
import com.billionav.navi.naviscreen.schedule.ScheduleModel;
import com.billionav.navi.uicommon.UIC_RouteCommon;

public class getRouteInfoTask implements TriggerListener{

	private static final int REQUEST_STATE_INVALED = -1;
	private static final int REQUEST_STATE_REQUESTING = 1;
	private static final int REQUEST_STATE_SUCCEED = 2;
	private static final int REQUEST_STATE_FAILED = 3;
	
	
	private static final int requestNone = 0;
	private static final int requestHome = 1;
	private static final int calcHome = 2;
	private static final int requestCompany = 3;
	private static final int calcCompany = 4;
	
	private static final int calcSchedulePoint = 100;
	
	private int calcIndex  = -1;
	private int reqId = requestNone;
	private static getRouteInfoTask instance;
	public static getRouteInfoTask getInstance() {
		if(instance == null) {
			instance = new getRouteInfoTask();
		}
		return instance;
	}
	public RouteInfoData getCompanyData() {
		return m_CompanyData;
	}
	
	public RouteInfoData gethomeData() {
		return m_HomeData;
	}
	public class RouteInfoData{
		public int ETA_distance;
		public int ETA_time;
		public UIPointData data;
		public boolean hasData = false;
	}
	private RouteInfoData m_HomeData;
	private RouteInfoData m_CompanyData;

	private getRouteInfoTask() {
//		taskList = new ArrayList<RouteInfoData>();
		GlobalTrigger.getInstance().addTriggerListener(this);
		
	}
	public void doTask() {
		if(UIC_RouteCommon.Instance().isRouteExistAndGuideOn()) {
			return;
		}
		if(reqId == requestNone) {
			reqId = requestHome;
			UIPointControlJNI.Instance().reqGetHome();
			Log.d("test", "getRouteInfoTask do task start");
		}else {
			new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
				
				@Override
				public void run() {
					doTask();
				}
			},5000);
			Log.d("test", "getRouteInfoTask do task wait");
		}
	}
	
	public int doScheduleTask(int index) {
		int count = ScheduleDataList.getInstance().getScheduleCount();
		Log.d("HybridUS", "reqId = " + reqId + " ; index = " + index + " ; count = " + count);
		if(reqId == requestNone && index >= 0 && count > index) {
			reqId = calcSchedulePoint;
			ScheduleModel bean = ScheduleDataList.getInstance().getScheduleAtIndex(index);
			if(bean.lon != 0 && bean.lat != 0){
				RouteCalcController.instance().rapidGetRouteInfoWithData(bean.pointName,
						bean.lon, bean.lat,"","");
			}else{
				bean.actualToArrivalTime = -1;
				checkIfHasTasks(index);
				return requestNone;
			}
			calcIndex = index;
			GlobalTrigger.getInstance().addTriggerListener(this);
			return requestNone;
		}else{
			return reqId;
		}
	}
	
	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		if (triggerInfo.m_iTriggerID == NSTriggerID.POINT_RESPONSE_GET_POINT_LIST) {
			if (reqId == requestHome) {
				// UIPointControlJNI.Instance().setHomeRequested();
				boolean isEnableHome = UIPointControlJNI.Instance()
						.isHomeExist();
				if (isEnableHome) {
					if (UIPointControlJNI.Instance().isHomeExist()) {
						UIPointData homedata = UIPointControlJNI.Instance()
								.getHome();
						Log.d("test", "POINT_RESPONSE_GET_POINT_LIST home=" + homedata.getName()+" "+
								homedata.getLon()+" "+ homedata.getLat()+" "+
								homedata.getAddress()+" "+
								homedata.getTelNo());
						RouteCalcController.instance()
								.rapidGetRouteInfoWithData(homedata.getName(),
										homedata.getLon(), homedata.getLat(),
										homedata.getAddress(),
										homedata.getTelNo());
						RouteInfoData info = new RouteInfoData();
						info.data = homedata;
						reqId = calcHome;
						m_HomeData = info;
					}
				} else {
					reqId = requestCompany;
					UIPointControlJNI.Instance().reqGetCompany();
				}
				// adapter.notifyDataSetChanged();
				// reqId = requestCompany;
				// UIPointControlJNI.Instance().reqGetCompany();
			} else if (reqId == requestCompany) {
				// UIPointControlJNI.Instance().setCompanyRequested();
				if (UIPointControlJNI.Instance().isComapanyExist()) {
					reqId = calcCompany;
					UIPointData companydata = UIPointControlJNI.Instance()
							.getCompany();
					Log.d("test", "POINT_RESPONSE_GET_POINT_LIST companydata=" + companydata.getName()+" "+
							companydata.getLon()+" "+ companydata.getLat()+" "+
							companydata.getAddress()+" "+
							companydata.getTelNo());
					RouteCalcController.instance().rapidGetRouteInfoWithData(
							companydata.getName(), companydata.getLon(),
							companydata.getLat(), companydata.getAddress(),
							companydata.getTelNo());
					RouteInfoData info = new RouteInfoData();
					info.data = companydata;
					m_CompanyData =info;
				} else {
					reqId = requestNone;
				}

			}
		}
		if (triggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_PATH_FIND_FINISH_PATH_INFO) {
			String pntName = triggerInfo.m_String1;
			if (TextUtils.isEmpty(pntName)) {
				Log.e("test", "request pnt name is null ,return");
			}
			Log.d("test", "UIC_MN_TRG_PATH_FIND_FINISH_PATH_INFO::"
					+ triggerInfo.m_String1 + " " + (int) triggerInfo.m_lParam2
					+ " " + (int) triggerInfo.m_lParam3);
			Log.d("HybridUS", "calc result  = " + (int) triggerInfo.m_lParam1 + " ; reqId = " + reqId);
			if(reqId == calcHome) {
				if (triggerInfo.m_lParam1 != 0) {
//					m_HomeData.requestState = REQUEST_STATE_FAILED;
				} else {
//					info.requestState = REQUEST_STATE_SUCCEED;
					if(m_HomeData != null) {
						m_HomeData.hasData = true;
						m_HomeData.ETA_distance = (int) triggerInfo.m_lParam2;
						m_HomeData.ETA_time = (int) triggerInfo.m_lParam3;
					}
				}
			} else if(reqId == calcCompany) {
				if (triggerInfo.m_lParam1 != 0) {
				} else {
					if(m_CompanyData != null) {
						m_CompanyData.hasData = true;
						m_CompanyData.ETA_distance = (int) triggerInfo.m_lParam2;
						m_CompanyData.ETA_time = (int) triggerInfo.m_lParam3;
					}
				}
			} else if(reqId == calcSchedulePoint){
				int arrivalTime = (int)triggerInfo.m_lParam3;
				Log.d("test", "calcIndex = " + calcIndex + " ; ArrivalTime = " + arrivalTime);
				Log.d("HybridUS", "calcIndex = " + calcIndex + " ; ArrivalTime = " + arrivalTime);
				if (triggerInfo.m_lParam1 == 0) {
					ScheduleDataList.getInstance()
						.getScheduleAtIndex(calcIndex).actualToArrivalTime = arrivalTime;
				}else{
					ScheduleDataList.getInstance()
						.getScheduleAtIndex(calcIndex).actualToArrivalTime = -1;
				}
				checkIfHasTasks(calcIndex);
			}
			
			if(reqId == calcHome) {
				reqId = requestCompany;
				UIPointControlJNI.Instance().reqGetCompany();
			} else if(reqId == calcCompany) {
				reqId = requestNone;
			}
		}

		return false;
	}
	
	private boolean checkIfHasTasks(int index) {
		int count = ScheduleDataList.getInstance().getScheduleCount();
		Log.d("test", "index = "+index+" count="+count);
		if (index  < count - 1) {
			calcIndex = index + 1;
			Log.d("HybridUS", "reqId = " + reqId + " ; calcIndex = " + calcIndex + " ; count = " + count);
			ScheduleModel bean = ScheduleDataList.getInstance().getScheduleAtIndex(calcIndex);
			if(bean.lon != 0 && bean.lat != 0){ 
				RouteCalcController.instance().rapidGetRouteInfoWithData(bean.pointName,
						bean.lon, bean.lat,"","");
				bean.actualToArrivalTime = -1;
			}else{
				checkIfHasTasks(calcIndex);
			}
			return true;
		} else {
			reqId = requestNone;
			HybridUSTools.getInstance().checkArrivalStates();
			return false;
		}
	}

}
