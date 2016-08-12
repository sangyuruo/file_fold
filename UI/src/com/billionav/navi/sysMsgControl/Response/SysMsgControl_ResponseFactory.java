package com.billionav.navi.sysMsgControl.Response;

import java.util.HashMap;

import android.util.Log;

import com.billionav.navi.sysMsgControl.SysMsgControl_RequestType;


public class SysMsgControl_ResponseFactory {

	private static SysMsgControl_ResponseFactory m_sSysMsgcFactory = new SysMsgControl_ResponseFactory();
	private static long m_sRequestID = 0;
	private static HashMap m_lUserRequestMap = new HashMap<String,SysMsgControl_ResponseBase>();
	
	public static SysMsgControl_ResponseFactory Instance() {
		return m_sSysMsgcFactory;
	}
	
	public SysMsgControl_ResponseBase CreateResponse(int iRequestID) {
		SysMsgControl_ResponseBase response = null;
		switch (iRequestID) {
			case SysMsgControl_RequestType.SM_REQ_MESSAGE_ID:
				response = new SysMsgControl_ResponseGetSysMsg(iRequestID);
				break;
		}
		response.setM_lUserRequestID(GetRequestID());
		m_lUserRequestMap.put(response.getM_lUserRequestID() + "", response);
		return response;
	}

	private synchronized long GetRequestID() {
		if(m_sRequestID >= Long.MAX_VALUE) {
			m_sRequestID = 0;
		}
		return ++m_sRequestID;
	}
	
	private boolean CancleUserRequestBase(long iRequestID) {
		String key = iRequestID + "";
		if(m_lUserRequestMap.containsKey(key)) {
			SysMsgControl_ResponseBase request = (SysMsgControl_ResponseBase)m_lUserRequestMap.get(key);
			return request.cancelRequest();
		}
		return false;
	}
	
	public void RemoveUserRequest(long iRequestID) {
		String key = iRequestID + "";
		if(m_lUserRequestMap.containsKey(key)) {
			m_lUserRequestMap.remove(key);
			Log.d("[DataSyncControl]", "[DataSyncControl]--UserRequestID:RemoveUserRequest " + key);
		}
	}
	
	public boolean CancleUserRequest(long iRequestID){
		if(CancleUserRequestBase(iRequestID)) {
			Log.d("[DataSyncControl]", "[DataSyncControl]--UserRequestID:CancleUserRequest success" + iRequestID);
			RemoveUserRequest(iRequestID);
			return true;
		} else {
			Log.d("[DataSyncControl]", "[DataSyncControl]--UserRequestID:CancleUserRequest Failed" + iRequestID);
			return false;
		}
		
	}
	
	public static long getM_sRequestID() {
		return m_sRequestID;
	}

	public static void setM_sRequestID(long m_sRequestID) {
		SysMsgControl_ResponseFactory.m_sRequestID = m_sRequestID;
	}
}
