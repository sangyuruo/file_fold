package com.billionav.navi.usercontrol.Response.SNS;

import com.billionav.navi.usercontrol.UserControl_CommonVar;

public class UserControl_ResponsePosterGetPhoto extends UserControl_ResponseSNSBase{
	private byte[] bImage = null;
	
	public UserControl_ResponsePosterGetPhoto(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_PM_GET_PHOTO);
	}
	
	public boolean ParseResultInfo(byte[] inputData) {
		//get Image Bytes
		bImage = inputData;
		return true;
	}
	

	public byte[] getbImage() {
		return bImage;
	}

	public void setbImage(byte[] bImage) {
		this.bImage = bImage;
	}
	
	
	
}
