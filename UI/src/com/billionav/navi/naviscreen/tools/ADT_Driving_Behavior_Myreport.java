package com.billionav.navi.naviscreen.tools;

import java.util.Calendar;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.ui.R;

public class ADT_Driving_Behavior_Myreport extends ActivityBase {

	private EditText startDate = null;
	private EditText endDate = null;
	private ImageButton inquires_01 = null;
	private ImageButton inquires_02 = null;
	private TextView urgentAcceleration = null;
	private TextView urgentSlowdown = null;
	private TextView sharpTurn = null;
	private RatingBar level = null;
	
	private TextView pressLine = null;
	private TextView carDistance = null;

	private TextView currentlyRanking = null;

	private int urgentAcceleration_value;
	private int urgentSlowdown_value;
	private int sharpTurn_value;
	private int level_value = 0;
	private int pressLine_value = 0;
	private int carDistance_value = 0;

	private int currentlyRanking_value;

	private int mYear_start = 0;
	private int mMonth_start = 0;
	private int mDay_start = 0;
	
	private int temp_yearStart = 0;
	private int temp_monthStart = 0;
	private int temp_dayStart = 0;

	private int mYear_end = 0;
	private int mMonth_end = 0;
	private int mDay_end = 0;
	
	private int temp_yearEnd = 0;
	private int temp_monthEnd = 0;
	private int temp_dayEnd = 0;


	private CProgressDialog progressDialog = null;
	private DatePicker picker = null;

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_driving_behavior_myreport);
		setDefaultBackground();
		if(UserControl_ManagerIF.Instance().GetLogInUserInfo()!=null){
			String temp = UserControl_ManagerIF.Instance().GetLogInUserInfo().m_strNickName;
			if(!TextUtils.isEmpty(temp)){
				setTitle(getString(R.string.STR_MM_10_04_02_24,UserControl_ManagerIF.Instance().GetLogInUserInfo().m_strNickName));
			}else{
				setTitle(getString(R.string.STR_MM_10_04_02_24,""));
			}
			
		}else{
			setTitle(getString(R.string.STR_MM_10_04_02_24,""));
		}	
		final Calendar c = Calendar.getInstance();
		mYear_end = c.get(Calendar.YEAR);
		mMonth_end = c.get(Calendar.MONTH);
		mDay_end = c.get(Calendar.DAY_OF_MONTH);
		temp_yearEnd = mYear_end;
		temp_monthEnd = mMonth_end;
		temp_dayEnd = mDay_end;
		findViews();
	
		final Calendar c_start = Calendar.getInstance();
		c_start.add(Calendar.MONTH, -1);
		mYear_start = c_start.get(Calendar.YEAR);
		mMonth_start =c_start.get(Calendar.MONTH);
		mDay_start = c_start.get(Calendar.DAY_OF_MONTH);
		temp_yearStart = mYear_start;
		temp_monthStart = mMonth_start;
		temp_dayStart = mDay_start;
		setClickEvent();
		updateDisplay();
		upDateMyReportInfo();
		if (isNetEnable()) {
			long isSend = UserControl_ManagerIF.Instance()
					.SNSMYDATAGetUserStyleInfo("month",
							startDate.getText().toString());
			if (isSend != -1) {
				progressDialog = CProgressDialog.makeProgressDialog(
						ADT_Driving_Behavior_Myreport.this,
						R.string.STR_MM_10_04_02_18);
				progressDialog.show();
			}
		}
	}


	private void updateDisplay() {
		endDate.setText(new StringBuilder()
				.append(mYear_end)
				.append("-")
				.append((mMonth_end + 1) < 10 ? "0" + (mMonth_end + 1)
						: (mMonth_end + 1)).append("-")
				.append((mDay_end < 10) ? "0" + mDay_end : mDay_end));
		startDate.setText(new StringBuilder()
				.append(mYear_start)
				.append("-")
				.append((mMonth_start + 1) < 10 ? "0" + (mMonth_start + 1)
						: (mMonth_start + 1)).append("-")
				.append((mDay_start < 10) ? "0" + mDay_start : mDay_start));

	}
   private void requestNewData(){
	   if(mYear_end > mYear_start){
			getReport();
	   }else if(mYear_end == mYear_start){
		   if(mMonth_end > mMonth_start){
			   getReport();
		   }else if(mMonth_end == mMonth_start){
			   if(mDay_end >= mDay_start){
				   getReport();
			   }
		   }else{
			   
		   }
	   }else{
		   
	   }
   }

private void getReport() {
	long isSend = UserControl_ManagerIF.Instance().SNSMYDATAGetUserStyleInfo("", startDate.getText().toString(), endDate.getText().toString());
	if (isSend != -1) {
	    progressDialog = CProgressDialog.makeProgressDialog(ADT_Driving_Behavior_Myreport.this, R.string.STR_MM_10_04_02_18);
	    progressDialog.show();
	}
}

private boolean isSettingStartTimeEnable(){
//	if(temp_yearStart <= mYear_end && temp_monthStart <= mMonth_end && temp_dayStart <= mDay_end){
	if(temp_yearStart < mYear_end){
		return true;
	}else if(temp_yearStart == mYear_end){
		if(temp_monthStart < mMonth_end){
			return true;
		}else if(temp_monthStart == mMonth_end){
			if(temp_dayStart <= mDay_end){
				return true;
			}else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	else{
		return false;
	}
}
private boolean isSettingEndTimeEnable(){
//	if(temp_yearEnd >= mYear_start && temp_monthEnd >= mMonth_start && temp_dayEnd >= mDay_start){
	if(temp_yearEnd > mYear_start){
		return true;
	}else if(temp_yearEnd == mYear_start){
		if(temp_monthEnd > mMonth_start){
			return true;
		}else if(temp_monthEnd == mMonth_start){
			if(temp_dayEnd >= mDay_start){
				return true;
			}else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	else{
		return false;
	}
}

	private void setClickEvent() {
		inquires_01.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				picker = new DatePicker(ADT_Driving_Behavior_Myreport.this);
				picker.init(mYear_start, mMonth_start, mDay_start,
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								temp_yearStart = year;
								temp_monthStart = monthOfYear;
								temp_dayStart = dayOfMonth;

							}
						});
				showDateDialog(R.string.STR_MM_10_04_02_37,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if(isSettingStartTimeEnable()){
									mYear_start = temp_yearStart;
									mMonth_start = temp_monthStart;
									mDay_start = temp_dayStart;
									updateDisplay();
									requestNewData();
								}else{
									showDialog();
								}
							}
						});

			}

		});
		inquires_02.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				picker = new DatePicker(ADT_Driving_Behavior_Myreport.this);
				picker.init(mYear_end, mMonth_end, mDay_end,
						new OnDateChangedListener() {

							@Override
							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								temp_yearEnd = year;
								temp_monthEnd = monthOfYear;
								temp_dayEnd = dayOfMonth;
							}
						});
				showDateDialog(R.string.STR_MM_10_04_02_38,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if(isSettingEndTimeEnable()){
									mYear_end = temp_yearEnd;
									mMonth_end = temp_monthEnd;
									mDay_end = temp_dayEnd;
									updateDisplay();
									requestNewData();
								}else{
									showDialog();
								}
							}
						});

			}
		});

	}

	private void showDialog() {
		CustomDialog b = new CustomDialog(ADT_Driving_Behavior_Myreport.this);
		b.setMessage(R.string.MSG_10_04_02_03);

		b.setTitle(R.string.STR_MM_10_04_02_39);
		b.setNegativeButton(R.string.STR_COM_003, null);

		b.show();
	}
	
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		// TODO Auto-generated method stub
		int iTriggerID = triggerInfo.GetTriggerID();
		int iParams = (int) triggerInfo.GetlParam1();
		switch (iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_UC_MYDATA_GET_USERDATA: //
			progressDialog.dismiss();
			if (iParams == 0) {
				getMyReportInfoValue();
				upDateMyReportInfo();
			} else if (iParams == 1) {
				CustomToast.showToast(ADT_Driving_Behavior_Myreport.this, R.string.STR_COM_013, Toast.LENGTH_SHORT);
			} else if (iParams == 2) {
				CustomToast.showToast(ADT_Driving_Behavior_Myreport.this,
						R.string.STR_COM_014,
						Toast.LENGTH_SHORT);
			}
			return true;
		default:
			return super.OnTrigger(triggerInfo);
		}
	}

	private void showDateDialog(int stringId,OnClickListener l) {
		CustomDialog dialog = new CustomDialog(ADT_Driving_Behavior_Myreport.this);
	
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); 
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		RelativeLayout layout = new RelativeLayout(ADT_Driving_Behavior_Myreport.this);
		layout.addView(picker, lp);
		dialog.addView(layout);
		dialog.setTitle(stringId);
		dialog.setNegativeButton(R.string.STR_COM_001, null);
		dialog.setPositiveButton(R.string.STR_COM_003, l);
		dialog.show();
	}
	private void upDateMyReportInfo() {

		urgentAcceleration.setText(getString(R.string.STR_MM_10_04_02_28, urgentAcceleration_value));
		urgentSlowdown.setText(getString(R.string.STR_MM_10_04_02_28, urgentSlowdown_value));
		sharpTurn.setText(getString(R.string.STR_MM_10_04_02_28,sharpTurn_value));
		level.setRating(level_value);
		currentlyRanking.setText(getString(R.string.STR_MM_10_04_02_17,currentlyRanking_value));

		pressLine.setText(getString(R.string.STR_MM_10_04_02_28,pressLine_value));
		carDistance.setText(getString(R.string.STR_MM_10_04_02_28,carDistance_value));
	}

	private void getMyReportInfoValue() {

		String temp_urgentAcceleration_end = UserControl_ManagerIF.Instance()
				.getM_cUserStyleInfo().getM_StrCloseRapidSpeedUp();
		String temp_urgentAcceleration_start = UserControl_ManagerIF.Instance()
				.getM_cUserStyleInfo().getM_strOpenRapidSpeedUp();
		if (!TextUtils.isEmpty(temp_urgentAcceleration_end)
				&& !TextUtils.isEmpty(temp_urgentAcceleration_start)) {
			urgentAcceleration_value =(int)( Float.parseFloat(temp_urgentAcceleration_end) - Float.parseFloat(temp_urgentAcceleration_start));
		}

		String temp_urgentSlowdown_end = UserControl_ManagerIF.Instance()
				.getM_cUserStyleInfo().getM_StrCloseRapidSpeedDown();
		String temp_urgentSlowdown_start = UserControl_ManagerIF.Instance()
				.getM_cUserStyleInfo().getM_strOpenRapidSpeedDown();
		if (!TextUtils.isEmpty(temp_urgentSlowdown_end)
				&& !TextUtils.isEmpty(temp_urgentSlowdown_start)) {

			urgentSlowdown_value = (int)( Float.parseFloat(temp_urgentSlowdown_end) - Float.parseFloat(temp_urgentSlowdown_start));
		}

		String temp_sharpTurn_end = UserControl_ManagerIF.Instance()
				.getM_cUserStyleInfo().getM_StrCloseRapidTurn();
		String temp_sharpTurn_start = UserControl_ManagerIF.Instance()
				.getM_cUserStyleInfo().getM_strOpenRapidTurn();
		if (!TextUtils.isEmpty(temp_sharpTurn_end)
				&& !TextUtils.isEmpty(temp_sharpTurn_start)) {

			sharpTurn_value = (int)( Float.parseFloat(temp_sharpTurn_end) - Float.parseFloat(temp_sharpTurn_start));
		}

		String temp_level_end = UserControl_ManagerIF.Instance()
				.getM_cUserStyleInfo().getM_StrCloseStar();
		String temp_level_start = UserControl_ManagerIF.Instance()
				.getM_cUserStyleInfo().getM_strOpenStar();
		if (!TextUtils.isEmpty(temp_level_end)
				&& !TextUtils.isEmpty(temp_level_start)) {

			level_value = (int)( Float.parseFloat(temp_level_end) - Float.parseFloat(temp_level_start));
		}
        String temp_pressLine_end = UserControl_ManagerIF.Instance().getM_cUserStyleInfo().getM_strCloseDriveOnline();
        String temp_pressLine_start = UserControl_ManagerIF.Instance().getM_cUserStyleInfo().getM_strOpenDriveOnline();
		if (!TextUtils.isEmpty(temp_pressLine_end)
				&& !TextUtils.isEmpty(temp_pressLine_start)) {

			pressLine_value = (int)( Float.parseFloat(temp_pressLine_end) - Float.parseFloat(temp_pressLine_start));
		}

		String temp_carDistance_end = UserControl_ManagerIF.Instance().getM_cUserStyleInfo().getM_strCloseDriveClose();
		String temp_carDistance_start = UserControl_ManagerIF.Instance().getM_cUserStyleInfo().getM_strOpenDriveClose();
		if (!TextUtils.isEmpty(temp_carDistance_end)
				&& !TextUtils.isEmpty(temp_carDistance_start)) {

			carDistance_value = (int)( Float.parseFloat(temp_carDistance_end) - Float.parseFloat(temp_carDistance_start));
		}
		String temp_currentlyRanking = UserControl_ManagerIF.Instance()
				.getM_cUserStyleInfo().getM_strUserRankAtPeriodEnd();
		if (!TextUtils.isEmpty(temp_currentlyRanking)) {
			currentlyRanking_value = Integer.valueOf(UserControl_ManagerIF
					.Instance().getM_cUserStyleInfo()
					.getM_strUserRankAtPeriodEnd());
		}

	}

	private void findViews() {
		startDate = (EditText) findViewById(R.id.my_report_startdate);
		endDate = (EditText) findViewById(R.id.my_report_enddate);
		inquires_01 = (ImageButton) findViewById(R.id.my_report_inquires01);
		inquires_02 = (ImageButton) findViewById(R.id.my_report_inquires02);
		urgentAcceleration = (TextView) findViewById(R.id.my_report_urgent_acceleration);
		urgentSlowdown = (TextView) findViewById(R.id.my_report_urgent_slowdown);
		sharpTurn = (TextView) findViewById(R.id.my_report_sharp_turn);
		level = (RatingBar) findViewById(R.id.my_report_level);
		currentlyRanking = (TextView) findViewById(R.id.my_report_currently_ranking);
		pressLine = (TextView) findViewById(R.id.my_report_press_line);
		carDistance = (TextView) findViewById(R.id.my_report_car_distance);
	}

}
