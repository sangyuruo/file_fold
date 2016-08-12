package com.billionav.navi.naviscreen.auth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.ViewHelp;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.system.SystemInfo;
import com.billionav.navi.uicommon.UIC_AuthCommon;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.ui.R;


public class ADT_Auth_Login extends ActivityBase implements OnEditorActionListener{

	private EditText userNameEdit = null;
	private EditText passWordEdit = null;
	private TextView findPassWord = null;
	private Button goButton = null;
	private Button afterButton = null;
	private Button registerButton = null;
	private CProgressDialog progressDialog = null;
	private LinearLayout layout = null;
    private boolean isClickAfter = false;
    
    
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setNoTitle();
		setContentView(R.layout.adt_auth_login);		
		UIC_AuthCommon.getInstance().setEnterClass(MenuControlIF.Instance().GetHierarchyBelowWinscapeClass());

		setTitle(R.string.STR_MM_09_01_01_24);
		setDefaultBackground();
		findViews();
		setAboutLayout();
		setClickListener();
		
		UserControl_UserInfo[] userInfo = UserControl_ManagerIF.Instance().GetLoginInfoListFromFile();
		if(userInfo!=null&&userInfo.length>0&&userInfo[0]!=null){
			if(userInfo[0].m_iLoginType==UserControl_UserInfo.USER_LOGIN_TYPE_PHONE){
				userNameEdit.setText(userInfo[0].m_strPhoneNum);
				passWordEdit.setText(userInfo[0].m_strUserPassWord);
			}else if(userInfo[0].m_iLoginType==UserControl_UserInfo.USER_LOGIN_TYPE_EMAIL){
				userNameEdit.setText(userInfo[0].m_strEmail);
				passWordEdit.setText(userInfo[0].m_strUserPassWord);
			}else if(userInfo[0].m_iLoginType==UserControl_UserInfo.USER_LOGIN_TYPE_NANE){
				userNameEdit.setText(userInfo[0].m_strNickName);
				passWordEdit.setText(userInfo[0].m_strUserPassWord);
			}else{
				userNameEdit.setText("");
				passWordEdit.setText("");
				goButton.setEnabled(false);
			}
			
		}else{
			userNameEdit.setText("");
			passWordEdit.setText("");
			goButton.setEnabled(false);
		}
		
		setfindPasswordButtonEnable(true);
//		setregisterButtonEnable(false);
		monitorKeyLogin();
		
		UserControl_ManagerIF.Instance().setCustomTimerListener(new UserControl_ManagerIF.onCountingListener() {
			@Override
			public void onTick(int countDown) {
				setfindPasswordButtonEnable(false);
				findPassWord.setText(getApplication().getString(R.string.STR_MM_09_01_01_11, countDown));  
			}
			
			@Override
			public void onFinish() {
				setfindPasswordButtonEnable(true);
				findPassWord.setText(R.string.STR_MM_09_01_01_35);
			}
		});
		if(UserControl_ManagerIF.Instance().isfindpassworddelaying()){
			setfindPasswordButtonEnable(false);
			findPassWord.setText(getApplication().getString(R.string.STR_MM_09_01_01_11, UserControl_ManagerIF.Instance().getCurCount()));
		}
	}
	
	private void setregisterButtonEnable(boolean isEnable) {
		registerButton.setEnabled(isEnable);
		if(isEnable){
			registerButton.setTextColor(0XFF47ABD9);
		}else{
			registerButton.setTextColor(0XFFAAAAAA);
		}
	}

	private void setfindPasswordButtonEnable(boolean isEnable){
		findPassWord.setEnabled(isEnable);
		if(isEnable){
			findPassWord.setTextColor(0XFF47ABD9);
		}else{
			findPassWord.setTextColor(0XFFAAAAAA);
		}
	}
	
	private void monitorKeyLogin() {
		passWordEdit.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() ==  KeyEvent.ACTION_DOWN){
					if (isAllNumber(userNameEdit.getText().toString().trim())) {
						loginByTelephoneNumber();
					} else {
						loginByEmail();
					}
					return true;
				}
				return false;
			}
		});
		
		userNameEdit.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) && event.getAction() ==  KeyEvent.ACTION_DOWN){
					passWordEdit.setSelection(passWordEdit.getText().toString().length());
					passWordEditshowInputSoftKeyBoard();
					return true;
				}
				return false;
			}
		});
	}
	private Handler passWordEditshowInputSoftKeyBoardHandler = null;
	private void passWordEditshowInputSoftKeyBoard(){
		if(passWordEditshowInputSoftKeyBoardHandler == null){
			passWordEditshowInputSoftKeyBoardHandler =new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					ViewHelp.showInputSoftKeyBoard(passWordEdit);
				}
				
			};
		}
		passWordEditshowInputSoftKeyBoardHandler.sendMessageDelayed(new Message(), 500);
	}	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		setAboutLayout();
		
		
	}

	private void setAboutLayout() {
		if(ScreenMeasure.isPortrait()){
			layout.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams l0 = (LayoutParams) layout.getChildAt(0).getLayoutParams();
			LinearLayout.LayoutParams l1 = (LayoutParams) layout.getChildAt(1).getLayoutParams();
			l0.weight = 2.5f;
			l1.weight = 1;
			layout.setBackgroundResource(R.drawable.navicloud_and_517a);
			
		}else{
			layout.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams l0 = (LayoutParams) layout.getChildAt(0).getLayoutParams();
			LinearLayout.LayoutParams l1 = (LayoutParams) layout.getChildAt(1).getLayoutParams();
			l0.weight = 1.1f;
			l1.weight = 1;
			layout.setBackgroundResource(R.drawable.navicloud_and_517b);
		}
	}

	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isClickAfter = true;
//			jniPointControl_new.Instance().NotifyUserLogstatus("",
//					jniPointControl_new.PNT_USER_LOG_STATUS_LOGOUT);
			UIC_AuthCommon.getInstance().leaveAuthWinscape();
			return true;
		}
		return super.OnKeyDown(keyCode, event);
	}

	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
		isClickAfter =false;
		NSViewManager.GetViewManager().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
		ViewHelp.closeInputKeyBoard(userNameEdit,passWordEdit);
		NSViewManager.GetViewManager().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	private void hideSoftInputFromWindow(IBinder windowToken){
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(windowToken, 0);
	}
	
	private void hideSoftInputFromWindow(){
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

	private boolean checkUserNameValid(boolean hasFoucs) {
		if (hasFoucs) {
			return false;
		}
		String username = userNameEdit.getText().toString().trim();
		if ("".equals(username) || null == username) {
			SpannableStringBuilder ssbuilder = setErrorTextColor(R.string.STR_MM_09_02_02_04);
			userNameEdit.setError(ssbuilder);
			return false;
		}
		if (isAllNumber(username)) {
			boolean isvalid = UserControl_ManagerIF.Instance().IsValidPhoneNum(
					username);
			if (!isvalid) {
				SpannableStringBuilder ssbuilder = setErrorTextColor(R.string.STR_MM_09_01_01_22);
				userNameEdit.setError(ssbuilder);
				return false;
			}
		} else {
			boolean isvalid = UserControl_ManagerIF.Instance().IsValidEmail(
					username);
//			if (!isvalid && username.contains("@")) {
			if (!isvalid) {
				SpannableStringBuilder ssbuilder = setErrorTextColor(R.string.STR_MM_09_01_01_26);
				userNameEdit.setError(ssbuilder);
				return false;
			}
		}
		return true;
	}

	private SpannableStringBuilder setErrorTextColor(int id) {
		String estring = getString(id);
		estring = estring + "      ";
		int ecolor = 0XFFFF0000; // whatever color you want			
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
		ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
		return ssbuilder;
	}

	private boolean checkPassWordValid(boolean hasFoucs) {
		if (hasFoucs) {
			return false;
		}
		String passWd = passWordEdit.getText().toString();
		if ("".equals(passWd)) {
			return false;
		}
		int ret = UserControl_ManagerIF.Instance().IsValidPassword(passWd);
		if (ret == UserControl_CommonVar.VALIDATE_INPUT_OK) {
			return true;
		} else if (ret == UserControl_CommonVar.VAILDATE_INPUT_TOO_SHORT) {
			SpannableStringBuilder ssbuilder = setErrorTextColor(R.string.STR_MM_09_01_01_32);
			passWordEdit.setError(ssbuilder);
			//you need close keyboard if screen is portrait status when user name or password is invalid. 
			if(!ScreenMeasure.isPortrait()){
				hideSoftInputFromWindow(passWordEdit.getWindowToken());
			}
			return false;
		} else if (ret == UserControl_CommonVar.VAILDATE_INPUT_TOO_LONG) {
			SpannableStringBuilder ssbuilder = setErrorTextColor(R.string.STR_MM_09_01_01_33);
			passWordEdit.setError(ssbuilder);
			//you need close keyboard if screen is portrait status when user name or password is invalid. 
			if(!ScreenMeasure.isPortrait()){
				hideSoftInputFromWindow(passWordEdit.getWindowToken());
			}
			return false;
		} else {
			// format error
			SpannableStringBuilder ssbuilder = setErrorTextColor(R.string.STR_MM_09_01_01_34);
			passWordEdit.setError(ssbuilder);
			//you need close keyboard if screen is portrait status when user name or password is invalid. 
			if(!ScreenMeasure.isPortrait()){
				hideSoftInputFromWindow(passWordEdit.getWindowToken());
			}
			return false;
		}

	}
	private void setClickListener() {
		userNameEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(isClickAfter){
					return;
				}
				checkUserNameValid(hasFocus);
			}
		});
		passWordEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(isClickAfter){
					return;
				}
				checkPassWordValid(hasFocus);
			}
		});
		findPassWord.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
                if(!checkUserNameValid(false)){
                	return;
                }
                if(!isNetEnable()){
    				return;
    			}
                hideSoftInputFromWindow();
                showCheckingProgressCancelDialog();
			}

		});

		afterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    isClickAfter = true;
//			    jniPointControl_new.Instance().NotifyUserLogstatus("", jniPointControl_new.PNT_USER_LOG_STATUS_LOGOUT);
			    UIC_AuthCommon.getInstance().leaveAuthWinscape();
			}
		});
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 isClickAfter = true;
				ForwardWinChange(ADT_Auth_Agreement.class);
			}
		});
		goButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					String userName = userNameEdit.getText().toString().trim();
					if (isAllNumber(userName)) {
						loginByTelephoneNumber();
					} else if(UserControl_ManagerIF.Instance().IsValidEmail(userName)){
						loginByEmail();
					} else {
//						loginByNickName();
						//else :show error message
						checkUserNameValid(false);
					}

			}
			
		});
		userNameEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if((!TextUtils.isEmpty(s))&&(!TextUtils.isEmpty(passWordEdit.getText().toString()))){
					goButton.setEnabled(true);
				}else {
					goButton.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		passWordEdit.setOnEditorActionListener(this);
		passWordEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if((!TextUtils.isEmpty(s))&&(!TextUtils.isEmpty(userNameEdit.getText().toString().trim()))){
					goButton.setEnabled(true);
				}else {
					goButton.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	private void loginByTelephoneNumber() {

		boolean returnvalue = checkUserNameValid(false);
		returnvalue = checkPassWordValid(false)&&returnvalue;
		if(returnvalue){
			if(!isNetEnable()){
				return;
			}
			long isSend = UserControl_ManagerIF.Instance().LoginByPhone(userNameEdit.getText().toString().trim(), passWordEdit.getText().toString(),SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_UI);
			if(isSend == -1){
				CustomToast.showToast(ADT_Auth_Login.this, getString(R.string.STR_MM_09_01_01_10), Toast.LENGTH_SHORT);
			}else{
				lockForClickListener();
			    progressDialog = CProgressDialog.makeProgressDialog(this, R.string.STR_MM_09_01_01_14);
			    progressDialog.show();
			}	
		}
	}
	private void loginByEmail() {
		boolean returnvalue = checkUserNameValid(false);
		returnvalue = checkPassWordValid(false)&&returnvalue;
		if(returnvalue){
			if(!isNetEnable()){
				return;
			}
    		long isSend = UserControl_ManagerIF.Instance().LoginByMail(userNameEdit.getText().toString().trim(), passWordEdit.getText().toString(),SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_UI);
			if(isSend == -1 ){
				CustomToast.showToast(ADT_Auth_Login.this, getString(R.string.STR_MM_09_01_01_10), Toast.LENGTH_SHORT);
			}else{
				lockForClickListener();
				progressDialog = CProgressDialog.makeProgressDialog(this, R.string.STR_MM_09_01_01_14);
				progressDialog.show();
			}
		}
	}

	private void loginByNickName() {
		boolean ret = checkUserNameValid(false);
		ret = checkPassWordValid(false) && ret;
		if(ret) {
			if(!isNetEnable()){
				return;
			}
			long isSend = UserControl_ManagerIF.Instance().LoginByName(userNameEdit.getText().toString().trim(), passWordEdit.getText().toString(),SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_UI);
			if(isSend == -1 ){
				CustomToast.showToast(ADT_Auth_Login.this, getString(R.string.STR_MM_09_01_01_10), Toast.LENGTH_SHORT);
			}else{
				lockForClickListener();
				progressDialog = CProgressDialog.makeProgressDialog(this, R.string.STR_MM_09_01_01_14);
				progressDialog.show();
			}
		}
	}
	private void findViews() {
		userNameEdit = (EditText) findViewById(R.id.adt_Auth_Login_Username_edit_text);
		passWordEdit = (EditText) findViewById(R.id.adt_Auth_Login_Password_edit_text);
		findPassWord = (TextView) findViewById(R.id.adt_Auth_Login_Find_Password_text);
		goButton = (Button) findViewById(R.id.adt_Auth_Login_Go);
		afterButton = (Button) findViewById(R.id.adt_Auth_Login_after_new);
		registerButton = (Button) findViewById(R.id.adt_Auth_Login_reg_new);

		layout = (LinearLayout) findViewById(R.id.layout_111);

		userNameEdit.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				userNameEdit.setFocusable(true);
				userNameEdit.requestFocus();
			}
		});
	}

	private boolean isAllNumber(String str) {
		return str.matches("\\d*");
	}
	
	private boolean isEmail(String str) {
		if(null == str) {
			return false;
		}
		return str.matches("\\w@\\w.\\w");
	}

	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		//TODO Auto-generated method stub
		
		int iTriggerID = triggerInfo.GetTriggerID();
		switch(iTriggerID)
		{
		case NSTriggerID.UIC_MN_TRG_UC_USER_LOGIN:
			progressDialog.dismiss();
			int iParams = (int) triggerInfo.GetlParam1();
			Log.d("test","trigger UIC_MN_TRG_UC_USER_LOGIN iParam="+iParams+" error code ="+(int) triggerInfo.GetlParam2());
			if (0 == iParams) {
				int[] lonlat = UIMapControlJNI.GetCenterLonLat();
//				SnsControl.Instance().RequestSnsDataRestartTimer(lonlat[0],
//						lonlat[1]);
				UIC_AuthCommon.getInstance().leaveAuthWinscape();
				ReLogin_TriggerListener.addReLoginListener();
				SharedPreferenceData.OPEN_AUTO_LOGIN.setValue(true);
			} else if (1 == iParams) {
				SharedPreferenceData.OPEN_AUTO_LOGIN.setValue(false);
				showErrorCode((int) triggerInfo.GetlParam2());
			} else if (2 == iParams) {
				SharedPreferenceData.OPEN_AUTO_LOGIN.setValue(false);
				CustomToast.showToast(ADT_Auth_Login.this,
						getString(R.string.STR_MM_09_02_02_17),
						Toast.LENGTH_LONG);
			}
			return true;
		case NSTriggerID.UIC_MN_TRG_UC_GET_PASS_BY_PHONE:
			progressDialog.dismiss();

			if (0 == (int) triggerInfo.GetlParam1()) {
				CustomToast
						.showToast(ADT_Auth_Login.this,
								getString(R.string.MSG_09_01_01_01),
								Toast.LENGTH_SHORT);
			} else if (1 == (int) triggerInfo.GetlParam1()) {
				CustomToast.showToast(ADT_Auth_Login.this,
						getString(R.string.STR_MM_09_01_01_18),
						Toast.LENGTH_SHORT);
			} else if (2 == (int) triggerInfo.GetlParam1()) {
				CustomToast.showToast(ADT_Auth_Login.this,
						getString(R.string.STR_MM_09_01_01_10),
						Toast.LENGTH_SHORT);
			}
			return true;
		case NSTriggerID.UIC_MN_TRG_UC_GET_PASS_BY_EMAIL:
			progressDialog.dismiss();
			if(0 == (int) triggerInfo.GetlParam1()){   
				CustomToast.showToast(ADT_Auth_Login.this, getString(R.string.MSG_09_01_01_02), Toast.LENGTH_LONG);
			}else if(1 == (int) triggerInfo.GetlParam1()){
				CustomToast.showToast(ADT_Auth_Login.this, getString(R.string.STR_MM_09_01_01_18), Toast.LENGTH_SHORT);
			}else if(2 == (int) triggerInfo.GetlParam1()){
				CustomToast.showToast(ADT_Auth_Login.this, getString(R.string.STR_MM_09_01_01_10), Toast.LENGTH_SHORT);
			}
			return true;
		case NSTriggerID.UIC_MN_TRG_UC_GET_VERIFICATION_CODE:
			int iParamsCode = (int) triggerInfo.GetlParam1();
			if(0 == iParamsCode){		
				String name = userNameEdit.getText().toString();
				if(UserControl_ManagerIF.Instance().IsValidEmail(name)){
					CustomToast.showToast(ADT_Auth_Login.this,getString(R.string.MSG_09_01_01_02), Toast.LENGTH_SHORT);
				}else if(UserControl_ManagerIF.Instance().IsValidPhoneNum(name)){
					CustomToast.showToast(ADT_Auth_Login.this,getString(R.string.MSG_09_01_01_01), Toast.LENGTH_SHORT);
					BundleNavi.getInstance().putString("loginname", name);
					ForwardWinChange(ADT_Auth_ForgetPassword.class);
				}
				//if receive verification code ,then set findpassword button enable is false
				setfindPasswordButtonEnable(false);
				if(!UserControl_ManagerIF.Instance().isfindpassworddelaying()){
					UserControl_ManagerIF.Instance().startCustomTimer();
				}
			}else if(1 == iParamsCode){
				showErrorCode((int) triggerInfo.GetlParam2());
				
			}else if(2 == iParamsCode){
				CustomToast.showToast(ADT_Auth_Login.this,getString(R.string.STR_MM_09_01_01_10), Toast.LENGTH_SHORT);
			}
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			return true;
		default:
			return super.OnTrigger(triggerInfo);
		}
	}
	private void showErrorCode(int error){
		SpannableStringBuilder ssbuilder;
		switch(error){
		case 107:
			//password error
			 ssbuilder = setErrorTextColor(R.string.STR_MM_09_01_01_31);
			passWordEdit.setError(ssbuilder);
			return ;
		case 104:
			//password error
			 ssbuilder = setErrorTextColor(R.string.STR_MM_09_01_01_31);
			passWordEdit.setError(ssbuilder);
			return ;
		case 108:
			 //user name does not exist
			 ssbuilder = setErrorTextColor(R.string.STR_MM_09_01_01_29);
			userNameEdit.setError(ssbuilder);
			return ;
		case 116:
			//user name does not exist
			 ssbuilder = setErrorTextColor(R.string.STR_MM_09_01_01_29);
			userNameEdit.setError(ssbuilder);
			return ;
			default:
				return ;
		}
	}


	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if(actionId == EditorInfo.IME_ACTION_GO) {
			if (isAllNumber(userNameEdit.getText().toString().trim())) {
				loginByTelephoneNumber();
			} else {
				loginByEmail();
			}
			return true;
		}
		return false;
	}
	
	private void showCheckingProgressCancelDialog(){

		CustomDialog b = new CustomDialog(
				ADT_Auth_Login.this);
		b.setMessage(R.string.STR_MM_09_01_01_37);
		b.setTitle(R.string.STR_MM_09_01_03_01);
		b.setNegativeButton(R.string.STR_COM_001, null);
		
		b.setPositiveButton(R.string.STR_MM_09_01_01_36,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(!isNetEnable()){
							return;
						}
						UserControl_UserInfo userInfo = new UserControl_UserInfo();
						String name = userNameEdit.getText().toString();
						long ret = UserControl_ManagerIF.Instance()
								.getVerificationCode(userInfo, name);
						if (ret != -1) {
							progressDialog = CProgressDialog
									.makeProgressDialog(ADT_Auth_Login.this,
											R.string.STR_MM_09_02_02_19);
							progressDialog.show();
						} else {
							CustomToast.showToast(ADT_Auth_Login.this,
									getString(R.string.STR_MM_09_02_02_24),
									Toast.LENGTH_SHORT);
						}
					}
				});
		b.show();		
				
	}
	@Override
	protected void OnDestroy() {
		UserControl_ManagerIF.Instance().destroyCustomTimerListener();
		super.OnDestroy();
	}
	
}
