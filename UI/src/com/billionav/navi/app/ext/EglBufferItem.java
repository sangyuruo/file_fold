package com.billionav.navi.app.ext;

public class EglBufferItem {
	int[] mapBytes;
	int widthVehicle;
	int heightVehicle;
	int index;
	
	
	public EglBufferItem(int[] mapBytes, int widthVehicle, int heightVehicle, int index) {
		super();
		this.mapBytes = mapBytes;
		this.widthVehicle = widthVehicle;
		this.heightVehicle = heightVehicle;
		this.index= index;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int[] getMapBytes() {
		return mapBytes;
	}

	public void setMapBytes(int[] mapBytes) {
		this.mapBytes = mapBytes;
	}

	public int getWidthVehicle() {
		return widthVehicle;
	}

	public void setWidthVehicle(int widthVehicle) {
		this.widthVehicle = widthVehicle;
	}

	public int getHeightVehicle() {
		return heightVehicle;
	}

	public void setHeightVehicle(int heightVehicle) {
		this.heightVehicle = heightVehicle;
	}

}
