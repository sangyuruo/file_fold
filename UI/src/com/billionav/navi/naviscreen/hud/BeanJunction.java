package com.billionav.navi.naviscreen.hud;

import java.util.ArrayList;

import jp.pioneer.huddevelopkit.DataJunction;

public class BeanJunction {
	private int			routeID;
	private int   guidePointID;
	private boolean		leftSignBoard;	
	private boolean		rightSignBoard;	
	private int		type;
	private String		name;
	
	private ArrayList<DataJunction> Junctions = new ArrayList<DataJunction>();

	public int getRouteID() {
		return routeID;
	}

	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	public int getGuidePointID() {
		return guidePointID;
	}

	public void setGuidePointID(int guidePointID) {
		this.guidePointID = guidePointID;
	}

	public boolean isLeftSignBoard() {
		return leftSignBoard;
	}

	public void setLeftSignBoard(boolean leftSignBoard) {
		this.leftSignBoard = leftSignBoard;
	}

	public boolean isRightSignBoard() {
		return rightSignBoard;
	}

	public void setRightSignBoard(boolean rightSignBoard) {
		this.rightSignBoard = rightSignBoard;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<DataJunction> getJunctions() {
		return Junctions;
	}

	public void addJnnctionData(int dirction, String roadNumber, int roadNumberType) {
		Junctions.add(new DataJunction(EnumData.getRoadDirection(dirction), roadNumber, roadNumberType));
	}
}
