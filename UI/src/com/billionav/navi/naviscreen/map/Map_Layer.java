package com.billionav.navi.naviscreen.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.listcomponent.ListContainer;
import com.billionav.navi.component.listcomponent.ListContainerCustomHeight;
import com.billionav.navi.component.listcomponent.ListItemCheckbox;
import com.billionav.navi.component.listcomponent.ListItemText;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.ui.R;

public class Map_Layer extends RelativeLayout {
	private ListContainerCustomHeight list;
	
//	static boolean friend = false;
//	static boolean surroundingEventdisplay = false;

	public Map_Layer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public Map_Layer(Context context) {
		super(context);
		
		initialize(context);
	}
	
	public static void initMapLayerData() {
		int surrorundingSates = SharedPreferenceData.getInt(SharedPreferenceData.STUPDM_SURROUNDING_EVENT_INT_KEY);
		UIMapControlJNI.SetSNSMarkSwitch(surrorundingSates);
		
		UIMapControlJNI.SetTrafficSwitcher(SharedPreferenceData.getBoolean(SharedPreferenceData.STUPDM_TRAFFIC_LINE_DISPLAY));
		
		int favouriteState = SharedPreferenceData.getInt(SharedPreferenceData.STUPDM_FAVORITE_DISPLAY_INT_KEY);
		UIMapControlJNI.SetRegPlaceSwitch(favouriteState);
	}

	private void initialize(Context context) {
		setBackgroundResource(R.drawable.navicloud_and_726a);
		int left = DensityUtil.dp2px(context, 1);
		int top = DensityUtil.dp2px(context, 1);
		int right = DensityUtil.dp2px(context, 1);
		int bottom = DensityUtil.dp2px(context, 0);
		setPadding(left, top, right, bottom);
		
		addView(list = new ListContainerCustomHeight(context));
		list.getlistView().setBackgroundResource(0);
		list.getlistView().setDivider(getResources().getDrawable(R.drawable.navicloud_and_741a));

		list.setSelector(R.xml.list_selector_map_layer);
		
		onPrepareList(list);
		
		updateButtonClearMap();
		
	    setListeners();
	}

	private static final int COMMON_STATE_ON = 1;
	private static final int COMMON_STATE_OFF = 0;
	
	private void setListeners() {
		realTimeTraffic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isChecked = realTimeTraffic.isChecked();
				SharedPreferenceData.setValue(SharedPreferenceData.STUPDM_TRAFFIC_LINE_DISPLAY, isChecked);
				UIMapControlJNI.SetTrafficSwitcher(isChecked);
				updateButtonClearMap();
			}
		});
		favorite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isChecked = favorite.isChecked();
				int newStatus = isChecked ? COMMON_STATE_ON : COMMON_STATE_OFF;
				SharedPreferenceData.setValue(SharedPreferenceData.STUPDM_FAVORITE_DISPLAY_INT_KEY, newStatus);
				UIMapControlJNI.SetRegPlaceSwitch(newStatus);
				updateButtonClearMap();
			}
		});
		surroundingEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int newState = surroundingEvent.isChecked()? COMMON_STATE_ON : COMMON_STATE_OFF;
				SharedPreferenceData.setValue(SharedPreferenceData.STUPDM_SURROUNDING_EVENT_INT_KEY, newState);
				UIMapControlJNI.SetSNSMarkSwitch(newState);
				updateButtonClearMap();
			}
		});
		clearMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				realTimeTraffic.setChecked(false);
				favorite.setChecked(false);
				surroundingEvent.setChecked(false);
				updateButtonClearMap();
			}
		});
	}
	
	private void updateButtonClearMap(){
		boolean isChecked = realTimeTraffic.isChecked()  || surroundingEvent.isChecked() || favorite.isChecked();
		setButtonClearMapEnable(isChecked);
	}
	
	private void setButtonClearMapEnable(boolean enabled){
		clearMap.setEnabled(enabled);
		if(enabled) {
			clearMap.setItemImage(R.drawable.navicloud_and_455a);
		} else {
			clearMap.setItemImage(R.drawable.navicloud_and_455c);
		}
	};

		boolean isTrafficLineOn = false;
		protected void onPrepareList(ListContainer list){
		if(!isInEditMode()){
			isTrafficLineOn = SharedPreferenceData.getBoolean(SharedPreferenceData.STUPDM_TRAFFIC_LINE_DISPLAY);
		}
		
		boolean isSurrondingEventOn = false;
		if(!isInEditMode()){
			isSurrondingEventOn = SharedPreferenceData.getInt(SharedPreferenceData.STUPDM_SURROUNDING_EVENT_INT_KEY, COMMON_STATE_OFF) == COMMON_STATE_ON;
		}
		
		boolean isFavoriteOn = false;
		if(!isInEditMode()) {
			isFavoriteOn = SharedPreferenceData.getInt(SharedPreferenceData.STUPDM_FAVORITE_DISPLAY_INT_KEY, COMMON_STATE_OFF) == COMMON_STATE_ON;
		}
		
		list.addItem(realTimeTraffic = new ListItemCheckbox(getContext(), R.drawable.navicloud_and_451a, R.string.STR_MM_01_01_01_10, isTrafficLineOn));
		//		list.addItem(friendLoction = new ListItemCheckbox(getContext(), R.drawable.navicloud_and_452a, R.string.STR_MM_01_01_01_07, SharedPreferenceData.TEMP_friendLoction.getBoolean()));
		list.addItem(favorite = new ListItemCheckbox(getContext(), R.drawable.navicloud_and_452a, R.string.STR_MM_01_03_01_07, isFavoriteOn));
		list.addItem(surroundingEvent = new ListItemCheckbox(getContext(), R.drawable.navicloud_and_453a, R.string.STR_MM_01_01_01_08, isSurrondingEventOn));
		list.addItem(clearMap = new ListItemText (getContext(),R.drawable.navicloud_and_455a, R.string.STR_MM_01_01_01_11)); 
	}
	
	private ListItemCheckbox realTimeTraffic;
//	private ListItemCheckbox friendLoction;
	private ListItemCheckbox favorite;
	private ListItemCheckbox surroundingEvent;
	private ListItemText clearMap;

}
