package com.billionav.navi.component.basiccomponent;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.ui.R;


public class ListPreference extends DialogPreference{


	private String[] enties;
	private int index = 0;
	public ListPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

	}

	public ListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void showDialog(Bundle state) {
		// TODO Auto-generated method stub
		CustomDialog dialog = new CustomDialog(getContext());
		dialog.setTitle(getDialogTitle().toString());
		dialog.setSingleChoiceItems(enties, index, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(index != which)
				{
					seletedIndex(which);
					callChangeListener(which);
				}
				dialog.dismiss();
			}
		});
		dialog.setNegativeButton(R.string.STR_COM_001, null);
		dialog.show();
	}

	public void setEnties(int... enties){
		this.enties = new String[enties.length];
		for(int i=0; i<enties.length; i++) {
			this.enties[i] = getContext().getString(enties[i]);
		}
		setsummaryText();
	}
	public void setEnties(String... enties){
		this.enties = enties.clone();
		setsummaryText();
	}
	private void setsummaryText(){
		if(enties != null){
			setSummary(enties[index]);
			
		}
	}
	public void seletedIndex(int index){
		this.index = index;
		setsummaryText();
	}
	
}
