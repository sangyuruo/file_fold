package com.billionav.navi.datasynccontrol.Response;

import com.billionav.navi.net.PResponse;

public class DataSyncControl_ResponseSinglePOI  extends DataSyncControl_ResponseFileBase{

	public DataSyncControl_ResponseSinglePOI(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		int iResCode = getResCode();
		String strResFileName = "";
		strResFileName = getM_strResFileName();
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			//TODO
		} else {
			//TODO
			
		}
	}
	
	
}
