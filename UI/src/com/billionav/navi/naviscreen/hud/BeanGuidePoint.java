package com.billionav.navi.naviscreen.hud;

import java.util.ArrayList;

import jp.pioneer.huddevelopkit.DataGuidePoint;
import jp.pioneer.huddevelopkit.DataLane;

public class BeanGuidePoint {
	private int routeId;
	private ArrayList<DataGuidePoint> guidePoints = new ArrayList<DataGuidePoint>();
	
	public BeanGuidePoint() {
	}
	
	public BeanGuidePoint(int routeId) {
		this.routeId = routeId;
	}
	
	public final void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public int getRouteId() {
		return routeId;
	}
	public ArrayList<DataGuidePoint> getGuidePoints() {
		return guidePoints;
	}

	public void addGuidePoint(
		int guidePointID,
		double 	latitude,	
		double 	longitude,	
		String guideName,	
		int  	image,	
		int  	roundaboutInfo,
		int  	guideColor,	
		String		roadNumber,
		int		roadNumberSignboard	
	) {
		guidePoints.add(new DataGuidePoint(guidePointID, 
				latitude, 
				longitude, 
				guideName, 
				image, 
				EnumData.getRoundaboutGateway(roundaboutInfo), 
				EnumData.getGuideColor(guideColor), 
				roadNumber, 
				roadNumberSignboard));
	}
	
	public void addLanToGuidePoint(int arrowImage, int attribute, int increase) {
		DataGuidePoint guidePoint = guidePoints.get(guidePoints.size()-1);
		if(guidePoint.laneList == null) {
			guidePoint.setLaneGuide(new ArrayList<DataLane>());
		}
		ArrayList<DataLane> list = guidePoint.laneList;
		list.add(new DataLane(arrowImage, EnumData.getLaneAttribute(attribute), EnumData.getLaneIncrease(increase)));
	}

}
