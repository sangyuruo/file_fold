package com.billionav.navi.naviscreen.favorite;

import java.io.Serializable;

public class PointdataBackup implements Serializable{
	/**
	 * this class is used save backup of pointdata.
	 */
	private static final long serialVersionUID = -1752977892712248484L;
	long[] lonlat = new long[2];
	int recorid;
	String name = "";
	String address = "";
	String tel = "";
//	public PointdataBackup(jniUIC_PNT_PointData_new j_pointData_new) {
//		// TODO Auto-generated constructor stub
//		if(j_pointData_new != null){
//			name = j_pointData_new.getName();
//			address = j_pointData_new.getAddress();
//			tel = j_pointData_new.getTel();
//			recorid = j_pointData_new.getRecordID();
//			lonlat[0] = j_pointData_new.GetLonLat()[0];
//			lonlat[1] = j_pointData_new.GetLonLat()[1];
//		}
//	}
	public long[] getLonlat() {
		return lonlat;
	}
	public void setLonlat(long[] lonlat) {
		this.lonlat = lonlat;
	}
	public int getRecorid() {
		return recorid;
	}
	public void setRecorid(int recorid) {
		this.recorid = recorid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
}
