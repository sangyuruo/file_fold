package com.billionav.navi.naviscreen.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.ui.R;


public class ADT_Eco_Settings extends ActivityBase {

	private final static int Car_Weight =0;
	private final static int Car_Exhaust =1;
	private final static int Car_Height =2;
	private final static int Car_Width = 3;
	private final static int Car_AvgOilCost =4;
		
//	private final jniEcoControl ecoControl = new jniEcoControl();
	private ListViewNavi listView;
	private final String[] textValues = new String[5];
	private String[] textNames = null;
	private float favgOilCost ;
	private double carWeight;
	private double carHeight;
	private double carExhaust;
	private double carWidth;
	private float curAvgOilCost;
	private BaseAdapter adapter;
	private CustomDialog customDialog;
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.scr_eco_setting);
		textNames = new String[]{getString(R.string.STR_MM_10_03_01_02),getString(R.string.STR_MM_10_03_01_03),getString(R.string.STR_MM_10_03_01_04),getString(R.string.STR_MM_10_03_01_05),getString(R.string.STR_MM_10_03_01_06)};
		setDefaultBackground();
		setTitle(R.string.STR_MM_10_03_01_27);
		listView = (ListViewNavi)findViewById(R.id.eco_list);
		getData();
		adapter = new EcoSettingAdapter(getApplicationContext());
		listView.setAdapter(adapter );
		registerEvent();
	}
	
	private void registerEvent(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showAlert((int)id);
			}
		});
	}
	
	private void showAlert(int id){
		 
		 customDialog = new CustomDialog(this);
		 customDialog.setTitle(textNames[id]);
		 customDialog.setPositiveButton(R.string.STR_COM_003,new DLGClickListener(id));
		 customDialog.setNegativeButton(R.string.STR_COM_001, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
//				updateVariables();
			}
		});
		 
		 setCustomDialoginfo(id, customDialog);
		 
		 customDialog.show();

		 
	}
	
	
	private void setCustomDialoginfo(int id,CustomDialog dialog){
		switch (id) {
		case Car_Weight:
			dialog.setClutchInfo( (float)(carWeight/1000), 0.2f, 4 ,1, "t");
			break;
		case Car_Exhaust:
			dialog.setClutchInfo( (float)carExhaust, 0.1f, 5.0f ,0.9f, "L");
			break;
		case Car_Height:
			dialog.setClutchInfo( (float)carHeight, 5, 250 ,100, "cm");
			break;
		case Car_Width:
			dialog.setClutchInfo( (float)carWidth, 5, 250 ,140, "cm");
			break;
		case Car_AvgOilCost:
			dialog.setClutchInfo( (float)curAvgOilCost, 0.5f, 50 ,0, "km/L");
			break;
		default:
			break;
		}
	}
	
	private void getData(){
//		updateVariables();
		updateList(false);
	}
	
//	private void updateVariables(){
//		carWeight = ecoControl.GetCarWeight() ;	
//		if(carWeight > 4000 || carWeight <1000 ){
//			carWeight = 1600;
//		}
//		carExhaust = ecoControl.GetCarExhaust();	
//		if(carExhaust > 5.0 || carExhaust <0.9 ){
//			carExhaust = 1.8;
//		}
//		carHeight = ecoControl.GetCarHeight();	
//		if(carHeight > 250 || carHeight < 100 ){
//			carHeight = 140;
//		}
//		carWidth = ecoControl.GetCarWidth();
//		if(carWidth > 250 || carWidth < 140 ){
//			carWidth = 180;
//		}
//		curAvgOilCost=favgOilCost = ecoControl.GetAvgFuelConsumption();
//		if(curAvgOilCost>50.0f){
//			curAvgOilCost=favgOilCost =50.0f;
//		}else if(curAvgOilCost<0.0f){
//			curAvgOilCost=favgOilCost =0.0f;
//		}
//	}
	
	private void setCarData(){
//	   ecoControl.SetCarData((int)carHeight,(int)carWidth,(int)(carWeight),(float) carExhaust);
	}
	
	private void setAvgData(){
//		ecoControl.SetAvgFuelDiff(curAvgOilCost- favgOilCost);
	}
	
	private void updateList(boolean isNotify){
		textValues[0] = ((int)(carWeight /100.0f +0.5f))/10.0f +"t";
		textValues[1] = ((int)(carExhaust*10+0.5f))/10.0f +"L";
		textValues[2] = (int)(carHeight+0.5f)   +"cm";
		textValues[3] = (int)(carWidth +0.5f)  + "cm";
		if(curAvgOilCost <0){
			textValues[4] = "---km/L";
		}else{
			textValues[4] = ((int)(curAvgOilCost*10+0.5))/10.0f +"km/L";
		}
		if(isNotify){
			adapter.notifyDataSetChanged();
		}
	}
	
	private void updateList(){
		updateList(true);
	}
	
	private class DLGClickListener implements DialogInterface.OnClickListener{ 
		private final int id;
		public DLGClickListener(int id ){
			this.id  = id;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (id) {
			case Car_AvgOilCost:
				curAvgOilCost = customDialog.getClutchValue();
				setAvgData();
				break;
			case Car_Exhaust:
				carExhaust = customDialog.getClutchValue();
				setCarData();
				break;
			case Car_Height:
				carHeight = customDialog.getClutchValue();
				setCarData();
				break;
			case Car_Width:
				carWidth = customDialog.getClutchValue();
				setCarData();
				break;
			case Car_Weight:
				carWeight = customDialog.getClutchValue()*1000;
				setCarData();
				break;
			}
			updateList();
			dialog.dismiss();
			
		}
	}
	
	

	public static class ViewHolder {
		TextView txtView01;
		TextView txtView02;
	}

	public class EcoSettingAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public EcoSettingAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return textNames.length;
		}

		@Override
		public Object getItem(int position) {
			return 0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public boolean areAllItemsEnabled() {

			return false;
		}
		
		@Override
		public boolean isEnabled(int position) {
			if(position == Car_AvgOilCost && favgOilCost <0){
				return false;
			}
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item_txt2, parent,false);
				viewHolder = new ViewHolder();
				viewHolder.txtView01 = (TextView) convertView
						.findViewById(R.id.list_txt01);
				viewHolder.txtView02 = (TextView) convertView
						.findViewById(R.id.list_txt02);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.txtView01.setText(textNames[position]);
			viewHolder.txtView02.setText(textValues[position]);
			convertView.setBackgroundResource(R.drawable.list_selector_background);
			return convertView;

		}

	}
	
	
}
