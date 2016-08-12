package com.billionav.navi.update;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.dataupdate.DataUpdateManager;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.uitools.DialogTools;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.versioncontrol.VersionControl_CommonVar;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;
import com.billionav.navi.versioncontrol.VersionControl_VersionDataFormat;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetTopDataVersion;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseBase;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseGetSrchTopDataVersion;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseGetTopDataVersion;
import com.billionav.ui.R;

public class UpdateController implements TriggerListener{
	public static final int UPDATE_CONTROL_FORMAT_CHECK_OK = 0;
	public static final int UPDATE_CONTROL_EXIST_DEL_REQUEST = 1;
	public static final int UPDATE_CONTROL_FORMAT_CHECK_DISMATCH = 2;
	private static final String MAP_FORMAT_VERSION = "MapFormatVersion";
	
	private static final int DATA_UPDATE_STATE_INVALED = -1;
	private static final int DATA_UPDATE_STATE_BELOW_MINUN = 0x001;
	private static final int DATA_UPDATE_STATE_HAS_NEW_VERSION = 0x010;
	private static final int DATA_UPDATE_STATE_LATEST = 0x100;
	
	private int mapDataUpdateState = DATA_UPDATE_STATE_INVALED;
	private int srchDataUpdateState = DATA_UPDATE_STATE_INVALED;
	
	private onApkUpdataListener listener;
	private static UpdateController instance;
	private VersionControl_VersionDataFormat formatDataInfos = null;
	private String updateInformations = "";
//	private DataUpdateManager dataManager = new DataUpdateManager();
	public static UpdateController getInstance(){
		if(null == instance){
			instance = new UpdateController();
		}
		return instance;
	}
	public  UpdateController(){
		GlobalTrigger.getInstance().addTriggerListener(this);
	}
	public int checkDataFormat(){
		if(DataUpdateManager.isExistClearDataRequest()){
			//Delete data buffer ,interface supported by Tangshaohua
			return UPDATE_CONTROL_EXIST_DEL_REQUEST;
		}
		if(DataUpdateManager.isFormatVersionSame()){
			return UPDATE_CONTROL_FORMAT_CHECK_OK;
		}else{
			return UPDATE_CONTROL_FORMAT_CHECK_DISMATCH;
		}
	}
	public static final int UC_RESPONES_SUC = 0; //suc
	public static final int UC_RESPONES_SRV_FAIL = 1; // Service fail
	public static final int UC_RESPONES_LOC_FAIL = 2; //Local fail

	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		//on trigger, need update triggerID for ApkVersionOK
		
		if(NSTriggerID.UIC_MN_TAG_UC_GET_ALL_APK_UPDATE_INFOS == triggerInfo.GetTriggerID()){
			Log.d("test","UIC_MN_TAG_UC_GET_ALL_APK_UPDATE_INFOS param1="+triggerInfo.GetlParam1());
			if(VersionControl_CommonVar.VC_ERROR_CODE_HAVE_UPDATE == triggerInfo.GetlParam1()){
				listener.ApkHasNewVersion();
			} else if(VersionControl_CommonVar.VC_ERROR_CODE_MUST_UPDATE == triggerInfo.GetlParam1()){
				listener.APKMustUpdate();
			}else{
				listener.ApkIsLatest();
			}
		}else if(NSTriggerID.UIC_MN_TAG_UC_GET_TOP_DATA_VERSION == triggerInfo.GetTriggerID()){
			Log.d("test","triggerID UIC_MN_TRG_UC_GET_METADATA_INFO received");
			if(VersionControl_ResponseBase.UC_RESPONES_SUC == triggerInfo.GetlParam1()){
				onDataUpdateInfoPrepared(triggerInfo.GetString1());
			}
			return true;
		}
		return false;
	}
	private void onDataUpdateInfoPrepared(String tag) {
		if(VersionControl_ResponseGetTopDataVersion.TAG.equals(tag)) {
			if(MapDataMustUpdate()){
//				listener.dataIsTooOld();
				mapDataUpdateState = DATA_UPDATE_STATE_BELOW_MINUN;
			} else if(MapDataNeedUpdate()) {
				mapDataUpdateState = DATA_UPDATE_STATE_HAS_NEW_VERSION;
//				listener.dataHasNewVersion();
			} else {
				mapDataUpdateState = DATA_UPDATE_STATE_LATEST;
			}
		} else if(VersionControl_ResponseGetSrchTopDataVersion.TAG.equals(tag)) {
			if(isSrchDataVailed()) {
				if(SrchDataMustUpdate()) {
					srchDataUpdateState = DATA_UPDATE_STATE_BELOW_MINUN;
				} else if(SrchDataNeedUpdate()) {
					srchDataUpdateState = DATA_UPDATE_STATE_HAS_NEW_VERSION;
				} else {
					srchDataUpdateState = DATA_UPDATE_STATE_LATEST;
				}
			}
		} else {
			return;
		}
		if(mapDataUpdateState > DATA_UPDATE_STATE_INVALED 
				&& srchDataUpdateState > DATA_UPDATE_STATE_INVALED) {
			int conbineState = mapDataUpdateState | srchDataUpdateState;
			if((conbineState & DATA_UPDATE_STATE_BELOW_MINUN) == DATA_UPDATE_STATE_BELOW_MINUN) {
				listener.dataIsTooOld();
			} else if((conbineState & DATA_UPDATE_STATE_HAS_NEW_VERSION) == DATA_UPDATE_STATE_HAS_NEW_VERSION) {
				listener.dataHasNewVersion();
			} else {
				return;
			}
		} else {
			return;
		}
	}
	
	private boolean MapDataMustUpdate() {
		return DataUpdate.belowMinVersion();
	}

	private boolean isSrchDataVailed() {
		return DataUpdate.isCurSrchDataVersionVailed();
	}
	
	private boolean SrchDataMustUpdate() {
		return DataUpdate.SrchDataBelowMinVersion();
	}
	
	private boolean SrchDataNeedUpdate() {
		return !DataUpdate.isSrchDataLatestVersion();
	}
	public interface onApkUpdataListener{
		public void ApkHasNewVersion();
		public void dataHasNewVersion();
		public void dataIsTooOld();
		public void ApkIsLatest();
		public void APKMustUpdate();
	}
	
	public void requestApkList(){
		if(null != listener) return;
		versionRequestApkList(false);
	}
	private boolean needShowApkIsLatestDialog = false;
	public void versionRequestApkList(boolean needShowApkLatestDialog){
		this.needShowApkIsLatestDialog = needShowApkLatestDialog;
		if(null == listener){
			this.listener = new UpdateController.onApkUpdataListener() {
				
				@Override
				public void dataHasNewVersion() {
					showDataHasNewVersionDialog();
				}
				@Override
				public void ApkHasNewVersion() {
					formatDataInfos = VersionControl_ManagerIF.Instance().getM_cLatestVersionInfo();
					formatDataInfos.getcMetaDataMap();
					updateInformations = VersionControl_ManagerIF.Instance().GetReleaseUpdateInfos();
					showApkHasNewVersionDialog();
				}
				@Override
				public void dataIsTooOld() {
					showDataMustUpdateDialog();
					
				}
				@Override
				public void ApkIsLatest() {
					DataUpdate.requestLatestDataVersionOnserver();
					if(needShowApkIsLatestDialog) {
						showApkIsLatestDialog();
					}
					
				}
				@Override
				public void APKMustUpdate() {
					formatDataInfos = VersionControl_ManagerIF.Instance().getM_cLatestVersionInfo();
					formatDataInfos.getcMetaDataMap();
					updateInformations = VersionControl_ManagerIF.Instance().GetReleaseUpdateInfos();
					showApkMustUpdateDialog();
				}
				
			};
		}
		ApkUpdate.requestApkVersionOnserver();
	}
	
	private void showApkMustUpdateDialog() {
		showApkUpdateDialog(true);
	}
	private void showApkIsLatestDialog() {
		CustomDialog dialog = new CustomDialog(NSViewManager.GetViewManager());
		dialog.setTitle(R.string.STR_MM_06_02_03_04);
		dialog.setMessage(R.string.MSG_MM_06_02_03_02);

		dialog.setPositiveButton(R.string.STR_MM_09_01_02_02, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

//	private void onApkListPrepared(){
//		if(ApkNeedUpdate()){
//			listener.ApkHasNewVersion();
////				RequestApkUpdate();
//		}else{
//			listener.ApkIsLatest();
//			DataUpdate.requestLatestDataVersionOnserver();
//		}
//	}
	public void requestDataUpdate() {
		//data version should be decided after Interface ok
		int dataVersion = 0;
		DataUpdateManager.requestClearDataForDataVersionChanged(dataVersion);
		
		DialogTools.createExitDialog(NSViewManager.GetViewManager());
		
	}
	public void RequestApkUpdate() {
		ApkUpdate.requestApkUpdate();
		
	}
	private boolean MapDataNeedUpdate() {
		return !DataUpdate.isLatestVersion();
	}
	private boolean ApkNeedUpdate() {
		return !ApkUpdate.isLatestVersion();
	}
	private void removeTriggerListener() {
		GlobalTrigger.getInstance().removeTriggerListener(this);
	}
	
	
	public boolean hasAPKUpdated(Context c){
		return ApkUpdate.hasAPKVersionUpdated(c);
	}
	protected void showDataMustUpdateDialog() {
		CustomDialog dialog = new CustomDialog(NSViewManager.GetViewManager());
		dialog.setTitle(R.string.STR_COM_025);
		dialog.setMessage(R.string.MSG_COM_01_05);
		dialog.setPositiveButton(R.string.MSG_00_00_00_10, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mapDataUpdateState == DATA_UPDATE_STATE_BELOW_MINUN) {
					DataUpdate.informMapDataNeedUpdate();
				}
				if(srchDataUpdateState == DATA_UPDATE_STATE_BELOW_MINUN) {
					DataUpdate.informSrchDataNeedUpdate();
				}
				SystemTools.exitSystem();
			}
		});
//		dialog.setNegativeButton(R.string.STR_COM_001, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				SystemTools.exitSystem();
//			}
//		});
		dialog.setCancelable(false);
		dialog.show();
	}
	private void showApkUpdateDialog(final boolean bMust) {
		CustomDialog dialog = new CustomDialog(NSViewManager.GetViewManager());
		String apkVerName = "";
		if(null != formatDataInfos && null != formatDataInfos.getStrVerSion()){
			apkVerName = formatDataInfos.getStrVerSion();
		}
		dialog.setTitle(NSViewManager.GetViewManager().getString(R.string.STR_COM_021)+" "+apkVerName);
		if(!TextUtils.isEmpty(updateInformations)){
			String text = String.format(NSViewManager.GetViewManager().getResources().getString(R.string.MSG_00_00_00_01, updateInformations));
			dialog.setMessage(text);
			//for htmt test
//			String html="HTML text"
//	          
//			dialog.setMessage(Html.fromHtml(html));      

		}else{
			dialog.setMessage(R.string.STR_COM_024);
		}
		if(isDataFormatChanged()){
			Context context = NSViewManager.GetViewManager().getApplicationContext();
			dialog.addView(createAlertMessage(context));
		}
		dialog.setPositiveButton(R.string.MSG_00_00_00_09, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				RequestApkUpdate();
				dialog.dismiss();
			}
		});
		dialog.setNegativeButton(R.string.MSG_00_00_00_08, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(bMust){
					SystemTools.exitSystem();
				} else {
					dialog.dismiss();
					DataUpdate.requestLatestDataVersionOnserver();
				}
			}
		});
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if(bMust){
					SystemTools.exitSystem();
				} else {
					DataUpdate.requestLatestDataVersionOnserver();
				}
			}
		});
		dialog.setEnterBackKeyAllowClose(true);
		dialog.show();
		Log.d("test","updatedialog show ret=-"+bMust);
	}
	
	protected boolean showApkHasNewVersionDialog() {
		showApkUpdateDialog(false);
		return true;
	}

	private View createAlertMessage(Context context) {
		TextView view = new TextView(NSViewManager.GetViewManager().getApplicationContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, R.id.linearLayout1);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		params.leftMargin = DensityUtil.dp2px(context, 20);
		params.rightMargin = DensityUtil.dp2px(context, 20);
		params.bottomMargin = DensityUtil.dp2px(context, 10);
		view.setLayoutParams(params);
		view.setText(R.string.MSG_00_00_00_02);
		view.setTextColor(Color.RED);
		return view;
	}
	private boolean isDataFormatChanged() {
		String latestMapFormatVersion = formatDataInfos.getcMetaDataMap().get(MAP_FORMAT_VERSION);
		int latestVer = -1;
		try{
			latestVer = Integer.parseInt(latestMapFormatVersion);
		}catch(Exception e){
			Log.d("UI_LOG","the latestMapformatVersion is null or data error");
		}
		if(latestVer != -1 && latestVer > DataUpdateManager.getFormatVersion()){
			return true;
		}
		return false;
	}
	protected boolean showDataHasNewVersionDialog() {
		CustomDialog dialog = new CustomDialog(NSViewManager.GetViewManager());
		dialog.setTitle(R.string.MSG_00_00_00_07);
		dialog.setMessage(R.string.MSG_00_00_00_03);
		dialog.setPositiveButton(R.string.MSG_00_00_00_09, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mapDataUpdateState == DATA_UPDATE_STATE_HAS_NEW_VERSION) {
					DataUpdate.informMapDataNeedUpdate();
					dialog.dismiss();
					showRestartDialog();
				}
				if(srchDataUpdateState == DATA_UPDATE_STATE_HAS_NEW_VERSION) {
					DataUpdate.informSrchDataNeedUpdate();
				}
			}
		});
		dialog.setNegativeButton(R.string.MSG_00_00_00_08, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		dialog.setCancelable(false);
		dialog.show();
		
		return false;
	}
	private void showRestartDialog(){
		CustomDialog dialog = new CustomDialog(NSViewManager.GetViewManager());
		dialog.setTitle(R.string.MSG_00_00_00_07);
		dialog.setMessage(R.string.MSG_00_00_00_04);
		dialog.setPositiveButton(R.string.MSG_00_00_00_11, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SystemTools.exitSystem();
			}
		});
		dialog.setNegativeButton(R.string.MSG_00_00_00_12, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}
}
