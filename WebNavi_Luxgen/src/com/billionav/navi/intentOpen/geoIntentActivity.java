package com.billionav.navi.intentOpen;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class geoIntentActivity extends Activity{

	private static final String TAG = "IntentCall";
	
	private static final String PREFIX_GEO_CONTACTS = "geo:0,0?q=";
	@Override
	protected void onResume() {
		super.onResume();
		Intent i = getIntent();
		Uri data = i.getData();
		String u=data.getSchemeSpecificPart();
		System.out.println("!!!!!!!!!"+u);
//		final String[] poiMessage = u.split(",");
//		try{
//			if(null != poiMessage && poiMessage.length == 3) {
//				new Handler(getMainLooper()).post(new Runnable(){
//					@Override
//					public void run() {
//						long lat = (long) (Double.parseDouble(poiMessage[0]) *256*3600);
//						long lon = (long) (Double.parseDouble(poiMessage[1]) *256*3600);
//						if(null == MenuControlIF.Instance()) {
//							IntentOpenCtrl.pushIntent(IntentOpenCtrl.INTENT_KIND_ROUTE
//									, new POIData(poiMessage[2], new long[]{lon, lat}));
//							Intent intent = new Intent(geoIntentActivity.this, com.billionav.navi.naviscreen.open.MapAbcActivity.class);
//							startActivity(intent);
//						} else {
//							RouteCalcController.instance().rapidRouteCalculateWithData(
//									poiMessage[2],lon, lat);
//						}
//					}
//				});
//			} else {
//				new CustomDialog(this).setMessage("URI ERROR, the program can not start");
//				Log.e(TAG, "uri error uri="+data);
//			}
//		} catch (Exception e){
//			new CustomDialog(this).setMessage("URI ERROR, the program can not start");
//			Log.e(TAG, "uri error uri="+data);
//		} finally {
//			this.finish();
//		}
		this.finish();
		
	}
}
