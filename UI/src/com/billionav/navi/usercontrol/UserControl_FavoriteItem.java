package com.billionav.navi.usercontrol;

public class UserControl_FavoriteItem {
	private String strFavoriteID;
	private int strFavoriteType;
	private UserControl_FavoritePosterItem item = null;
	
	
	public String getStrFavoriteID() {
		return strFavoriteID;
	}
	public void setStrFavoriteID(String strFavoriteID) {
		this.strFavoriteID = strFavoriteID;
	}
	public int getStrFavoriteType() {
		return strFavoriteType;
	}
	public void setStrFavoriteType(int strFavoriteType) {
		this.strFavoriteType = strFavoriteType;
	}
	public UserControl_FavoritePosterItem getItem() {
		return item;
	}
	public void setItem(UserControl_FavoritePosterItem item) {
		this.item = item;
	}
}
