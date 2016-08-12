package com.billionav.navi.component.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.billionav.ui.R;

public class CProgressDialog extends Dialog{
	private final CustomDialog dialog;
	private boolean cancelable;
	private CharSequence text;
	private TextView textView;
	
	
	public CProgressDialog(Context context) {
		super(context);
		dialog = new CustomDialog(context);
		dialog.addView(createView(context));
		
	}
	@Override
	public void setOnKeyListener(OnKeyListener onKeyListener) {
		// TODO Auto-generated method stub
		dialog.setOnKeyListener(onKeyListener);
	}
	public View createView(Context c) {
		LayoutInflater inflater = LayoutInflater.from(c);
		View v = inflater.inflate(R.layout.dialog_progress_view, null);
		textView = (TextView) v.findViewById(R.id.text1);
		textView.setText(text);
		return v;
	}
	public static CProgressDialog makeProgressDialog(Context c, int textId) {
		CProgressDialog dialog = new CProgressDialog(c);
		dialog.setText(textId);
		return dialog;
	}
	
	public static CProgressDialog makeProgressDialog(Context c, String text) {
		CProgressDialog dialog = new CProgressDialog(c);
		dialog.setText(text);
		return dialog;
	}
	public void setText(int resId) {
		this.text = dialog.getContext().getText(resId);
		
		if(textView != null) {
			textView.setText(resId);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}
	public void setText(CharSequence text){
		this.text = text;
		
		if(textView != null) {
			textView.setText(text);
		}
	}
	@Override
	public void setCancelable(boolean flag) {
		this.cancelable = flag;
		super.setCancelable(flag);
		dialog.setCancelable(flag);
	}
	@Override
	public void setOnCancelListener(OnCancelListener listener) {
		dialog.setOnCancelListener(listener);
	}
	@Override
	public void cancel() {
		if(cancelable){
			dialog.cancel();
		}
	}
	
	@Override
	public void dismiss() {
		dialog.dismiss();
	}

	@Override
	public boolean isShowing() {
		return dialog.isShowing();
	}

	@Override
	public void show() {
		dialog.show();
		

	}
	
	public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
		dialog.setOnDismissListener(dismissListener);
	}

    public void setNegativeButton(int textId, final OnClickListener listener) {
    	dialog.setNegativeButton(textId, listener);
    }
    
    public void setNegativeButton(CharSequence text, final OnClickListener listener) {
    	dialog.setNegativeButton(text, listener);
    }


}
