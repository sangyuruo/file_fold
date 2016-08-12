package com.billionav.navi.sysMsgControl.Request;

import com.billionav.navi.sysMsgControl.SysMsgControl_RequestType;

public class SysMsgControl_RequestFactory {

private static SysMsgControl_RequestFactory m_cSysMsgControlRequestFactory = new SysMsgControl_RequestFactory();
	
	private SysMsgControl_RequestFactory(){
	}
	
	public static SysMsgControl_RequestFactory Instance(){
		return m_cSysMsgControlRequestFactory;
	}
	
	public SysMsgControl_RequestBase CreateRequest(int iRequesType){
		SysMsgControl_RequestBase request = null;
		switch (iRequesType) {
			case SysMsgControl_RequestType.SM_REQ_MESSAGE_ID:
				request = new SysMsgControl_RequestGetSysMsg(iRequesType);
				break;
		}
		return request;
	}
}
