package com.billionav.navi.component.guidebar.base;

import com.billionav.jni.UIGuideControlJNI;
import com.billionav.jni.UIGuideControlJNI;
import com.billionav.jni.UIPathControlJNI;

public class GuideInfoDataManager {
	
	private String 	streetName = "";
	private int		turningImageID;
	private String	turningDistance = "";
	private int		remainingTimeImageID;
	private String	remainingTimeString = "";
	private String 	arriveTimeString = "";
	private String	remainingDistanceString = "";
	private int[]	guideLaneImageID;
	private int 	guideProgress;
	private int     guidePointKind;
	private int		gpsStatus;
	
	private static GuideInfoDataManager m_pInstance;
	
	public static GuideInfoDataManager Instance() {
		if (null == m_pInstance) {
			m_pInstance = new GuideInfoDataManager();
		}
		return m_pInstance;
	}
	
	public String getStartName(){
		UIPathControlJNI pathControl = new UIPathControlJNI();
		String name = pathControl.GetPointName(
					UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID, 0, true);
		
		return name;
	}
	
	public String getDestinationName(){
//		UIPathControlJNI pathControl = new UIPathControlJNI();
//		int pathNum =pathControl.GetNumPathPoint(
//				UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID,
//				UIPathControlJNI.UIC_PT_NUM_POINT_ALL);
//		String name = pathControl.GetPointName(
//					UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID, pathNum - 1, true);
		
		return "";
	}
	
	public long[] getDestinationLonLat(){
		UIPathControlJNI pathControl = new UIPathControlJNI();
		int pathNum =pathControl.GetNumPathPoint(
				UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID,
				UIPathControlJNI.UIC_PT_NUM_POINT_ALL);
		
		long[] lonlat = pathControl.GetCenterLonLat(
					UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID, pathNum - 1, true);
		return lonlat;
	}
	
	public int getRoutePointNum(){
		UIPathControlJNI pathControl = new UIPathControlJNI();
		int pathNum =pathControl.GetNumPathPoint(
				UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID,
				UIPathControlJNI.UIC_PT_NUM_POINT_ALL);
		return pathNum;
	}
	

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setTurningImageID(int turningImageID) {
		this.turningImageID = turningImageID;
	}

	public int getTurningImageID() {
		return turningImageID;
	}

	public void setTurningDistance(String turningDistance) {
		this.turningDistance = turningDistance;
	}

	public String getTurningDistance() {
		return turningDistance;
	}

	public void setRemainingTimeImageID(int remainingTimeImageID) {
		this.remainingTimeImageID = remainingTimeImageID;
	}

	public int getRemainingTimeImageID() {
		return remainingTimeImageID;
	}

	public void setRemainingTimeString(String remainingTimeString) {
		this.remainingTimeString = remainingTimeString;
	}

	public String getRemainingTimeString() {
		return remainingTimeString;
	}

	public void setRemainingDistanceString(String remainingDistanceString) {
		this.remainingDistanceString = remainingDistanceString;
	}

	public String getRemainingDistanceString() {
		return remainingDistanceString;
	}

	public void setGuideLaneImageID(int[] guideLaneImageID) {
		this.guideLaneImageID = guideLaneImageID;
	}

	public int[] getGuideLaneImageID() {
		return guideLaneImageID;
	}

	public String getArriveTimeString() {
		return arriveTimeString;
	}

	public void setArriveTimeString(String arriveTimeString) {
		this.arriveTimeString = arriveTimeString;
	}
	public boolean syncGuideProgress(){
		int prog = UIGuideControlJNI.getInstance().GetETAInfo_DisProgressForDest();
		if(prog <0 ){
			prog = 0;
		}else if(prog > 100){
			prog = 100;
		}
		if(Math.abs(guideProgress - prog) > 2){
			guideProgress = prog;
			return true;
		}else if(guideProgress != prog && (prog == 0 || prog == 100)){
			guideProgress = prog;
			return true;
		}
		return false;
	}
	public int getGuideProgress(){
		return guideProgress;
	}
	
	public void setGuidePointKind(int kind){
		guidePointKind = kind;
	}
	public int getGuidePointKind(){
		return guidePointKind;
	}

	public void setGPSStatus(int GpsDimension) {
		this.gpsStatus = GpsDimension;
		
	}
	public int getGpsStatus(){
		return gpsStatus;
	}
}
