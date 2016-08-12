package com.billionav.navi.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.ui.R;

public class DebugLayout extends RelativeLayout implements View.OnClickListener{
	
	private static DebugLayout instance;
	
	private static final boolean Debug = false;
	
	private static final boolean WriteToFile = false;
	
	private static SimpleDateFormat df = new SimpleDateFormat("[yy-MM-dd hh:mm:ss]: ");
	private static final String MAIN_TAG = "carcon_service";
	
	private TextView sensorStopStateWatch;
	private TextView sensorGyroStateWatch;
	private TextView sensorGsnsStateWatch;
	
	private TextView LogText;
	private Button clearLog;

	public static DebugLayout instance() {
		return instance;
	}
	
	public DebugLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public DebugLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public DebugLayout(Context context) {
		super(context);
		initialize();
	}

	private void initialize() {
		instance = this;
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.debug_layout, this);
	    
	    sensorStopStateWatch = (TextView) findViewById(R.id.sensor_stop_state_watch);
	    sensorGyroStateWatch = (TextView) findViewById(R.id.sensor_gyro_state_watch);
	    sensorGsnsStateWatch = (TextView) findViewById(R.id.sensor_gsns_state_watch);
	    LogText = (TextView) findViewById(R.id.log_text);
	    clearLog = (Button)findViewById(R.id.clear_log);
	    sensorStopStateWatch.setVisibility(View.GONE);
	    sensorGyroStateWatch.setVisibility(View.GONE);
	    sensorGsnsStateWatch.setVisibility(View.GONE);
	    
	    clearLog.setOnClickListener(this);
	}
	
//	private void refreshSensorStateWatch() {
//		final int stopStatus = jniGpsSnsModule.getGpsSnsModule().GetSnsStopState();
//		final boolean gyroStatus = jniGpsSnsModule.getGpsSnsModule().GetGyroOKStatus();
//		final boolean gsnsStatus = jniGpsSnsModule.getGpsSnsModule().GetGsnsOKStatus();
//
//		String script = "unkown status";
//		
//		sensorStopStateWatch.setTextColor(Color.RED);
//		sensorGyroStateWatch.setTextColor(Color.RED);
//		sensorGsnsStateWatch.setTextColor(Color.RED);
//		if(stopStatus == 0) {
//			script = "Running...";
//			sensorStopStateWatch.setTextColor(Color.BLUE);
//		} else if(stopStatus == 1) {
//			script = "stop";
//		}
//		sensorStopStateWatch.setText("Sensor Stop status "+stopStatus+" : " + script);
//		
//		if(gyroStatus){
//			script = "GYRO OK";
//			sensorGyroStateWatch.setTextColor(Color.BLUE);
//		}else{
//			script = "GYRO NG";
//		}
//		sensorGyroStateWatch.setText(script+" return ="+gyroStatus);
//		
//		if(gsnsStatus){
//			script = "GSNS OK";
//			sensorGsnsStateWatch.setTextColor(Color.BLUE);
//		}else{
//			script = "GSNS NG";
//		}
//		sensorGsnsStateWatch.setText(script+" return ="+gsnsStatus);
//	}

	public void setSensorStateWatch(boolean on) {
		if(on) {
		    sensorStopStateWatch.setVisibility(View.VISIBLE);
		    sensorGyroStateWatch.setVisibility(View.VISIBLE);
		    sensorGsnsStateWatch.setVisibility(View.VISIBLE);
			startRefreshSensorStopStateWatch();
		} else {
		    sensorStopStateWatch.setVisibility(View.GONE);
		    sensorGyroStateWatch.setVisibility(View.GONE);
		    sensorGsnsStateWatch.setVisibility(View.GONE);
			stopRefreshSensorStopStateWatch();
		}
	}

	public void startRefreshSensorStopStateWatch() {
		stopRefreshSensorStopStateWatch();
		refreshSensorStopStatsWatchHandler.sendEmptyMessage(1);
	}
	
	public void stopRefreshSensorStopStateWatch() {
		refreshSensorStopStatsWatchHandler.removeMessages(1);
	}
	
	public void log(final String str) {
		if(!Debug) {
			return;
		}
		
		((Activity)getContext()).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				LogText.append(str+"\n");
				if(clearLog.getVisibility() != View.VISIBLE) {
					clearLog.setVisibility(View.VISIBLE);
					LogText.setVisibility(View.VISIBLE);
				}
			}
		});
		
		if(WriteToFile) {
			String path = "/mnt/sdcard/HUD_UILog.txt";
			try {
				RandomAccessFile randomFile = new RandomAccessFile(path, "rw");
				long fileLength = randomFile.length();
				randomFile.seek(fileLength);
				randomFile.writeBytes(df.format(new Date()) + '['+ MAIN_TAG+']' +": "+str+" \n");
				randomFile.close();
			} catch (IOException e) {
				File f = new File(path);
				if (!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
//						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	private void clearLog() {
		LogText.setText(null);
		LogText.setVisibility(View.GONE);
		clearLog.setVisibility(View.GONE);
	}
	
	private Handler refreshSensorStopStatsWatchHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1) {
//				refreshSensorStateWatch();
				sendEmptyMessageDelayed(1, 1000);
			}
		};
	};

	@Override
	public void onClick(View v) {
		if(v == clearLog) {
			clearLog();
		}
		
	}
	
}
