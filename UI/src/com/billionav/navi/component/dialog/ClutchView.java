package com.billionav.navi.component.dialog;


import java.text.DecimalFormat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.ui.R;

public class ClutchView extends RelativeLayout {
	private ImageView decrease;
	private ImageView increase;
	private TextView value;
	private TextView unit;
	
	private float mValue = 0;
	private float mStep;
	
	private float mMax;
	private float mMin;
	
	private OnChangedListener listener;

	public ClutchView(Context context) {
		super(context);
		initialize();
	}

	public ClutchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public ClutchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	private void initialize() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.dialog_clutch, this);
		
	    findViews();
	    
	    setListeners();
	}
	
	public void setCommonInfo(float value, float step, float max, float min, String unit) {
		mValue = value;
		mStep = step;
		mMax = max;
		mMin = min;
		this.value.setText(new DecimalFormat("#########.#").format(mValue));
		this.unit.setText(unit);
	}
	
	public void setOnChangedListener(OnChangedListener l) {
		this.listener = l;
	}
	
	public float getValue() {
		return mValue;
	}

	private void findViews() {
		decrease = (ImageView) findViewById(R.id.iv_decrease);
		increase = (ImageView) findViewById(R.id.iv_increase);
		value = (TextView) findViewById(R.id.tv_value);
		unit = (TextView) findViewById(R.id.tv_unit);
	}
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0x100){
				//decrease
				mValue = mValue - mStep;
				mValue = Math.max(mMin, mValue);
				value.setText(new DecimalFormat("#########.#").format(mValue));
				if(listener != null) {
					listener.onChange(mValue);
				}
			}else if(msg.what == 0x101){
				//increase
				mValue = mValue + mStep;
				mValue = Math.min(mMax, mValue);
				value.setText(new DecimalFormat("#########.#").format(mValue));
				if(listener != null) {
					listener.onChange(mValue);
				}
			}
			this.sendEmptyMessageDelayed(msg.what, 300);
		}
	};
	private void setListeners() {
		decrease.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mValue = mValue - mStep;
				mValue = Math.max(mMin, mValue);
				value.setText(new DecimalFormat("#########.#").format(mValue));
				if(listener != null) {
					listener.onChange(mValue);
				}
				
			}
			
		});

		decrease.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					handler.sendEmptyMessage(0x100);
				}
				else if(event.getAction() != MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_MOVE){
					handler.removeMessages(0x100);
				}
				return true;
			}
		});
		increase.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					handler.sendEmptyMessage(0x101);
				}
				else if(event.getAction() != MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_MOVE){
					handler.removeMessages(0x101);
				}
				return true;
			}
		});
		increase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mValue = mValue + mStep;
				mValue = Math.min(mMax, mValue);
				value.setText(new DecimalFormat("#########.#").format(mValue));
				if(listener != null) {
					listener.onChange(mValue);
				}
			}
		});
		
	}

	public interface OnChangedListener {
		void onChange(float value);
	}

}
