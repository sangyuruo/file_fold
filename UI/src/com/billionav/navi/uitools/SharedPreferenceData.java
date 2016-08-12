/**
 * 
 */
package com.billionav.navi.uitools;

import android.app.Activity;
import android.content.SharedPreferences;
import com.billionav.DRIR.jni.jniDRIR_MainControl;
import com.billionav.jni.UIBaseConnJNI;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.naviscreen.NaviViewManager;


/**
 * @author liuzhaofeng
 *
 */
public enum SharedPreferenceData {
	
	
	KEY_SPEED_SWITCH(false),
	
	GENRE_ID_LIST(""),
	
	ACCESS_TOKEN(""),
	EXPIRES_IN(""),
	MATCHING_LOG(false),
	OPEN_GPS_NEED_TIP(true),
	
	DEBUG_LOCATIONWRITELOGS(true),
	
	STUPDM_DEBUG_DEVELOP_DBG(false),
	
	
	DRIR_SETTING_DRID_ONOFF(jniDRIR_MainControl.DRIR_SETTING_DR_FUC_DEFAULT),             //on off
	DRIR_SETTING_DRID_RESOLUTION(jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_STD),  //high middle low
	DRIR_SETTING_DRID_RECDQUALITY(jniDRIR_MainControl.DRIR_SETTING_DR_RECQUALITY_MIDDLE), //high middle low
	DRIR_SETTING_DRID_GSNSLEVEL(jniDRIR_MainControl.DRIR_SETTING_DR_GSNSLEVEL_MIDDLE), //high middle low
	DRIR_SETTING_DRID_DMRECMODE(jniDRIR_MainControl.DRIR_SETTING_DR_SDRECMODE_STOP),  //on off
	DRIR_SETTING_DRID_GSNSFUC(jniDRIR_MainControl.DRIR_SETTING_DR_GSNSFUC_OFF),   //on off
	DRIR_SETTING_DRID_SDCAPACITY(jniDRIR_MainControl.DRIR_SETTING_DR_SDCAP2), //500-1024M
	DRIR_SETTING_DRID_LOG(jniDRIR_MainControl.DRIR_SETTING_DR_LOG_OFF),       //on off
	DRIR_SETTING_DRID_UPLOADFUC(jniDRIR_MainControl.DRIR_SETTING_DR_UPLOAD_ON),    //on off
//	DRIR_SETTING_DRID_FRAMERATE(jniDRIR_MainControl.DRIR_SETTING_DR_FRAME_5), //
//	
	DRIR_SETTING_DRID_RECMODE(jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_DEFAULT), //high standard long
	DRIR_SETTING_DRID_SDRECMODE(jniDRIR_MainControl.DRIR_SETTING_DR_SDRECMODE_OVERWRITE),// overide stop
//
	DRIR_SETTING_IRID_ALTERLEVEL(jniDRIR_MainControl.DRIR_SETTING_IR_ALTERLEVEL_HIGH),
//	DRIR_SETTING_IRID_GSENSORADJUST(jniDRIR_MainControl.DRIR_SETTING_IR_GSENSORADJUST_ON),
//	DRIR_SETTING_IRID_DEMOMODE(jniDRIR_MainControl.DRIR_SETTING_IR_DEMOMODE_OFF),
	DRIR_SETTING_IRID_FRONTCARINFO(jniDRIR_MainControl.DRIR_SETTING_IR_FRONTCARINFO_CAR),
	DRIR_SETTING_IRID_CRASHTIMEALARM(jniDRIR_MainControl.DRIR_SETTING_IR_CRASHTIMEALARM_DEFAULT),
	DRIR_SETTING_IRID_CARMODEL(jniDRIR_MainControl.DRIR_SETTING_IR_CARMODE_NORMAL),
	DRIR_SETTING_IRID_SPEEDLIMIT(jniDRIR_MainControl.DRIR_SETTING_IR_SPEEDLIMIT_LOW),

	//DR debug setting
	DRIR_DEBUG_SETTING_DRID_ONOFF(1),
	DRIR_DEBUG_SETTING_DRID_RECDQUALITY(1),
	DRIR_DEBUG_SETTING_DRID_GSNSFUC(0),
	DRIR_DEBUG_SETTING_DRID_LOG(0),
	DRIR_DEBUG_SETTING_DRID_FRAMERATE(0),
	DRIR_DEBUG_SETTING_DRID_RECORD_CALIBRATE(0),
	DRIR_DEBUG_SETTING_DRID_DRVEVT(false),
	DRIR_DEBUG_SETTING_DRID_GSNSLEVEL(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DR_GSNSLEVEL_DEFAULT),
	//IR debug setting
	DRIR_DEBUG_SETTING_IRID_DEMORUNSPPED(-1),
	DRIR_DEBUG_SETTING_IRID_ROADSDET(0), 
	DRIR_DEBUG_SETTING_IRID_LDW(0),
	DRIR_DEBUG_SETTING_IRID_RESERVE1(0), 
	DRIR_DEBUG_SETTING_IRID_RESERVE2(0),
	DRIR_DEBUG_SETTING_IRID_RESERVE3(50), 
	DRIR_DEBUG_SETTING_IRID_RESERVE4(50),
	DRIR_DEBUG_SETTING_IRID_RESERVE5(50), 
    
	//opening
	OPEN_DISCAIMER_SETTING(false), 
	
	//map layer
	STUPDM_SURROUNDING_EVENT_INT_KEY(0),
	STUPDM_TRAFFIC_LINE_DISPLAY(false),
	STUPDM_FAVORITE_DISPLAY_INT_KEY(0),
	
//	OPEN_NEED_LOGIN(true),//need login
	OPEN_AUTO_LOGIN(false),//need auto login progress
	IS_NEED_AR_TIP(true), 
	MAP_FONT_SIZE(UIMapControlJNI.MAP_FONT_SIZE_MEDIUM),
	SCHEDULE_START_TIME(HybridUSTools.START_TIME_TEN_MINUE),
	DAY_NIGHT_SETTING(UIBaseConnJNI.DAY_OPTION_AUTO),
	CAMERA_TIP(true),
	
	// whether auto pop up download offline package tip.
	OFFLINE_AUTO_TIP(true),
	
	//HUD SETTING
	HUD_POI(false),
	HUD_TRAFFIC_INFO(false),
	HUD_TRAFFIC_RESTRICT(false),
	HUD_SPEEDCAMER(false),
	HUD_TIME_24(false),
	
	HUD_LOGOMARK1(-1),
	HUD_LOGOMARK2(-1),
	HUD_LOGOMARK3(-1),
	HUD_LOGOMARK4(-1),
	HUD_LOGOMARK5(-1),
	//CRADLE SETTING
	GPS_CONNECTION_MODE(SetupOptionValue.LOCATION_FUNC_AUTO),
	;

	
	private static final String STORE_NAME = "WebNavi";
	
	private Object obj;

	private SharedPreferenceData(float obj){
		this.obj = obj;
	}
	private SharedPreferenceData(int obj){
		this.obj = obj;
	}
	private SharedPreferenceData(String obj){
		this.obj = obj;
	}
	private SharedPreferenceData(boolean obj){
		this.obj = obj;
	}
	
	private void setDefVal(){
		if(obj != null){
			if(obj instanceof Boolean){
				setValue(this, (Boolean)obj);
			} else if(obj instanceof Integer){
				setValue(this, (Integer)obj);
			} else if(obj instanceof Float){
				setValue(this, (Float)obj);
			} else if(obj instanceof String){
				setValue(this, (String)obj);
			}
		}
	}
	
	/**
	 * initialize all datas
	 * */
	public static void setDefaultValues(){
		for(SharedPreferenceData d: values()){
			d.setDefVal();
		}
	}
	
	/**
	 * save data that type is int
	 * */
	public static void setValue(SharedPreferenceData key, int value){
		checkValue(key, value);
		SharedPreferences.Editor editor = getEditor();
		editor.putInt(key.name(), value);
		editor.commit();
	}
	
	/**
	 * save data that type is int
	 * */
	public void setValue(int value){
		setValue(this, value);
	}
	
	/**
	 * save data that type is string 
	 * */
	public static void setValue(SharedPreferenceData key, String value){
		checkValue(key, value);
		SharedPreferences.Editor editor = getEditor();
		editor.putString(key.name(), value);
		editor.commit();
	}
	
	/**
	 * save data that type is string 
	 * */
	public void setValue(String value){
		setValue(this, value);
	}
	
	/**
	 * save data that type is boolean 
	 * */
	public static void setValue(SharedPreferenceData key, boolean value){
		checkValue(key, value);
		SharedPreferences.Editor editor = getEditor();
		editor.putBoolean(key.name(), value);
		editor.commit();
	}
	
	/**
	 * save data that type is boolean 
	 * */
	public void setValue(boolean value){
		setValue(this, value);
	}
	
	
	/**
	 * save data that type is float
	 * */
	public static void setValue(SharedPreferenceData key, float value){
		checkValue(key, value);
		SharedPreferences.Editor editor = getEditor();
		editor.putFloat(key.name(), value);
		editor.commit();
	}
	
	/**
	 * save data that type is float
	 * */
	public void setValue(float value){
		setValue(this, value);
	}
	
	/**
	 * 
	 * get int-type data from local
	 * 
	 * @param key the field you want to get
	 * @param defValue default value if data is not exist
	 * 
	 * @return the value
	 * */
	public static int getInt(SharedPreferenceData key, int defValue){
		return getSharedPreferences().getInt(key.name(), defValue);
	}
	
	/**
	 * 
	 * get int-type data from local
	 * 
	 * @param key the field you want to get
	 * 
	 * @return the value, if the data is not exist the method will return default value or -1
	 * */
	public static int getInt(SharedPreferenceData key){
		if(key.obj == null){
			return getInt(key, -1);
		} else {
			return getInt(key, ((Integer)key.obj));
		}
	}
	
	/**
	 * 
	 * get int-type data from local<br/>
	 * 
	 * example int i = SharedPreferenceData.KEY_EXAMPLE.getString()
	 * 
	 * @return the value, if the data is not exist the method will return  default value or -1
	 * */
	public int getInt(){
		return getInt(this);
	}
	
	/**
	 * 
	 * get String-type data from local
	 * 
	 * @param key the field you want to get
	 * @param defValue default value if data is not exist
	 * 
	 * @return the value
	 * */
	public static String getString(SharedPreferenceData key, String defValue){
		return getSharedPreferences().getString(key.name(), defValue);
	}
	
	/**
	 * 
	 * get String-type data from local
	 * 
	 * @param key the field you want to get
	 * 
	 * @return the value, if the data is not exist the method will return default value or ""
	 * */
	public static String getString(SharedPreferenceData key){
		if(key.obj == null){
			return getString(key, "");
		}else{
			return getString(key, key.obj.toString());
		}
	}
	
	/**
	 * 
	 * get String-type data from local<br/>
	 * 
	 * example String s = SharedPreferenceData.KEY_EXAMPLE.getString()
	 * 
	 * @return the value, if the data is not exist the method will return default value or ""
	 * */
	public String getString(){
		return getString(this);
	}

	/**
	 * 
	 * get boolean-type data from local
	 * 
	 * @param key the field you want to get
	 * @param defValue default value if data is not exist
	 * 
	 * @return the value
	 * */
	public static boolean getBoolean(SharedPreferenceData key, boolean defValue){
		return getSharedPreferences().getBoolean(key.name(), defValue);
	}
	
	/**
	 * 
	 * get boolean-type data from local
	 * 
	 * @param key the field you want to get
	 * 
	 * @return the value, if the data is not exist the method will return default value or false
	 * */
	public static boolean getBoolean(SharedPreferenceData key){
		if(key.obj == null){
			return getBoolean(key, false);
		} else {
			return getBoolean(key, (Boolean)key.obj);
		}
	}
	
	/**
	 * 
	 * get boolean-type data from local<br/>
	 * 
	 * example boolean b = SharedPreferenceData.KEY_EXAMPLE.getBoolean()
	 * 
	 * @return the value, if the data is not exist the method will return default value or false
	 * */
	public boolean getBoolean(){
		return getBoolean(this);
	}


	/**
	 * 
	 * get float-type data from local
	 * 
	 * @param key the field you want to get
	 * @param defValue default value if data is not exist
	 * 
	 * @return the value
	 * */
	public static float getFloat(SharedPreferenceData key, float defValue){
		return getSharedPreferences().getFloat(key.name(), defValue);
	}
	/**
	 * 
	 * get float-type data from local
	 * 
	 * @param key the field you want to get
	 * 
	 * @return the value, if the data is not exist the method will return default value or 0f
	 * */
	public static float getFloat(SharedPreferenceData key){
		if(key.obj == null){
			return getFloat(key, 0f);
		} else {
			return getFloat(key, (Float)key.obj);
		}
	}
	
	/**
	 * 
	 * get float-type data from local<br/>
	 * 
	 * etc. float f = SharedPreferenceData.KEY_EXAMPLE.getFloat()
	 * 
	 * @return the value, if the data is not exist the method will return default value or 0f
	 * */
	public float getFloat(){
		return getFloat(this);
	}

	
	private static SharedPreferences getSharedPreferences() {
		return NaviViewManager.GetViewManager().getSharedPreferences(STORE_NAME, Activity.MODE_WORLD_READABLE);
	}
	
	
	private static SharedPreferences.Editor getEditor() {
		return NaviViewManager.GetViewManager().getSharedPreferences(STORE_NAME, Activity.MODE_WORLD_READABLE).edit();
	}
	
	
	private static void checkValue(SharedPreferenceData key, Object value){
		if(key.obj!=null && value!=null && !value.getClass().equals(value.getClass())){
			throw new IllegalArgumentException(
					"SharedPreferences key = "+key.name()+", " +
					"data type = "+key.obj.getClass().getName()+", " +
					"value type = "+value.getClass().getName()+", " +
					"type not match!");
		}
	}
	
}
