package com.billionav.navi.naviscreen.setting;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.listcomponent.ListItemScheduleResult;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.map.TouchMapControl.PointBean;
import com.billionav.navi.naviscreen.schedule.ScheduleDataList;
import com.billionav.navi.naviscreen.schedule.ScheduleModel;
import com.billionav.navi.uitools.HybridUSTools;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.getRouteInfoTask;
import com.billionav.ui.R;

public class ADT_Schedule_Activity extends ActivityBase{
	
	private ListView listContainer;
	private SchedulePointListAdapter adpter;

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.src_schedule_list);
		findViews();
		initialize();
		setTitle(R.string.STR_COM_037);
	}
	

	private void findViews() {
		listContainer = (ListView) findViewById(R.id.list_container);
		listContainer.setAdapter(adpter = new SchedulePointListAdapter());
	}
	
	private void initialize() {
		setDefaultBackground();
		//add point
		addActionItem2(R.drawable.navicloud_and_446a, R.string.STR_COM_039, new OnClickListener() {
			@Override
			public void onClick(View v) {
				MenuControlIF.Instance().ForwardWinChange(ADT_Schedule_PointMap.class);
			}
		});
		
		addActionItem2(R.drawable.navicloud_and_427a, R.string.STR_COM_051, new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgress();
				HybridUSTools.getInstance().readCalendar(ADT_Schedule_Activity.this, true);
			}
		});
		
		listContainer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PointBean bean = new PointBean();
				long[] lonlat = new long[2];
				lonlat[0] = ScheduleDataList.getInstance().getScheduleAtIndex(position).lon;
				lonlat[1] = ScheduleDataList.getInstance().getScheduleAtIndex(position).lat;
				bean.setLonlat(lonlat);
				bean.setName(ScheduleDataList.getInstance().getScheduleAtIndex(position).pointName);
				getBundleNavi().put("SchedulePointBean", bean);
				MenuControlIF.Instance().ForwardWinChange(ADT_Schedule_Result.class);
			}
		});
		
		listContainer.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("HybridUS", "setOnLongClickListener : position = " + position) ;
				ScheduleDataList.getInstance().currentPosition = position;
				showDeleteSchedulePointWarning(new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ScheduleDataList.getInstance().remoeScheduleAtIndex(
								ScheduleDataList.getInstance().currentPosition);
						refreshList();
					}
				});
				return true;
			}
		});
	}
	
	private void showDeleteSchedulePointWarning(DialogInterface.OnClickListener listener){
		CustomDialog dialog = new CustomDialog(this);
		dialog.setTitle(getString(R.string.STR_COM_049));
		dialog.setMessage(getString(R.string.STR_COM_050));
		dialog.setPositiveButton("OK", listener);
		dialog.setNegativeButton("Cancel", null);
		dialog.show();
		dialog.setEnterBackKeyAllowClose(true);
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		refreshETAInfo();
	}
	
	private void refreshETAInfo(){
		int count = ScheduleDataList.getInstance().getScheduleCount();
		for(int i =0; i< count; i++) {
			int actualToArrivalTime = ScheduleDataList.getInstance().getScheduleAtIndex(i).actualToArrivalTime;
			if(actualToArrivalTime == 0){
				getRouteInfoTask.getInstance().doScheduleTask(i);
				refreshList();
				break;
			}
		}
	}

	private void refreshList() {
		adpter.notifyDataSetChanged();
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		if(cTriggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_PATH_FIND_FINISH_PATH_INFO){
//			ScheduleDataList.getInstance()
//				.getScheduleAtIndex((int)cTriggerInfo.m_lParam1)
//					.actualToArrivalTime = (int)cTriggerInfo.m_lParam2;
			refreshList();
		}
		if(cTriggerInfo.m_iTriggerID == NSTriggerID.SEARCH_RESPONSE_PINPOINT_INFO){
			String pointName = cTriggerInfo.m_String1;
			if(!TextUtils.isEmpty(pointName)){
				ScheduleDataList.getInstance().getScheduleAtIndex(
						ScheduleDataList.getInstance().getScheduleCount() - 1).pointName = pointName;
				ScheduleDataList.getInstance().saveScheduleList();
				refreshList();
			}
		}
		if(cTriggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_INTENT_SYNC_CALENDAR_CALLBACK){
			dismissProgress();
			refreshETAInfo();
			return true;
		}
		return super.OnTrigger(cTriggerInfo);
	}
	
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		refreshList();
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			
		}
		return super.OnKeyDown(keyCode, event);
	}
	
	Dialog dialog = null; 
	
	Calendar calendar = Calendar.getInstance();
	
	TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker timerPicker, int hourOfDay, int minute) {
			
		}
	};
	
	public void showWarnIsInvalidTimeDialog() {
		CustomDialog dialog = new CustomDialog(this);
		dialog.setTitle(this.getString(R.string.STR_MM_10_04_02_39));
		dialog.setMessage(this.getString(R.string.STR_COM_042));
		dialog.setPositiveButton(R.string.MSG_00_00_00_11, null);
		dialog.show();
	}
	
	class SchedulePointListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return ScheduleDataList.getInstance().getScheduleCount();
		}

		@Override
		public Object getItem(int position) {
			return ScheduleDataList.getInstance().getScheduleAtIndex(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ScheduleModel bean = ScheduleDataList.getInstance().getScheduleAtIndex(position);
			bean.index = position;
			String name =  bean.pointName;
			String expectTime = "";
			if(HybridUSTools.getInstance().isNotEmpty(bean.exceptToArriveTime)){
				expectTime = ADT_Schedule_Activity.this.getString(R.string.STR_COM_038) +"  :  "+ bean.exceptToArriveTime;
			}
			String arrivalTime = "";
			if(bean.actualToArrivalTime > 0){
				arrivalTime = ADT_Schedule_Activity.this.getString(R.string.STR_COM_040) 
						+ "  :  " + RouteTool.subStitutionTime(bean.actualToArrivalTime);
			}else if(bean.actualToArrivalTime == -1){
				arrivalTime = ADT_Schedule_Activity.this.getString(R.string.STR_COM_040) 
						+ "  : - ";
			}
			if(convertView == null) {
				convertView = new ListItemScheduleResult(ADT_Schedule_Activity.this, 
						name, arrivalTime, expectTime, R.drawable.navicloud_and_462a);
				if(convertView != null){
					convertView.setTag(bean);
					((ListItemScheduleResult)convertView).setClockListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ScheduleModel bean = (ScheduleModel)v.getTag();
							ScheduleDataList.getInstance().currentPosition = bean.index;
							if(!getDateTimePickerDialog(bean.index).isShowing()){
								getDateTimePickerDialog(bean.index).show();
							}
						}
					});
				}
				((ListItemScheduleResult)convertView).setTextColor(bean.validTime);
			} else {
				ListItemScheduleResult item = (ListItemScheduleResult) convertView;
				item.setInfo(name,arrivalTime, expectTime);
				item.setTag(bean);
				item.setTextColor(bean.validTime);
			}
			
			return convertView;
		}
	}
	
	
//	public Calendar time = Calendar.getInstance(Locale.US);
	private DatePicker datePicker;
	private TimePicker timePicker;
	
	private AlertDialog timeDialog;
	public AlertDialog getDateTimePickerDialog(int index){
		LinearLayout dateTimeLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.date_time_picker, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.DatePicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.TimePicker);
		timePicker.setIs24HourView(true);
		Calendar time = Calendar.getInstance();
		if(HybridUSTools.getInstance().isNotEmpty(ScheduleDataList.getInstance().getScheduleAtIndex(index).exceptToArriveTime)){
			int[] data = HybridUSTools
					.getTimeFromString(ScheduleDataList.getInstance()
							.getScheduleAtIndex(index).exceptToArriveTime);
			time.set(Calendar.YEAR, data[0]);
			time.set(Calendar.MONTH, data[1] - 1);
			time.set(Calendar.DAY_OF_MONTH, data[2]);
			time.set(Calendar.HOUR_OF_DAY, data[3]);
			time.set(Calendar.MINUTE, data[4]);
		}
		datePicker.init(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), null);
		
		timePicker.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(time.get(Calendar.MINUTE));
		
		
		timeDialog = new AlertDialog.Builder(this).setTitle("Set time").setView(dateTimeLayout)
				.setPositiveButton("Set", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						datePicker.clearFocus();
						timePicker.clearFocus();
						Calendar c = Calendar.getInstance();
						c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
								timePicker.getCurrentHour(), timePicker.getCurrentMinute());
						
						if(RouteTool.isValidTime(ScheduleDataList.getInstance().currentPosition 
								, df.format(c.getTime()))){
							ScheduleModel model = ScheduleDataList.getInstance()
									.getScheduleAtIndex(ScheduleDataList.getInstance().currentPosition);
							model.exceptToArriveTime = df.format(c.getTime());
							model.validTime = true;
							ScheduleDataList.getInstance().saveScheduleList();
							refreshList();
						}else{
							showWarnIsInvalidTimeDialog();
						}
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).show();
		return timeDialog;
	}

}
