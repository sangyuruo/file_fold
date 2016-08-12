package com.billionav.navi.usercontrol;

import java.util.ArrayList;

public class UserControl_PositionResData {
	
	private ArrayList<UserControl_PositionMessage> m_cListMessage = null;
	private ArrayList<UserControl_PositionConfig> m_cListConfig = null;
	
	public ArrayList<UserControl_PositionMessage> getM_cListMessage() {
		return m_cListMessage;
	}
	public void setM_cListMessage(
			ArrayList<UserControl_PositionMessage> m_cListMessage) {
		this.m_cListMessage = m_cListMessage;
	}
	public ArrayList<UserControl_PositionConfig> getM_cListConfig() {
		return m_cListConfig;
	}
	public void setM_cListConfig(ArrayList<UserControl_PositionConfig> m_cListConfig) {
		this.m_cListConfig = m_cListConfig;
	}
	
	
}
