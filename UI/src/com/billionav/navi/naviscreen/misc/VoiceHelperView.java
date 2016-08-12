package com.billionav.navi.naviscreen.misc;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.billionav.ui.R;

public class VoiceHelperView extends RelativeLayout{
	
	//Reference VrStateChanged.proto
	
	
	public static final int VOICE_HELPER_STATE_IDLE = 0;//STATE_IDLE = 0;	// gray
	public static final int VOICE_HELPER_STATE_RECORDING = 1;//STATE_SPEECH = 1; // twinkling(red)
	public static final int VOICE_HELPER_STATE_RECOGNIZING = 2;//STATE_BUSY = 2;	// swirling(red)
	public static final int VOICE_HELPER_STATE_ROBOT_SPEAK = 3;//STATE_PROMPT = 3;   // TTS prompt//robot speak
	
//	private int voiceHelperState;
	private ImageView back;
	private ImageView front;
	private Animation animation;
	public VoiceHelperView(Context context) {
		super(context);
		init(context);
	}
	public VoiceHelperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.voice_helper_view, this);
		back = (ImageView)findViewById(R.id.voice_helper_view_back);
		front = (ImageView)findViewById(R.id.voice_helper_view_front);
		switchState(VOICE_HELPER_STATE_IDLE);
	}
	public void switchState(int toState){
//		voiceHelperState = toState;
		switch(toState){
			case VOICE_HELPER_STATE_IDLE:switchToStateIdle();break;
//			case VOICE_HELPER_STATE_WAITING:switchToStateWaiting();break;
			case VOICE_HELPER_STATE_RECORDING:switchToStateWaiting();break;
			case VOICE_HELPER_STATE_RECOGNIZING:switchToStateRecognizing();break;
			case VOICE_HELPER_STATE_ROBOT_SPEAK:switchToStatePromoting();break;
//			case VOICE_HELPER_STATE_RECOGNIZE_FINISH:switchToStateRecognizeFinish();break;
			default:break;
		}
	}
	private void switchToStatePromoting() {
		back.setBackgroundResource(0);
		back.setImageResource(0);
		back.setAnimation(null);
		
		front.setBackgroundResource(0);
		front.setImageResource(R.drawable.navicloud_and_474c);
		front.setAnimation(null);
		
	}
	private void switchToStateRecognizing() {
		if(null != animation && animation.hasStarted()){
//			animation.cancel();
		}
		animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
		back.setBackgroundResource(0);
		back.setImageResource(R.drawable.navicloud_and_478a);
		back.setAnimation(animation);
		animation.start();
		
		front.setBackgroundResource(0);
		front.setImageResource(0);
		front.setAnimation(null);
		
	}
//	private void switchToStateRecording() {
//		AnimationDrawable animation = getRecordingBackground();
//		back.setBackgroundResource(0);
//		back.setImageDrawable(animation);
//		animation.start();
//		back.setAnimation(null);
//		
//		front.setBackgroundResource(0);
//		front.setImageResource(R.drawable.navicloud_and_474a);
//		front.setAnimation(null);
//		
//	}
	private void switchToStateWaiting() {
		if(null != animation && animation.hasStarted()){
			animation.cancel();
		}
		animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_change);
		back.setBackgroundResource(0);
		back.setImageResource(R.drawable.navicloud_and_474b);
		back.setAnimation(animation);
		animation.start();
		
		front.setBackgroundResource(0);
		front.setImageResource(R.drawable.navicloud_and_474a);
		front.setAnimation(null);
		
	}
	private void switchToStateIdle() {
		back.setBackgroundResource(0);
		back.setImageResource(0);
		back.setAnimation(null);
		
		front.setBackgroundResource(0);
		front.setImageResource(R.drawable.navicloud_and_474a);
		front.setAnimation(null);
	}
	private AnimationDrawable getRecordingBackground(){
		 AnimationDrawable animationDrawable = new AnimationDrawable();
		 animationDrawable.setOneShot(false);
		 return animationDrawable;
	}

}
