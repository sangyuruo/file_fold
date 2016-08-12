package com.billionav.navi.naviscreen.map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.DRIR.Utils.BatteryWatcher;
import com.billionav.DRIR.jni.jniDRIR_MainControl;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.camera.CameraView;
import com.billionav.navi.component.basiccomponent.ArTimerText;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.mapcomponent.ARGLSurfaceView;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSAnimationID;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.naviscreen.arrun.ADT_AR_List;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;

public class ADT_DR_Main extends ActivityBase{

	private ArTimerText timerText;
    private ARGLSurfaceView surfaceView;
    private ImageButton dr_folder;
    private ImageButton dr_vedio;
    private TextView vedio_text;
    private GestureDetector gestureDetector;
    final private int Notification_ID_BASE = 110; 
    private NotificationManager nm;  
    private PendingIntent pd = null;  
    private boolean isCancel = false;
    private final static int tmpmessageType = 1;
    private Notification baseNF;  
    private int count = 0;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1) {
				count = 0;
			}
		};
	};
	 private Handler temHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(BatteryWatcher.CreateInstance(ADT_DR_Main.this).IsDeviceTempHigh()){
//					jniVoicePlayIF.Instance().PlayGuide(0x1CF0110A);
					CustomToast.showToast(ADT_DR_Main.this, R.string.MSG_COM_00_00_02, 1000);
					removeSurfaceView();
//					DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_DM_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
					BackWinChange();
				}
			}
			
		};
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setNoTitle();
		setContentView(R.layout.adt_dr_main);
		nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		try {
			pd = PendingIntent.getActivity(this, 0, null, 0);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("PEND", "catch Exception");
			pd = null;
		}
		
		 
		baseNF = new Notification();
		baseNF.icon = R.drawable.navicloud_and_582a;
		baseNF.flags = Notification.FLAG_NO_CLEAR;
		if(pd!=null){
			Log.d("PEND", "pd="+pd);
			baseNF.setLatestEventInfo(this, "", "", pd); 
		}else{
			baseNF.setLatestEventInfo(this, "", "", null); 
			Log.d("PEND", "pd="+pd);
		}
		    
		findviewes();
		dr_vedio.setImageResource(R.drawable.navicloud_and_036a);
		setClickEvents();	
		//Whether the temperature too high?
//		if(BatteryWatcher.CreateInstance(ADT_DR_Main.this).IsDeviceTempHigh()){
////			jniVoicePlayIF.Instance().PlayGuide(0x1CF0110A);
//			CustomToast.showToast(ADT_DR_Main.this, R.string.MSG_COM_00_00_01, 1000);
//			temHandler.sendMessageDelayed(temHandler.obtainMessage(tmpmessageType), 10000);
//		}
		
//		avoidFlash();
//		dr_folder.setVisibility(View.INVISIBLE);
//		dr_vedio.setVisibility(View.INVISIBLE);
//		surfaceView.setVisibility(View.INVISIBLE);
//		new Handler().postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				dr_folder.setVisibility(View.VISIBLE);
//				dr_vedio.setVisibility(View.VISIBLE);
//			}
//		},500);
		avoidFlash();
	}
	
	private void avoidFlash() {
		dr_folder.setVisibility(View.GONE);
		dr_vedio.setVisibility(View.GONE);
		timerText.setVisibility(View.GONE);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				dr_folder.setVisibility(View.VISIBLE);
				dr_vedio.setVisibility(View.VISIBLE);
				timerText.setVisibility(View.INVISIBLE);
			}
		}, 2000);
	}

	
	@Override
	public boolean OnTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		gestureDetector.onTouchEvent(event);
    	return true;
	}

	private void sendMessageForCancel() {
		handler.removeMessages(1);
		handler.sendEmptyMessageDelayed(1, 500);
	}
	private void setClickEvents() {
		dr_folder.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(jniDRIR_MainControl.IsDMSave()){
					showDialog() ;
				}else{
					removeSurfaceView();
					MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
//			    	DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_LOW_DM_MODE, DRIRAppMain.DRIRAPP_EXTERNAL_OTHER_MODE);
					MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_AR_List.class);
				}
				
			}
		});
		
		dr_vedio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jniDRIR_MainControl.LongPush();
			}
		});
		
		gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
			if(isContains((int)e.getX(),(int)e.getY())){
				count++;
				if(count==4){
					count = 0;
					if(vedio_text.getVisibility()!=View.VISIBLE){
						DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_LOW_DM_MODE, DRIRAppMain.DRIRAPP_DM_MODE);
						vedio_text.setVisibility(View.VISIBLE);
						vedio_text.setText(R.string.STR_MM_05_01_01_02);
					}else{
						DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_DM_MODE, DRIRAppMain.DRIRAPP_LOW_DM_MODE);
						vedio_text.setVisibility(View.GONE);
						vedio_text.setText("");
					}
				}
			}else{
				sendMessageForCancel();
			}
				return super.onSingleTapConfirmed(e);
			}

		});

	}
	
    @Override
    public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
    	
  
    	timerText.notifyTrigger(cTriggerInfo);

		switch (cTriggerInfo.m_iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_DRIR_TEMPHIGH:
//			jniVoicePlayIF.Instance().PlayGuide(0x1CF0110A);
			CustomToast.showToast(this, R.string.MSG_COM_00_00_01, 1000);
			temHandler.sendMessageDelayed(temHandler.obtainMessage(tmpmessageType), 10000);
			break;
		case NSTriggerID.UIC_MN_TRG_DRIR_TEMPNORMAL:
			break;
		case NSTriggerID.UIC_MN_TRG_DRIR_IRDRAW:
			surfaceView.RequestDrawing();
			break;
		case NSTriggerID.UIC_MN_TRG_DRIR_DMSTART:
			nm.notify(Notification_ID_BASE, baseNF);
			isCancel = true;
			dr_vedio.setImageResource(R.drawable.navicloud_and_035a);
			break;
		case NSTriggerID.UIC_MN_TRG_DRIR_DMEND:
			nm.cancel(Notification_ID_BASE);
			isCancel = false;
			dr_vedio.setImageResource(R.drawable.navicloud_and_036a);
			break;
		}
    	return super.OnTrigger(cTriggerInfo);
    }
    
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			removeSurfaceView();
			DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_DM_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
			showExitHintDialog();
			return true;
		}
		
		return super.OnKeyDown(keyCode, event);
	}
	
	private void showExitHintDialog() {
		CustomDialog customDialog = new CustomDialog(this);
		customDialog.setTitle(R.string.MSG_05_01_01_01_01);
		customDialog.setMessage(R.string.MSG_05_01_01_02_02);
		customDialog.setPositiveButton(R.string.STR_COM_031, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				BackWinChange();
			}
		});
		customDialog.setNegativeButton(R.string.STR_COM_001, null);
		customDialog.show();
	}
	
	private boolean isFirst = true;
    @Override
    protected void OnResume() {
    	// TODO Auto-generated method stub
    	super.OnResume();
    	if(jniDRIR_MainControl.IsDMSave()){
			dr_vedio.setImageResource(R.drawable.navicloud_and_035a);
		}else{
			dr_vedio.setImageResource(R.drawable.navicloud_and_036a);
		}
    	if(isFirst){
    		isFirst = false;
    	}else{
    		CameraView.Instance().Maximize();
    	}
    	CameraView.Instance().Maximize();
    	UIMapControlJNI.SetScreenID(SCRMapID.SCR_ID_ArCamera);
		MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ForwardARscreenControl.getinstance().DRIRFunChange();
			}
		},1000);
    }
    
 
	@Override
	protected void onStart() {
		
		super.onStart();
	}
	
	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
		MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		surfaceView.RequestDrawing();
	}
	
	@Override
	protected void OnStop() {
		super.OnStop();
		if(isCancel){
			nm.cancel(Notification_ID_BASE);
		}
		CameraView.Instance().Minimize();
	}
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
		temHandler.removeMessages(tmpmessageType);
		temHandler = null;
	}
	
	private void findviewes(){

		
		timerText = (ArTimerText)findViewById(R.id.txtview);
		surfaceView = (ARGLSurfaceView) findViewById(R.id.surfaceview);
		dr_folder = (ImageButton) findViewById(R.id.dr_folder);
		dr_vedio = (ImageButton) findViewById(R.id.dr_vedio);
		vedio_text = (TextView) findViewById(R.id.video_text);
		vedio_text.setVisibility(View.GONE);
		vedio_text.setText("");
	}
	
	private boolean isContains(int x, int y){
		Rect r = new Rect();
		r.top = ScreenMeasure.getHeight()-ScreenMeasure.getHightOfStatusbar()-100;
		r.bottom = ScreenMeasure.getHeight() - ScreenMeasure.getHightOfStatusbar();
		r.left = ScreenMeasure.getWidth() - 100;
		r.right = ScreenMeasure.getWidth();
		return r.contains(x, y);
	}
	private void removeSurfaceView() {
		if(surfaceView!=null&&surfaceView.getParent()!=null){
			((ViewGroup)surfaceView.getParent()).removeView(surfaceView);
		}
	}
	private void showDialog() {
		CustomDialog b = new CustomDialog(this);
		b.setMessage(R.string.MSG_05_01_01_01_02);

		b.setTitle(R.string.MSG_05_01_01_01_01);
		b.setNegativeButton(R.string.STR_COM_001, null);

		b.setPositiveButton(R.string.STR_COM_031,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						removeSurfaceView();
						MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
				    	DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_DM_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
						MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_AR_List.class);
					}

				});
		b.show();
	}
}
