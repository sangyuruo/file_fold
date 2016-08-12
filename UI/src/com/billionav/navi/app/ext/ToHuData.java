package com.billionav.navi.app.ext;

/**
 * 
 * @author sangjun
 *
 */
public class ToHuData {
	byte[] dataToWrite;
	int start = 0;
	int len = 0 ;
	int frameCount = 0;
	
	public ToHuData(byte[] dataToWrite, int start, int len,int frameCount) {
		super();
		this.dataToWrite = dataToWrite;
		this.start = start;
		this.len = len;
		this.frameCount = frameCount;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}
	public byte[] getDataToWrite() {
		return dataToWrite;
	}
	public void setDataToWrite(byte[] dataToWrite) {
		this.dataToWrite = dataToWrite;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
}
