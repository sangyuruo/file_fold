package com.billionav.navi.naviscreen.hud;

import java.util.ArrayList;

import jp.pioneer.huddevelopkit.DataWayPoint;
import jp.pioneer.huddevelopkit.HUDConstants.DistanceUnit;
import jp.pioneer.huddevelopkit.HUDConstants.GuideTargetNum;
import jp.pioneer.huddevelopkit.HUDConstants.NavEvent;
import jp.pioneer.huddevelopkit.HUDConstants.TrafficStateColor;
import jp.pioneer.huddevelopkit.HUDConstants.WaypointNum;
import jp.pioneer.huddevelopkit.NavARRouteInfo;
import jp.pioneer.huddevelopkit.NavAlert;
import jp.pioneer.huddevelopkit.NavAroundPOIInfo;
import jp.pioneer.huddevelopkit.NavAroundSpeedCamera;
import jp.pioneer.huddevelopkit.NavCarCondition;
import jp.pioneer.huddevelopkit.NavCurrentState;
import jp.pioneer.huddevelopkit.NavGuidePointInfo;
import jp.pioneer.huddevelopkit.NavJunctionInfo;
import jp.pioneer.huddevelopkit.NavRouteRestrictInfo;
import jp.pioneer.huddevelopkit.NavRouteSpeedCamera;
import jp.pioneer.huddevelopkit.NavRouteTrafficInfo;
import jp.pioneer.huddevelopkit.NavSpec;
import jp.pioneer.huddevelopkit.NavTargetPointInfo;
import jp.pioneer.huddevelopkit.NavTimeLineInfo;
import jp.pioneer.huddevelopkit.PHUDConnectManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.billionav.jni.UIHudControl;
import com.billionav.navi.component.DebugLayout;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.naviscreen.hud.BeanCurrentState.GuidepointDistance;
import com.billionav.navi.naviscreen.hud.BeanCurrentState.WaypointTime;
import com.billionav.navi.net.PConnectReceiver;

public class HudManager implements TriggerListener{
	
	private static final HudManager instance = new HudManager();
	public static HudManager getInstance() {
		return instance;
	}
	
	
	public Handler mHandler;
	public HandlerThread mHandlerThread;

	// final variable  must same with HudMsgControl.h
	public static final int AR_ROUTE = 2;
	public static final int AROUND_POI = 4;
	public static final int TARGET_POINT = 5;
	public static final int TIMELINE = 6;
	public static final int JUNCTION = 7;
	public static final int GUIDE_POINT = 8;
	public static final int AROUND_SPEEDCAMERA = 11;
	public static final int ROUTE_RESTRICTION = 13;
	public static final int ROUTE_SPEEDCAMERA = 14; //don't have no
	public static final int ROUTE_TRAFFICINFO = 15;
	public static final int ALERT = 20;
	public static final int CAR_CONDITION = 31;
	public static final int CURRENT_STATE = 32;
	public static final int SPEC = 33;
	public static final int NOTIFICATION = 34;
	
	public static final int REGULAR_UPDATE = 100;
	
	
	private INaviListener mlistener;
	private HudData hudData;
	
	
	private HudManager() {
		
		if(!isHudVersion()){
			return;
		}
		
		UIHudControl.attachJ();
		
		hudData = new HudData();
		
		mHandlerThread = new HandlerThread("HudManager");
		mHandlerThread.start();

	    mHandler = new Handler(mHandlerThread.getLooper()) { 
	    	public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case REGULAR_UPDATE:
					break;
				case CAR_CONDITION:
					sendCarCondition(); 
					break;
				case CURRENT_STATE:
					sendCurrentState(); 
					break;
				case SPEC:            
					sendNaviSpec();
					break;
				case AR_ROUTE:           
					DebugLayout.instance().log("tag="+msg.arg1);
					sendARRoute(msg.arg1);
					break;
				case AROUND_POI:       
					sendAroundPoi();
					break;
				case TARGET_POINT:    
					sendTarget();
					break;
				case TIMELINE:        
					sendTimeLine();
					break;
				case JUNCTION:
					sendJunction();
					break;
				case GUIDE_POINT:
					sendGuidePoint();
					break;
				case AROUND_SPEEDCAMERA:
					sendASpeedCamera();
					break;
				case ROUTE_RESTRICTION:
					sendRRestriction();
					break;
				case ROUTE_SPEEDCAMERA:
					sendRSpeedCamera();
					break;
				case ROUTE_TRAFFICINFO:
					sendRTraffic();
					break;
				case ALERT:
					sendAlert();     
					break;
				case NOTIFICATION:
					sendNotification(msg.arg1, msg.arg2); 
					break;
				default:
					break;
				}
	    	};
	    	
	    	private void sendNotification(int arg1, int arg2) {
				PHUDConnectManager.pEventNotify(EnumData.getNavEvent(arg1), arg2);
			}

			private void sendAlert() {
				if (PHUDConnectManager.pIsConnected() == false) return;
				BeanAlert alert = hudData.getBeanAlert();
				NavAlert.setAlertKind(alert.getAlertKind());
				NavAlert.setAlertMarkType(alert.getType());
				NavAlert.setDistanceToAlertObject(alert.getDistStr(), alert.getUnit());
				NavAlert.updatedStatus();
				
			}

			private void sendRTraffic() {
				if (isHUDConnected() == false) return;

				BeanRouteTraffic bean = hudData.getBeanRouteTraffics();
				NavRouteTrafficInfo.setRouteTraffic(bean.getRouteId(), bean.getRouteTrafficis());
				NavRouteTrafficInfo.setTrafficColor(bean.getCrowdColor(), bean.getJamColor());
				NavRouteTrafficInfo.updatedStatus();
			}

			private void sendRSpeedCamera() {
				if (isHUDConnected() == false) return;

				NavRouteSpeedCamera.setRouteSpeedCamera(hudData.getRouteSpeedCameraRouteId(), hudData.getRouteSpeedCameras());
				NavRouteSpeedCamera.updatedStatus();

			}

			private void sendRRestriction() {
				if (isHUDConnected() == false) return;
				NavRouteRestrictInfo.setRouteRestrict(hudData.getRouteRestrictRouteId(), hudData.getRouteRestricts());
				NavRouteRestrictInfo.updatedStatus();

			}

			private void sendASpeedCamera() {
				if (isHUDConnected() == false) return;
				NavAroundSpeedCamera.setAroundSpeedCamera(hudData.getAroundSpeedCameras());
				NavAroundSpeedCamera.updatedStatus();

			}

			private void sendGuidePoint() {
				if (isHUDConnected() == false) return;
				BeanGuidePoint bean = hudData.getGuidePoints();
								
				NavGuidePointInfo.setGuidePoint(bean.getRouteId(), bean.getGuidePoints());
				NavGuidePointInfo.updatedStatus();
				
			}

			private void sendJunction() {
				if (isHUDConnected() == false) return;
				BeanJunction junction = hudData.getJunctionInfo();
				NavJunctionInfo.setSideSignBoardExistence(junction.isLeftSignBoard(), junction.isRightSignBoard());
				NavJunctionInfo.setJunctionDisplay(junction.getRouteID(), junction.getGuidePointID(), junction.getType(), junction.getName());
				NavJunctionInfo.setJunction(junction.getJunctions());
				NavJunctionInfo.updatedStatus();
			}

			private void sendTimeLine() {
				if (isHUDConnected() == false) return;

				BeanTimeLine bean = hudData.getTimeLineInfo();
				NavTimeLineInfo.setTimeLine(bean.getRouteId(), bean.getTimeLines());
				NavTimeLineInfo.updatedStatus();

			}

			private void sendTarget() {
				if (isHUDConnected() == false) return;
				BeanTarget target = hudData.getTarget();

				NavTargetPointInfo.setDestinationPoint(target.getRouteID(), 
						target.getImage(), target.getLatitude(), target.getLongitude(), target.getRouteDistance());
				NavTargetPointInfo.setWaypoint(target.getWayPoints());
				NavTargetPointInfo.updatedStatus();
			}

			private void sendAroundPoi() {
				if (isHUDConnected() == false) return;

				NavAroundPOIInfo.setAroundPOI(hudData.getAroundDatas());
				NavAroundPOIInfo.updatedStatus();

			}

			private void sendARRoute(int tag) {
				if (isHUDConnected() == false) return;
				
				BeanARRouteInfo route = hudData.getARRouteInfo(tag);
				
				NavARRouteInfo.setARRouteLineCoordinates(route.getmRouteID(), route.getRouteGranularity(), route.getList());
				NavARRouteInfo.updatedStatus();
				DebugLayout.instance().log("send AR Route id="+route.getmRouteID()+" granularity="+route.getRouteGranularity());
				UIHudControl.removeARRouteByTag();
			}
			
			private void sendNaviSpec() {
				if (isHUDConnected() == false) return;
				
				BeanSpc spc = hudData.getBeanSpc();
				
				NavSpec.setColorCarMark(spc.getCarClor());
				NavSpec.setColorDestinationMark(spc.getDestinationColor());
				NavSpec.setColorWaypointMark(spc.getWaypointColor());
				NavSpec.setGeodeticDatum(spc.getGeodeticDatumType());
				NavSpec.setMapColorSwitchingFunction(spc.getMapColorSwitchingFlag());
				NavSpec.setTimeInterval(spc.getTimeCarCondition(), spc.getTimeCurrentState());
				NavSpec.updatedStatus();
			}
			
			private void sendCarCondition() {

				if (isHUDConnected() == false) return;
				
				BeanCarCondition car = hudData.getCarCondition();
				NavCarCondition.setCurrentPosition(car.getLatitude(), car.getLongitude());
				NavCarCondition.setBearing(car.getBearing());
				NavCarCondition.setSpeed(car.getSpeed());
				NavCarCondition.setExtentLocation(car.getAcceleration(), car.getAngularVelocity(), car.getPitch());
				NavCarCondition.setGpsReception(car.getGpsState());
				NavCarCondition.setInTunnel(car.isInTunnelFlag());
				NavCarCondition.setOnRoad(car.isOnRoadFlag());
				NavCarCondition.setOtherSignalReception(car.getSignalState());
				NavCarCondition.setCarNonSequentiality(car.isCarNonSequentiality());
				NavCarCondition.setOnRoute(car.isOnRoute());
				NavCarCondition.updatedStatus();
			}
			
			private void sendCurrentState() {

				if (isHUDConnected() == false) return;
				
				BeanCurrentState state = hudData.getCurrentState();

				NavCurrentState.setCurrentTime(state.getYear(), state.getMonth(), state.getDay(), state.getHour(), state.getMinute(), state.getSecond());
				
				NavCurrentState.setMapColorMode(state.getMapColorMode());
				NavCurrentState.setTimeDisplayFormat(state.getTimeFormat());
				NavCurrentState.setUnitMeasurement(state.getUnitSystem());
				NavCurrentState.setBehaviorSettingInBackground(state.getBehaviorInBackground());
				NavCurrentState.setUiInfoVisibility(state.isPoi(), state.isCongestion(), state.isSmooth(), state.isRestriction(), state.isSpeedcamera());
				NavCurrentState.setSpeedLimit(state.getSpeedLimit(), state.isOverSpeed());
				NavCurrentState.setDispGroupOfSpeedCamera(state.getGroupOfSpeedCamera());
				NavCurrentState.setRouteSimulation(state.isRouteSimulation());

				NavCurrentState.setRouteID(state.getRouteID());
				
				
				if (state.getRouteID() == 0) {
					NavCurrentState.setRouteID(0);

					/* TurnList info */
					NavCurrentState.setDistanceToGuidePoint(GuideTargetNum.TARGET_GUIDE_POINT_INVALID, -1, -1, null, DistanceUnit.UNIT_INVALID);

					/* TimeLine info */
					NavCurrentState.setDistanceToDestination(-1);
					NavCurrentState.setEstimatedArrivalTimeForDestination(-1, -1);
					NavCurrentState.setEstimatedArrivalTimeForWaypoint(WaypointNum.TARGET_WAYPOINT_INVALID, -1, -1, -1);
				} else {

					NavCurrentState.setDistanceToDestination(state.getDistance());
					NavCurrentState.setEstimatedArrivalTimeForDestination(state.getDestinationHour(), state.getDestinationMinute());

					//GuidePoint
					ArrayList<GuidepointDistance> guidePoints = state.getGuidePoints();
					for(int i=0; i<guidePoints.size(); i++) {
						GuidepointDistance point = (GuidepointDistance) guidePoints.get(i);
						NavCurrentState.setDistanceToGuidePoint(point.target, point.id, point.iDistance, point.cDistance, point.distanceUnit);
					}

					ArrayList<WaypointTime> waypointTimes = state.getWaypointTimes();
					for(WaypointTime time:waypointTimes) {
						NavCurrentState.setEstimatedArrivalTimeForWaypoint(time.target, time.id, time.hour, time.minute);
					}
				}
				NavCurrentState.updatedStatus();
			}


	    };
	    
		GlobalTrigger.getInstance().addTriggerListener(this);
	}
	
	public void setNaviListener(INaviListener listener) {
		mlistener = listener;
	}
	
	private boolean isHUDConnected() {
		return PHUDConnectManager.pIsConnected();
	}
	
	private boolean disConnect(){
		return PHUDConnectManager.pShutDownHUDConnect();
	}
	
	private void clear() {
		disConnect();
	}
	
	public void destroy() {
		if(!isHudVersion()) {
			return;
		}
		UIHudControl.detatchJ();
		clear();
		
		if(mHandlerThread != null){
			mHandlerThread.quit();
			mHandlerThread = null;
		}
		if(mHandler != null){
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		
	}
	

	private void sendMessageMyThread(int what) {
		mHandler.sendEmptyMessage(what);
	}

	private void sendMessageMyThread(int what, int arg1, int arg2) {
		Message msg = mHandler.obtainMessage(what, arg1, arg2);
		mHandler.sendMessage(msg);
	}

	/*
	 * start SDK Communicate
	 */
	public void onStartComm() {
	}
	
	/*
	 * stop SDK Communicate
	 */
	public void onStopComm() {
		mHandler.removeCallbacksAndMessages(null);
	}
	

	public void stopRouteGuideRequest() {

		clear();

		PHUDConnectManager.pEventNotify(NavEvent.EVENT_FINISH_ROUTE_GUIDE, 0);

		//remove API command related to the route
		mHandler.removeMessages(AR_ROUTE);
		mHandler.removeMessages(TARGET_POINT);
		mHandler.removeMessages(TIMELINE);
		mHandler.removeMessages(JUNCTION);
		mHandler.removeMessages(GUIDE_POINT);
		mHandler.removeMessages(ROUTE_RESTRICTION);
		mHandler.removeMessages(ROUTE_SPEEDCAMERA);
		mHandler.removeMessages(ROUTE_TRAFFICINFO);

	}

	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		if(triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_HUD_INFO_UPDATE) {
			return false;
		}
		
		if(triggerInfo.m_lParam1 == NOTIFICATION) {
			sendMessageMyThread((int)triggerInfo.m_lParam1, (int)triggerInfo.m_lParam2, (int)triggerInfo.m_lParam3);
		} else {
			sendMessageMyThread((int)triggerInfo.m_lParam1);
		}
		return true;
	}

	public void notifyHUDEvent(int eventID, int param1, int param2) {
//		if(eventID == NOTIFICATION) {
			sendMessageMyThread(eventID, param1, param2);
//		} else {
//			sendMessageMyThread(eventID);
//		}
	}
	
	/**
	 * over API-12(android 3.1, honeycomb), you can use USB manager.
	 * so API Level must over 12.
	 * 
	 * */
	public final boolean isHudVersion() {
		return (Build.VERSION.SDK_INT >= 12);
	}


}
