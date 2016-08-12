package com.billionav.navi.naviscreen.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.billionav.navi.component.ViewHelp;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.ui.R;

public class ADT_Auth_ChangePassword extends ActivityBase{
	private EditText oldPassWordEdit = null;
	private EditText newPassWordEdit = null;
	private EditText confirmPassWordEdit = null;
	private TextView oldPassWordErrorTipText = null;
	private TextView newPassWordErrorTipText = null;
	private TextView confirmPassWordErrorTipText = null;
	private TextView errorTip = null;
	private String oldPassWordTipStr = "";
	private String newPassWordTipStr = "";
	private String confirmTipStr = "";
	private boolean isCickBack = false;
	private CProgressDialog progressDialog = null;

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_auth_changepassword);
		setTitle(R.string.STR_MM_06_02_04_01);
		setDefaultBackground();
		findViews();
		addActionItemsClick();
		setClickEvent();
	}

	private void findViews() {
		oldPassWordEdit = (EditText) findViewById(R.id.adt_Auth_Change_old_edit_text);
		newPassWordEdit = (EditText) findViewById(R.id.adt_Auth_Change_new_edit_text);
		confirmPassWordEdit = (EditText) findViewById(R.id.adt_Auth_Change_confirm_edit_text);
		
		oldPassWordErrorTipText = (TextView) findViewById(R.id.adt_Auth_Change_old_error_tip_text);
		newPassWordErrorTipText = (TextView) findViewById(R.id.adt_Auth_Change_new_error_tip_text);
		confirmPassWordErrorTipText = (TextView) findViewById(R.id.adt_Auth_Change_confirm_error_tip_text);
		errorTip = (TextView) findViewById(R.id.change_main_tip);
		oldPassWordErrorTipText.setVisibility(View.INVISIBLE);
		newPassWordErrorTipText.setVisibility(View.INVISIBLE);
		confirmPassWordErrorTipText.setVisibility(View.INVISIBLE);
		oldPassWordEdit.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				oldPassWordEdit.setFocusable(true);
				oldPassWordEdit.requestFocus();
			}
		});
	}

	private void addActionItemsClick() {
		// add cancel button
		addActionItem3(R.string.STR_COM_001, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isCickBack = true;
				BackWinChange();
			}
		});
		//add confirm button
		addActionItem3(R.string.STR_MM_09_01_01_36, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				boolean return_value = CheckOldPasswdValid(false);
				return_value = CheckNewPasswdValid(false) && return_value;
				return_value = CheckPasswdCfValid(false) && return_value;
				errorTip.setText(oldPassWordTipStr + newPassWordTipStr
						+ confirmTipStr);
				if (return_value) {
					if (!isNetEnable()) {
						return;
					}
					String oldpassWord = oldPassWordEdit.getText().toString();
					String newpassWord = newPassWordEdit.getText().toString();
					boolean ret = UserControl_ManagerIF.Instance()
							.UpdatePasswd(oldpassWord, newpassWord);
					if (!ret) {
						CustomToast.showToast(ADT_Auth_ChangePassword.this,
								getString(R.string.STR_MM_06_02_04_08),
								Toast.LENGTH_SHORT);
					} else {
						lockForClickListener();
						progressDialog = CProgressDialog.makeProgressDialog(
								ADT_Auth_ChangePassword.this,
								R.string.STR_MM_06_02_04_09);
						progressDialog.show();
					}
				}
				
			}
		});
	}

	private void setClickEvent() {
		oldPassWordEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (isCickBack) {
					return;
				}
				CheckOldPasswdValid(hasFocus);
				errorTip.setText(oldPassWordTipStr + newPassWordTipStr
						+ confirmTipStr);
			}
		});
		newPassWordEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (isCickBack) {
					return;
				}
				CheckNewPasswdValid(hasFocus);
				errorTip.setText(oldPassWordTipStr + newPassWordTipStr
						+ confirmTipStr);
			}
		});
		confirmPassWordEdit
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (isCickBack) {
							return;
						}
						CheckPasswdCfValid(hasFocus);
						errorTip.setText(oldPassWordTipStr + newPassWordTipStr
								+ confirmTipStr);
					}
				});
	}

	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isCickBack = true;
			BackWinChange();
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
		ViewHelp.closeInputKeyBoard(oldPassWordEdit,newPassWordEdit,confirmPassWordEdit);
		NSViewManager.GetViewManager().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		//TODO Auto-generated method stub
		
		int iTriggerID = triggerInfo.GetTriggerID();
		switch (iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_UC_CHANGE_PASS:
			progressDialog.dismiss();
			int iParams = (int) triggerInfo.GetlParam1();
			if (0 == iParams) {
				isCickBack = true;
				CustomToast.showToast(ADT_Auth_ChangePassword.this,
						getString(R.string.STR_MM_06_02_04_07),
						Toast.LENGTH_SHORT);
				BackWinChange();
			} else {
				CustomToast.showToast(ADT_Auth_ChangePassword.this,
						getString(R.string.STR_MM_06_02_04_08),
						Toast.LENGTH_SHORT);
			}
			return true;
		default:
			return super.OnTrigger(triggerInfo);
		}
	}
	private boolean CheckOldPasswdValid(boolean act) {
		if (act) {
			oldPassWordTipStr = "";
			oldPassWordErrorTipText.setVisibility(View.INVISIBLE);
			return false;
		}
		String inputPassWd = oldPassWordEdit.getText().toString();
		String localPassWd = UserControl_ManagerIF.Instance()
				.GetLogInUserInfo().m_strUserPassWord;

		boolean ret = localPassWd.equals(inputPassWd);
		if (ret) {
			oldPassWordTipStr = "";
			oldPassWordErrorTipText.setVisibility(View.INVISIBLE);
		} else {
			oldPassWordTipStr = getString(R.string.STR_MM_06_02_04_04);
			oldPassWordErrorTipText.setVisibility(View.VISIBLE);
			return false;
		}
		return true;
	}
	
	private boolean CheckNewPasswdValid(boolean act) {
		if (act) {
			newPassWordTipStr = "";
			newPassWordErrorTipText.setVisibility(View.INVISIBLE);
			return false;
		}
		String passWd = newPassWordEdit.getText().toString();
		int ret = UserControl_ManagerIF.Instance().IsValidPassword(passWd);
		if (ret == UserControl_CommonVar.VALIDATE_INPUT_OK) {
			newPassWordTipStr = "";
			newPassWordErrorTipText.setVisibility(View.INVISIBLE);
		} else if (ret == UserControl_CommonVar.VAILDATE_INPUT_TOO_SHORT) {
			newPassWordTipStr = getString(R.string.STR_MM_09_02_02_29);
			newPassWordErrorTipText.setVisibility(View.VISIBLE);
			return false;
		} else if (ret == UserControl_CommonVar.VAILDATE_INPUT_TOO_LONG) {
			newPassWordTipStr = getString(R.string.STR_MM_09_02_02_30);
			newPassWordErrorTipText.setVisibility(View.VISIBLE);
			return false;
		} else if (ret == UserControl_CommonVar.VAILDATE_INPUT_FORMAT_ERROR) {

			newPassWordTipStr = getString(R.string.STR_MM_09_02_02_32);
			newPassWordErrorTipText.setVisibility(View.VISIBLE);
			return false;
		}
		return true;
	}	
	
	private boolean CheckPasswdCfValid(boolean act) {
		if (act) {
			confirmTipStr = "";
			confirmPassWordErrorTipText.setVisibility(View.INVISIBLE);
			return false;
		}
		String passCf = confirmPassWordEdit.getText().toString();
		boolean isSame = passCf.equals(newPassWordEdit.getText().toString());
		if (isSame) {

			confirmTipStr = "";
			confirmPassWordErrorTipText.setVisibility(View.INVISIBLE);
		} else {
			confirmTipStr = getString(R.string.STR_MM_09_02_02_08);
			confirmPassWordErrorTipText.setVisibility(View.VISIBLE);
			return false;
		}
		return true;
	}	
}
