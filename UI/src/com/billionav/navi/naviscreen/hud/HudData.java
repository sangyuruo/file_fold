package com.billionav.navi.naviscreen.hud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import android.util.Log;

import com.billionav.jni.UIHudControl;

import jp.pioneer.huddevelopkit.DataAroundPOI;
import jp.pioneer.huddevelopkit.DataAroundSpeedCamera;
import jp.pioneer.huddevelopkit.DataGuidePoint;
import jp.pioneer.huddevelopkit.DataJunction;
import jp.pioneer.huddevelopkit.DataLane;
import jp.pioneer.huddevelopkit.DataRouteRestrict;
import jp.pioneer.huddevelopkit.DataRouteSpeedCamera;
import jp.pioneer.huddevelopkit.DataRouteTraffic;
import jp.pioneer.huddevelopkit.DataTimeLine;
import jp.pioneer.huddevelopkit.DataWayPoint;
import jp.pioneer.huddevelopkit.HUDConstants.AlertKind;
import jp.pioneer.huddevelopkit.HUDConstants.DistanceUnit;
import jp.pioneer.huddevelopkit.HUDConstants.SpeedCameraGroupMask;

public class HudData {
	
	HudData() {
	}
	public BeanSpc getBeanSpc() {
		BeanSpc bean = new BeanSpc();
		bean.setCarClor(EnumData.getCarcolor(UIHudControl.getSpccarcolor()));
		bean.setDestinationColor(EnumData.getDestinationcolor(UIHudControl.getSpcdestinationcolor()));
		bean.setGeodeticDatumType(EnumData.getGeodeticdatumtype(UIHudControl.getSpcgeodeticdatumtype()));
		bean.setWaypointColor(EnumData.getWaypointcolor(UIHudControl.getSpcwaypointcolor()));
		bean.setMapColorSwitchingFlag(UIHudControl.getSpcMapColorSwitchingFlag());
		bean.setTimeCarCondition(UIHudControl.getSpcTimeCarCondition());
		bean.setTimeCurrentState(UIHudControl.getSpcTimeCurrentState());
		return bean;
	}
	
	public BeanCarCondition getCarCondition() {
		BeanCarCondition bean = new BeanCarCondition();
		bean.setAcceleration(UIHudControl.getCarAcceleration());
		bean.setAngularVelocity(UIHudControl.getCarAngularVelocity());
		bean.setBearing(UIHudControl.getCarBearing());
		bean.setGpsState(EnumData.getGpsstate(UIHudControl.getCarGpsState()));
		bean.setInTunnelFlag(UIHudControl.getCarInTunnelFlag());
		bean.setLatitude(UIHudControl.getCarLatitude());
		bean.setLongitude(UIHudControl.getCarLongitude());
		bean.setOnRoadFlag(UIHudControl.getCarOnRoadFlag());
		bean.setOnRoute(UIHudControl.getCarOnRouteFlag());
		bean.setPitch(UIHudControl.getCarPitch());
		bean.setSignalState(EnumData.getSignalstate(UIHudControl.getCarSignalState()));
		bean.setSpeed(UIHudControl.getCarSpeed());
		return bean;
	}
	
	public BeanCurrentState getCurrentState(){
		BeanCurrentState bean = new BeanCurrentState();
		
		UIHudControl.CurrentTime time = UIHudControl.getCurrentTime();
		bean.setYear(time.iYear);
		bean.setMonth(time.iMonth);
		bean.setDay(time.iDay);
		bean.setHour(time.iHour);
		bean.setMinute(time.iMinute);
		bean.setSecond(time.iSecond);
		
		bean.setMapColorMode(EnumData.getMapcolor(UIHudControl.getCurrentMapColorMode()));
		bean.setTimeFormat(EnumData.getTimeformat(UIHudControl.getCurrentTimeFormat()));
		bean.setUnitSystem(EnumData.getUnittypesystem(UIHudControl.getCurrentUnitSystem()));
		bean.setBehaviorInBackground(EnumData.getSettingbackground(UIHudControl.getCurrentBehaviorInBackground()));
		
		bean.setRouteSimulation(UIHudControl.getCurrentRouteSimulation());
		EnumSet<SpeedCameraGroupMask> mask = EnumSet.of(EnumData.getSpeedcameragroupmask(UIHudControl.getCurrentGroupOfSpeedCamera()));
		bean.setGroupOfSpeedCamera(mask);
		
		UIHudControl.Visibility vis = UIHudControl.getCurrentVisibility();
		bean.setPoi(vis.bPoi);
		bean.setCongestion(vis.bCongestion);
		bean.setSmooth(vis.bSmooth);
		bean.setRestriction(vis.bRestriction);
		bean.setSpeedcamera(vis.bSpeedcamera);
		
		bean.setRouteID(UIHudControl.getCurrentRouteId());
		bean.setDistance(UIHudControl.getCurrentRouteDistance());
		
		bean.setSpeedLimit(UIHudControl.getCurrentSpeedLimit());
		bean.setOverSpeed(UIHudControl.getCurrentSpeedOvered());
		
		int distanceCount = UIHudControl.getCurrentGuidepointDistanceCount();
		for(int i=0; i<distanceCount; i++) {
			UIHudControl.GuidepointDistance guide = new UIHudControl.GuidepointDistance();
			UIHudControl.getCurrentGuidepointDistance(guide, i);
			
			bean.addGuidePoint(EnumData.getGuidetargetnum(guide.iTarget), 
					guide.iID, 
					guide.iDistance, 
					guide.cDistance, 
					EnumData.getDistanceunit(guide.iDistanceUnit)
					);
		}
		
		bean.setDestinationHour(UIHudControl.getCurrentDestinationTimeHour());
		bean.setDestinationMinute(UIHudControl.getCurrentDestinationTimeMinute());
		
		int waypointTimeCount = UIHudControl.getCurrentWaypointTimeCount();
		for(int i=0; i<waypointTimeCount; i++) {
			UIHudControl.WaypointTime wayPoint = new UIHudControl.WaypointTime();
			UIHudControl.getCurrentWaypointTime(wayPoint, i);
			
			bean.addWaypointTime(EnumData.getWaypointnum(wayPoint.iTarget), 
					wayPoint.iID, 
					wayPoint.iHour, 
					wayPoint.iMinute 
					);
		}
		
		return bean;
	}
	
	
	public ArrayList<DataAroundPOI> getAroundDatas() {
		
		ArrayList<DataAroundPOI> aroundDataList = new ArrayList<DataAroundPOI>();
		
		aroundDataList.addAll(Arrays.asList((DataAroundPOI[])UIHudControl.getAroundPOIs()));
		return aroundDataList;
	}
	
	public BeanARRouteInfo getARRouteInfo(int tag) {
		BeanARRouteInfo info = new BeanARRouteInfo();
		info.setmRouteID(UIHudControl.getARRouteId(tag));
		info.setRouteGranularity(UIHudControl.getARRouteGranularity(tag));
		UIHudControl.DataARRoute[] arRoutes = UIHudControl.getARRouteDatas(tag);
		for(UIHudControl.DataARRoute r : arRoutes) {
			info.addDataARRoute(r.startLatitude, r.endLatitude, r.startLongitude, r.endLongitude, r.color);
		}
		
		return info;
		
	}
	
	public BeanAlert getBeanAlert(){
		return new BeanAlert(UIHudControl.getAlertKind(), 
				UIHudControl.getAlertMarkType(), 
				UIHudControl.getAlertDistStr(),
				UIHudControl.getAlertUnit());
	}
	
	public BeanTimeLine getTimeLineInfo() {
		
		BeanTimeLine bean = new BeanTimeLine();
		
		bean.setRouteId(UIHudControl.getTimeRouteId());
		
		UIHudControl.DataTimeLine[] timelines = UIHudControl.getTimeLineInfo();
		for(UIHudControl.DataTimeLine t : timelines) {
			bean.addTimeLine(t.startDistance, t.endDistance, t.color);
		}
		return bean;
	}
	
	public BeanTarget getTarget() {
		BeanTarget target = new BeanTarget();
		target.setRouteID(UIHudControl.getTargetRouteID());
		target.setImage(UIHudControl.getTargetImage());
		target.setLatitude(UIHudControl.getTargetLatitude());
		target.setLongitude(UIHudControl.getTargetLongitude());
		target.setRouteDistance(UIHudControl.getTargetRouteDistance());
		target.addWayPoint((DataWayPoint[]) UIHudControl.getTargetWayPoints());
		return target;
	}
	
	public BeanJunction getJunctionInfo() {
		BeanJunction bean = new BeanJunction();
		bean.setRouteID(UIHudControl.getJunRouteId());
		bean.setGuidePointID(UIHudControl.getJunGuidePointId());
		bean.setLeftSignBoard(UIHudControl.getJunLeftSignBoard());
		bean.setRightSignBoard(UIHudControl.getJunRightSignBoard());
		bean.setType(UIHudControl.getJunSignboardType());
		bean.setName(UIHudControl.getJunDirectionName());
		UIHudControl.DataJunction[] juns = UIHudControl.getJunctionInfo();
		for(UIHudControl.DataJunction jun : juns) {
			bean.addJnnctionData(jun.dirction, jun.roadNumber, jun.roadNumberType);
		}
		return bean;
	}
	
	public ArrayList<DataAroundSpeedCamera> getAroundSpeedCameras() {
		UIHudControl.DataAroundSpeedCamera[] datas = UIHudControl.getAroundSpeedCameraInfo();
		
		ArrayList<DataAroundSpeedCamera> list = new ArrayList<DataAroundSpeedCamera>(datas.length);
		for(UIHudControl.DataAroundSpeedCamera data : datas) {
			list.add(new DataAroundSpeedCamera(data.latitude, data.longitude, data.type, EnumData.getDisplayGroup(data.displayGroup)));
		}
		return list;
	}
	
	public int getRouteSpeedCameraRouteId() {
		return UIHudControl.getRouteSpeedCameraRouteId();
	}
	
	public ArrayList<DataRouteSpeedCamera> getRouteSpeedCameras() { 
		UIHudControl.DataRouteSpeedCamera[] datas = UIHudControl.getRouteSpeedCameraInfo();
		
		ArrayList<DataRouteSpeedCamera> list = new ArrayList<DataRouteSpeedCamera>(datas.length);
		for(UIHudControl.DataRouteSpeedCamera data : datas) {
			list.add(new DataRouteSpeedCamera(data.latitude, data.longitude, data.distance, data.type, EnumData.getDisplayGroup(data.displayGroup)));
		}
		return list;
	}
	
	public BeanRouteTraffic getBeanRouteTraffics() {
		BeanRouteTraffic bean = new BeanRouteTraffic();
		bean.setRouteId(UIHudControl.getRouteTrafficRouteId());
		bean.setCrowdColor(UIHudControl.getRouteTrafficCrowdColor());
		bean.setJamColor(UIHudControl.getRouteTrafficJamColor());
		
		UIHudControl.DataRouteTraffic[] datas = UIHudControl.getRouteTrafficInfo();
		for(UIHudControl.DataRouteTraffic d : datas) {
			bean.addRouteTrafficis(d.startLatitude, d.startLongitude, d.startDistance, d.endLatitude, d.endLongitude, d.endDistance, d.routeTrafficType);
		}
		return bean;
	}
	
	
	public int  getRouteRestrictRouteId() {
		return UIHudControl.getRouteRestrictRouteId();
	}
	
	public ArrayList<DataRouteRestrict>  getRouteRestricts() {
		UIHudControl.DataRestrict[] datas = UIHudControl.getRouteRestrictInfo();
		
		ArrayList<DataRouteRestrict> list = new ArrayList<DataRouteRestrict>(datas.length);
		for(UIHudControl.DataRestrict d : datas) {
			list.add(new DataRouteRestrict(d.latitude, d.longitude, d.startDistance, d.sype));
		}
		return list;
	}
	
	public BeanGuidePoint getGuidePoints() {
		BeanGuidePoint bean = new BeanGuidePoint();
		UIHudControl.getGuidePointInfo(bean);
		return bean;
	}

}
