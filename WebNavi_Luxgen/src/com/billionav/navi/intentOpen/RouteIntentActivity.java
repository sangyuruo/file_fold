package com.billionav.navi.intentOpen;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.intentOpen.IntentOpenCtrl.POIData;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.uitools.RouteCalcController;

public class RouteIntentActivity extends Activity{
	private static final String TAG = "IntentCall";
	
	private TriggerListener guideFinishedTriggerListener = new TriggerListener() {
		
		@Override
		public boolean onTrigger(NSTriggerInfo triggerInfo) {
			if(triggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_GUD_GUDFINISH) {
				Log.d("test","aaaaaaa");
				if(0 == triggerInfo.m_lParam1) {
					
				}
				new Handler(getMainLooper()).postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						NaviViewManager.GetViewManager().moveTaskToBack(true);
						GlobalTrigger.getInstance().removeTriggerListener(guideFinishedTriggerListener);
					}
				}, 5000);
			}
			return false;
		}
	};
	@Override
	protected void onResume() {
		super.onResume();
		Intent i = getIntent();
		Uri data = i.getData();
		String u=data.getSchemeSpecificPart();
		final String[] poiMessage = u.split(",");
		try{
			if(null != poiMessage && poiMessage.length == 3) {
				new Handler(getMainLooper()).post(new Runnable(){
					@Override
					public void run() {
						long lat = (long) (Double.parseDouble(poiMessage[0]) *256*3600);
						long lon = (long) (Double.parseDouble(poiMessage[1]) *256*3600);
						if(null == MenuControlIF.Instance()) {
							IntentOpenCtrl.pushIntent(IntentOpenCtrl.INTENT_KIND_ROUTE
									, new POIData(poiMessage[2], new long[]{lon, lat}));
							Intent intent = new Intent(RouteIntentActivity.this, com.billionav.navi.naviscreen.NaviViewManager.class);
//							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						} else {
							RouteCalcController.instance().rapidRouteCalculateWithData(
									poiMessage[2],lon, lat);
						}
						addGuideFinishedTrigger();
					}

					private void addGuideFinishedTrigger() {
						GlobalTrigger.getInstance().addTriggerListener(guideFinishedTriggerListener);
						
					}
				});
			} else {
				new CustomDialog(this).setMessage("URI ERROR, the program can not start");
				Log.e(TAG, "uri error uri="+data);
			}
		} catch (Exception e){
			new CustomDialog(this).setMessage("URI ERROR, the program can not start");
			Log.e(TAG, "uri error uri="+data);
		} finally {
			this.finish();
		}
		
	}
}
