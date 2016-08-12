package com.billionav.DRIR;

import android.content.Context;
import android.widget.Toast;

public class DRIRGlobalToast {
	private static Context mContext = null;
	public static void CreateContext(Context context)
	{
		mContext = context;
	}
	
	public static void GlobalToast(String strText, int duration)
	{
		if (null != mContext)
		{
			Toast toast = Toast.makeText(mContext, strText, duration);
			toast.show();
		}
	}
}
