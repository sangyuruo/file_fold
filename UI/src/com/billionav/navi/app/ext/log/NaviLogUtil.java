package com.billionav.navi.app.ext.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.billionav.navi.app.AndroidNaviAPP;
import com.billionav.navi.app.ext.NaviConstant;

import android.os.Environment;
import android.util.Log;

public class NaviLogUtil {
	
	public static void debugMediacodec(String msg) {
		if (NaviConstant.DEBUG_MEDIA_CODEC) {
			Log.d(NaviConstant.TAG_MEDIA_CODEC, msg);
		}
	}
	
	public static void debugToHuLoopTime(String msg) {
		if (NaviConstant.DEBUG_TO_HU_LOOP_TIME) {
			Log.d(NaviConstant.TAG_DEBUG_TO_HU_LOOP_TIME, msg);
		}
	}
	
	public static void debugEglStep(String msg) {
		if (NaviConstant.DEBUG_EGL_STEP) {
			Log.d(NaviConstant.TAG_EGL_STEP, msg);
			if( NaviConstant.WRITE_EGL_STEP_LOG_TOFILE ){
				AndroidNaviAPP.getLogHandler().queueMessage( new NaviLogBean("eglstep.csv" , msg) );
			}
		}
	}
	
	public static void debugEglPixMap(String msg) {
		if (NaviConstant.DEBUG_EGL_STEP) {
			Log.d(NaviConstant.TAG_EGL_STEP, msg);
		}
	}
	
	public static void debugMap(String msg) {
		if (NaviConstant.DEBUG_MAP) {
			Log.d(NaviConstant.TAG_MAP, msg);
		}
	}

	public static void debug(String tag, String msg) {
		if (NaviConstant.DEBUG) {
			Log.d(tag, msg);
		}
	}
	
	public static void debugTouch( String msg) {
		if (NaviConstant.DEBUG_TOUCH) {
			Log.d(NaviConstant.TAG_TOUCH, msg);
		}
	}

	public static void error(String tag, String msg) {
		if (NaviConstant.LOG_ERROR) {
			Log.e(tag, msg);
			if (NaviConstant.WRITE_ERROR_LOG_TOFILE) {
				AndroidNaviAPP.getLogHandler().queueMessage( new NaviLogBean("errlog.txt" , msg) );
			}
		}
	}
	
	public static void errorPoi(String tag, String msg) {
		if (NaviConstant.LOG_ERROR) {
			Log.e(tag, msg);
			if (NaviConstant.WRITE_LOG_TOFILE) {
				writeLogToFileSync("errpoilog.txt", tag + ": " + msg);
			}
		}
	}

	public static void debugEGL(String tag, String msg) {
		if (NaviConstant.DEBUG_MAP) {
			Log.e(tag, msg);
			// writeLogToFile("tag: " + msg);
		}
	}

	public synchronized static void writeLogToFile(String filename, String msg) {
		File file = new File(Environment.getExternalStorageDirectory(),
				filename);
		Log.d("EglHelper", Environment.getExternalStorageDirectory().getPath());
		// Environment.getExternalStorageDirectory()获取当前手机默认的sd卡路径
		BufferedWriter out;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			out.write(msg);
			out.newLine();
			out.flush();
			out.close();
		} catch (Exception e) {
			Log.e("EglHelper", e.getMessage());
		}
	}

	private static void writeLogToFileSync(final String filename,
			final String msg) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeLogToFile(filename, msg);
			}
		}).start();
	}

	/**
	 * 打印发送到车机时的日志
	 * @param msg
	 */
	public static void debugSendToHU(String msg) {
		if( NaviConstant.DEBUG_SEND_TOHU ){
			Log.d(NaviConstant.TAG_LOOP_SEND_TOHU, msg);
		}
		
		if (NaviConstant.DEBUG_COST_TIME) {
			if( NaviConstant.WRITE_COST_TIME_TOFILE ){
				AndroidNaviAPP.getLogHandler().queueMessage( new NaviLogBean("sendHu.csv" , msg) );
			}
		}
	}
	
	/**
	 * 打印egl在手机画图的耗时信息
	 * @param msg
	 */
	public static void debugEglCostTime(String msg) {
		if (NaviConstant.DEBUG_COST_TIME) {
			Log.d("EglHelper", msg);
			if( NaviConstant.WRITE_COST_TIME_TOFILE ){
				AndroidNaviAPP.getLogHandler().queueMessage( new NaviLogBean("eglCost.csv" , msg) );
			}
		}
	}
	
	/**
	 * 打印发送消息给车机线程的循环信息
	 * @param msg
	 */
	public static void debugLoopTime(String msg) {
		if (NaviConstant.DEBUG_COST_TIME) {
			Log.d("EglHelper", msg);
//			if( NaviConstant.WRITE_COST_TIME_TOFILE ){
//				AndroidNaviAPP.getLogHandler().queueMessage( new NaviLogBean("loopTime.csv" , msg) );
//			}
		}
	}
	
	/**
	 * 打印同步池日志
	 * @param msg
	 */
	public static void debugEglPool(String msg) {
		if (NaviConstant.DEBUG_COST_TIME) {
			Log.d("EglPool", msg);
			if( NaviConstant.WRITE_COST_TIME_TOFILE ){
				AndroidNaviAPP.getLogHandler().queueMessage( new NaviLogBean("eglPool.csv" , msg) );
			}
		}
	}

	public static void debugSendSuccess(String msg) {
		if (NaviConstant.DEBUG_SEND_SUCCESS) {
			Log.d(NaviConstant.TAG_LOOP_SEND_SUCCESS, msg);
		}
	}
	
	public static void debugEglConfig(String msg) {
		Log.d("eglConfig", msg);
//		AndroidNaviAPP.getLogHandler().queueMessage( new NaviLogBean("eglConfig.txt" , msg) );
	}
	
	/**
	 *
	 * @param msg
	 */
	public static void debugEglDrawCostTime(String msg) {
		if (NaviConstant.DEBUG_EGL_DRAW_COST_TIME) {
			Log.d("EglDrawCostTime", msg);
			if( NaviConstant.WRITE_EGL_DRAW_COST_TIME_TOFILE ){
				AndroidNaviAPP.getLogHandler().queueMessage( new NaviLogBean("eglStepCost.csv" , msg) );
			}
		}
	}
	
	/**
	 *
	 * @param msg
	 */
	public static void debugQueueToHUCostTime(String msg) {
		if (NaviConstant.DEBUG_QUEUE_TOHU_COST_TIME) {
			Log.d("QueueToHUCostTime", msg);
			if( NaviConstant.WRITE_QUEUE_TOHU_COST_TIME_TOFILE ){
				AndroidNaviAPP.getLogHandler().queueMessage( new NaviLogBean("queueToHUCost.csv" , msg) );
			}
		}
	}
	
	/**
	 *
	 * @param msg
	 */
	public static void debugDecoderCostTime(String msg) {
		if (NaviConstant.DEBUG_DECODER_COST_TIME) {
			Log.d("DecoderCostTime", msg);
			if( NaviConstant.WRITE_DECODER_COST_TIME_TOFILE ){
				AndroidNaviAPP.getLogHandler().queueMessage( new NaviLogBean("decoderCost.csv" , msg) );
			}
		}
	}
}
