package com.billionav.navi.naviscreen.auth;



import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.ui.R;

public class ADT_Auth_ForgetPassword extends ActivityBase {

	private EditText passWordEdit = null;
	private EditText confirmPassWordEdit = null;
	private EditText securityCodeEdit = null;
	private TextView passWordErrorTipText = null;
	private TextView confirmPassWordErrorTipText = null;
	private TextView securityCodeErrorTipText = null;
	private CProgressDialog progressDialog = null;
	private TextView errorTip = null;
	private String passWordTipStr = "";
	private String confirmTipStr = "";
	private String secTipStr = "";
	private boolean isClickBack = false;
	
	private String loginName;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_auth_forgetpassword);		
		setTitle(R.string.STR_MM_06_02_04_01);
		setDefaultBackground();
		
		loginName = BundleNavi.getInstance().getString("loginname");
		
		findViews();
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
		addActionItem3(R.string.STR_MM_09_01_03_04, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String passWord = passWordEdit.getText().toString();
				String securityCode = securityCodeEdit.getText().toString();
				boolean returnvalue = true;
				returnvalue = checkPasswdValid(false) && returnvalue;
				returnvalue = checkPasswdCfValid(false) && returnvalue;
				returnvalue = checkPincodeValid(false) && returnvalue;

				if(!isNetEnable()){
					return;
				}
				if (returnvalue) {
					UserControl_UserInfo userInfo = new UserControl_UserInfo();
					userInfo.m_strUserPassWord = passWord;
					
					long ret = UserControl_ManagerIF.Instance()
							.setNewPasswordByCode(userInfo, loginName, passWord, securityCode);
					if (ret != -1) {
						lockForClickListener();
						progressDialog = CProgressDialog
								.makeProgressDialog(ADT_Auth_ForgetPassword.this,
										R.string.STR_MM_09_02_02_21);
						progressDialog.show();
					} else {
						CustomToast.showToast(ADT_Auth_ForgetPassword.this,
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
		NSViewManager.GetViewManager().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

			secTipStr = getString(R.string.STR_MM_09_02_02_14);
			securityCodeErrorTipText.setVisibility(View.VISIBLE);
			return false;
		}

	}	

	private void setClickListener() {
		
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
				errorTip.setText(passWordTipStr+confirmTipStr+secTipStr);
				
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
				errorTip.setText(passWordTipStr+confirmTipStr+secTipStr);
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
				errorTip.setText( passWordTipStr
						+ confirmTipStr +  secTipStr);
			}
		});

	}

	private void findViews() {
		passWordEdit = (EditText) findViewById(R.id.adt_Auth_Register_Password_edit_text);
		confirmPassWordEdit = (EditText) findViewById(R.id.adt_Auth_Register_Confirm_Password_edit_text);
		securityCodeEdit = (EditText) findViewById(R.id.adt_Auth_Register_Security_code_edit_text);
		passWordErrorTipText = (TextView) findViewById(R.id.adt_Auth_Register_Password_error_tip_text);
		confirmPassWordErrorTipText = (TextView) findViewById(R.id.adt_Auth_Register_Confirm_Password_error_tip_text);
		securityCodeErrorTipText = (TextView) findViewById(R.id.adt_Auth_Register_Security_code_error_tip_text);
		errorTip = (TextView) findViewById(R.id.main_tip);
		errorTip.setVisibility(View.VISIBLE);
		passWordErrorTipText.setVisibility(View.INVISIBLE);
		confirmPassWordErrorTipText.setVisibility(View.INVISIBLE);
		securityCodeErrorTipText.setVisibility(View.INVISIBLE);
	
	}
	
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		boolean bRet = false;
		
		int iTriggerID = triggerInfo.GetTriggerID();
		int iParams = (int) triggerInfo.GetlParam1();
		switch(iTriggerID)
		{
		
		case NSTriggerID.UIC_MN_TRG_UC_SET_NEW_PASSWORD:
			if(0 == iParams){		
				CustomToast.showToast(ADT_Auth_ForgetPassword.this,getString(R.string.STR_MM_06_02_04_07), Toast.LENGTH_SHORT);
				BackSearchWinChange(ADT_Auth_Login.class);
//				UserControl_ManagerIF.Instance().LoginByPhone(telephoneNumEdit.getText().toString(), passWordEdit.getText().toString(),SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_REGIST_LOGIN);
			}else if(1 == iParams){
				showErrorCode((int) triggerInfo.GetlParam2());
			}else if(2 == iParams){
				CustomToast.showToast(ADT_Auth_ForgetPassword.this,getString(R.string.STR_MM_09_01_01_10), Toast.LENGTH_SHORT);
			}
			if(progressDialog != null){
				progressDialog.dismiss();
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
		default:
			break;
		}
	}

}
