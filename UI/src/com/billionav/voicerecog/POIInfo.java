package com.billionav.voicerecog;

public class POIInfo {
	private String name;
	private String addr;
	private String distance;
	private String genreid;
	private String lat;
	private String lon;
	private String poiid;
	private String tel;
	
	private String linkid;
	private String linkside;
	
	public String getName() {
		return name;
	}

	public String getAddr() {
		return addr;
	}

	public String getDistance() {
		return distance;
	}

	public String getGenreid() {
		return genreid;
	}

	public String getLat() {
		return lat;
	}

	public String getLon() {
		return lon;
	}

	public String getPoiid() {
		return poiid;
	}

	public String getTel() {
		return tel;
	}

	public String getLinkid() {
		return linkid;
	}

	public String getLinkside() {
		return linkside;
	}

	protected void setName(String name) {
//		try{
//			this.name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
//		}catch(Exception ex) {
			this.name = name;
//		}
	}
	
	protected void setAddr(String addr) {
//		try{
//			this.addr = new String(addr.getBytes("ISO-8859-1"), "UTF-8");
//		}catch(Exception ex) {
			this.addr = addr;
//		}
	}

	protected void setDistance(String distance) {
		this.distance = distance;
	}

	protected void setGenreid(String genreid) {
		this.genreid = genreid;
	}

	protected void setLat(String lat) {
		this.lat = lat;
	}

	protected void setLon(String lon) {
		this.lon = lon;
	}

	protected void setPoiid(String poiid) {
		this.poiid = poiid;
	}

	protected void setTel(String tel) {
		this.tel = tel;
	}

	public void setLinkid(String linkid) {
		this.linkid = linkid;
	}

	public void setLinkside(String linkside) {
		this.linkside = linkside;
	}
}