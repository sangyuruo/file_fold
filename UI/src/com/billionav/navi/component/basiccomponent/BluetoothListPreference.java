package com.billionav.navi.component.basiccomponent;

import android.content.Context;
import android.util.AttributeSet;

import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.uicommon.UIC_CradleCommon;

public class BluetoothListPreference extends ListPreference implements TriggerListener{

	public BluetoothListPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setEnties("external gps", "internal gps", "none");
		refresh();
	}

	public BluetoothListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setEnties("external gps", "internal gps", "none");
		refresh();
	}

	private void refresh() {
//		if(CLocationListener.Instance().isCurrentExternalGpsOn()) {
//			super.seletedIndex(0);
//			setSummary("external gps on");
//		} else if(CLocationListener.Instance().isCurrentInternalGpsOn()) {
//			super.seletedIndex(1);
//			setSummary("internal gps on");
//		} else {
//			super.seletedIndex(2);
//			setSummary("none connect");
//		}
	}
	@Override
	public void seletedIndex(int index) {
		// TODO Auto-generated method stub
		super.seletedIndex(index);
//		if(index == 2) {
//			CLocationListener.Instance().SwitchToDisable(true);
//		//	summaryText.setText("none connect");
//			setSummary("none connect");
//		} else if(index == 1){
//			CLocationListener.Instance().SwitchToInterGPS(true);
//			setSummary("internal gps on");
//		} else {
//			boolean isAutoConnect = UIC_CradleCommon.autoConnect();
//			if(isAutoConnect) {
//				setSummary("connectting, please wait...");
//			} else {
//				setSummary("connect error, please connect manual");
//			}
//		}
		
		GlobalTrigger.getInstance().addTriggerListener(this);
	}

	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		// TODO Auto-generated method stub
		switch (triggerInfo.GetTriggerID()) {
		case NSTriggerID.UIC_MN_TRG_LOC_GPS_CONNECT_STATUS:
//			if(triggerInfo.GetlParam1() == CLocationListener.BT_CONNECT_ACTION_EXCEPTION){
//				setSummary("connect fail");
//			}
			break;
		case NSTriggerID.UIC_MN_TRG_ECL_CERTIFICATE_FINISHED:
			if(triggerInfo.GetlParam1()==1){
				setSummary("external gps on");
			}
			break;
		default:
			break;
		}
		return false;
	}

	public void removeTriggerListener() {
		GlobalTrigger.getInstance().removeTriggerListener(this);
	}
}
