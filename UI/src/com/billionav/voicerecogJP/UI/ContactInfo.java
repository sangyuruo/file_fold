package com.billionav.voicerecogJP.UI;

import java.io.Serializable;

import android.net.Uri;

public class ContactInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = null;
	private String number = null;
	private String photo = null;
	private Uri uri = null;
	private Long photoID;
	private Long contactid;
	
	public Uri getUri(){
		return uri;
	}
	public void setUri(Uri uri){
		this.uri = uri;
	}
	public String getPhoto() {
		return photo;
	}
	public Long getPhotoID() {
		return photoID;
	}
	public void setPhotoID(Long photoID) {
		this.photoID = photoID;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getName() {
		return name;
	}
	public Long getContactid() {
		return contactid;
	}
	public void setContactid(Long contactid) {
		this.contactid = contactid;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
}
