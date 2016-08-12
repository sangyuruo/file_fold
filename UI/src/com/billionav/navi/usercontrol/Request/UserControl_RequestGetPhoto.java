package com.billionav.navi.usercontrol.Request;

public class UserControl_RequestGetPhoto extends UserControl_RequestBase {

	public UserControl_RequestGetPhoto(int iRequestId) {
		super(iRequestId);
		
	}
	private boolean DownloadUserPhotoByEmail(String email)
	{
		String sRequestUrl = GetAuthServerUrl();
		sRequestUrl += "accounts/profile/photo?";
		sRequestUrl+="email="+email;
		SendRequestByGet(sRequestUrl,true);
		return true;
	}
	private boolean DownloadUserPhotoByCellphone(String cellphone)
	{
		String sRequestUrl = GetAuthServerUrl();
		sRequestUrl += "accounts/profile/photo?";
		sRequestUrl+="cellphone="+cellphone;
		SendRequestByGet(sRequestUrl,true);
		return true;
	}
	private boolean DownloadUserPhotoByUserId(String userId)
	{
		String sRequestUrl = GetAuthServerUrl();
		sRequestUrl += "accounts/profile/photo?";
		sRequestUrl+="id="+userId;
		SendRequestByGet(sRequestUrl,true);
		return true;
	}
	private boolean DownloadUserPhotoMyself()
	{
		String sRequestUrl = GetAuthServerUrl();
		sRequestUrl += "accounts/profile/photo";
		SendRequestByGet(sRequestUrl,true);
		return true;
	}
	
	public boolean DownloadUserPhoto(String email,String cellphone,String userId )
	{
		if(0 != email.compareTo(""))
		{
			return DownloadUserPhotoByEmail(email);
		}
		else
			if(0 != cellphone.compareTo(""))
			{
				return  DownloadUserPhotoByCellphone(cellphone);
			}
			else
				if(0 != userId.compareTo(""))
				{
					return DownloadUserPhotoByUserId(userId);
				}
				else
				{
					return  DownloadUserPhotoMyself();
				}
	}
	
	
}
