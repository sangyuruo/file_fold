package com.billionav.navi.naviscreen.setting;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceScreen;

import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.ui.R;

public class ADT_Setting_Cradle_Info extends PrefenrenceActivityBase{

	private PreferenceScreen lonLat = null;
	private PreferenceScreen gps_Status = null;
	private PreferenceScreen gps_Number = null;
	
	private PreferenceScreen dis_status = null;
	private PreferenceScreen dir_status = null;
	
	private PreferenceScreen ver_Info = null;
//	private CradleInfo Cradle;
//	private CradleInfo RefreshCradle;
//	private CradleVersion Version;
//	private CradleVersion RefreshVersion;
	
	private int du;
	private int fen;
	private int miao;
	private int dmiao;
	private char symbol = (char)176;
	
	private byte byX;
	private byte byY;
	private byte byZ;
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.adt_settings_cradle_info);
		setTitle("Cradle Info");
		findViews();
		opentimer();
	}

	

	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		super.OnDestroy();
		m_handlers.removeCallbacks(m_runnables);
	}
	private void findViews() {
		lonLat = (PreferenceScreen) getPreferenceManager().findPreference("lonlat");
		gps_Status = (PreferenceScreen) getPreferenceManager().findPreference("gpsstatus");
		gps_Number = (PreferenceScreen) getPreferenceManager().findPreference("gpsnumber");
		
		dis_status = (PreferenceScreen) getPreferenceManager().findPreference("disstatus");
		dir_status = (PreferenceScreen) getPreferenceManager().findPreference("dirstatus");
		
		ver_Info = (PreferenceScreen) getPreferenceManager().findPreference("version");
	}

	private long delayTimes = 500;
	private Handler m_handlers = new Handler();
	private Runnable m_runnables = new Runnable() {

		@Override
		public void run() {
			m_handlers.removeCallbacks(m_runnables);
			opentimer();
		}

	};
	private void opentimer(){
//		RefreshCradle = jniLocInfor.getLocInfor().GetCradleInformation();
//		RefreshVersion = jniLocInfor.getLocInfor().GetCradleVersion();
//		if(!RefreshCradle.bValid){
//			
//		//	BackWinChange();
//		}
//		if((Cradle != RefreshCradle) || (Version != RefreshVersion)){
//			Cradle = RefreshCradle;
//			Version = RefreshVersion;
//			refreshinfo();
//		}
		m_handlers.postDelayed(m_runnables, delayTimes);
	}
	
	private void refreshinfo(){
		setLonlat();
		SSDital();
	
//		CradleInfo cradleInfo = jniLocInfor.getLocInfor().GetCradleInformation();
//		GPSNSlearn(cradleInfo == null ? false : cradleInfo.bGsnsOk);
//		GPSROlearn(cradleInfo == null ? false : cradleInfo.bGyroOk);
		VersionDital();
		GPSNumber();
	}
	
	private void GPSNSlearn(boolean gsnsOk){
		if(gsnsOk){
			dis_status.setSummary("Learning Finish");		
		}else{
			dis_status.setSummary("No Learning");				
		}
	}
	
	private void GPSROlearn(boolean gyroOk){
		if(gyroOk){
			dir_status.setSummary("Learning Finish");		
		}else{
			dir_status.setSummary("No Learning");				
		}
	}
	private void GPSNumber(){

//		gps_Number.setSummary(String.valueOf(Cradle.iSateliteCnt));
	}
	
	private void setLonlat(){
		lonLat.setSummary(setlen()+setlon());
	}
	private String setlen(){
//		du = (int)(Cradle.dLatitude / 3600);
//		fen = (int)((Cradle.dLatitude - du * 3600) / 60);
//		miao = (int)(Cradle.dLatitude - du * 3600 - fen * 60);
//		dmiao = (int)(Cradle.dLatitude * 10 - du * 36000 - fen * 600 - miao * 10);
		return "North Latitude:" + du + symbol + fen + "'" + miao + "." + dmiao + "\"";
	}
	
	private String setlon(){
//		du = (int)(Cradle.dLongitude / 3600);
//		fen = (int)((Cradle.dLongitude - du * 3600) / 60);
//		miao = (int)(Cradle.dLongitude - du * 3600 - fen * 60);
//		dmiao = (int)(Cradle.dLongitude * 10 - du * 36000 - fen * 600 - miao * 10);
		return "East Longitude:" + du + symbol + fen + "'" + miao + "." + dmiao + "\"";
	}
	
	private void SSDital(){

//		int num = Cradle.iGpsDimension;
//		switch(num){
//		case 0:
//			gps_Status.setSummary("No Position Mensuration");
//			break;
//		case 1:
//			gps_Status.setSummary("1Dimensional Position Mensuration");
//			break;
//		case 2:
//			gps_Status.setSummary("2Dimensional Position Mensuration");
//			break;
//		case 3:
//			gps_Status.setSummary("3Dimensional Position Mensuration");
//			break;
//		default:
//			break;
//		}
	}
	
	private void VersionDital(){
//		byX = Version.byX;
//		byY = Version.byY;
//		byZ = Version.byZ;
		ver_Info.setSummary(byX + "." + byY + "." + byZ);
	}
}
