package com.billionav.navi.naviscreen.setting;

import java.util.ArrayList;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.uicommon.UIC_CradleCommon;
import com.billionav.ui.R;

public class ADT_Setting_Bluetooth extends ActivityBase{

	private ListViewNavi listView;
	private CProgressDialog progressdialog;
//	private BTDevice_Info connectDevice;
	private ArrayList<Object> arrayList = new ArrayList<Object>();
    private CustomAdapter myadapter = null;
	
    private BroadcastReceiver broadcastReceiver;
	
	private static final int DIALOG_TYPE_CONECTION_FAIL = 1011;
	private static final int DIALOG_TYPE_CONECTION_SUCESS = 1012;
	
	private Handler endConnection = new Handler(){
		
		public void handleMessage(Message msg) {
			if(msg.what==1){
				setGPSInternal();
				
				showCustomDialog(DIALOG_TYPE_CONECTION_FAIL );
				cancelProgressDialog();
			}
		}

	};

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		
		setContentView(R.layout.adt_bluetooth_setting);
		setDefaultBackground();
		
		setTitle("Cradle settings");
		
		findViews();
		addActionItem3("Open Bluetooth Setting", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openBluetoothSetting();
			}
		});
		setListener();
		
		
		registerReceiver();
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			BackWinChange();
			return true;
		}
		return super.OnKeyDown(keyCode, event);
	}

	private void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(BluetoothAdapter.getDefaultAdapter()==null || !BluetoothAdapter.getDefaultAdapter().isEnabled()) {
					CustomToast.showToast(ADT_Setting_Bluetooth.this, "bluetooth not enable", 3000);
					return;
				}
				
//				CLocationListener.Instance().SwitchToCradleGPS(connectDevice = (BTDevice_Info)arrayList.get(position));
				getProgressDialog().show();
			}
		});
		
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		boolean bret = super.OnTrigger(triggerInfo);
		
		switch (triggerInfo.GetTriggerID()) {
		case NSTriggerID.UIC_MN_TRG_LOC_GPS_CONNECT_STATUS:
			
//			if(triggerInfo.GetlParam1() == CLocationListener.BT_CONNECT_ACTION_EXCEPTION){
				endConnection.removeMessages(1);
				cancelProgressDialog();
				
				showCustomDialog(DIALOG_TYPE_CONECTION_FAIL);
				setGPSInternal();
//			}
			break;
		case NSTriggerID.UIC_MN_TRG_ECL_CERTIFICATE_FINISHED:
			
			endConnection.removeMessages(1);
			cancelProgressDialog();
			
			if(triggerInfo.GetlParam1()==1){
				showCustomDialog(DIALOG_TYPE_CONECTION_SUCESS);
//				UIC_CradleCommon.addDevice(connectDevice);
			} else {
				showCustomDialog(DIALOG_TYPE_CONECTION_FAIL);
				setGPSInternal();
			}
			break;
		default:
			break;
		}
		return bret;
	}

	private void showCustomDialog(int dialogType) {
		if(dialogType == DIALOG_TYPE_CONECTION_FAIL) {
			CustomToast.showToast(this, "connect fail", 2000);
		} else if(dialogType == DIALOG_TYPE_CONECTION_SUCESS) {
			CustomToast.showToast(this, "connect sucess", 2000);
		}
		
	}

	private void findViews() {
		listView = (ListViewNavi) findViewById(R.id.list_blue_tooth);
		myadapter = new CustomAdapter();
		listView.setCacheColorHint(0);
		listView.setAdapter(myadapter);
		
	}

	@Override
	protected void OnResume() {
		super.OnResume();
		updateAdapter();
	}
	
	private void openBluetoothSetting() {
		Intent buletoothIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
	    startActivity(buletoothIntent);
	}
	
	private void updateAdapter(){
		
		arrayList.clear();
		arrayList.add("Pairings");
//		ArrayList<BTDevice_Info> devices01 = UIC_CradleCommon.getBondedDevices();
//		if(devices01 != null&&devices01.size()>0){
//			arrayList.addAll(devices01);
//		}

		arrayList.add("Recently connected");
		if(UIC_CradleCommon.getNumOfBackupList()>0){
			Log.d("BULE", "UIC_CradleCommon.getNumOfBackupList()>0");
//			arrayList.addAll(UIC_CradleCommon.getBackupDevices());
		}
		Log.d("BULE", "UIC_CradleCommon.getNumOfBackupList()="+UIC_CradleCommon.getNumOfBackupList());
		myadapter.notifyDataSetChanged();
	}
	
	
	private CProgressDialog getProgressDialog() {
		if(progressdialog == null) {
			progressdialog = CProgressDialog.makeProgressDialog(this, "connecting...");
			progressdialog.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK){
						endConnection.removeMessages(1);
						setGPSInternal();
					}
					return false;
				}
			});
		}
		
		return progressdialog;
	}
	
	private void cancelProgressDialog() {
		getProgressDialog().dismiss();
		
	}

	private void setGPSInternal(){
//		CLocationListener.Instance().SwitchToInterGPS(true);
//		new jniSetupControl().SetInitialStatus(
//			      jniSetupControl.STUPDM_LAST_GPS_SETTING, jniSetupControl.STUPDM_COMMON_ON);
	}

	
	public  class CustomAdapter extends BaseAdapter{

		
		@Override
		public boolean isEnabled(int position) {
			return getItemViewType(position)  > 0;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			if(arrayList.get(position) instanceof String){
				return -1;
			}
			return 1;
		}
		
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if(convertView == null){
				viewHolder = new ViewHolder();
				if(getItemViewType(position) < 0){ 
					convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item_bluetooth_setting_tag, null);
					viewHolder.text01 = (TextView) convertView.findViewById(R.id.text_blue_tooth_01);
	             }else{ 
	            	 convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item_bluetooth_setting, null); 
	            	 viewHolder.text01 = (TextView) convertView.findViewById(R.id.name);
	            	 viewHolder.text02 = (TextView) convertView.findViewById(R.id.address);

	             } 
				convertView.setTag(viewHolder);
			}else{
				
				viewHolder = (ViewHolder) convertView.getTag();
	
			}
			
			
			if(getItemViewType(position) < 0){
				viewHolder.text01.setText((CharSequence)getItem(position));
			}else{
//				viewHolder.text01.setText(((BTDevice_Info)getItem(position)).Name);
//				viewHolder.text02.setText(((BTDevice_Info)getItem(position)).Address);
			}
			return convertView;
		}
		
	}
	
	
	private void registerReceiver(){
		if(broadcastReceiver== null){
			broadcastReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					if(intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED) 
							){
						updateAdapter();
					}
				}
			};
			
			IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			registerReceiver(broadcastReceiver, filter);
			
		}
	}
	
	
	private void cancelReceiver(){
		if(broadcastReceiver != null){
			unregisterReceiver(broadcastReceiver);
			broadcastReceiver = null;
		}
		
	}
	
	public class ViewHolder{
		TextView text01;
		TextView text02;
	}
	
	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		cancelReceiver();
		super.OnDestroy();
	}
}
