package com.billionav.navi.usercontrol.Request;

public class UserControl_RequestGetResource extends UserControl_RequestBase{
	public UserControl_RequestGetResource(int iRequestId) {
		super(iRequestId);
		
	}

	public boolean GetResource(String type,String id,String email,String cellphone)
	{
		if(0 == type.compareTo(""))
			return false;
		String sRequestUrl = GetAuthServerUrl();
		sRequestUrl += "sns/getresource?";
		sRequestUrl+="type="+type;
		if(0 != id.compareTo(""))
		{
			sRequestUrl+="&id="+id;
		}
		else
			if(0 != email.compareTo(""))
			{
				sRequestUrl+="&email="+email;
			}
			else
				if(0 != cellphone.compareTo(""))
				{
					sRequestUrl+="&cellphone="+cellphone;
				}
		SendRequestByGet(sRequestUrl,true);
		return true;
	}
}
