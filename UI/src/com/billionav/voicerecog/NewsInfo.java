package com.billionav.voicerecog;

public class NewsInfo {
	private String title ;
	private String description;
	private String source; // optional
	private String link; // optional
	
	public NewsInfo(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
	
	
}
