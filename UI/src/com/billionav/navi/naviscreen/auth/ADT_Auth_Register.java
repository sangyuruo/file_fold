package com.billionav.navi.naviscreen.auth;



import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.ViewHelp;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.system.SystemInfo;
import com.billionav.navi.uicommon.UIC_AuthCommon;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.ui.R;

public class ADT_Auth_Register extends ActivityBase {
	private final int TELEPHONE_REGISTER = 0;
	private final int EMAIL_REGISTER = 1;
	
	private LinearLayout telephoneLayoutInput = null;
	private LinearLayout telephoneLayoutAuthCode = null;
	private LinearLayout mailboxLayout = null;
	private LinearLayout mailboxNoticeLayout = null;
	private Button registerTypeButton = null;
	private EditText userNameEdit = null;
	private EditText passWordEdit = null;
	private EditText confirmPassWordEdit = null;
	private EditText telephoneNumEdit = null;
	private EditText mailboxNumEdit = null;
	private EditText securityCodeEdit = null;
	private Button getSecurityCodeButton = null;
	private TextView userNameErrorTipText = null;
	private TextView passWordErrorTipText = null;
	private TextView confirmPassWordErrorTipText = null;
	private TextView telephoneErrorTipText = null;
	private TextView mailboxErrorTipText = null;
	private TextView securityCodeErrorTipText = null;
	private CProgressDialog progressDialog = null;
	private TextView errorTip = null;
	private String usernameTipStr = "";
	private String passWordTipStr = "";
	private String confirmTipStr = "";
	private String phoneTipStr = "";
	private String mailboxTipStr = "";
	private String secTipStr = "";
	private boolean isClickBack = false;
	private CustomDialog dialog = null;
	private int currentRegisterType = TELEPHONE_REGISTER;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_auth_register);		
		setTitle(R.string.STR_MM_09_02_02_01);
		setDefaultBackground();
		findViews();
		initRegTypeSpinner();
		addAcitonItems();
		setClickListener();
		
	}
	private void addAcitonItems() {
		//back
		addActionItem3(R.string.STR_MM_09_02_02_15, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isClickBack = true;
				BackSearchWinChange(ADT_Auth_Login.class);
				
			}
		});
		//submit
		addActionItem3(R.string.STR_MM_09_02_02_16, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String userName = userNameEdit.getText().toString();
				String passWord = passWordEdit.getText().toString();
				String telePhone = telephoneNumEdit.getText().toString();
				String email = mailboxNumEdit.getText().toString();
				String securityCode = securityCodeEdit.getText().toString();
				boolean returnvalue = checkUserValid(false);
				returnvalue = checkPasswdValid(false) && returnvalue;
				returnvalue = checkPasswdCfValid(false) && returnvalue;
				if(currentRegisterType == TELEPHONE_REGISTER){
					returnvalue = checkMobileValid(false) && returnvalue;
					returnvalue = checkPincodeValid(false) && returnvalue;
					errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+phoneTipStr+secTipStr);
				}else if(currentRegisterType == EMAIL_REGISTER){
					returnvalue = checkEmailValid(false) && returnvalue;
					errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+mailboxTipStr);
				}else{}
				if(!isNetEnable()){
					return;
				}
				if (returnvalue) {
					UserControl_UserInfo userInfo = new UserControl_UserInfo();
					long ret = -1 ;
					userInfo.m_strNickName = userName;
					userInfo.m_strUserPassWord = passWord;
					if(currentRegisterType == TELEPHONE_REGISTER){
						userInfo.m_strPhoneNum = telePhone;
						ret = UserControl_ManagerIF.Instance().RegisterByPhone(userInfo, securityCode);
					}else if(currentRegisterType == EMAIL_REGISTER){
						userInfo.m_strEmail = email;
						ret = UserControl_ManagerIF.Instance().RegisterByMail(userInfo);
					}else{}
					if (ret != -1) {
						lockForClickListener();
						progressDialog = CProgressDialog
								.makeProgressDialog(ADT_Auth_Register.this,
										R.string.STR_MM_09_02_02_21);
						progressDialog.show();
					} else {
						CustomToast.showToast(ADT_Auth_Register.this,
								getString(R.string.STR_MM_09_01_01_10),
								Toast.LENGTH_SHORT);
					}
				}
			}
		});
	}
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			isClickBack = true;
			BackSearchWinChange(ADT_Auth_Login.class);
			return true;
		}
		return super.OnKeyDown(keyCode, event);
	}

	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
		NSViewManager.GetViewManager().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}
	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
		ViewHelp.closeInputKeyBoard(userNameEdit,passWordEdit,confirmPassWordEdit,telephoneNumEdit,securityCodeEdit);
		NSViewManager.GetViewManager().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}
	
	private boolean checkUserValid(boolean act) {
		if (act) {
			usernameTipStr = "";
			userNameErrorTipText.setVisibility(View.INVISIBLE);
			return false;
		}
		String username = userNameEdit.getText().toString();
		int ret = UserControl_ManagerIF.Instance().IsValidNickName(username);
		if (ret == UserControl_CommonVar.VALIDATE_INPUT_OK) {
			usernameTipStr = "";
			userNameErrorTipText.setVisibility(View.INVISIBLE);
			return true;
		} else if (ret == UserControl_CommonVar.VAILDATE_INPUT_TOO_SHORT) {
			usernameTipStr = getString(R.string.STR_MM_09_02_02_04);
			userNameErrorTipText.setVisibility(View.VISIBLE);
			return false;
		} else if (ret == UserControl_CommonVar.VAILDATE_INPUT_TOO_LONG) {
			usernameTipStr = getString(R.string.STR_MM_09_02_02_28);
			userNameErrorTipText.setVisibility(View.VISIBLE);
			return false;
		} else  {
			// format error
			usernameTipStr = getString(R.string.STR_MM_09_02_02_31);
			userNameErrorTipText.setVisibility(View.VISIBLE);
			return false;
		}
	
	}	

	private boolean checkPasswdValid(boolean act) {
		if (act) {
			passWordTipStr = "";
			passWordErrorTipText.setVisibility(View.INVISIBLE);
			return false;
		}
		String passWd = passWordEdit.getText().toString();
		int ret = UserControl_ManagerIF.Instance().IsValidPassword(passWd);
		if (ret == UserControl_CommonVar.VALIDATE_INPUT_OK) {
			passWordTipStr = "";
			passWordErrorTipText.setVisibility(View.INVISIBLE);
			return true;
		} else if (ret == UserControl_CommonVar.VAILDATE_INPUT_TOO_SHORT) {
			passWordTipStr = getString(R.string.STR_MM_09_02_02_29);
			passWordErrorTipText.setVisibility(View.VISIBLE);
			return false;
		} else if (ret == UserControl_CommonVar.VAILDATE_INPUT_TOO_LONG) {

			passWordTipStr = getString(R.string.STR_MM_09_02_02_30);
			passWordErrorTipText.setVisibility(View.VISIBLE);
			return false;
		} else {
			// format error
			passWordTipStr = getString(R.string.STR_MM_09_02_02_32);
			passWordErrorTipText.setVisibility(View.VISIBLE);
			return false;
		}

	}	

	private boolean checkPasswdCfValid(boolean act) {
		if (act) {
			confirmTipStr = "";
			confirmPassWordErrorTipText.setVisibility(View.INVISIBLE);
			return false;
		}
		String passCf = confirmPassWordEdit.getText().toString();
		boolean isSame = passCf.equals(passWordEdit.getText().toString());
		if (isSame) {
			confirmTipStr = "";
			confirmPassWordErrorTipText.setVisibility(View.INVISIBLE);
			return true;
		} else {
			confirmTipStr = getString(R.string.STR_MM_09_02_02_08);
			confirmPassWordErrorTipText.setVisibility(View.VISIBLE);
			return false;
		}
	}	

	private boolean checkMobileValid(boolean act) {
		if (act) {
			phoneTipStr = "";
			telephoneErrorTipText.setVisibility(View.INVISIBLE);
			return false;
		}
		String phone = telephoneNumEdit.getText().toString();
		boolean isvalid = UserControl_ManagerIF.Instance().IsValidPhoneNum(
				phone);
		if (isvalid) {
			phoneTipStr = "";
			telephoneErrorTipText.setVisibility(View.INVISIBLE);
			return true;
		} else {
			phoneTipStr = getString(R.string.STR_MM_09_02_02_11);
			telephoneErrorTipText.setVisibility(View.VISIBLE);
			return false;
		}
	}
	
	private boolean checkEmailValid(boolean act) {
		if (act) {
			mailboxTipStr = "";
			mailboxErrorTipText.setVisibility(View.INVISIBLE);
			return false;
		}
		String email = mailboxNumEdit.getText().toString();
		boolean isvalid = UserControl_ManagerIF.Instance().IsValidEmail(
				email);
		if (isvalid) {
			mailboxTipStr = "";
			mailboxErrorTipText.setVisibility(View.INVISIBLE);
			return true;
		} else {
			mailboxTipStr = getString(R.string.STR_MM_09_02_02_39);
			mailboxErrorTipText.setVisibility(View.VISIBLE);
			return false;
		}
	}
	
	private boolean checkPincodeValid(boolean act) {
		if (act) {
			secTipStr = "";
			securityCodeErrorTipText.setVisibility(View.INVISIBLE);
			return false;
		}
		String pinCode = securityCodeEdit.getText().toString();
		if (!TextUtils.isEmpty(pinCode)) {

			secTipStr = "";
			securityCodeErrorTipText.setVisibility(View.INVISIBLE);
			return true;
		} else {

			secTipStr = getString(R.string.STR_MM_09_02_02_33);
			securityCodeErrorTipText.setVisibility(View.VISIBLE);
			return false;
		}

	}	

	private void setClickListener() {
		
		userNameEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(isClickBack){
					return;
				}
				checkUserValid(hasFocus);
				if(currentRegisterType == TELEPHONE_REGISTER){
					errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+phoneTipStr+secTipStr);
				}else if(currentRegisterType == EMAIL_REGISTER){
					errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+mailboxTipStr);
				}else{}
			}
		});
		passWordEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(isClickBack){
					return;
				}
				checkPasswdValid(hasFocus);
				if (!hasFocus){
					checkPasswdCfValid(false);
				}
				if(currentRegisterType == TELEPHONE_REGISTER){
					errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+phoneTipStr+secTipStr);
				}else if(currentRegisterType == EMAIL_REGISTER){
					errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+mailboxTipStr);
				}else{}
			}
		});
		confirmPassWordEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(isClickBack){
					return;
				}
				checkPasswdCfValid(hasFocus);
				if(currentRegisterType == TELEPHONE_REGISTER){
					errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+phoneTipStr+secTipStr);
				}else if(currentRegisterType == EMAIL_REGISTER){
					errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+mailboxTipStr);
				}else{}
			}
		});
		telephoneNumEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(isClickBack){
					return;
				}
				checkMobileValid(hasFocus);
				errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+phoneTipStr+secTipStr);
			}
		});
		mailboxNumEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(isClickBack){
					return;
				}
				checkEmailValid(hasFocus);
				errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+mailboxTipStr);
			}
		});
		securityCodeEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (isClickBack) {
					return;
				}
				checkPincodeValid(hasFocus);
				errorTip.setText(usernameTipStr + passWordTipStr
						+ confirmTipStr + phoneTipStr + secTipStr);
			}
		});

		getSecurityCodeButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {

				boolean isValid = checkUserValid(false);
				isValid = checkPasswdValid(false) && isValid;
				isValid = checkPasswdCfValid(false) && isValid;
				isValid = checkMobileValid(false) && isValid;
//				isValid = checkPincodeValid(false) && isValid;

				errorTip.setText(usernameTipStr+passWordTipStr+confirmTipStr+phoneTipStr);
					if (isValid) {
						if(!isNetEnable()){
							return;
						}
						CustomTimer timer = new CustomTimer(30000, 1000);
						getSecurityCodeButton.setEnabled(false);
						long status = UserControl_ManagerIF.Instance()
								.ReqValidateCode(
										telephoneNumEdit.getText().toString());
						if (status != -1) {
							lockForClickListener();
							progressDialog = CProgressDialog
									.makeProgressDialog(ADT_Auth_Register.this,
											R.string.STR_MM_09_02_02_19);
							progressDialog.show();
						} else {

							CustomToast.showToast(ADT_Auth_Register.this,
									getString(R.string.STR_MM_09_01_01_10),
									Toast.LENGTH_SHORT);
						}
						timer.start();
					}
				}

			
		});
	}

	private void findViews() {
		telephoneLayoutInput = (LinearLayout) findViewById(R.id.adt_Auth_Register_Telephone_layout_input);
		telephoneLayoutAuthCode = (LinearLayout) findViewById(R.id.adt_Auth_Register_Telephone_layout_authcode);
		mailboxLayout = (LinearLayout) findViewById(R.id.adt_Auth_Register_Mailbox_layout);
		mailboxNoticeLayout = (LinearLayout) findViewById(R.id.adt_Auth_Register_Mailbox_notice_layout);
		userNameEdit = (EditText) findViewById(R.id.adt_Auth_Register_Username_edit_text);
		passWordEdit = (EditText) findViewById(R.id.adt_Auth_Register_Password_edit_text);
		confirmPassWordEdit = (EditText) findViewById(R.id.adt_Auth_Register_Confirm_Password_edit_text);
		telephoneNumEdit = (EditText) findViewById(R.id.adt_Auth_Register_Telephone_edit_text);
		mailboxNumEdit = (EditText) findViewById(R.id.adt_Auth_Register_Mailbox_edit_text);
		securityCodeEdit = (EditText) findViewById(R.id.adt_Auth_Register_Security_code_edit_text);
		getSecurityCodeButton =  (Button) findViewById(R.id.adt_Auth_Register_Get_Security_code_button);		
		userNameErrorTipText = (TextView) findViewById(R.id.adt_Auth_Register_Username_error_tip_text);
		passWordErrorTipText = (TextView) findViewById(R.id.adt_Auth_Register_Password_error_tip_text);
		confirmPassWordErrorTipText = (TextView) findViewById(R.id.adt_Auth_Register_Confirm_Password_error_tip_text);
		telephoneErrorTipText = (TextView) findViewById(R.id.adt_Auth_Register_Telephone_error_tip_text);
		mailboxErrorTipText = (TextView) findViewById(R.id.adt_Auth_Register_Mailbox_error_tip_text);
		securityCodeErrorTipText = (TextView) findViewById(R.id.adt_Auth_Register_Security_code_error_tip_text);
		errorTip = (TextView) findViewById(R.id.main_tip);
		errorTip.setVisibility(View.VISIBLE);
		userNameErrorTipText.setVisibility(View.INVISIBLE);
		passWordErrorTipText.setVisibility(View.INVISIBLE);
		confirmPassWordErrorTipText.setVisibility(View.INVISIBLE);
		telephoneErrorTipText.setVisibility(View.INVISIBLE);
		mailboxErrorTipText.setVisibility(View.INVISIBLE);
		securityCodeErrorTipText.setVisibility(View.INVISIBLE);
		if(!TextUtils.isEmpty(SystemInfo.GetPhoneNumber())){
			telephoneNumEdit.setText(SystemInfo.GetPhoneNumber());
		}
		userNameEdit.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				userNameEdit.setFocusable(true);
				userNameEdit.requestFocus();
			}
		});
	
	}
	
	private void initRegTypeSpinner(){
		registerTypeButton = (Button) findViewById(R.id.adt_Auth_Register_Type_Spinner);
		dialog = new CustomDialog(ADT_Auth_Register.this);
		dialog.setTitle(getString(R.string.STR_MM_09_02_02_37));
		final String[] entity = {getString(R.string.STR_MM_09_02_02_35),getString(R.string.STR_MM_09_02_02_36)};
		dialog.setSingleChoiceItems(entity, 0, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if(which == currentRegisterType){
						dialog.dismiss();
						return ;
					}
					if(which == 0){
						telephoneLayoutInput.setVisibility(View.VISIBLE);
						telephoneLayoutAuthCode.setVisibility(View.VISIBLE);
						mailboxLayout.setVisibility(View.GONE);
						mailboxNoticeLayout.setVisibility(View.GONE);
						currentRegisterType = TELEPHONE_REGISTER;
						registerTypeButton.setText(getString(R.string.STR_MM_09_02_02_35));
					}else if(which == 1){
						telephoneLayoutInput.setVisibility(View.GONE);
						telephoneLayoutAuthCode.setVisibility(View.GONE);
						mailboxLayout.setVisibility(View.VISIBLE);
						mailboxNoticeLayout.setVisibility(View.VISIBLE);
						currentRegisterType = EMAIL_REGISTER;
						registerTypeButton.setText(getString(R.string.STR_MM_09_02_02_36));
					}else{}
					errorTip.setText("");
					dialog.dismiss();
				}
		});
		dialog.setNegativeButton(R.string.STR_COM_001, null);
		
		registerTypeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.show();
			}
			
		});
	}
	
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		boolean bRet = false;
		
		int iTriggerID = triggerInfo.GetTriggerID();
		int iParams = (int) triggerInfo.GetlParam1();
		switch(iTriggerID)
		{
		case NSTriggerID.UIC_MN_TRG_UC_REQ_VALIDATECODE:// security code
			progressDialog.dismiss();
			if(0 == iParams){
				CustomToast.showToast(ADT_Auth_Register.this, getString(R.string.STR_MM_09_02_02_23), Toast.LENGTH_SHORT);
			}else if(1 == iParams){
				showErrorCode((int) triggerInfo.GetlParam2());
			}else if(2 == iParams){
				CustomToast.showToast(ADT_Auth_Register.this, getString(R.string.STR_MM_09_01_01_10), Toast.LENGTH_SHORT);
			}
			break;
		
		case NSTriggerID.UIC_MN_TRG_UC_REG_BY_PHONE:// register by phone
			if(0 == iParams){
				UserControl_ManagerIF.Instance().LoginByPhone(telephoneNumEdit.getText().toString(), passWordEdit.getText().toString(),SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_REGIST_LOGIN);

			}else if(1 == iParams){
				progressDialog.dismiss();
				showErrorCode((int) triggerInfo.GetlParam2());
				
			}else if(2 == iParams){
				progressDialog.dismiss();
				CustomToast.showToast(ADT_Auth_Register.this,getString(R.string.STR_MM_09_01_01_10), Toast.LENGTH_SHORT);
			}
			break;
		case NSTriggerID.UIC_MN_TRG_UC_REG_BY_EMAIL:// register by email
			if(0 == iParams){
				UserControl_ManagerIF.Instance().LoginByMail(mailboxNumEdit.getText().toString(), passWordEdit.getText().toString(),SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_REGIST_LOGIN);

			}else if(1 == iParams){
				progressDialog.dismiss();
				showErrorCode((int) triggerInfo.GetlParam2());
				
			}else if(2 == iParams){
				progressDialog.dismiss();
				CustomToast.showToast(ADT_Auth_Register.this,getString(R.string.STR_MM_09_01_01_10), Toast.LENGTH_SHORT);
			}
			break;
		case NSTriggerID.UIC_MN_TRG_UC_USER_LOGIN:
			progressDialog.dismiss();
			if(iParams==0){
				int[] lonlat = UIMapControlJNI.GetCenterLonLat();
//				SnsControl.Instance().RequestSnsDataRestartTimer(lonlat[0], lonlat[1]);
				UIC_AuthCommon.getInstance().leaveAuthWinscape();
				ReLogin_TriggerListener.addReLoginListener();
			}else{
				BackSearchWinChange(ADT_Auth_Login.class);
			}
			break;
		default:
			super.OnTrigger(triggerInfo);
			break;
		}
		return bRet;
	}

	private void showErrorCode(int error){
		switch (error) {
        case 100:
        	//	email has registered
        	errorTip.setText(R.string.STR_MM_09_02_02_40);
        	mailboxErrorTipText.setVisibility(View.VISIBLE);
        	break;
        case 101:
        	//	email is invalid
        	errorTip.setText(R.string.STR_MM_09_02_02_39);
        	mailboxErrorTipText.setVisibility(View.VISIBLE);
        	break;
        case 102:
        	//	telephone has registered
        	errorTip.setText(R.string.STR_MM_09_02_02_10);
        	telephoneErrorTipText.setVisibility(View.VISIBLE);
        	break;
        case 103:
        	//	telephone is invalid
        	errorTip.setText(R.string.STR_MM_09_02_02_11);
        	telephoneErrorTipText.setVisibility(View.VISIBLE);
        	break;
        case 104:
        	//  password is invalid
        	errorTip.setText(R.string.STR_MM_09_02_02_25);
        	passWordErrorTipText.setVisibility(View.VISIBLE);
        	break;
        case 105:
        	//security code is invalid
        	errorTip.setText(R.string.STR_MM_09_02_02_14);
        	securityCodeErrorTipText.setVisibility(View.VISIBLE);
        	break;
        case 109:
        	//  nick name is invalid
        	errorTip.setText(R.string.STR_MM_09_02_02_03);
        	userNameErrorTipText.setVisibility(View.VISIBLE);
        	break;
        case 115:
			//nick name has registered
        	errorTip.setText(R.string.STR_MM_09_02_02_03);
        	userNameErrorTipText.setVisibility(View.VISIBLE);
        	break;
		default:
			break;
		}
	}

	private class CustomTimer extends CountDownTimer {  
		  
        public CustomTimer(long millisInFuture, long countDownInterval) {  
            super(millisInFuture, countDownInterval);  
        }  
        @Override  
        public void onFinish() {  
        	getSecurityCodeButton.setText(R.string.STR_MM_09_02_02_12);  
        	getSecurityCodeButton.setEnabled(true);
        }  
  
        @Override  
        public void onTick(long millisUntilFinished) {  
        	getSecurityCodeButton.setText(getApplication().getString(R.string.STR_MM_09_02_02_22, (int)(millisUntilFinished/1000)));  
        }  
  
    }  
}
