package com.billionav.navi.component.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.billionav.ui.R;

public class CustomToast {
	private static Toast toast = null;
	public static void showToast(Context context, CharSequence text, int duration) {
		if(toast == null){
			toast = new Toast(context);
		}
		else{
			//android 3.0+
			if(android.os.Build.VERSION.SDK_INT >= 11){
					// android 4.0+
				if(android.os.Build.VERSION.SDK_INT >= 15){
					toast.cancel(); 
				}
				toast = new Toast(context);
			}
			else{
				toast.cancel(); 
			}
		}
		toast.setDuration(duration);
		toast.setView(getToastView(context, text));
		toast.show();
	}
	
	public static void showToast(Context context, int resId, int duration) {
		showToast(context, context.getText(resId), duration);
	}
	
	public static void cancelToast(){
		if(toast != null){
			toast.cancel(); 
		}
	}
	
	private static View getToastView(Context context, CharSequence text) {
		LayoutInflater inflater = LayoutInflater.from(context);            
		View view = inflater.inflate(R.layout.dialog_toast_view, null);
		TextView tv = (TextView) view.findViewById(R.id.textview);
		tv.setText(text);
		return view;
	}
	
}
