package com.billionav.navi.app.ext.surface;

public enum NaviSurfaceType {
	DEFAULT(0),
	
	;
	NaviSurfaceType(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	int type;
}
