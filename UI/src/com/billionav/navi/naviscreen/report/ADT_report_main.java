package com.billionav.navi.naviscreen.report;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIUserReportJNI;
import com.billionav.jni.UIVoiceControlJNI;
import com.billionav.navi.camera.CameraView;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.map.ADT_AR_Main;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.map.ForwardARscreenControl;
import com.billionav.navi.naviscreen.misc.TelephonyListener;
import com.billionav.navi.uicommon.UIC_ARVideoCommon;
import com.billionav.navi.uicommon.UIC_WeiboCommon;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UploadData;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.ui.R;
//import com.billionav.voicerecog.VoiceRecognizer;
//import com.billionav.voicerecog.VoiceRecognizer.RecognitionListener;
import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.WeiboException;


public class ADT_report_main extends ActivityBase implements RequestListener,TelephonyListener{


	private int report_category = -1;
	private int report_type = -1 ;
	private String pic_path = "";

    private CProgressDialog progressDialog = null;
    
    private RelativeLayout report_initialize = null;
    private RelativeLayout report_main = null;
    private GridView gridView = null;
    private ImageView icon = null;
    private ImageView picture = null;
    private EditText extraInfo = null;
    private ImageView voice = null;
    private RelativeLayout release = null;
    private ImageButton takePic = null;
    private MyAdapter adapter = null; 
    
    private RelativeLayout image_layout = null;
    private ImageButton  image_delete = null;
//    private RecognitionListener listener = null;
    private Animation take_pic_animation = null;
    private boolean isNeed = true;

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setNoTitle();
		setContentView(R.layout.adt_report_main_new);
		findViewsNew();
		adapter = new MyAdapter(this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				setAboutParameters(arg2);
				setDefaultExtraInfo(report_category,report_type);
				report_initialize.setVisibility(View.GONE);
				report_main.setVisibility(View.VISIBLE);
			}

		});
//		listener = new RecognitionListener() {
//			
//			@Override
//			public void onVrResults(final List<String> results) {
//				// TODO Auto-generated method stub
//				runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						voice.setEnabled(true);
//						if (results.size() > 0) {
//							final String temp = results.get(0);
//							if (extraInfo != null) {
//								extraInfo.setText(temp);
//							}
//						}
//					}
//				});
//
//			}
//		};
		setClickEvent();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ForwardARscreenControl.getinstance().DRIRFunChange();
			}
		},1000);
		isFirst = true;
		NaviViewManager.GetViewManager().setTelephonyListener(this);
	}

	private void setAboutParameters(int value) {
		// TODO Auto-generated method stub
		switch(value){
		case 0:
			report_category = 2;
			break;
		case 1:
			report_category = 3;
			break;
		case 2:
			report_category = 1;
			break;
		case 3:
			report_category = 5;
			break;
		case 4:
			report_category = 4;
			report_type = 3;
			break;
		case 5:
			report_category = 4;
			report_type = 4;
			break;
		case 6: 
			report_category = 4;
			report_type = 1;
			break;
		case 7:
			report_category = 4;
			report_type = 2;
			default:
			break;
		}
		
	}
	private void findViewsNew() {
		report_initialize = (RelativeLayout) findViewById(R.id.report_new_01);
		report_main = (RelativeLayout) findViewById(R.id.report_new_02);
		gridView = (GridView) findViewById(R.id.gridView01);
		icon = (ImageView) findViewById(R.id.report_main_image_icon);
		picture = (ImageView) findViewById(R.id.report_main_image_pic);
		extraInfo = (EditText) findViewById(R.id.report_main_input);
		voice = (ImageView) findViewById(R.id.report_main_voice);
		release = (RelativeLayout) findViewById(R.id.report_main_release);
		takePic = (ImageButton)findViewById(R.id.report_main_takePic);
		
		image_layout = (RelativeLayout) findViewById(R.id.report_layout);
		image_delete = (ImageButton) findViewById(R.id.delete_image);
		
		image_layout.setVisibility(View.GONE);
		extraInfo.setSingleLine(true);
		extraInfo.setEllipsize(TruncateAt.END);
		report_initialize.setVisibility(View.VISIBLE);
		report_main.setVisibility(View.GONE);
		picture.setVisibility(View.INVISIBLE);
		take_pic_animation = AnimationUtils.loadAnimation(this, R.anim.take_picture);
	}

	private boolean isFirst = false;
	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
		if(!isFirst){
			CameraView.Instance().Maximize();
		} else {
			isFirst = false;
		}
		MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
		MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

	@Override
	protected void OnStop() {
		// TODO Auto-generated method stub
		super.OnStop();
		if (isNeed) {
			CameraView.Instance().Minimize();
		}
		isNeed = true;
	}

	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UIVoiceControlJNI.getInstance().stopVoiceRecog();
			if(!voice.isEnabled()) {
				voice.setEnabled(true);
			}
			if (image_layout.getVisibility() == View.VISIBLE) {
				image_layout.setVisibility(View.GONE);
				return true;
			}
			if (report_main.getVisibility() != View.GONE
					&& report_initialize.getVisibility() != View.VISIBLE) {
				report_initialize.setVisibility(View.VISIBLE);
				report_main.setVisibility(View.GONE);
				return true;
			}
			report_initialize.setVisibility(View.GONE);
			report_main.setVisibility(View.GONE);
			if (getBundleNavi().getPreviousActivityClass().equals(
					ADT_AR_Main.class)) {
				isNeed = false;
				ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_CAMEEA_MODE,
						DRIRAppMain.DRIRAPP_AR_MODE);
				ForwardKeepDepthWinChange(ADT_AR_Main.class);
				return true;
			} else {
				ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_CAMEEA_MODE,
						DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
			}
			setAnimationAndBackWinchange();

		}
		return super.OnKeyDown(keyCode, event);
	}

	
	private void setClickEvent() {
		
		voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isNetEnable()){
					return;
				}
				voice.setEnabled(false);
				UIVoiceControlJNI.getInstance().requestVoiceInput((int)System.currentTimeMillis(), getString(R.string.STR_MM_08_01_01_22));
			}
		});
		picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(pic_path)) {
					Bitmap bit = BitmapFactory.decodeFile(pic_path);
					Drawable drawable = new BitmapDrawable(bit);
					image_layout.setBackgroundDrawable(drawable);
					image_layout.setVisibility(View.VISIBLE);
				}
				
			}
		});
		image_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image_layout.setVisibility(View.GONE);
			}
		});
		image_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image_layout.setVisibility(View.GONE);
				picture.setImageBitmap(null);
				picture.setVisibility(View.GONE);
				UIC_ARVideoCommon.deleteFiles(pic_path);
				pic_path = "";
			}
		});
		takePic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DRIRAppMain.TakeOneShootPic();
			}
		});
		
		release.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserControl_UploadData data = new UserControl_UploadData();
				final int[] lonLat = UIMapControlJNI.GetCenterLonLat();
				
				
				
				data.setM_iCategory(report_category);
				data.setM_strLon(String.valueOf(lonLat[0]));
				data.setM_strLat(String.valueOf(lonLat[1]));
				if (report_category == 4) {
					data.setM_iType(report_type);
				}
				if (!TextUtils.isEmpty(extraInfo.getText().toString())) {
					ArrayList<String> array = new ArrayList<String>();

					try {
						String str = new String(extraInfo.getText().toString()
								.getBytes(), "UTF-8");
						array.add(str);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					data.setM_listText(array);
				}
				if (!TextUtils.isEmpty(pic_path)) {
					ArrayList<String> pic_Path = new ArrayList<String>();
					pic_Path.add(pic_path);
					data.setM_listPicturesPath(pic_Path);
				}
				if (!isNetEnable()) {
					return;
				}
				long isSend = UserControl_ManagerIF.Instance().SNSUploadData(
						data);
				
//				try {
//					UIUserReportJNI.sendReport(
//							System.currentTimeMillis(), 
//							lonLat[0], lonLat[1], report_category, report_type, pic_path, null, new String(extraInfo.getText().toString()
//									.getBytes(), "UTF-8"));
//				} catch (UnsupportedEncodingException e) {
//					Log.d("test","report error");
//					e.printStackTrace();
//				}
//				long isSend = 1;
				if (isSend != -1) {
					lockForClickListener();
					progressDialog = CProgressDialog.makeProgressDialog(
							ADT_report_main.this, R.string.STR_MM_08_01_01_08);
					progressDialog.show();
				}
				}

		});
	}
	
	private void showReleaseWeiboDialog(final UserControl_UserInfo userInfo){
		CustomDialog dialog = new CustomDialog(ADT_report_main.this);
		dialog.setTitle(R.string.MSG_08_04_01_01);
		dialog.setMessage(R.string.MSG_08_04_01_02);
		dialog.setNegativeButton(R.string.MSG_08_04_01_03, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
						if (report_main.getVisibility() == View.VISIBLE) {
							report_main.setVisibility(View.GONE);
						}
						if (getBundleNavi().getPreviousActivityClass().equals(
								ADT_AR_Main.class)) {
							isNeed = false;
							ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_CAMEEA_MODE,
									DRIRAppMain.DRIRAPP_AR_MODE);
							ForwardKeepDepthWinChange(ADT_AR_Main.class);

						} else {
							ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_CAMEEA_MODE,
									DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
							setAnimationAndBackWinchange();

						}
						return;
			}
		});
		dialog.setPositiveButton(R.string.MSG_08_04_01_04, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						UIC_WeiboCommon.publishWeibo(userInfo, pic_path, getString(R.string.STR_COM_016)+String.valueOf(System.currentTimeMillis()), ADT_report_main.this);
					}
				}, 10);
				if(report_main.getVisibility()==View.VISIBLE){
					report_main.setVisibility(View.GONE);
				}
				if(getBundleNavi().getPreviousActivityClass().equals(ADT_AR_Main.class)){
					isNeed = false;
					ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_CAMEEA_MODE, DRIRAppMain.DRIRAPP_AR_MODE);
					ForwardKeepDepthWinChange(ADT_AR_Main.class);
					
				}else{
					ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_CAMEEA_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
					setAnimationAndBackWinchange();
				}
				return;

			}
		});
		dialog.show();
		
	}

	 

	
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		
		int iTriggerID = triggerInfo.GetTriggerID();
		int iParams = (int) triggerInfo.GetlParam1();
		switch(iTriggerID)
		{
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_VOICE_INPUT_RESULT:
			extraInfo.setText(triggerInfo.m_String1);
			voice.setEnabled(true);
			return true;
		case NSTriggerID.UIC_MN_TRG_UC_PM_UPLOADDATA:
			progressDialog.dismiss();
			if(iParams == 0){
				  int[] lonlat = UIMapControlJNI.GetCenterLonLat();
//				    SnsControl.Instance().RequestSnsDataRestartTimer(lonlat[0], lonlat[1]);
					UserControl_UserInfo userInfo = UIC_WeiboCommon.getUserInfo();
					if(userInfo != null) {
						showReleaseWeiboDialog(userInfo);
						return true;
					}

				CustomToast.showToast(ADT_report_main.this, R.string.MSG_08_04_01_05, Toast.LENGTH_SHORT);
				if(report_main.getVisibility()==View.VISIBLE){
					report_main.setVisibility(View.GONE);
				}
				if(getBundleNavi().getPreviousActivityClass().equals(ADT_AR_Main.class)){
					isNeed = false;
					ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_CAMEEA_MODE, DRIRAppMain.DRIRAPP_AR_MODE);
					ForwardKeepDepthWinChange(ADT_AR_Main.class);
					return true;
				}else{
					ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_CAMEEA_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
					setAnimationAndBackWinchange();
				}
				
			}else if (iParams == 1){
				CustomToast.showToast(ADT_report_main.this, R.string.STR_COM_013, Toast.LENGTH_SHORT);
			}else if(iParams == 2){
				CustomToast.showToast(ADT_report_main.this, R.string.STR_COM_014, Toast.LENGTH_SHORT);
			}
			return true;
		case NSTriggerID.UIC_MN_TRG_DRIR_TAKEPIC_END:
			
			pic_path = triggerInfo.GetString1();
			picture.setImageBitmap(getImageThumbnail(triggerInfo.GetString1(), 56, 35));
			picture.setVisibility(View.VISIBLE);
			take_pic_animation.reset();
			picture.startAnimation(take_pic_animation);
			return true;
		default:
			return super.OnTrigger(triggerInfo);
		}
	}


	private void setDefaultExtraInfo(int category ,int type){
		
		switch(category){
		case 1:
			extraInfo.setText(R.string.STR_MM_08_05_01_25);
			icon.setImageResource(R.drawable.navicloud_and_263a);
			break;
		case 2:
			extraInfo.setText(R.string.STR_MM_08_05_01_22);
			icon.setImageResource(R.drawable.navicloud_and_261a);
			break;
		case 3:
			extraInfo.setText(R.string.STR_MM_08_05_01_23);
			icon.setImageResource(R.drawable.navicloud_and_262a);
			break;
		case 4:
			if(type == 1){
				extraInfo.setText(R.string.STR_MM_08_05_01_43);
				icon.setImageResource(R.drawable.navicloud_and_267a);
			}else if(type == 2){
				extraInfo.setText(R.string.STR_MM_08_05_01_44);
				icon.setImageResource(R.drawable.navicloud_and_268a);
			}else if(type == 3){
				extraInfo.setText(R.string.STR_MM_08_05_01_41);
				icon.setImageResource(R.drawable.navicloud_and_265a);
			}else if(type == 4){
				extraInfo.setText(R.string.STR_MM_08_05_01_42);
				icon.setImageResource(R.drawable.navicloud_and_266a);
			}
			break;
		case 5: 
			extraInfo.setText(R.string.STR_MM_08_05_01_21);
			icon.setImageResource(R.drawable.navicloud_and_264a);
			break;
			default:
				break;
		}

	}

	 private Bitmap getImageThumbnail(String imagePath, int width, int height) {  
	        return UIC_ARVideoCommon.getImageThumbnail(imagePath, width, height);  
	    }


	@Override
	public void onComplete(String response) {
		// TODO Auto-generated method stub
		
		 runOnUiThread(new Runnable() {

	            @Override
	            public void run() {
	            	CustomToast.showToast(ADT_report_main.this, R.string.STR_COM_016, Toast.LENGTH_SHORT);
	            }
	        });

	}

	@Override
	public void onIOException(IOException e) {
		// TODO Auto-generated method stub
		CustomToast.showToast(ADT_report_main.this, R.string.STR_COM_017, Toast.LENGTH_SHORT);
	
	}

	@Override
	public void onError(final WeiboException e) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {

            @Override
            public void run() {
            	
            	CustomToast.showToast(ADT_report_main.this, R.string.STR_COM_017, Toast.LENGTH_SHORT);
            }
        });

	}
	
	

	class MyAdapter extends BaseAdapter{

		private final Integer[] images = {R.drawable.navicloud_and_261b,
				R.drawable.navicloud_and_262b,
				R.drawable.navicloud_and_263b,
				R.drawable.navicloud_and_264b,
				R.drawable.navicloud_and_265b,
				R.drawable.navicloud_and_266b,
				R.drawable.navicloud_and_267b,
				R.drawable.navicloud_and_268b,
				
		};
		private final String[] strs = {getString(R.string.STR_MM_08_01_01_04),
				getString(R.string.STR_MM_08_01_01_05),
				getString(R.string.STR_MM_08_01_01_07),
				getString(R.string.STR_MM_08_01_01_03),
				getString(R.string.STR_MM_08_01_01_09),
				getString(R.string.STR_MM_08_01_01_10),
				getString(R.string.STR_MM_08_01_01_11),
				getString(R.string.STR_MM_08_01_01_12),
				};
		private final LayoutInflater inflater;

		public MyAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ItemView item = null;
			if(convertView == null){
				item = new ItemView();
				convertView = inflater.inflate(R.layout.view_report_initialize_item,parent,false);
				item.image = (ImageView) convertView.findViewById(R.id.item_imageview01);
				item.text = (TextView) convertView.findViewById(R.id.item_textview01);
				convertView.setTag(item);
			}else{
				item = (ItemView) convertView.getTag();
			}
			item.image.setBackgroundResource(images[position]);
			item.text.setText(strs[position]);
			
			return convertView;
		}
		
	}
	
	private static class ItemView{
		ImageView image = null;
		TextView text = null;
	}
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
		UIVoiceControlJNI.getInstance().stopVoiceRecog();
		take_pic_animation.cancel();
		take_pic_animation = null;
	}
	private void setAnimationAndBackWinchange(){
		if(ADT_Main_Map_Navigation.class.equals(MenuControlIF.Instance().GetHierarchyBelowWinscapeClass())){
			MenuControlIF.Instance().setWinchangeWithoutAnimation();
		}
		BackWinChange();
	}

	@Override
	public void callStateIdle() {
		// TODO Auto-generated method stub
		Log.d("UIReport", "callback callStateIdle");
		if(!voice.isEnabled()) {
			voice.setEnabled(true);
		}
	}

	@Override
	public void callStateOffhook() {
		Log.d("UIReport", "callback callStateOffhook");
		if(voice.isEnabled()) {
			voice.setEnabled(false);
		}
	}
//
//	@Override
//	public void callStateRinging() {
//		// TODO Auto-generated method stub
//		Log.d("UIReport", "callback callStateRinging");
//	}
}
