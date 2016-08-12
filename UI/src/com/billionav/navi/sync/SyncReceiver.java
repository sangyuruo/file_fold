package com.billionav.navi.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.billionav.navi.naviscreen.NaviViewManager;

public class SyncReceiver extends BroadcastReceiver {
	static final String TAG = "MainActivity";

	public void onReceive(Context context, Intent intent) {
		AppLinkService serviceInstance = AppLinkService.getInstance();

		String action = intent.getAction();

		if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
			int plugType = intent.getIntExtra("plugged", 0);
			Toast.makeText(context, "action :" + action + " " + plugType,
					Toast.LENGTH_LONG).show();

			// usb connected

			if (plugType == 2) {

				if (NaviViewManager.GetViewManager() == null) {
					Log.v("AbelDebugUSB", "if2 NaviViewManager == null");
					Intent startIntent = new Intent(context,
							NaviViewManager.class);
					startIntent.putExtras(intent);
					// context.startService(startIntent);
					context.startService(startIntent);
				}
				if (serviceInstance == null) {
					Log.v("AbelDebugUSB", "if1 serviceInstance == null");
					Intent startIntent = new Intent(context,
							AppLinkService.class);
					// startIntent.putExtras(intent);
					startIntent.putExtra("type", "USBconnected");
					context.startService(startIntent);
				}
				if (serviceInstance == null) {
					Log.v("AbelDebugUSB", "if3 serviceInstance == null");
					serviceInstance = AppLinkService.getInstance();

				} else {
					Log.v("AbelDebugUSB", "else3 serviceInstance != null");
					serviceInstance.startProxy();
				}

			} else {
				if (serviceInstance != null) {
					serviceInstance.setUseUsbConnected(false);
				}
			}

		}

		/*
		 * if
		 * (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(intent.getAction())
		 * || UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction()))
		 * { UsbAccessory accessory =
		 * (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
		 * if (accessory != null) { if (serviceInstance != null){ Log.i(TAG,
		 * "Bt off stop service"); Intent stopIntent = new Intent(context,
		 * AppLinkService.class); stopIntent.putExtras(intent);
		 * context.stopService(stopIntent); } } }
		 */
	}
}
