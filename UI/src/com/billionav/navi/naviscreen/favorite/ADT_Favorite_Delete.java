package com.billionav.navi.naviscreen.favorite;
import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.navi.component.actionbar.ActionbarInput;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.download.TabHostListContainer;
import com.billionav.navi.system.PLog;
import com.billionav.ui.R;

public class ADT_Favorite_Delete extends ActivityBase{
	private TabHostNavi tabhost;
	private Faverate_ListContainer_Delete faverate_listContainer = null;
	private ActionbarInput searchinput;
	private String searchFilerStr;
	private int currentListState;
	private ArrayList<UIPointData> pointLists;
	
	private boolean isDeleted = false;
	
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		initializereSource();
		initializeLoad();	
		addActionItem3(R.string.STR_MM_01_03_03_01, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int[] checkedids = faverate_listContainer.getCheckedIds();
				if(currentListState == TabHostListContainer.MY_TAB_ONE) {
					UIPointControlJNI.Instance().reqDeleteFavorites(checkedids);
				} else {
					UIPointControlJNI.Instance().reqDeleteHistroies(checkedids);
				}
				
				showProgress();
				
			}
		});
		addActionItem3(R.string.STR_MM_01_03_03_02, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				faverate_listContainer.allselect();
				faverate_listContainer.refresh("");
			}
		});
	}
	
	private void initializereSource()
	{
		currentListState = getBundleNavi().getInt("currentListState");
		pointLists = (ArrayList<UIPointData>) getBundleNavi().get("displayData");
		searchFilerStr = getBundleNavi().getString("searchFilerStr");
		
		searchinput = new ActionbarInput(this);
		faverate_listContainer = new Faverate_ListContainer_Delete(this,false);
		faverate_listContainer.setCurrentListState(currentListState);
		faverate_listContainer.setData(pointLists);
		tabhost = new TabHostNavi(this,faverate_listContainer,searchinput);
		tabhost.dismissTabhost();
	}
	
	private void initializeLoad() {
		setContentView(tabhost);
		if (currentListState == TabHostListContainer.MY_TAB_ONE){
			setTitleText(R.string.STR_MM_01_03_01_07);
		}
		else{
			setTitleText(R.string.STR_MM_06_01_01_121);
		}
		setDefaultBackground();
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		faverate_listContainer.refresh("");
	}
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		PLog.i("Trigger", "Trigger");
		switch(cTriggerInfo.m_iTriggerID)
		{
		case NSTriggerID.POINT_RESPONSE_DELETE_POINT:
			if(cTriggerInfo.m_lParam1 != 0) {
				if(currentListState == TabHostListContainer.MY_TAB_ONE) {
					UIPointControlJNI.Instance().reqGetFavorite("");
				} else {
					UIPointControlJNI.Instance().reqGetHistroy("");
				}
			}
			break;
		case NSTriggerID.POINT_RESPONSE_GET_POINT_LIST:
			UIPointData[] points;
			
//			if(currentListState == TabHostListContainer.MY_TAB_ONE) {
//				points = UIPointControlJNI.Instance().getFavoriteList();
//			} else {
//				points = UIPointControlJNI.Instance().getHistroyList();
//			}
			points = UIPointControlJNI.Instance().getBookmarkData();
			faverate_listContainer.setFilter(points, searchFilerStr);
			faverate_listContainer.refresh("");
			isDeleted = true;
			dismissProgress();
			break;
		default:
			break;
		}
		return super.OnTrigger(cTriggerInfo);
	}
		
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(isDeleted) {
				UIPointData[] points;
				
//				if(currentListState == TabHostListContainer.MY_TAB_ONE) {
//					points = UIPointControlJNI.Instance().getFavoriteList();
//				} else {
//					points = UIPointControlJNI.Instance().getHistroyList();
//				}
				points = UIPointControlJNI.Instance().getBookmarkData();
				getBundleNavi().putBoolean("deleteback", true);
				getBundleNavi().put("deletedata", points);
				
			}
		}
		return super.OnKeyDown(keyCode, event);
	}
}
