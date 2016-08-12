package com.billionav.voicerecog;

public class MusicInfo {
	public String artist;
	public String album;
	public String genre;
	public String playlist;
	public String song;
	
	static public boolean isNotEmpty(String text) {
		return (null != text && text.trim().length() > 0);
	}
	
	public boolean isEmpty() {
		if (isNotEmpty(artist))
			return false;
		if (isNotEmpty(album))
			return false;
		if (isNotEmpty(genre))
			return false;
		if (isNotEmpty(playlist))
			return false;
		if (isNotEmpty(song))
			return false;
		return true;
	}
	
	public String toString() {
		return "{" + "artist:"+ artist + ",song:" + song + ",album"+ album 
			+ ",genre:" + genre+ ",playlist:" + playlist +"}";
	}
}
