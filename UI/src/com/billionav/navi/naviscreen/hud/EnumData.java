package com.billionav.navi.naviscreen.hud;

import jp.pioneer.huddevelopkit.HUDConstants.AlertKind;
import jp.pioneer.huddevelopkit.HUDConstants.CarColor;
import jp.pioneer.huddevelopkit.HUDConstants.DestinationColor;
import jp.pioneer.huddevelopkit.HUDConstants.DisplayGroup;
import jp.pioneer.huddevelopkit.HUDConstants.DistanceUnit;
import jp.pioneer.huddevelopkit.HUDConstants.GeodeticDatumType;
import jp.pioneer.huddevelopkit.HUDConstants.GpsState;
import jp.pioneer.huddevelopkit.HUDConstants.GuideColor;
import jp.pioneer.huddevelopkit.HUDConstants.GuideTargetNum;
import jp.pioneer.huddevelopkit.HUDConstants.LaneAttribute;
import jp.pioneer.huddevelopkit.HUDConstants.LaneIncrease;
import jp.pioneer.huddevelopkit.HUDConstants.LineColor;
import jp.pioneer.huddevelopkit.HUDConstants.MapColor;
import jp.pioneer.huddevelopkit.HUDConstants.NavEvent;
import jp.pioneer.huddevelopkit.HUDConstants.RoadDirection;
import jp.pioneer.huddevelopkit.HUDConstants.RoundaboutGateway;
import jp.pioneer.huddevelopkit.HUDConstants.RouteGranularity;
import jp.pioneer.huddevelopkit.HUDConstants.RouteTrafficType;
import jp.pioneer.huddevelopkit.HUDConstants.SettingBackground;
import jp.pioneer.huddevelopkit.HUDConstants.SignalState;
import jp.pioneer.huddevelopkit.HUDConstants.SpeedCameraGroupMask;
import jp.pioneer.huddevelopkit.HUDConstants.TimeFormat;
import jp.pioneer.huddevelopkit.HUDConstants.TimeLineColor;
import jp.pioneer.huddevelopkit.HUDConstants.TrafficStateColor;
import jp.pioneer.huddevelopkit.HUDConstants.UnitTypeSystem;
import jp.pioneer.huddevelopkit.HUDConstants.WaypointColor;
import jp.pioneer.huddevelopkit.HUDConstants.WaypointNum;
import android.util.SparseArray;

public class EnumData {
	private EnumData(){};
	
	private static final SparseArray<CarColor> carColor = new SparseArray<CarColor>(5);
	private static final SparseArray<DestinationColor> destinationColor = new SparseArray<DestinationColor>(5);
	private static final SparseArray<WaypointColor> waypointColor = new SparseArray<WaypointColor>(5);
	private static final SparseArray<GeodeticDatumType> geodeticDatumType = new SparseArray<GeodeticDatumType>(5);
	
	private static final SparseArray<GpsState> gpsState = new SparseArray<GpsState>(2);
	private static final SparseArray<SignalState> signalState = new SparseArray<SignalState>(5);
	// current state
	private static final SparseArray<MapColor> mapColor = new SparseArray<MapColor>(2);
	private static final SparseArray<TimeFormat> timeFormat = new SparseArray<TimeFormat>(5);
	private static final SparseArray<UnitTypeSystem> unitTypeSystem = new SparseArray<UnitTypeSystem>(5);
	private static final SparseArray<SettingBackground> settingBackground = new SparseArray<SettingBackground>(5);
	private static final SparseArray<SpeedCameraGroupMask> speedCameraGroupMask = new SparseArray<SpeedCameraGroupMask>(5);
	private static final SparseArray<GuideTargetNum> guideTargetNum = new SparseArray<GuideTargetNum>(5);
	private static final SparseArray<DistanceUnit> distanceUnit = new SparseArray<DistanceUnit>(5);
	private static final SparseArray<WaypointNum> waypointNum = new SparseArray<WaypointNum>(5);
	
	//ar route
	private static final SparseArray<RouteGranularity> routeGranularity= new SparseArray<RouteGranularity>(2);
	private static final SparseArray<LineColor> lineColor = new SparseArray<LineColor>(5);
	//alert
	private static final SparseArray<AlertKind> alertKind = new SparseArray<AlertKind>(5);
	//time line
	private static final SparseArray<TimeLineColor> timeLineColor = new SparseArray<TimeLineColor>(5);
	//notification
	private static final SparseArray<NavEvent> navEvent = new SparseArray<NavEvent>(4);
	//junction
	private static final SparseArray<RoadDirection> roadDirection = new SparseArray<RoadDirection>(5);
	//around camera
	private static final SparseArray<DisplayGroup> displayGroup = new SparseArray<DisplayGroup>(3);
	// route traffic
	private static final SparseArray<RouteTrafficType> routeTrafficType = new SparseArray<RouteTrafficType>(2);
	private static final SparseArray<TrafficStateColor> trafficStateColor = new SparseArray<TrafficStateColor>(3);
	
	//guide point
	private static final SparseArray<RoundaboutGateway> roundaboutGateway = new SparseArray<RoundaboutGateway>(3);
	private static final SparseArray<GuideColor> guideColor = new SparseArray<GuideColor>(6);
	private static final SparseArray<LaneAttribute> laneAttribute = new SparseArray<LaneAttribute>(3);
	private static final SparseArray<LaneIncrease> laneIncrease = new SparseArray<LaneIncrease>(3);
	
	
	public static CarColor getCarcolor(int index) {
		return carColor.get(index, CarColor.SPEC_CAR_COLOR_RED);
	}

	public static DestinationColor getDestinationcolor(int index) {
		return destinationColor.get(index, DestinationColor.SPEC_DESTINATION_COLOR_RED);
	}

	public static WaypointColor getWaypointcolor(int index) {
		return waypointColor.get(index, WaypointColor.SPEC_WAYPOINT_COLOR_RED);
	}

	public static GeodeticDatumType getGeodeticdatumtype(int index) {
		return geodeticDatumType.get(index, GeodeticDatumType.GEODETIC_TYPE_WGS84);
	}

	public static GpsState getGpsstate(int index) {
		return gpsState.get(index, GpsState.GPS_STATE_NG);
	}

	public static SignalState getSignalstate(int index) {
		return signalState.get(index, SignalState.OTHER_SIGNAL_STATE_NG);
	}

	public static MapColor getMapcolor(int index) {
		return mapColor.get(index, MapColor.MAP_COLOR_MODE_DAY);
	}

	public static TimeFormat getTimeformat(int index) {
		return timeFormat.get(index, TimeFormat.USE_24_HOUR_FORMAT);
	}

	public static UnitTypeSystem getUnittypesystem(int index) {
		return unitTypeSystem.get(index, UnitTypeSystem.UNIT_TYPE_IMPERIAL_SYSTEM);
	}

	public static SettingBackground getSettingbackground(int index) {
		return settingBackground.get(index, SettingBackground.BACKGROUND_GO_ON_ROUTE_GUIDANCE);
	}

	public static SpeedCameraGroupMask getSpeedcameragroupmask(int index) {
		return speedCameraGroupMask.get(index, SpeedCameraGroupMask.DISPLAY_GROUP_A);
	}

	public static GuideTargetNum getGuidetargetnum(int index) {
		return guideTargetNum.get(index, GuideTargetNum.TARGET_GUIDE_POINT_INVALID);
	}

	public static DistanceUnit getDistanceunit(int index) {
		return distanceUnit.get(index, DistanceUnit.UNIT_INVALID);
	}

	public static WaypointNum getWaypointnum(int index) {
		return waypointNum.get(index, WaypointNum.TARGET_WAYPOINT_INVALID);
	}

	public static RouteGranularity getRouteGranularity(int index) {
		return routeGranularity.get(index, RouteGranularity.ROUTE_GRANULARITY_FINE);
	}

	public static LineColor getLineColor(int index) {
		return lineColor.get(index, LineColor.AR_LINE_COLOR_RED);
	}

	public static AlertKind getalertKind(int index) {
		return alertKind.get(index, AlertKind.ALERT_KIND_SPEEDCAMERA);
	}

	public static TimeLineColor getTimeLineColor(int index) {
		return timeLineColor.get(index, TimeLineColor.TIMELINE_COLOR_RED);
	}

	public static NavEvent getNavEvent(int index) {
		return navEvent.get(index, NavEvent.EVENT_MAKE_NEW_ROUTE);
	}

	public static RoadDirection getRoadDirection(int index) {
		return roadDirection.get(index, RoadDirection.ROAD_DIRECTION_NONE);
	}

	public static DisplayGroup getDisplayGroup(int index) {
		return displayGroup.get(index, DisplayGroup.BELONG_GROUP_A);
	}

	public static RouteTrafficType getRouteTrafficType(int index) {
		return routeTrafficType.get(index, RouteTrafficType.ROUTE_TRAFFIC_TYPE_CROWD);
	}

	public static TrafficStateColor getTrafficStateColor(int index) {
		return trafficStateColor.get(index, TrafficStateColor.TRAFFIC_COLOR_RED);
	}

	public static RoundaboutGateway getRoundaboutGateway(int index) {
		return roundaboutGateway.get(index, RoundaboutGateway.GUIDE_ROUNDABOUT_INFO_INVALID);
	}
	
	public static GuideColor getGuideColor(int index) {
		return guideColor.get(index, GuideColor.GUIDE_BASE_COLOR_RED);
	}
	
	public static LaneAttribute getLaneAttribute(int index) {
		return laneAttribute.get(index, LaneAttribute.LANE_ATTRIBUTE_NON_GUIDED);
	}
	
	public static LaneIncrease getLaneIncrease(int index) {
		return laneIncrease.get(index, LaneIncrease.NONE_INCREASE_LANE);
	}
	
	static{
		carColor.append(1, CarColor.SPEC_CAR_COLOR_RED);
		carColor.append(2, CarColor.SPEC_CAR_COLOR_GREEN);
		carColor.append(3, CarColor.SPEC_CAR_COLOR_BLUE);
		carColor.append(4, CarColor.SPEC_CAR_COLOR_ORANGE);
		carColor.append(5, CarColor.SPEC_CAR_COLOR_YELLOW);
		carColor.append(6, CarColor.SPEC_CAR_COLOR_PURPLE);
		
		
		destinationColor.append(1, DestinationColor.SPEC_DESTINATION_COLOR_RED);
		destinationColor.append(2, DestinationColor.SPEC_DESTINATION_COLOR_GREEN);
		destinationColor.append(3, DestinationColor.SPEC_DESTINATION_COLOR_BLUE);
		destinationColor.append(4, DestinationColor.SPEC_DESTINATION_COLOR_ORANGE);
		destinationColor.append(5, DestinationColor.SPEC_DESTINATION_COLOR_YELLOW);
		destinationColor.append(6, DestinationColor.SPEC_DESTINATION_COLOR_PURPLE);
		
		waypointColor.append(1, WaypointColor.SPEC_WAYPOINT_COLOR_RED);
		waypointColor.append(2, WaypointColor.SPEC_WAYPOINT_COLOR_GREEN);
		waypointColor.append(3, WaypointColor.SPEC_WAYPOINT_COLOR_BLUE);
		waypointColor.append(4, WaypointColor.SPEC_WAYPOINT_COLOR_ORANGE);
		waypointColor.append(5, WaypointColor.SPEC_WAYPOINT_COLOR_YELLOW);
		waypointColor.append(6, WaypointColor.SPEC_WAYPOINT_COLOR_PURPLE);
		
		geodeticDatumType.append(1, GeodeticDatumType.GEODETIC_TYPE_WGS84);
		geodeticDatumType.append(2, GeodeticDatumType.GEODETIC_TYPE_Tokyo);
		geodeticDatumType.append(3, GeodeticDatumType.GEODETIC_TYPE_BJ54);

		gpsState.append(1, GpsState.GPS_STATE_NG);
		gpsState.append(2, GpsState.GPS_STATE_OK);
		
		signalState.append(1, SignalState.OTHER_SIGNAL_STATE_NG);
		signalState.append(2, SignalState.OTHER_SIGNAL_STATE_OK);
		
		mapColor.append(0, MapColor.MAP_COLOR_MODE_DAY);
		mapColor.append(1, MapColor.MAP_COLOR_MODE_NIGHT);
		mapColor.append(2, MapColor.MAP_COLOR_MODE_DAWN);
		mapColor.append(3, MapColor.MAP_COLOR_MODE_TWILIGHT);
		
		timeFormat.append(0, TimeFormat.USE_24_HOUR_FORMAT);
		timeFormat.append(1, TimeFormat.USE_12_HOUR_FORMAT);
		
		unitTypeSystem.append(0, UnitTypeSystem.UNIT_TYPE_METRIC_SYSTEM);
		unitTypeSystem.append(1, UnitTypeSystem.UNIT_TYPE_IMPERIAL_SYSTEM);
		
		settingBackground.append(0, SettingBackground.BACKGROUND_GO_ON_ROUTE_GUIDANCE);
		settingBackground.append(1, SettingBackground.BACKGROUND_SUSPEND_ROUTE_GUIDANCE);
		
		speedCameraGroupMask.append(0x0001, SpeedCameraGroupMask.DISPLAY_GROUP_A);
		speedCameraGroupMask.append(0x0002, SpeedCameraGroupMask.DISPLAY_GROUP_B);
		speedCameraGroupMask.append(0x0004, SpeedCameraGroupMask.DISPLAY_GROUP_C);
		
		guideTargetNum.append(-1, GuideTargetNum.TARGET_GUIDE_POINT_INVALID);
		guideTargetNum.append(1, GuideTargetNum.TARGET_GUIDE_POINT_1st);
		guideTargetNum.append(2, GuideTargetNum.TARGET_GUIDE_POINT_2nd);
		guideTargetNum.append(3, GuideTargetNum.TARGET_GUIDE_POINT_3rd);
		
		distanceUnit.append(-1, DistanceUnit.UNIT_INVALID);
		distanceUnit.append(0, DistanceUnit.UNIT_KILOMETER);
		distanceUnit.append(1, DistanceUnit.UNIT_METER);
		distanceUnit.append(2, DistanceUnit.UNIT_MILE);
		distanceUnit.append(3, DistanceUnit.UNIT_YARDS);
		distanceUnit.append(4, DistanceUnit.UNIT_FEET);
		
		waypointNum.append(-1, WaypointNum.TARGET_WAYPOINT_INVALID);
		waypointNum.append(0, WaypointNum.TARGET_WAYPOINT_1st);
		waypointNum.append(1, WaypointNum.TARGET_WAYPOINT_2nd);
		waypointNum.append(2, WaypointNum.TARGET_WAYPOINT_3rd);
		waypointNum.append(3, WaypointNum.TARGET_WAYPOINT_4th);
		waypointNum.append(4, WaypointNum.TARGET_WAYPOINT_5th);
		
		routeGranularity.append(0x0481, RouteGranularity.ROUTE_GRANULARITY_FINE);
		routeGranularity.append(0x0482, RouteGranularity.ROUTE_GRANULARITY_COARSE);
		
		
		lineColor.append(1, LineColor.AR_LINE_COLOR_RED);
		lineColor.append(2, LineColor.AR_LINE_COLOR_GREEN);
		lineColor.append(3, LineColor.AR_LINE_COLOR_BLUE);
		lineColor.append(4, LineColor.AR_LINE_COLOR_ORANGE);
		lineColor.append(5, LineColor.AR_LINE_COLOR_YELLOW);
		lineColor.append(6, LineColor.AR_LINE_COLOR_PURPLE);
		
		alertKind.append(1, AlertKind.ALERT_KIND_SPEEDCAMERA);
		alertKind.append(2, AlertKind.ALERT_KIND_TRAFFIC);
		
		timeLineColor.append(1, TimeLineColor.TIMELINE_COLOR_RED);
		timeLineColor.append(2, TimeLineColor.TIMELINE_COLOR_GREEN);
		timeLineColor.append(3, TimeLineColor.TIMELINE_COLOR_BLUE);
		timeLineColor.append(4, TimeLineColor.TIMELINE_COLOR_ORANGE);
		timeLineColor.append(5, TimeLineColor.TIMELINE_COLOR_YELLOW);
		timeLineColor.append(6, TimeLineColor.TIMELINE_COLOR_PURPLE);
		
		navEvent.append(0, NavEvent.EVENT_MAKE_NEW_ROUTE);
		navEvent.append(1, NavEvent.EVENT_FINISH_ROUTE_GUIDE);
		navEvent.append(2, NavEvent.EVENT_REROUTE_EXIST_MILEAGE);
		navEvent.append(3, NavEvent.EVENT_REROUTE_NON_MILEAGE);
		
		roadDirection.append(0, RoadDirection.ROAD_DIRECTION_NONE);
		roadDirection.append(1, RoadDirection.ROAD_DIRECTION_EAST);
		roadDirection.append(2, RoadDirection.ROAD_DIRECTION_WEST);
		roadDirection.append(3, RoadDirection.ROAD_DIRECTION_SOUTH);
		roadDirection.append(4, RoadDirection.ROAD_DIRECTION_NONE);

		displayGroup.append(0, DisplayGroup.BELONG_GROUP_A);
		displayGroup.append(1, DisplayGroup.BELONG_GROUP_B);
		displayGroup.append(2, DisplayGroup.BELONG_GROUP_C);
		
		routeTrafficType.append(1, RouteTrafficType.ROUTE_TRAFFIC_TYPE_CROWD);
		routeTrafficType.append(2, RouteTrafficType.ROUTE_TRAFFIC_TYPE_JAM);
		
		trafficStateColor.append(1, TrafficStateColor.TRAFFIC_COLOR_RED);
		trafficStateColor.append(4, TrafficStateColor.TRAFFIC_COLOR_ORANGE);
		trafficStateColor.append(5, TrafficStateColor.TRAFFIC_COLOR_YELLOW);
		
		roundaboutGateway.append(0, RoundaboutGateway.GUIDE_ROUNDABOUT_INFO_INVALID);
		roundaboutGateway.append(1, RoundaboutGateway.GUIDE_ROUNDABOUT_INFO_ENTRANCE);
		roundaboutGateway.append(2, RoundaboutGateway.GUIDE_ROUNDABOUT_INFO_EXIT);
		
		guideColor.append(1, GuideColor.GUIDE_BASE_COLOR_RED);
		guideColor.append(2, GuideColor.GUIDE_BASE_COLOR_GREEN);
		guideColor.append(3, GuideColor.GUIDE_BASE_COLOR_BLUE);
		guideColor.append(4, GuideColor.GUIDE_BASE_COLOR_ORANGE);
		guideColor.append(5, GuideColor.GUIDE_BASE_COLOR_YELLOW);
		guideColor.append(6, GuideColor.GUIDE_BASE_COLOR_PURPLE);
		
		
		laneAttribute.append(1, LaneAttribute.LANE_ATTRIBUTE_RECOMMENDED);
		laneAttribute.append(2, LaneAttribute.LANE_ATTRIBUTE_GUIDED);
		laneAttribute.append(3, LaneAttribute.LANE_ATTRIBUTE_NON_GUIDED);
		
		laneIncrease.append(0, LaneIncrease.NONE_INCREASE_LANE);
		laneIncrease.append(1, LaneIncrease.RIGHT_INCREASE_LANE);
		laneIncrease.append(2, LaneIncrease.LEFT_INCREASE_LANE);

	}
	
	


}
