package com.billionav.navi.luxgenbranch.naviscreen.dest;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.navi.cn.Luxgen.R;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.map.ADT_AR_Main;
import com.billionav.navi.naviscreen.map.ADT_AR_Main_Tip;
import com.billionav.navi.naviscreen.map.ADT_DR_Main;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.map.ForwardARscreenControl;
import com.billionav.navi.naviscreen.misc.ADT_Voice_Recognition;
import com.billionav.navi.naviscreen.report.ADT_report_main;
import com.billionav.navi.naviscreen.setting.ADT_Settings_Main;
import com.billionav.navi.uitools.DialogTools;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.voicerecog.VoiceRecognizer;

public class ADT_Top_Menu_Luxgen extends ActivityBase{
	private RelativeLayout backgLayout;
	private GridView gridView;
	private SimpleAdapter simpleAdapter;
	private ArrayList<HashMap<String, Object>>  dataList = null;
	
	private static final int VIEW_ID_SETTING  = 0;
	private static final int VIEW_ID_NAVI = 1;
	private static final int VIEW_ID_VOICE = 2;
	private static final int VIEW_ID_REPORT = 3;
	private static final int VIEW_ID_AR = 4;
	private static final int VIEW_ID_VIDEO = 5;
	
	
	private static final double GRID_WIDTH_LANDSCAPE = (double)275/960;
	private static final double GRID_HEIGHT_LANDSCAPE = (double)200/540;
	private static final double GRID_WIDTH_PORTCAL = (double)220/540;
	private static final double GRID_HEIGHT_PORTCAL = (double)256/960;
	
	private static final double MARGIN_TOP_PORTCAL = (double)77/960;
	private static final double MARGIN_TOP_LANDSCAPE = (double)63/540;
	private static final int SHADOW_WIDTH = 25;
	
	private static final int POSITION_2 = 2;
	private static final int POSITION_3 = 3;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_top_menu_luxgen, false);
		backgLayout = (RelativeLayout) findViewById(R.id.layout);
		gridView = (GridView)findViewById(R.id.gridview);
		dataList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map =new HashMap<String, Object>();
//		//button 0
//		map.put("item_image", R.drawable.topmenu_vr_selector);
//		map.put("item_name", getString(R.string.STR_MM_01_00_00_02));
//		dataList.add(map);
//		//button 1
//		map =new HashMap<String, Object>();
//		map.put("item_image", R.drawable.topmenu_report_selector);
//		map.put("item_name", getString(R.string.STR_MM_01_00_00_03));
//		dataList.add(map);
		//button 2
		map =new HashMap<String, Object>();
		map.put("item_image", R.drawable.topmenu_setting_selector);
//		map.put("item_name", getString(R.string.STR_MM_01_00_00_04));
		
		dataList.add(map);
		//button 3
		map =new HashMap<String, Object>();
		map.put("item_image", R.drawable.topmenu_navi_selector);
//		map.put("item_name", getString(R.string.STR_MM_01_00_00_05));
		dataList.add(map);
		//button 4
//		map =new HashMap<String, Object>();
//		map.put("item_image", R.drawable.topmenu_ar_selector);
//		map.put("item_name", getString(R.string.STR_MM_01_00_00_06));
//		dataList.add(map);
		
//		//button 5
//		map =new HashMap<String, Object>();
//		map.put("item_image", R.drawable.topmenu_vedio_selector);
//		map.put("item_name", getString(R.string.STR_MM_01_00_00_07));
//		dataList.add(map);
		
		simpleAdapter = new SimpleAdapter(this, dataList, R.layout.top_menu_item, new String[]{"item_image","item_name"}, new int[]{R.id.top_image,R.id.top_text}){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = super.getView(position, convertView, parent);
				LayoutParams param = convertView.getLayoutParams();
				param.width = (int) (ScreenMeasure.getWidth()*(ScreenMeasure.isPortrait() ?GRID_WIDTH_PORTCAL:GRID_WIDTH_LANDSCAPE));
				param.height = (int) (ScreenMeasure.getHeight()*(ScreenMeasure.isPortrait() ?GRID_HEIGHT_PORTCAL:GRID_HEIGHT_LANDSCAPE));
				
				if(!ScreenMeasure.isPortrait() || (position != POSITION_2 && position != POSITION_3)) {
					convertView.setId(position);
				} else {
					ImageView mark = (ImageView) convertView.findViewById(R.id.top_image);
					TextView text = (TextView) convertView.findViewById(R.id.top_text);
					
					mark.setImageResource(position == POSITION_2? R.drawable.topmenu_navi_selector:R.drawable.topmenu_setting_selector);
//					text.setText(position == POSITION_2? R.string.STR_MM_01_00_00_05:R.string.STR_MM_01_00_00_04);
					convertView.setId(position == POSITION_2?VIEW_ID_NAVI:VIEW_ID_SETTING);
				}
				return convertView;
			}
		};
		gridView.setAdapter(simpleAdapter);
		adjustLayout();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch(view.getId()){
					case VIEW_ID_VOICE://voice
						if(VoiceRecognizer.instance().enterVrMode()) {
							ForwardWinChange(ADT_Voice_Recognition.class);
			            }
						break;
					case VIEW_ID_REPORT://report
						ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_CAMEEA_MODE);
						ForwardWinChange(ADT_report_main.class);
						break;
					case VIEW_ID_SETTING://setting
						ForwardWinChange(ADT_Settings_Main.class);
						break;
					case VIEW_ID_NAVI://navi
						getBundleNavi().putBoolean("Navigation", true);
						ForwardKeepDepthWinChange(ADT_Main_Map_Navigation.class);
						break;
					case VIEW_ID_AR://ar
						if(SharedPreferenceData.IS_NEED_AR_TIP.getBoolean()){
							ForwardWinChange(ADT_AR_Main_Tip.class);
						}else{
							MenuControlIF.Instance().setWinchangeWithoutAnimation();
							ForwardARscreenControl.getinstance().setDRIRFunChangeState( DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE,DRIRAppMain.DRIRAPP_AR_MODE);
							ForwardWinChange(ADT_AR_Main.class);
						}
						break;
					case VIEW_ID_VIDEO://video
						ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_LOW_DM_MODE);
						ForwardWinChange(ADT_DR_Main.class);
						break;
						default:break;
				}
				
			}
		});
	}
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	adjustLayout();
    	
    }
	private void adjustLayout() {
		boolean isPort = ScreenMeasure.isPortrait();
        backgLayout.setBackgroundResource(isPort ? R.drawable.topmenu_540x922_cn_bg : R.drawable.topmenu_960x502_cn_bg);
        gridView.setBackgroundResource(isPort ? R.drawable.topmenu_540x922_cn_fr : R.drawable.topmenu_960x502_cn_fr);
        gridView.setNumColumns(isPort ? 2:3);
        
        RelativeLayout.LayoutParams rlv = (RelativeLayout.LayoutParams)gridView.getLayoutParams();
        rlv.width = (int) (ScreenMeasure.getWidth() * (isPort?GRID_WIDTH_PORTCAL *2:GRID_WIDTH_LANDSCAPE *3) + 2);
        rlv.height = (int) (ScreenMeasure.getHeight() * (isPort?GRID_HEIGHT_PORTCAL *3:GRID_HEIGHT_LANDSCAPE *2) + 2*SHADOW_WIDTH +4);
        rlv.setMargins(0, (int) (ScreenMeasure.getHeight() *(isPort?MARGIN_TOP_PORTCAL:MARGIN_TOP_LANDSCAPE)), 0, 0);
        //        gridView.setLayoutParams(rlv);
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			DialogTools.createExitDialog(this).show();
			return true;
		}		
		return super.OnKeyDown(keyCode, event);
	}
}
