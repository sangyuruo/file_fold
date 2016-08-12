package com.billionav.voicerecog;

public class ContactInfo {
	private String name;
	private String phoneNo;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public String toString() {
		return "Name:" + name + "  phone:" + phoneNo;
	}
}
