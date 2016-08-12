package com.billionav.navi.component.basiccomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.DRIR.jni.jniDRIR_MainControl;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.ui.R;

public class ArTimerText extends RelativeLayout {

	private TextView txtView;
	public ArTimerText(Context context){
		super(context);
		init();
	}
	
	public ArTimerText(Context context ,AttributeSet attr){
		super(context, attr);
		init();
		
	}
	
	private void init(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.ar_timer_text, this);
		
		txtView = (TextView)findViewById(R.id.ar_timer_text);
		setVisibility(View.INVISIBLE);

	}
	
	
	public void notifyResume(){
		if(jniDRIR_MainControl.IsDMSave()){
			setVisibility(View.VISIBLE);
		}else{
			setVisibility(View.INVISIBLE);
		}
	}
	
	private void setText(long hour,long minu,long second){
		String text = String.format("%02d:%02d:%02d", hour ,minu,second);
		txtView.setText(text);
	}
	
	public void notifyTrigger(NSTriggerInfo cTriggerInfo){
		
		switch (cTriggerInfo.GetTriggerID()) {
		case NSTriggerID.UIC_MN_TRG_DRIR_DMSTART:
			break;
			
		case NSTriggerID.UIC_MN_TRG_DRIR_RECTIME:
		//	if(getVisibility() == View.INVISIBLE){
				setVisibility(View.VISIBLE);
		//	}
			setText(cTriggerInfo.m_lParam1, cTriggerInfo.m_lParam2, cTriggerInfo.m_lParam3);
			this.invalidate();
			break;
		
		case NSTriggerID.UIC_MN_TRG_DRIR_DMEND:
			setVisibility(View.INVISIBLE);
			this.invalidate();
			break;

		default:
			break;
		}
	}
	
	public void onResume(){
		if(!jniDRIR_MainControl.IsDMSave()){
			if(getVisibility() == View.VISIBLE){
				setVisibility(View.INVISIBLE);
			}
		}
	}
}
