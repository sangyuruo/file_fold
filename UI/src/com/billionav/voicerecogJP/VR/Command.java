package com.billionav.voicerecogJP.VR;



public class Command {
	public static final int CMD_UNNOWN 		= 0x1fffffff;	// Unknown command
	public static final int CMD_HOME 		= 0x10000001;
	public static final int CMD_JAM 		= 0x10000002;

	private Integer cmd;
	private String content;
	
	public Command(Integer cmd, String content) {
		this.cmd = cmd;
		this.content = content;
	}
	
	public Integer getCmd() {
		return cmd;
	}
	
	public int getCmdCode() {
		return (null != cmd) ? cmd.intValue() : 0;
	}
	
	public String getContent() {
		return content;
	}
}
