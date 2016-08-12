package com.billionav.navi.usercontrol.Request.SNS;


public class UserControl_RequestPosterUserFeedback extends UserControl_RequestSNSBase{
	String strCommand ;
	String strPostID;
	
	public UserControl_RequestPosterUserFeedback(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestGetUserFeedBack() {
		if ("".equals(strCommand) || "".equals(strPostID)) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "sns/userfeedback?command=" + strCommand + "&postid=" + strPostID;
		setM_strPassParam01(strPostID);
		SendRequestByGet(sRequestUrl, true);
		return true;
	}

	public String getStrCommand() {
		return strCommand;
	}

	public void setStrCommand(String strCommand) {
		this.strCommand = strCommand;
	}

	public String getStrPostID() {
		return strPostID;
	}

	public void setStrPostID(String strPostID) {
		this.strPostID = strPostID;
	}
	
	

}
