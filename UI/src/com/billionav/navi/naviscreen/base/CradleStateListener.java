package com.billionav.navi.naviscreen.base;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;

import com.billionav.jni.jniSetupControl;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.gps.CLocationListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.uitools.SetupOptionValue;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.ui.R;

public class CradleStateListener implements TriggerListener{
	private static CradleStateListener instance;
	private Context context = null;
	public static CradleStateListener  createInstance(Context context){
		if(instance == null){
			instance = new CradleStateListener(context);
		}
		return instance;
	}
	private CradleStateListener(Context context){
		this.context = context;
	}
	
	// show gps  dialog
	private void showGPSDialogs(){
		
        final CustomDialog dialog = new CustomDialog(context);
        dialog.setTitle(R.string.MSG_01_00_01_01);
        dialog.setMessage(R.string.MSG_COM_01_07);
		
        dialog.setPositiveButton(R.string.STR_COM_003, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dg, int which) {
				context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});
		
		
        dialog.setNegativeButton(R.string.STR_COM_001,null);
	 	dialog.show();
	}
	
	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		// TODO Auto-generated method stub
		if(triggerInfo.GetTriggerID() == NSTriggerID.UIC_MN_TRG_SENSOR_CRADLE_STATE_CHANGED){
			CustomDialog hintDialog;
			switch((int)triggerInfo.m_lParam1){
			case CLocationListener.LOCSNS_CRADLE_USE_DISCONNECT:
				hintDialog = new CustomDialog(context);
				//cradle connection failed
				hintDialog.setTitle(R.string.STR_COM_036);
				hintDialog.setMessage(R.string.MSG_COM_01_06);
				hintDialog.setPositiveButton(R.string.STR_COM_034,  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//reset flow
						CLocationListener.Instance().ResetCradleConnectStatus();
					}
				});
				hintDialog.setNegativeButton(R.string.STR_COM_033,  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//judge whether interior GPS has been opened  
						if(!isGpsOn()){
							//change background setting(next open app is internal gps state)
							SharedPreferenceData.setValue(SharedPreferenceData.GPS_CONNECTION_MODE, SetupOptionValue.LOCATION_FUNC_INNER);
							CLocationListener.Instance().SetCradleConnectStatus(SetupOptionValue.LOCATION_FUNC_INNER);
							notifyScreen(SetupOptionValue.LOCATION_FUNC_INNER);
							//switch to interior GPS
							showGPSDialogs();
						}
					}
				});
				hintDialog.show();
				break;
			//below two cases mean auto connection GPS. 
			case CLocationListener.LOCSNS_AUTO_USE_DISCONNECT:
				Toast.makeText(context, R.string.MSG_COM_01_08, 3000).show();
				break;
			case CLocationListener.LOCSNS_AUTO_USE_GPSNOTOPEN:
				hintDialog = new CustomDialog(context); 
				hintDialog.setTitle(R.string.MSG_01_00_01_01);
				hintDialog.setMessage(R.string.MSG_COM_01_07);
				hintDialog.setPositiveButton(R.string.STR_COM_001, null);
				hintDialog.setNegativeButton(R.string.STR_COM_035, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//go to opening flow
						context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				});
				hintDialog.show();
				break;
			default:
				break;
			}
			return true;
		}
		return false;
	}
	
	protected void notifyScreen(int newState) {
		NSTriggerInfo cTriggerInfo = new NSTriggerInfo();
		cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_CRADLE_SETUP_DATA_CHANGED;
		cTriggerInfo.m_lParam1 = newState;
		MenuControlIF.Instance().TriggerForScreen(cTriggerInfo);
		
	}
	private boolean isGpsOn() {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); 
		boolean isGpsOn = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
		return isGpsOn;
	}
	
	public static void addCradleStateListener(Context context) {
		GlobalTrigger.getInstance().addTriggerListener(CradleStateListener.createInstance(context));
	}
	
	public static void removeCradleStateListener() {
		if(instance != null){
			GlobalTrigger.getInstance().removeTriggerListener(instance);
		}
	}
}
