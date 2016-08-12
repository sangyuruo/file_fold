package com.billionav.navi.system;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Process;
import android.view.KeyEvent;

import com.billionav.navi.component.dialog.CProgressBarDialog;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.dataupdate.DataUpdateListener;
import com.billionav.navi.dataupdate.DataUpdateManager;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.update.CameraUpdate;
import com.billionav.navi.update.UpdateController;
import com.billionav.ui.R;

public class StartRuntimeTask extends AsyncTask<Integer, Integer, Integer> {

	private static final int PUBLISH_PROGRESS_OK = 1;
	private static final int PUBLISH_PROGRESS_START_COPY_NDATA = 2;
	private static final int PUBLISH_PROGRESS_FINISH_COPY_NDATA = 2;
	private static final int PUBLISH_PROGRESS_SHOW_SDCARD_ERROR = -1;
	private static final int PUBLISH_PROGRESS_SHOW_SDCARD_LOW_MEMORY = -2;
	private static final int PUBLISH_PROGRESS_SHOW_DATA_DISMATCH_DIALOG = -3;
	private static final int PUBLISH_PROGRESS_SHOW_DATA_DEL_PROGRESS = -4;
	
	
	private Context context;
	private CProgressBarDialog progressBarDialog;
	private TaskSyncController taskController = new TaskSyncController();
	private boolean exitbackgroundThread = false;
	
	private static StartRuntimeTask instance;

	public static StartRuntimeTask getInstance() {
		if (null == instance) {
			instance = new StartRuntimeTask();
		}
		return instance;
	}
	
	private StartRuntimeTask() {
		this.context = NSViewManager.GetViewManager();
	}
	
	public TaskSyncController getTaskController() {
		return taskController;
	}

	private Handler showDeleteProgresshandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what != 0x121) { 
				return;
            }
			if(null == progressBarDialog){
				progressBarDialog = CProgressBarDialog.makeProgressDialog(context, R.string.MSG_01_01_04_01);
				progressBarDialog.show();
			}
			if(0 == msg.arg2){
				msg.arg2 = msg.arg1;
			}
			progressBarDialog.setProgress(msg.arg1/msg.arg2*100);
			if(msg.arg2 == msg.arg1){
				progressBarDialog.dismiss();
				progressBarDialog = null;
			}
		};
	};
	
	@Override
	protected Integer doInBackground(Integer... params) {
		int checkSDCardResult = checkSDCardExist();
		if(ADataManager.STORAGE_OK == checkSDCardResult){
			publishProgress(PUBLISH_PROGRESS_START_COPY_NDATA);
			AplRuntime.Instance().DataInitBackThrd();
			publishProgress(PUBLISH_PROGRESS_FINISH_COPY_NDATA);
			
			if (AplRuntime.Instance().CheckNDataState()) {
				notifyNDataNeedDownload();
				taskController.getSignal();
			}

			if (exitbackgroundThread) {
				Process.killProcess(Process.myPid());
				return 0;
			}
			if(SystemTools.isCH()){
				int returnCode = checkDataFormat();
				switch(returnCode){
				case UpdateController.UPDATE_CONTROL_FORMAT_CHECK_OK:
					startAPLInitBackThrd();
					publishProgress(PUBLISH_PROGRESS_OK);
					break;
				case UpdateController.UPDATE_CONTROL_EXIST_DEL_REQUEST:
					DataUpdateManager.clearDataForRequest(new DataUpdateListener() {
						
						@Override
						public void onDeleteFile(String fileName, int fileIndex, int totalFile,
								boolean isSuccess) {
							publishProgress(PUBLISH_PROGRESS_SHOW_DATA_DEL_PROGRESS, fileIndex, totalFile);
						}
						
						@Override
						public void onDataClearCompleted() {
							startAPLInitBackThrd();
							publishProgress(PUBLISH_PROGRESS_OK);
						}
					});
					break;
				case UpdateController.UPDATE_CONTROL_FORMAT_CHECK_DISMATCH:
					publishProgress(PUBLISH_PROGRESS_SHOW_DATA_DISMATCH_DIALOG);
					break;
					default:publishProgress(PUBLISH_PROGRESS_SHOW_SDCARD_ERROR);break;
				}
			}else{
				//skip APK check flow
				startAPLInitBackThrd();
				publishProgress(PUBLISH_PROGRESS_OK);
			}
		}else{
			if(checkSDCardResult == ADataManager.STORAGE_LOWCAPACITY) {
				publishProgress(PUBLISH_PROGRESS_SHOW_SDCARD_LOW_MEMORY);
			} else{
				publishProgress(PUBLISH_PROGRESS_SHOW_SDCARD_ERROR);
			}
		}
		
		return 0;
	}
	
	private void notifyNDataNeedDownload() {
		NSTriggerInfo info = new NSTriggerInfo();
		info.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DLNDATA_CHECK_FINISHED;
		info.m_lParam1 = 1;
		MenuControlIF.Instance().TriggerForScreen(info);
	}
	
	private CProgressDialog progDialog;

	@Override
	protected void onProgressUpdate(Integer... values) {
		if(values[0] == PUBLISH_PROGRESS_SHOW_SDCARD_ERROR){
			showSDCardNotExistDialog();
		}else if(values[0] == PUBLISH_PROGRESS_SHOW_SDCARD_LOW_MEMORY){
			showSDCardLowMemoryDialog();
		}else if(values[0] == PUBLISH_PROGRESS_SHOW_DATA_DISMATCH_DIALOG){
			showDataFormatDismatchDialog();
		}else if(values[0] == PUBLISH_PROGRESS_SHOW_DATA_DEL_PROGRESS){
			showDeleteProgresshandler.sendMessage(showDeleteProgresshandler.obtainMessage(0x121, values[1], values[2]));				
		}else if(values[0] == PUBLISH_PROGRESS_OK) {
			SystemTools.unRegisterStartTask();
//        	MapView.createInstance(context);
//			dismissWaitingProgress();
//			AplRuntime.Instance().AplInitMainThrd();
			cancel(false);
		}else if(values[0] == PUBLISH_PROGRESS_START_COPY_NDATA){
//			showWaitingProgress();
		}else if(values[0] == PUBLISH_PROGRESS_FINISH_COPY_NDATA){
//			dismissWaitingProgress();
		}
	}
	
	private void showWaitingProgress() {
		if(null == progDialog) {
			progDialog = CProgressDialog.makeProgressDialog(context,"");
			progDialog.setCancelable(false);
			progDialog.show();
		}
	}
	
	private void dismissWaitingProgress() {
		if(null != progDialog) {
			progDialog.dismiss();
			progDialog = null;
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
			

	private int checkSDCardExist() {
		return ADataManager.prepareAdata(context);
	}
	
	public int checkDataFormat() {
		return UpdateController.getInstance().checkDataFormat();
	}
	private void startAPLInitBackThrd(){

		AplRuntime.Instance().AplInitBackThrd();
	}
	
	private void showSDCardNotExistDialog() {
		CustomDialog dialog = new CustomDialog(context);
		dialog.setTitle(R.string.MSG_01_02_01_01_04);
		dialog.setMessage(R.string.STR_MM_01_01_04_31);
		dialog.setNegativeButton(R.string.STR_COM_003, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((NaviViewManager) NSViewManager.GetViewManager())
						.removeCameraView();
				System.exit(0);
			}
		});
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				System.exit(0);
			}

		});
		dialog.show();
	}
	
	private void showSDCardLowMemoryDialog() {
		CustomDialog dialog = new CustomDialog(context);
		dialog.setTitle(R.string.MSG_01_02_01_01_04);
		dialog.setMessage(R.string.STR_MM_01_01_04_34);
		dialog.setNegativeButton(R.string.STR_COM_003, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((NaviViewManager) NSViewManager.GetViewManager())
						.removeCameraView();
				System.exit(0);
			}
		});
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				System.exit(0);
			}

		});
		dialog.show();
	}

	private void showDataFormatDismatchDialog() {
		startAPLInitBackThrd();	
		CustomDialog dialog =    new CustomDialog(context);
		dialog.setTitle(R.string.STR_MM_01_01_04_29);
		dialog.setMessage(R.string.STR_COM_022);
		
		dialog.setPositiveButton(R.string.STR_COM_003, new OnClickListener() {
					
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new Handler().post(new Runnable() {
					
					@Override
					public void run() {
						startAPLInitBackThrd();	
						DataUpdateManager.clearDataForFormateChanged(new DataUpdateListener() {
							
							@Override
							public void onDeleteFile(String fileName, int fileIndex, int totalFile,
									boolean isSuccess) {
								showDeleteProgresshandler.sendMessage(showDeleteProgresshandler.obtainMessage(0x121, fileIndex, totalFile));
								
							}
							
							@Override
							public void onDataClearCompleted() {
								// TODO Auto-generated method stub
								
							}
						});
					}
				});
			}
		});
				

			
		dialog.setNegativeButton(R.string.STR_COM_001, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						NSViewManager.GetViewManager().finish();
						System.exit(0);
					}
				});
		dialog.show();
	}

	public class TaskSyncController {
		private int signalFlag = 0;

		public synchronized void getSignal() {
			while (this.signalFlag <= 0) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			signalFlag--;
		}

		public synchronized void raiseSignal() {
			signalFlag++;
			this.notifyAll();
		}
	}
	
	
	public void setExitbackgroundThread(boolean exitbackgroundThread) {
		this.exitbackgroundThread = exitbackgroundThread;
	}
}
