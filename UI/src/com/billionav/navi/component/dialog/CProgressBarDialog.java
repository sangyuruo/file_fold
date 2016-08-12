package com.billionav.navi.component.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.billionav.ui.R;

public class CProgressBarDialog implements IDialog{
	private final CustomDialog dialog;
	private ProgressBar currentProgress;
	private TextView textView;
	private CProgressBarDialog(Context context) {
		dialog = new CustomDialog(context);
		dialog.addView(createView(context));
		
	}
	private View createView(Context c) {
		LayoutInflater inflater = LayoutInflater.from(c);
		View v = inflater.inflate(R.layout.dialog_progress_bar, null);
		currentProgress = (ProgressBar) v.findViewById(R.id.progress_dialog_progress);
		textView = (TextView)v.findViewById(R.id.progress_dialog_text);
		return v;
	}
	public static CProgressBarDialog makeProgressDialog(Context c, int textId) {
		CProgressBarDialog dialog = new CProgressBarDialog(c);
		dialog.setText(textId);
		return dialog;
	}
	
	public static CProgressBarDialog makeProgressDialog(Context c, String text) {
		CProgressBarDialog dialog = new CProgressBarDialog(c);
		dialog.setText(text);
		return dialog;
	}
	
	public void setText(int resId) {
		CharSequence text = dialog.getContext().getText(resId);
		setText(text);
	}
		
	public void setText(CharSequence text){
		
		if(textView != null) {
			textView.setText(text);
		}
	}
	public void setProgress(int progress){
		if(progress > 100){
			progress = 100;
		}else if(progress < 0){
			progress = 0;
		}
		currentProgress.setProgress(progress);
	}
	@Override
	public void cancel() {
		dialog.cancel();
	}
	public void setCancelable(boolean cancelable){
		dialog.setCancelable(cancelable);
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
}
