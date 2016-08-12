package com.billionav.navi.service;

import java.io.Serializable;




public class NotificateModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3234676496641307969L;
	public int lon;
	public int lat;
	public String pointName;
	
	public long NotificateTime;
	public long arrivalTime;
	public long EtaTime;
	public String tag;
	public void copyFrom(NotificateModel model) {
		if(null == model){
			return;
		}
		this.lon = model.lon;
		this.lat = model.lat;
		this.pointName = model.pointName;
		
		this.NotificateTime = model.NotificateTime;
		this.arrivalTime = model.arrivalTime;
		this.EtaTime = model.EtaTime;
		this.tag = model.tag;
		
	}
}
