package com.billionav.navi.naviscreen.map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.jni.UIBaseConnJNI;
import com.billionav.jni.UIPathControlJNI;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.guidebar.GuideInfoBar.IllustStatusListener;
import com.billionav.navi.component.mapcomponent.AnimationControl;
import com.billionav.navi.component.mapcomponent.BottomBar;
import com.billionav.navi.component.mapcomponent.ButtonMapZoom;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.dest.ADT_Overflow_Menu;
import com.billionav.navi.naviscreen.favorite.ADT_Favorite;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.naviscreen.report.ADT_report_main;
import com.billionav.navi.naviscreen.setting.ADT_Settings_Main;
import com.billionav.navi.naviscreen.srch.ADT_POI_Main;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;

public class Map_Menu_Bar extends RelativeLayout implements IllustStatusListener{
	private LinearLayout menuLayout;
	private ImageView report;
	private ImageView mapLayer;
	private ImageView nearbySearch;
	private ImageView more;
	private BottomBar bottomarea;
	private ButtonMapZoom zoom;
	private TextView report_textview;
	private PopupWindow popupWindow;
	
//	private ImageView voiceRecognition;
//	private ImageView ar;
	private AnimationControl animationCtl;
	
	private int status;
	private static final int delayedTime = 5000;

	public Map_Menu_Bar(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.adt_map_menu_bar, this);
	    
		findViews();
		setListener();
		
		initialize();
		
		Map_Layer.initMapLayerData();
	}

	private void initialize() {
		int width = ScreenMeasure.isPortrait() ? ScreenMeasure.getWidth() : ScreenMeasure.getHeight();
		menuLayout.getLayoutParams().width = width;
				
//		adjustLayout();
		closeDelayed();
	}

	private void findViews() {
		menuLayout = (LinearLayout) findViewById(R.id.map_layer_menu);
		report = (ImageView) findViewById(R.id.map_layer_menu_report);
		mapLayer = (ImageView) findViewById(R.id.map_layer_map_layers);
		nearbySearch = (ImageView) findViewById(R.id.map_layer_menu_nearby_search);
		more = (ImageView) findViewById(R.id.map_layer_menu_more);
		bottomarea = (BottomBar)findViewById(R.id.map_bottom_area);
		zoom = (ButtonMapZoom) findViewById(R.id.button_map_zoom);
		report_textview = (TextView) findViewById(R.id.textView2);
//		voiceRecognition = (ImageView) findViewById(R.id.map_layer_voice);
//		ar = (ImageView) findViewById(R.id.map_layer_ar);
		
		animationCtl = new AnimationControl(menuLayout);
	}
	
	public void notifyStatusChanged(int status) {
		this.status = status;
//		adjustLayout();

		if(isDemoRun()) {
//			ar.setVisibility(View.GONE);
//			voiceRecognition.setVisibility(View.GONE);
			menuLayout.setVisibility(GONE);
		} else {
//			ar.setVisibility(View.VISIBLE);
//			voiceRecognition.setVisibility(View.VISIBLE);
//			menuLayout.setVisibility(VISIBLE);
		}
		closeDelayed();
	}
	
	public void onTap() {
		if(isDemoRun()) {
			if(zoom.getStatus() == AnimationControl.OPENED) {
				zoom.close();
			} else if(zoom.getStatus() == AnimationControl.CLOSED && !isIllustShow) {
				zoom.open();
				closeDelayed();
			}
		} else {
			if(animationCtl.getStatus() == AnimationControl.OPENED) {
				closeMenu();
			} else if(animationCtl.getStatus() == AnimationControl.CLOSED) {
				openMenu();
				closeDelayed();
			}
		}
	}

	private void openMenu() {
		if(isMainMap() && isPortait()) {
			animationCtl.openWithAnimation();
		} else {
			animationCtl.openWithoutSysmbolMove();
		}
		zoom.open();
	}

	private void closeMenu() {
		if(popupWindow != null && popupWindow.isShowing()) {
			closeDelayed();
			return;
		}
		if(isMainMap() && isPortait()) {
			animationCtl.closeWithAnimation();
		} else {
			animationCtl.closeWithoutSysmbolMove();
		}
		zoom.close();
	}

	private void setListener() {
		if(SystemTools.isCH()){
			((View)report.getParent()).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_CAMEEA_MODE);
//					MenuControlIF.Instance().setWinchangeWithoutAnimation();
					MenuControlIF.Instance().ForwardWinChange(ADT_Settings_Main.class);
				}
			});
		}else{
			report_textview.setText(R.string.STR_MM_03_01_02_07);
			report.setImageResource(R.drawable.navicloud_and_404a);
			((View) report.getParent())
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							MenuControlIF.Instance().ForwardWinChange(
									ADT_Favorite.class);
						}
					});
		}
		
		
		((View)mapLayer.getParent()).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isBottomMenuEnable()) {
					return;
				}
				if (popupWindow == null) {
					popupWindow = new PopupWindow(new Map_Layer(getContext()),
							DensityUtil.dp2px(getContext(), 218), DensityUtil.dp2px(
									getContext(), 173));
					ColorDrawable cd = new ColorDrawable(0);
					popupWindow.setBackgroundDrawable(cd);
					popupWindow.setFocusable(true);
					popupWindow.update();
					
					popupWindow.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss() {
							closeDelayed();
						}
					});
				}
				showPopWindow();

			}
		});
		
		((View)nearbySearch.getParent()).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuControlIF.Instance().ForwardWinChange(ADT_POI_Main.class);
			}
		});
		
		((View)more.getParent()).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				MenuControlIF.Instance().ForwardWinChange(ADT_Overflow_Menu.class);
			}
		});
		
		
		zoom.setZoomButtonClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeDelayed();
			}
		});
		
		MapView.getInstance().setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					removeCloseDeyedHandler();
				} else if((event.getAction() == MotionEvent.ACTION_UP)){
					closeDelayed();
				}
				return false;
			}
		});

	}
	
	private void showPopWindow() {
		// TODO Auto-generated method stub
		if(null == popupWindow) { return;
        }
		popupWindow.setAnimationStyle(R.style.Style_PopUpMenu_PopUpMenuUP);
		popupWindow.showAtLocation(menuLayout,Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,
				-menuLayout.getWidth()/8, menuLayout.getHeight());
		removeCloseDeyedHandler();
	}

//	private void showDialog() {
//		Builder b = new AlertDialog.Builder(
//				getContext());
//		b.setMessage(R.string.STR_MM_00_00_00_00);
//
//		b.setTitle(R.string.STR_MM_00_00_00_00);
//		b.setPositiveButton(R.string.STR_COM_001, null);
//
//		b.setNegativeButton(R.string.STR_COM_003,
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_AR_MODE);
//						MenuControlIF.Instance().ForwardWinChange(ADT_AR_Main.class);
//					}
//				});
//		b.create().show();
//	}
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			post(new Runnable(){
				@Override
				public void run() {
					showPopWindow();
				}
			});
		}
	}
	
//	private void adjustLayout() {
//		
//		boolean isPortait = (ScreenMeasure.isPortrait());
//		boolean isOpen = (animationCtl == null) || (animationCtl.getStatus() == AnimationControl.OPENED);
//		boolean isMainMap = (status == ADT_Main_Map_Navigation.MAIN_MAP);
//		setReferceViewHasHeight((!isMainMap && isPortait) || (isMainMap && isPortait && isOpen));
		
//		int top = DensityUtil.dp2px(getContext(), ScreenMeasure.isPortrait() ? 180 : 122);
//		((RelativeLayout.LayoutParams)zoom.getLayoutParams()).topMargin = top;
//	}
	
//	private void setReferceViewHasHeight(boolean has){
//		referenceView.getLayoutParams().height = has ? DensityUtil.dp2px(getContext(), 54) : 0;
//		referenceView.requestLayout();
//	}
	
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		switch (cTriggerInfo.m_iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE:
			zoom.updateButtonStatus();
			break;
		case NSTriggerID.UIC_MN_TRG_DEMO_START:
			if(isBottomMenuEnable()) {
				closeMenu();
			}
//			ar.setVisibility(View.GONE);
//			voiceRecognition.setVisibility(View.GONE);
			refreshARAndVoice();
			break;
		case NSTriggerID.UIC_MN_TRG_DEMO_STOP:
			if(status == ADT_Main_Map_Navigation.MAIN_MAP){
//				ar.setVisibility(View.VISIBLE);
//				voiceRecognition.setVisibility(View.VISIBLE);
				
			}
			refreshARAndVoice();
			break;
		case NSTriggerID.UIC_MN_TRG_W3_BT_EVENT:
			showConfirmContinueGuideDialog();
			break;
		case NSTriggerID.UIC_MN_TRG_PATH_SYNC_BT_MESSAGE_RESULT:
			dismissSyncProgroess();
			String message = "";
			if(0 != cTriggerInfo.m_lParam1) {
				message = getContext().getString(R.string.MSG_DEMO_DIALOG_SYNC_SUCCESS);
			} else {
				message = getContext().getString(R.string.MSG_DEMO_DIALOG_SYNC_FAILED);
			}
			CustomToast.showToast(getContext(), message, 2000);
			break;
		}
		return false;
	}
	

	private void showConfirmContinueGuideDialog() {
		final CustomDialog dialog = new CustomDialog(getContext());
		dialog.setTitle(R.string.MSG_DEMO_DIALOG_CONFIRM_SYNC_ROUTE_TITLE);
		dialog.setMessage(R.string.MSG_DEMO_DIALOG_CONFIRM_SYNC_ROUTE_CONTENT);
		dialog.setPositiveButton(R.string.MSG_00_00_00_11, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				UIBaseConnJNI.sendBtEvent(false);
				showWaitingDialog();
			}
		});
		dialog.setNegativeButton(R.string.MSG_00_00_00_12, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		dialog.show();
	}
	private CProgressDialog progDialog;
	protected void showWaitingDialog() {
		if(progDialog == null || !progDialog.isShowing()){
			progDialog = CProgressDialog.makeProgressDialog(getContext(), R.string.MSG_DEMO_DIALOG_SYNCING_ROUTE);
			progDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
					if(MenuControlIF.Instance().IsWinChangeLocked()){
						return true;
					}
					
					switch (keyCode) {
					case KeyEvent.KEYCODE_BACK:
						cancelSyncRoute();
						return true;
					case KeyEvent.KEYCODE_MENU:
						return true;
					case KeyEvent.KEYCODE_HOME:
					case KeyEvent.KEYCODE_SEARCH:
					case KeyEvent.KEYCODE_POWER:
						return true;
					default:
						return false;
					}
				}
			});
			progDialog.show();
		}
		
	}

	protected void cancelSyncRoute() {
		dismissSyncProgroess();
		nodtifyLib();
	}
	
	private void nodtifyLib() {
		// TODO Auto-generated method stub
		
	}

	private void dismissSyncProgroess() {
		if(progDialog != null && progDialog.isShowing()){
			progDialog.dismiss();
		}
	}

	private void closeDelayed(){
		closeDelayed(delayedTime);
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1) {
				
				if(isBottomMenuEnable()) {
					closeMenu();
				} else if(zoom.getStatus() == AnimationControl.OPENED) {
					zoom.close();
				}
				
			}
		}
		
	};
	
	private void closeDelayed(int delayMillis) {
		handler.removeMessages(1);
		handler.sendEmptyMessageDelayed(1, delayMillis);
    }
	
	public void removeCloseDeyedHandler() {
		handler.removeMessages(1);
	}
	
	private boolean isPortait() {
		return ScreenMeasure.isPortrait();
	}
	
	private boolean isMainMap(){
		return (status == ADT_Main_Map_Navigation.MAIN_MAP);
	}
	
	private boolean isBottomMenuEnable() {
		return (animationCtl.getStatus() == AnimationControl.OPENED);
	}
	
	private boolean isDemoRun() {
		return (new UIPathControlJNI().GetDemoStatus() == UIPathControlJNI.UIC_PT_DEMO_STATUS_ON);
	}

	@Override
	public void onIllustViewStatusChange(boolean isIllustShowing) {
		isIllustShow = isIllustShowing;
		if(isIllustShow && zoom.getStatus() != AnimationControl.CLOSED) {
			zoom.close();
		}
	}

	private boolean isIllustShow = false;

	public void destory() {
		bottomarea.destory();
	}
	public void refreshARAndVoice(){
		bottomarea.refreshARAndVoice();
	}
	public void onResume(){
		bottomarea.resume();
	}

	public void removeCloseDeyedHandlerAndCloseButtons() {
		removeCloseDeyedHandler();
		closeButtonsAtOnce();
	}

	private void closeButtonsAtOnce() {
		zoom.closeZoomAtOnce();
		animationCtl.closeButtonViewAtOnce();
	}
}
