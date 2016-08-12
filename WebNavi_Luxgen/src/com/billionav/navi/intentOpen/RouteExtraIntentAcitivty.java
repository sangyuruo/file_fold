package com.billionav.navi.intentOpen;

import android.app.Activity;
import android.content.Intent;
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

public class RouteExtraIntentAcitivty extends Activity{
	private static final String TAG = "IntentCall";
	private static final String SPILT_TAG = "$_$";
	
	private TriggerListener guideFinishedTriggerListener = new TriggerListener() {
		
		@Override
		public boolean onTrigger(NSTriggerInfo triggerInfo) {
			if(triggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_GUD_GUDFINISH) {
				Log.d("test","aaaaaaa");
				if(0 == triggerInfo.m_lParam1) {
					new Handler(getMainLooper()).postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							NaviViewManager.GetViewManager().moveTaskToBack(true);
							GlobalTrigger.getInstance().removeTriggerListener(guideFinishedTriggerListener);
						}
					}, 5000);
				}
			}
			return false;
		}
	};
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
		Intent i = getIntent();
		String u = i.getStringExtra("NAVI_CMD");
		if(u.startsWith(SPILT_TAG)) {
			u = u.substring(u.indexOf(SPILT_TAG)+SPILT_TAG.length(), u.length());
			u = u.replace(SPILT_TAG, ",");
		}
		final String[] poiMessage = u.split(",");
		try{
			if(null != poiMessage && poiMessage.length == 3) {
				new Handler(getMainLooper()).post(new Runnable(){
					@Override
					public void run() {
						double orglat = Double.parseDouble(poiMessage[2]);
						double orglon = Double.parseDouble(poiMessage[1]);
						long lat = (long) ((Double.parseDouble(poiMessage[2]))/(double)100000 *256*3600);
						long lon = (long) ((Double.parseDouble(poiMessage[1]))/(double)100000 *256*3600);
//						Log.d(TAG, "1111111");
						if(null == MenuControlIF.Instance()) {
							IntentOpenCtrl.pushIntent(IntentOpenCtrl.INTENT_KIND_ROUTE
									, new POIData(poiMessage[0], new long[]{lon, lat}));
							Intent intent = new Intent(RouteExtraIntentAcitivty.this, com.billionav.navi.naviscreen.NaviViewManager.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						} else {
							RouteCalcController.instance().rapidRouteCalculateWithData(
									poiMessage[0],lon, lat);
						}
						addGuideFinishedTrigger();
					}

					private void addGuideFinishedTrigger() {
						GlobalTrigger.getInstance().addTriggerListener(guideFinishedTriggerListener);
						
					}
				});
			} else {
				new CustomDialog(this).setMessage("URI ERROR, the program can not start");
				Log.e(TAG, "POI Data ERROR1="+u);
			}
		} catch (Exception e){
			new CustomDialog(this).setMessage("URI ERROR, the program can not start");
			e.printStackTrace();	
			Log.e(TAG, "POI Data ERROR2="+u);
		} finally {
			this.finish();
		}
		
	}
}
