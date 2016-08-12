package com.billionav.navi.naviscreen.map;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchDetailInfoResultJNI;
import com.billionav.jni.UIUserReportJNI;
import com.billionav.navi.component.listcomponent.ListViewNavi.SimpleAdapterBuilder;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.naviscreen.map.TouchMapControl.PointBean;
import com.billionav.navi.naviscreen.report.ADT_report_detail;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Detail;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.ui.R;

public class ADT_TouchPointList extends ActivityBase implements TriggerListener{
	
	private ListView listView;
	private ArrayList<PointBean> getlist;
	private ArrayList<PointBean> list;
	
	private long requestId = -1;
	private int index = -1;
	private SparseArray<PointBean> requstList;
	
	private SimpleAdapterBuilder builder;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_map_touch_point_list);
		setTitle(R.string.STR_MM_01_05_01_01);
		setDefaultBackground();
		listView = (ListView) findViewById(R.id.list1);
		
		requstList = new SparseArray<PointBean>();
		getlist = (ArrayList<PointBean>) (getBundleNavi().get("PointList"));
		list = new ArrayList<PointBean>();
		SimpleAdapter adapter = createAdapter(getlist);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final PointBean point = ADT_TouchPointList.this.list.get(position);
				BundleNavi.getInstance().putInt("index", point.getIndex());
				BundleNavi.getInstance().putInt("searchKind", TouchMapControl.getSearchTypeFromTouchType(point.getType()));
				BundleNavi.getInstance().put("previousClass", MenuControlIF.Instance().GetHierarchyBelowWinscapeClass());
				
				if(point.getType() == UIMapControlJNI.MAP_MARK_TYPE_USER_REPORT) {
					BundleNavi.getInstance().putString("postID", point.getPostID());
					MenuControlIF.Instance().ForwardWinChange(ADT_report_detail.class);
				} else if(point.getType() == UIMapControlJNI.MAP_MARK_TYPE_TRAFFIC){
//					BundleNavi.getInstance().putBoolean("TouchFlag", true);
//					if(list.size() == 1 && point.getCategory() == jniPointControl_new.PNT_DATAUNIT_KIND_REGIST){
//						BundleNavi.getInstance().putBoolean("OnlyOnePoint", true);
//					}
//					else{
//						BundleNavi.getInstance().putBoolean("OnlyOnePoint", false);
//					}
//					BundleNavi.getInstance().putInt("record", jniPointControl_new.Instance().GetRecordIDbyNameAndLonLat(point.getName(), (int)point.getLonlat()[0], (int)point.getLonlat()[1]));
//					if(RouteCalcController.instance().getRoutePointFindPurpose() != RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE){
//						MenuControlIF.Instance().ForwardWinChange(
//								(ADT_Favorite_Detail.class));
//					}
//					else{
//						if (!MenuControlIF.Instance().BackSearchWinChange(ADT_Favorite_Detail.class)) {
//							MenuControlIF.Instance().ForwardWinChange((ADT_Favorite_Detail.class));
//						}
//					}
					MenuControlIF.Instance().BackWinChange();
					MapOverwriteLayer.getInstance().showPopupTmc(point);
					
				} 
//				else if(point.getType() == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_TRAFFIC ){
//					MenuControlIF.Instance().BackWinChange();
//					MapOverwriteLayer.getInstance().showPopupTmc(point);
//				}
				else {
					BundleNavi.getInstance().put("SearchPointBean", SearchPointBean.obtainPointBeanToSearchBean(point));
					BundleNavi.getInstance().putBoolean("TouchFlag", true);
					if(RouteCalcController.instance().getRoutePointFindPurpose() != RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE){
						MenuControlIF.Instance().ForwardWinChange(
								(ADT_Srch_Detail.class));
					}
					else{
						if (!MenuControlIF.Instance().BackSearchWinChange(ADT_Srch_Detail.class)) {
							MenuControlIF.Instance().ForwardWinChange((ADT_Srch_Detail.class));
						}
					}
				}

			}
		});
		findPointNeedRequest();
		requestPoint();
		GlobalTrigger.getInstance().addTriggerListener(this);
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		SimpleAdapter adapter = createAdapter(getlist);
		listView.setAdapter(adapter);
	}

	private void findPointNeedRequest() {
		for(int i=0; i<getlist.size(); i++) {
			String name = getlist.get(i).getName();
			int type = getlist.get(i).getType();
			
			if(type == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_MAP_SYMBOL && TextUtils.isEmpty(name)) {
				requstList.put(i, getlist.get(i));
			}
		}
	}
	
	private SimpleAdapter createAdapter(ArrayList<PointBean> list) {
		this.list.clear();
		builder = new SimpleAdapterBuilder(R.layout.touch_point_list_item, android.R.id.icon, android.R.id.title, android.R.id.summary);
		for(PointBean p : list) {
//			if(p.getType() == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_POINT &&
//				p.getCategory() == jniPointControl_new.PNT_DATAUNIT_KIND_REGIST &&
//				(-1 == jniPointControl_new.Instance().GetRecordIDbyNameAndLonLat(p.getName(), (int)p.getLonlat()[0], (int)p.getLonlat()[1]))){
//				continue;
//			}
			Object name = TextUtils.isEmpty(p.getName()) ? R.string.STR_COM_018 : p.getName();
			Object summary = p.getSummary();
			this.list.add(p);
			builder.put(getReportImage(p.getType(), p.getCategory()), name, summary);
		}
		return builder.createSimpleAdapter(this);
	}

	private int getReportImage(int type, int category) {
		if(type == UIMapControlJNI.MAP_MARK_TYPE_USER_REPORT) {
			switch(category){
			case UIUserReportJNI.SNS_CATEGORY_TRAFFICJAM:
				return R.drawable.navicloud_and_262b;
			case UIUserReportJNI.SNS_CATEGORY_TRAFFICACCIDENT:
				return R.drawable.navicloud_and_261b;
			case UIUserReportJNI.SNS_CATEGORY_ROADCLOSE:
				return R.drawable.navicloud_and_267b;
			case UIUserReportJNI.SNS_CATEGORY_TRAFFICCONTROL:
				return R.drawable.navicloud_and_266b;
			case UIUserReportJNI.SNS_CATEGORY_ROADDAMAGE:
				return R.drawable.navicloud_and_265b;
			case UIUserReportJNI.SNS_CATEGORY_WATERFROZEN:
				return R.drawable.navicloud_and_268b;
			case UIUserReportJNI.SNS_CATEGORY_SPEEDCAMERA:
				return R.drawable.navicloud_and_263b;
			case UIUserReportJNI.SNS_CATEGORY_POLICE:
				return R.drawable.navicloud_and_264b;
			case UIUserReportJNI.SNS_CATEGORY_OTHER:
				return R.drawable.navicloud_and_260b;
			default:
				return 0;
			}
		} else if(type == UIMapControlJNI.MAP_MARK_TYPE_POINT){
			switch(category){
			case UIPointControlJNI.PntToUIResponse_PNT_POINT_TYPE_HOME:
				return R.drawable.navicloud_and_553a;
			case UIPointControlJNI.PntToUIResponse_PNT_POINT_TYPE_COMPANY:
				return R.drawable.navicloud_and_554a;
			case UIPointControlJNI.PntToUIResponse_PNT_POINT_TYPE_REGPOINT:
				return R.drawable.navicloud_and_568a;
			default:
				return 0;
			}
			
		} else if(type == UIMapControlJNI.MAP_MARK_TYPE_VICINITY
				|| type == UIMapControlJNI.MAP_MARK_TYPE_MAP_SYMBOL) {
			return R.drawable.navicloud_and_008a;
		} else if(type == UIMapControlJNI.MAP_MARK_TYPE_TRAFFIC){
			return trafficMarkid.get(category);
		}
		else {
			return 0;
		}
	}
	
	private static final SparseIntArray trafficMarkid = new SparseIntArray();
	static {
		trafficMarkid.put(1049, R.drawable.navicloud_and_294a);
		trafficMarkid.put(1025, R.drawable.navicloud_and_270a);
		trafficMarkid.put(1026, R.drawable.navicloud_and_271a);
		trafficMarkid.put(1027, R.drawable.navicloud_and_272a);
		trafficMarkid.put(1028, R.drawable.navicloud_and_273a);
		trafficMarkid.put(1029, R.drawable.navicloud_and_274a);
		trafficMarkid.put(1030, R.drawable.navicloud_and_275a);
		trafficMarkid.put(1031, R.drawable.navicloud_and_276a);
		trafficMarkid.put(1032, R.drawable.navicloud_and_277a);
		trafficMarkid.put(1033, R.drawable.navicloud_and_278a);
		trafficMarkid.put(1034, R.drawable.navicloud_and_279a);
		trafficMarkid.put(1035, R.drawable.navicloud_and_280a);
		trafficMarkid.put(1036, R.drawable.navicloud_and_281a);
		trafficMarkid.put(1037, R.drawable.navicloud_and_282a);
		trafficMarkid.put(1038, R.drawable.navicloud_and_283a);
		trafficMarkid.put(1039, R.drawable.navicloud_and_284a);
		trafficMarkid.put(1040, R.drawable.navicloud_and_285a);
		trafficMarkid.put(1041, R.drawable.navicloud_and_286a);
		trafficMarkid.put(1042, R.drawable.navicloud_and_287a);
		trafficMarkid.put(1045, R.drawable.navicloud_and_288a);
		trafficMarkid.put(1043, R.drawable.navicloud_and_289a);
		trafficMarkid.put(1044, R.drawable.navicloud_and_290a);
		trafficMarkid.put(1046, R.drawable.navicloud_and_291a);
		trafficMarkid.put(1047, R.drawable.navicloud_and_292a);
		trafficMarkid.put(1048, R.drawable.navicloud_and_293a);
	}

	
	private void requestPoint() {
		if(requstList.size()>0 && isNetEnable()){
			index = requstList.keyAt(0);
			PointBean point = requstList.valueAt(0);
			UISearchControlJNI.Instance().OnUpdateDetailInfo(UISearchControlJNI.UIC_SCM_SRCH_TYPE_MAP_SYMBOL, point.getIndex());
			requestId = UISearchControlJNI.Instance().GetReqID(UISearchControlJNI.UIC_SCM_SRCH_TYPE_MAP_SYMBOL);
			
		} else {
			requestId = -1;
			index = -1;
		}
	}

	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		if(triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_UISR_SEARCH_OK
				&& triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_UISR_SEARCH_EXCEPTION
				&& triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_UISR_SEARCH_ERROR) {
			return false;
		}
		
		if(triggerInfo.m_lParam1 != requestId) {
			return false;
		}
		
		if(triggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_UISR_SEARCH_OK) {
			
			UISearchDetailInfoResultJNI mDetailResult = UISearchControlJNI.Instance().GetDetailInfoResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_CHECK_TOUCH_POINT);

			requstList.get(index).setName(mDetailResult.GetTouchedMarkName(index));
			requstList.remove(index);
			int index1 = list.indexOf(getlist.get(index));
			Object[] data = builder.getData(index1);
			data[1] = mDetailResult.GetTouchedMarkName(index1);
			builder.set(index1, data);
			builder.notifyDataSetChanged();		
			
		}
		requestPoint();
		return true;
	}

	@Override
	protected void OnDestroy() {
		GlobalTrigger.getInstance().removeTriggerListener(this);
		super.OnDestroy();
	}

}
