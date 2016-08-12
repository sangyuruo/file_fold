package com.billionav.navi.sync;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.ui.R;

public class SyncUI {
	
	private static SyncUI instanceObj = new SyncUI();
	
	public static SyncUI sharedInstance (){
		return instanceObj;
	}

	public void propertiesUI() {
		
		final NaviViewManager context = NaviViewManager.GetViewManager();
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.properties,
				(ViewGroup) context.findViewById(R.id.properties_Root));

		final CheckBox mediaCheckBox = (CheckBox) view
				.findViewById(R.id.properties_checkMedia);
		final EditText appNameEditText = (EditText) view
				.findViewById(R.id.properties_appName);
		final RadioGroup transportGroup = (RadioGroup) view
				.findViewById(R.id.properties_radioGroupTransport);
		final EditText ipAddressEditText = (EditText) view
				.findViewById(R.id.properties_ipAddr);
		final EditText tcpPortEditText = (EditText) view
				.findViewById(R.id.properties_tcpPort);
		final CheckBox autoReconnectCheckBox = (CheckBox) view
				.findViewById(R.id.properties_checkAutoReconnect);

		ipAddressEditText.setEnabled(false);
		tcpPortEditText.setEnabled(false);
		autoReconnectCheckBox.setEnabled(false);

		//TODO 1216 屏蔽其它选项 start
//		transportGroup
//				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//					@Override
//					public void onCheckedChanged(RadioGroup group, int checkedId) {
//						boolean transportOptionsEnabled = checkedId == R.id.properties_radioWiFi;
//						ipAddressEditText.setEnabled(transportOptionsEnabled);
//						tcpPortEditText.setEnabled(transportOptionsEnabled);
//						autoReconnectCheckBox
//								.setEnabled(transportOptionsEnabled);
//					}
//				});
		//TODO 1216 屏蔽其它选项 end
		
		// display current configs
		final SharedPreferences prefs = context.getSharedPreferences(Const.PREFS_NAME,
				0);
		boolean isMedia = prefs.getBoolean(Const.PREFS_KEY_ISMEDIAAPP,
				Const.PREFS_DEFAULT_ISMEDIAAPP);
		String appName = prefs.getString(Const.PREFS_KEY_APPNAME,
				context.getString(R.string.application_name));
		int transportType = prefs.getInt(
				Const.Transport.PREFS_KEY_TRANSPORT_TYPE,
				Const.Transport.PREFS_DEFAULT_TRANSPORT_TYPE);
		String ipAddress = prefs.getString(
				Const.Transport.PREFS_KEY_TRANSPORT_IP,
				Const.Transport.PREFS_DEFAULT_TRANSPORT_IP);
		int tcpPort = prefs.getInt(Const.Transport.PREFS_KEY_TRANSPORT_PORT,
				Const.Transport.PREFS_DEFAULT_TRANSPORT_PORT);
		boolean autoReconnect = prefs.getBoolean(
				Const.Transport.PREFS_KEY_TRANSPORT_RECONNECT,
				Const.Transport.PREFS_DEFAULT_TRANSPORT_RECONNECT_DEFAULT);

		mediaCheckBox.setChecked(isMedia);
		appNameEditText.setText(appName);
		//TODO 1216 屏蔽其它选项 start
//		transportGroup
//				.check(transportType == Const.Transport.KEY_TCP ? R.id.properties_radioWiFi
//						: R.id.properties_radioBT);
		//TODO 1216 屏蔽其它选项 end
		
		ipAddressEditText.setText(ipAddress);
		tcpPortEditText.setText(String.valueOf(tcpPort));
		autoReconnectCheckBox.setChecked(autoReconnect);

		new AlertDialog.Builder(context)
				.setTitle("Please select properties")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						String appName = appNameEditText.getText().toString();
						boolean isMedia = mediaCheckBox.isChecked();
						//int transportType = transportGroup
						//		.getCheckedRadioButtonId() == R.id.properties_radioWiFi ? Const.Transport.KEY_TCP
						//		: Const.Transport.KEY_BLUETOOTH;
						int transportType = Const.Transport.PREFS_DEFAULT_TRANSPORT_TYPE;
						//TODO 1216 屏蔽其它选项 start
//						if(transportGroup.getCheckedRadioButtonId() == R.id.properties_radioWiFi){
//                                                    transportType = Const.Transport.KEY_TCP;
//                                                }
//                                                else if(transportGroup.getCheckedRadioButtonId() == R.id.properties_radioBT){
//                                                    transportType = Const.Transport.KEY_BLUETOOTH;
//                                                }
//                                                else if(transportGroup.getCheckedRadioButtonId() == R.id.properties_radioUSB){
//                                                    transportType = Const.Transport.KEY_USB;
//                                                }
//                                                else
//                                                    ;
						//TODO 1216 屏蔽其它选项 end
						transportType = Const.Transport.KEY_USB;
						Log.v("AbelDebugUSB","transportType "+transportType);
						String ipAddress = ipAddressEditText.getText()
								.toString();
						int tcpPort = Integer.parseInt(tcpPortEditText
								.getText().toString());
						boolean autoReconnect = autoReconnectCheckBox
								.isChecked();

						// save the configs
						boolean success = prefs
								.edit()
								.putBoolean(Const.PREFS_KEY_ISMEDIAAPP, isMedia)
								.putString(Const.PREFS_KEY_APPNAME, appName)
								.putInt(Const.Transport.PREFS_KEY_TRANSPORT_TYPE,
										transportType)
								.putString(
										Const.Transport.PREFS_KEY_TRANSPORT_IP,
										ipAddress)
								.putInt(Const.Transport.PREFS_KEY_TRANSPORT_PORT,
										tcpPort)
								.putBoolean(
										Const.Transport.PREFS_KEY_TRANSPORT_RECONNECT,
										autoReconnect).commit();
						if (!success) {
						}
						
						if (AppLinkService.getInstance() == null) {
							Intent startIntent = new Intent(context, AppLinkService.class);
							context.startService(startIntent);
							Log.d("MaChen TAG", "startService");
						} else {
							AppLinkService.getInstance().startProxy();
						}
					}
				}).setView(view).show();
	}
}
