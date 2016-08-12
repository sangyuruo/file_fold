package com.billionav.navi.naviscreen.debug;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.DialogInterface.OnKeyListener;
//import android.os.Bundle;
//import android.os.Handler;
//import android.preference.CheckBoxPreference;
//import android.preference.ListPreference;
//import android.preference.Preference;
//import android.preference.Preference.OnPreferenceChangeListener;
//import android.preference.Preference.OnPreferenceClickListener;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//
//import com.billionav.jni.jniEcoControl;
//import com.billionav.jni.FileSystemJNI;
//import com.billionav.jni.jniLocInfor;
//import com.billionav.jni.jniSetupControl;
//import com.billionav.navi.component.dialog.CustomDialog;
//import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
//import com.billionav.navi.system.PLog;
//import com.billionav.navi.uicommon.UIC_DebugCommon;
//import com.billionav.navi.uitools.DialogTools;
//import com.billionav.ui.R;
//
//public class ADT_Debug_CradleSetting extends PrefenrenceActivityBase {
//	
//	private ListPreference fileNameSetting;
//	private CheckBoxPreference m_pChild_ASLLogOutput;
//	private CheckBoxPreference m_pChild_ASLLogRestore;
//	private CheckBoxPreference m_pChild_SNSLogOutputSetting;
//	private CheckBoxPreference m_pChild_SNSLogRestoreSetting;
//	private CheckBoxPreference m_pChild_ECOLogOutputSetting;
//	private CheckBoxPreference m_pChild_ECOLogRestoreSetting;
//	
//	private ListPreference m_pChild_SNSStudyState;
//	private Preference m_pChild_SetCarData;
//	private Preference m_pChild_EcoMileageParaInit;
//	private ListPreference m_pChild_BTProfileSetting;
//	private Preference m_pChild_BTStatusRequest;
//	private ListPreference m_pChild_SystemLogSetting;
//	private Preference m_pChild_SystemLogRequest;
//	
//	private final String ALBUMNAME = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH) + "/RW/ASL/";
//	private final jniLocInfor mjniLocInfor = jniLocInfor.getLocInfor();
//	private final jniEcoControl ecoControl = new jniEcoControl();
//	
//	private final String[] PROFILE = new String[] {"HFP+A2DP+SPP", "HFP+SPP", "A2DP+SPP", "SPP"};
//	private final String[] PROFILE_VALUE = new String[] {
//			""+jniLocInfor.BTP_SETTING_HFP_A2DP_SPP, 
//			""+jniLocInfor.BTP_SETTING_HFP_SPP, 
//			""+jniLocInfor.BTP_SETTING_A2DP_SPP, 
//			""+jniLocInfor.BTP_SETTING_SPP};
//	
//	private final String[] SYSTEMLOG = new String[] {"FATAL", "ERROR", "WARN", "INFO", "DEBUG"};
//	private final String[] SYSTEMLOG_VALUE = new String[] {
//			"" + jniLocInfor.SYS_LOG_LVL_FATAL, 
//			"" + jniLocInfor.SYS_LOG_LVL_ERROR, 
//			"" + jniLocInfor.SYS_LOG_LVL_WARN, 
//			"" + jniLocInfor.SYS_LOG_LVL_INFO, 
//			"" + jniLocInfor.SYS_LOG_LVL_DEBUG};
//
//	
//	private String getIndexOfSystemLogLevel() {
//		return "" + mjniLocInfor.GetSystemLogLevel();
//	}
//
//	@Override
//	protected void OnCreate(Bundle savedInstanceState) {
//		super.OnCreate(savedInstanceState);
//		addPreferencesFromResource(R.xml.adt_debug_cradle);
//		setTitle("Cradle Setting");
//		
//		findViews();
//		
//		initialize();
//		
//		setListeners();
//	}
//
//	private void initialize() {
//		String fileName = UIC_DebugCommon.Instance().getAslFileName();
//		fileNameSetting.setSummary("FileName:" + fileName);
//		String[] fileList = getFileList();
//		fileNameSetting.setEntries(fileList);
//		fileNameSetting.setEntryValues(fileList);
//		int index = Arrays.asList(fileList).indexOf(fileName);
//		if(index >= 0) {
//			fileNameSetting.setValueIndex(index);
//		}
//		
//		m_pChild_SNSStudyState.setValueIndex(0);
//		
//		new CarDataSettingDialog(this, m_pChild_SetCarData);
//		
//		m_pChild_BTProfileSetting.setEntries(PROFILE);
//		m_pChild_BTProfileSetting.setEntryValues(PROFILE_VALUE);
//		m_pChild_BTProfileSetting.setValue(getBtpValue());
//		
//		m_pChild_SystemLogSetting.setEntries(SYSTEMLOG);
//		m_pChild_SystemLogSetting.setEntryValues(SYSTEMLOG_VALUE);
//		m_pChild_SystemLogSetting.setValue(getIndexOfSystemLogLevel());
//		
//		initComponents();
//		initLocation();
//	}
//	
//	private void setListeners() {
//		fileNameSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				String fileName = newValue.toString();
//				preference.setSummary("FileName:" + fileName);
//				UIC_DebugCommon.Instance().setAslFileName(fileName);
//				return true;
//			}
//		});
//		
//		m_pChild_ASLLogOutput.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				
//				clickLogOutput(preference, newValue, jniLocInfor.DEBUG_INFO_KIND_ASL_LOG);
//				return true;
//			}
//
//		});
//		
//		m_pChild_SNSLogOutputSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				clickLogOutput(preference, newValue, jniLocInfor.DEBUG_INFO_KIND_SNS_LOG);
//				return true;
//			}
//		});
//
//		m_pChild_ECOLogOutputSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				clickLogOutput(preference, newValue, jniLocInfor.DEBUG_INFO_KIND_ECO_LOG);
//				return true;
//			}
//		});
//		
//		m_pChild_ASLLogRestore.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				
//				clickLogReStore(preference, newValue, jniLocInfor.DEBUG_INFO_KIND_ASL_RESTORE);
//				return true;
//			}
//		});
//		
//		m_pChild_SNSLogRestoreSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				clickLogReStore(preference, newValue, jniLocInfor.DEBUG_INFO_KIND_SNS_RESTORE);
//				return true;
//			}
//		});
//
//		m_pChild_ECOLogRestoreSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				clickLogReStore(preference, newValue, jniLocInfor.DEBUG_INFO_KIND_ECO_RESTORE);
//				return true;
//			}
//		});
//		
//		m_pChild_SNSStudyState.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			private static final boolean CLEAR = false;
//			private static final boolean STORE = true;
//
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				int index = Integer.parseInt(newValue.toString());
//				switch (index) {
//				case 0:
//					if (mjniLocInfor.SetDummyLearn(CLEAR)) {
//						preference.setSummary("SetDummyLearn(CLEAR) true");
//					}
//					break;
//				case 1:
//					if (mjniLocInfor.SetDummyLearn(STORE)) {
//						preference.setSummary("SetDummyLearn(STORE) true");
//					}
//					break;
//				default:
//					break;
//				}
//
//				return true;
//			}
//		});
//		
//		m_pChild_EcoMileageParaInit.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				CustomDialog dialog = DialogTools.getDialogForSingleButton(ADT_Debug_CradleSetting.this, 
//						"Eco Mileage Para Init", "Eco Mileage Para Init, really?", 0, new OnClickListener(){
//
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								ecoControl.EcoParaInit();
//							}});
//				
//				dialog.show();
//				return true;
//			}
//		});
//		
//		m_pChild_BTProfileSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				int value = Integer.parseInt(newValue.toString());
//				mjniLocInfor.SetBtpSettingValue(value);
//				return true;
//			}
//		});
//		
//		m_pChild_BTStatusRequest.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				new AlertDialog.Builder(ADT_Debug_CradleSetting.this)
//				.setTitle("BT Status Request")
//				.setMessage("BT Profile Mode Request, OK?")
//				.setPositiveButton(R.string.STR_COM_003, new OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//								mjniLocInfor.SendBtpRequest();
//							}
//
//						})
//				 .setNegativeButton(R.string.STR_COM_001,
//						new OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//							}
//
//						})
//				  .show();
//				return true;
//			}
//		});
//		
//		m_pChild_SystemLogSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				int value = Integer.parseInt(newValue.toString());
//				mjniLocInfor.SetSystemLogLevel(value);
//				return true;
//			}
//		});
//		
//		m_pChild_SystemLogRequest.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				new AlertDialog.Builder(ADT_Debug_CradleSetting.this)
//				.setTitle("System Log Request")
//				.setMessage("System Log Request, OK?")
//				.setPositiveButton(R.string.STR_COM_003, new OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//								mjniLocInfor.SendSystemLogLevelRequest();
//							}
//
//						})
//				 .setNegativeButton(R.string.STR_COM_001,
//						new OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//							}
//
//						})
//				  .show();
//				return true;
//			}
//		});
//	}
//	
//	
//	private String getBtpValue() {
//		return ""+mjniLocInfor.GetBtpSettingValue();
//	}
//
//	private void clickLogReStore(Preference preference, Object newValue,
//			int jniKey) {
//		final CheckBoxPreference prefer = (CheckBoxPreference) preference;
//		if ((Boolean)newValue) {
//			//open transfer command 
//			if (!mjniLocInfor.StartDebugInfoByKind(jniKey)) {
//				PLog.d("Tora", "m_pChild_ASLLogRestore false");
//				prefer.setChecked(false);
//			}
//			PLog.d("Tora", "m_pChild_ASLLogRestore true");
//		} else {
//			//close transfer command
//			if (!mjniLocInfor.StopDebugInfoByKind(jniKey)) {
//				PLog.d("Tora", "m_pChild_ASLLogRestore true");
//				prefer.setChecked(true);
//			}
//			PLog.d("Tora", "m_pChild_ASLLogRestore false");
//		}
//	}
//
//	private void clickLogOutput(Preference preference, Object newValue, int jniKey) {
//		final CheckBoxPreference prefer = (CheckBoxPreference) preference;
//		if ((Boolean)newValue) {
//			if (!mjniLocInfor.StartDebugInfoByKind(jniKey)) {
//				prefer.setChecked(false);
//				PLog.d("Tora", "DEBUG_INFO_KIND_ASL_LOG StartDebugInfoByKind false");
//			}
//			// get the state of receive data 
//		} else {
//			final ProgressDialog mWaiting = new ProgressDialog(ADT_Debug_CradleSetting.this);
//			if (mWaiting != null) {
//				mWaiting.setMessage("Setting��");
//				mWaiting.show();
//				mWaiting.setCancelable(false);
//			}
//			prefer.setChecked(true);
//			if (!mjniLocInfor.StopDebugInfoByKind(jniKey)) {
//				PLog.d("Tora", "DEBUG_INFO_KIND_ASL_LOG StopDebugInfoByKind false");
//				if (mWaiting != null && mWaiting.isShowing()) {
//					mWaiting.cancel();
//				}
//				return;
//			}
//			PLog.d("Tora", "DEBUG_INFO_KIND_ASL_LOG StopDebugInfoByKind true");
//			Handler mHandler = new Handler();
//			mHandler.postDelayed(new Runnable() {
//
////				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					if (mWaiting != null && mWaiting.isShowing()) {
//						mWaiting.cancel();
//					}
//					prefer.setChecked(false);
//				}
//				
//			}, 3000);
//		}
//	}
//	
//	private String[] getFileList() {
//		ArrayList<String> fileList = new ArrayList<String>();
//		File file = new File(ALBUMNAME);
//		if (file.exists()) {
//			String[] files = file.list();
//			for (int i = 0 ; i < files.length ; i++) {
//				File subfile = new File(ALBUMNAME + files[i]);
//				if (!subfile.isDirectory()) {
////					if (subfile.toString().indexOf(SPLITCHAR) != -1) {
//					fileList.add(subfile.getName());
////					}
//				}
//			}
//		}
//		
//		return fileList.toArray(new String[0]);
//
//	}
//	
//	private void initComponents() {
//		if (UIC_DebugCommon.Instance().isIncomeFirst()) {
//			m_pChild_ASLLogRestore.setChecked(false);
//		}
//		
//		if (UIC_DebugCommon.Instance().isIncomeFirst()) {
//			m_pChild_ASLLogOutput.setChecked(false);
//		}
//		
//		if (UIC_DebugCommon.Instance().isIncomeFirst()) {
//			m_pChild_ECOLogRestoreSetting.setChecked(false);
//		}
//		
//		if (UIC_DebugCommon.Instance().isIncomeFirst()) {
//			m_pChild_ECOLogOutputSetting.setChecked(false);
//		}
//		
//		if (UIC_DebugCommon.Instance().isIncomeFirst()) {
//			m_pChild_SNSLogOutputSetting.setChecked(false);
//		}
//		
//		if (UIC_DebugCommon.Instance().isIncomeFirst()) {
//			m_pChild_SNSLogRestoreSetting.setChecked(false);
//		}
//		
//		if (UIC_DebugCommon.Instance().isIncomeFirst()) {
//			UIC_DebugCommon.Instance().setIncomeFirst(false);
//		}
//	}
//	
//	private void initLocation() {
//		if (mjniLocInfor != null) {
//			mjniLocInfor.StopDebugInfoByKind(jniLocInfor.DEBUG_INFO_KIND_ASL_RESTORE);
//			mjniLocInfor.StopDebugInfoByKind(jniLocInfor.DEBUG_INFO_KIND_ASL_LOG);
//			mjniLocInfor.StopDebugInfoByKind(jniLocInfor.DEBUG_INFO_KIND_ECO_RESTORE);
//			mjniLocInfor.StopDebugInfoByKind(jniLocInfor.DEBUG_INFO_KIND_ECO_LOG);
//			mjniLocInfor.StopDebugInfoByKind(jniLocInfor.DEBUG_INFO_KIND_SNS_RESTORE);
//			mjniLocInfor.StopDebugInfoByKind(jniLocInfor.DEBUG_INFO_KIND_SNS_LOG);
//		}
//	}
//
//
//
//	private void findViews() {
//		fileNameSetting =  (ListPreference) getPreferenceManager().findPreference("file_name_setting");
//		m_pChild_ASLLogOutput =  (CheckBoxPreference) getPreferenceManager().findPreference("ASLLogOutput");
//		m_pChild_ASLLogRestore =  (CheckBoxPreference) getPreferenceManager().findPreference("ASLLogRestore");
//		m_pChild_SNSLogOutputSetting =  (CheckBoxPreference) getPreferenceManager().findPreference("SNSLogOutputSetting");
//		m_pChild_SNSLogRestoreSetting =  (CheckBoxPreference) getPreferenceManager().findPreference("SNSLogRestoreSetting");
//		m_pChild_ECOLogOutputSetting =  (CheckBoxPreference) getPreferenceManager().findPreference("ECOLogOutputSetting");
//		m_pChild_ECOLogRestoreSetting =  (CheckBoxPreference) getPreferenceManager().findPreference("ECOLogRestoreSetting");
//		m_pChild_SNSStudyState =  (ListPreference) getPreferenceManager().findPreference("SNSStudyState");
//		m_pChild_SetCarData =  (Preference) getPreferenceManager().findPreference("SetCarData");
//		m_pChild_EcoMileageParaInit =  (Preference) getPreferenceManager().findPreference("EcoMileageParaInit");
//		m_pChild_BTProfileSetting =  (ListPreference) getPreferenceManager().findPreference("BTProfileSetting");
//		m_pChild_BTStatusRequest =  (Preference) getPreferenceManager().findPreference("BTStatusRequest");
//		m_pChild_SystemLogSetting =  (ListPreference) getPreferenceManager().findPreference("SystemLogSetting");
//		m_pChild_SystemLogRequest =  (Preference) getPreferenceManager().findPreference("SystemLogRequest");
//		
//	}
//
//}
//
//class CarDataSettingDialog {
//	
//	private EditText width  = null;
//	private EditText height  = null;
//	private EditText weight = null;
//	private EditText exhaust = null;
//	private Dialog mCarDataDlg = null;
//	
//	private final Preference prefenrence;
//
//	CarDataSettingDialog(Context context, Preference prefenrence) {
//		mCarDataDlg = getDialog(context);
//		this.prefenrence = prefenrence;
//		prefenrence.setSummary(toString());
//		prefenrence.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				show();
//				return true;
//			}
//		});
//	}
//	
//	private void show() {
//		refreshTextValue();
//		mCarDataDlg.show();
//	}
//	
//	private View getContentView(Context context) {
//		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	    View v = inflater.inflate(R.layout.dialog_car_data_setting, null);
//	    width = (EditText) v.findViewById(R.id.editText1);
//	    height = (EditText) v.findViewById(R.id.editText1);
//	    weight = (EditText) v.findViewById(R.id.editText1);
//	    exhaust = (EditText) v.findViewById(R.id.editText1);
//		return v;
//	}
//	
//	private Dialog getDialog(Context context) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setTitle("Set Car Data");
//		builder.setView(getContentView(context));
//		builder.setPositiveButton(R.string.STR_COM_003, new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				int weight1 = 0;
//				int height1 = 0;
//				int width1 = 0;
//				float exhaust1 = 0;
//				try {
//					width1 = Integer.parseInt(width.getText().toString());
//					height1 = Integer.parseInt(height.getText().toString());
//					weight1 = Integer.parseInt(weight.getText().toString());
//					exhaust1 = Float.parseFloat(exhaust.getText().toString());
//				} catch (NumberFormatException e) {
//					width1 = 0;
//					height1 = 0;
//					weight1 = 0;
//					exhaust1 = 0.f;
//				}
//				if (!new jniEcoControl().SetCarData(height1, width1, weight1, exhaust1)) {
//					InitFailDialog("Set Car Data Error");
//				} else {
//					new jniSetupControl().SetCarSizeStatus(jniSetupControl.CAR_SIZE_WIDTH, width1);
//					new jniSetupControl().SetCarSizeStatus(jniSetupControl.CAR_SIZE_HEIGHT, height1);
//					new jniSetupControl().SetCarSizeStatus(jniSetupControl.CAR_SIZE_WEIGHT, weight1);
//					new jniSetupControl().SetCarSizeStatus(jniSetupControl.CAR_SIZE_EXHAUST, (int)(exhaust1 * 100));
//				}
//				
//				prefenrence.setSummary(CarDataSettingDialog.this.toString());
//			}
//		});
//		
//		return builder.create();
//	}
//	
//	private void InitFailDialog(String msg) {
//		new AlertDialog.Builder(mCarDataDlg.getContext())
//			.setIcon(android.R.drawable.ic_dialog_alert)
//			.setTitle("Initialize Failure")
//			.setMessage(msg)
//			.setNegativeButton(R.string.STR_COM_003, new OnClickListener() {
//
//				public void onClick(DialogInterface dialog, int which) {
////					jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE1);
//				}
//			}).setOnKeyListener(new OnKeyListener() {
//
//				public boolean onKey(DialogInterface dialog, int keyCode,
//						KeyEvent event) {
////					jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE2);
//					dialog.cancel();
//					return false;
//				}
//				
//			}).show();
//	}
//
//	
//	private void refreshTextValue() {
//		width.setText(String.valueOf(new jniSetupControl().GetCarSizeStatus(jniSetupControl.CAR_SIZE_WIDTH)));
//		height.setText(String.valueOf(new jniSetupControl().GetCarSizeStatus(jniSetupControl.CAR_SIZE_HEIGHT)));
//		weight.setText(String.valueOf(new jniSetupControl().GetCarSizeStatus(jniSetupControl.CAR_SIZE_WEIGHT)));
//		exhaust.setText(String.valueOf((float)new jniSetupControl().GetCarSizeStatus(jniSetupControl.CAR_SIZE_EXHAUST) / 100));
//	}
//	
//	@Override
//	public final String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("width=" + new jniSetupControl().GetCarSizeStatus(jniSetupControl.CAR_SIZE_WIDTH));
//		sb.append(" ");
//		sb.append("height=" + new jniSetupControl().GetCarSizeStatus(jniSetupControl.CAR_SIZE_HEIGHT));
//		sb.append(" ");
//		sb.append("weight=" + new jniSetupControl().GetCarSizeStatus(jniSetupControl.CAR_SIZE_WEIGHT));
//		sb.append(" ");
//		sb.append("exhaust=" + (new jniSetupControl().GetCarSizeStatus(jniSetupControl.CAR_SIZE_EXHAUST)/100));
//		
//		return sb.toString();
//	}
//
//}
