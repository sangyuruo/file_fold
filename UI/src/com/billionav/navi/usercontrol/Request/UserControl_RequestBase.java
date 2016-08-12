package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PMultiPart;
import com.billionav.navi.net.PRequest;
import com.billionav.navi.net.PThreadManager;
import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseFactory;
 
public class UserControl_RequestBase {
	private UserControl_ResponseBase m_cResponse = null;
	private String m_strServerUrl ;
	private String m_strAuthServerUrl ;
	
	private int m_iRequestId = 0;
	private int m_iPassedParam;
	private int m_strPassedParam;
	private long m_iUserRequestID;
	
	public UserControl_RequestBase(int iRequestId) {
		m_iRequestId = iRequestId;
		if (null == m_cResponse) {
			m_cResponse = UserControl_ResponseFactory.Instance().CreateResponse(iRequestId);
		}

		m_strServerUrl = UserControl_ManagerIF.Instance().GetSRVUrl();
		m_strAuthServerUrl = UserControl_ManagerIF.Instance().GetAuthSRVUrl();

	}

	public String GetServerUrl() {
		return m_strServerUrl;
	}

	public String GetAuthServerUrl() {
		return m_strAuthServerUrl;
	}
	
	public int GetRequestId() {
		return m_iRequestId;
	}
	
	public UserControl_ResponseBase GetResponseBase() {
		return m_cResponse;
	}
	
	private void PassParam() {
		m_cResponse.setM_iPassedParam(m_iPassedParam);
		m_cResponse.setM_strPassedParam(m_strPassedParam);
	}
	
	public void SendRequestByPost(String url,PostData postData,boolean HasSessionToken) {
		if (null != m_cResponse) {
			m_cResponse.setURL(url);
			m_cResponse.setMethod(PRequest.METHODS_POST);
			m_cResponse.setResDataType(PRequest.RESPONSE_DATA_BUF); 	
			m_cResponse.setAuthFlag(HasSessionToken);
			m_cResponse.setPostData(postData);
			PassParam();
			PThreadManager.instance().PostRequest(m_cResponse);
		}
	}

	public void SendRequestByGet(String url,boolean HasSessionToken)
	{
		if (null != m_cResponse) {
			m_cResponse.setURL(url);
			m_cResponse.setMethod(PRequest.METHODS_GET);
			m_cResponse.setResDataType(PRequest.RESPONSE_DATA_BUF); 
			m_cResponse.setAuthFlag(HasSessionToken);
			PassParam();
			PThreadManager.instance().PostRequest(m_cResponse);
		}
	}

	

	public List<NameValuePair> ParamsFromUserinfo(UserControl_UserInfo userInfo)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(0 != userInfo.m_strEmail.compareTo(""))
		{
			params.add(new BasicNameValuePair("email",userInfo.m_strEmail));
		}
		
		if(0 != userInfo.m_strNickName.compareTo(""))
		{
			params.add(new BasicNameValuePair("nickname",userInfo.m_strNickName));
		}
		
		if(0 != userInfo.m_strUserPassWord.compareTo(""))
		{
			params.add(new BasicNameValuePair("pwd",userInfo.m_strUserPassWord));
		}
		
		if(0 != userInfo.m_strPhoneNum.compareTo(""))
		{
			params.add(new BasicNameValuePair("cellphone",userInfo.m_strPhoneNum));
		}
		
		if(0 != userInfo.m_strBirthday.compareTo("")){
			params.add(new BasicNameValuePair("birthday",userInfo.m_strBirthday));
		}
		
		if(0 != userInfo.m_strSex.compareTo("")){
			params.add(new BasicNameValuePair("gender",userInfo.m_strSex));
		}
		
		if(0 != userInfo.m_strHomeAddress.compareTo("")){
			params.add(new BasicNameValuePair("address",userInfo.m_strHomeAddress));
		}
		if(0 != userInfo.m_strMicroBlogURL.compareTo("")){
			params.add(new BasicNameValuePair("blog",userInfo.m_strMicroBlogURL));
		}
		
		if(0 != userInfo.m_strCompany.compareTo("")){
			params.add(new BasicNameValuePair("company",userInfo.m_strCompany));
		}
		
		if(0 != userInfo.m_strDescription.compareTo("")){
			params.add(new BasicNameValuePair("description",userInfo.m_strDescription));
		}
		
		if(0 != userInfo.m_strEducation.compareTo("")){
			params.add(new BasicNameValuePair("education",userInfo.m_strEducation));
		}
		
		if(0 != userInfo.m_strSchool.compareTo("")){
			params.add(new BasicNameValuePair("school",userInfo.m_strSchool));
		}
		
		if(0 != userInfo.m_strRealname.compareTo("")){
			params.add(new BasicNameValuePair("realname",userInfo.m_strRealname));
		}
		
		if(0 != userInfo.m_strOccupation.compareTo("")){
			params.add(new BasicNameValuePair("occupation",userInfo.m_strOccupation));
		}
		
		if(0 != userInfo.m_strMSN.compareTo("")){
			params.add(new BasicNameValuePair("MSN",userInfo.m_strMSN));
		}
		
		if(0 != userInfo.m_strQQ.compareTo("")){
			params.add(new BasicNameValuePair("QQ",userInfo.m_strQQ));
		}
		
		if(0 != userInfo.m_strInvitecode.compareTo("")){
			params.add(new BasicNameValuePair("invitecode",userInfo.m_strInvitecode));
		}
		
		if(null != userInfo.m_byPhoto){
			params.add(new BasicNameValuePair("photo",new String( userInfo.m_byPhoto) ) );
		}
		
		 return params;
		


	}

	public List<PMultiPart> PMultiPartFromUserinfo(UserControl_UserInfo userInfo)
	{
		List<PMultiPart> params = new ArrayList<PMultiPart>();
		if(0 != userInfo.m_strEmail.compareTo(""))
		{
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING, "email", userInfo.m_strEmail, null) );
		}
		
		if(0 != userInfo.m_strNickName.compareTo(""))
		{
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"nickname",userInfo.m_strNickName, null));
		}
		
		if(0 != userInfo.m_strUserPassWord.compareTo(""))
		{
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"pwd",userInfo.m_strUserPassWord, null));
		}
		
		if(0 != userInfo.m_strPhoneNum.compareTo(""))
		{
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"cellphone",userInfo.m_strPhoneNum, null));
		}
		
		if(0 != userInfo.m_strBirthday.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"birthday",userInfo.m_strBirthday, null));
		}
		
		if(0 != userInfo.m_strSex.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"gender",userInfo.m_strSex, null));
		}
		
		if(0 != userInfo.m_strHomeAddress.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"address",userInfo.m_strHomeAddress, null));
		}
		if(0 != userInfo.m_strMicroBlogURL.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"blog",userInfo.m_strMicroBlogURL, null));
		}
		
		if(0 != userInfo.m_strCompany.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"company",userInfo.m_strCompany, null));
		}
		
		if(0 != userInfo.m_strDescription.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"description",userInfo.m_strDescription, null));
		}
		
		if(0 != userInfo.m_strEducation.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"education",userInfo.m_strEducation, null));
		}
		
		if(0 != userInfo.m_strSchool.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"school",userInfo.m_strSchool, null));
		}
		
		if(0 != userInfo.m_strRealname.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"realname",userInfo.m_strRealname, null));
		}
		
		if(0 != userInfo.m_strOccupation.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"occupation",userInfo.m_strOccupation, null));
		}
		
		if(0 != userInfo.m_strMSN.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"MSN",userInfo.m_strMSN, null));
		}
		
		if(0 != userInfo.m_strQQ.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"QQ",userInfo.m_strQQ, null));
		}
		
		if(0 != userInfo.m_strInvitecode.compareTo("")){
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_STRING,"invitecode",userInfo.m_strInvitecode, null));
		}
		
		if(0 != UserControl_ManagerIF.Instance().getUserPhotoPath().compareTo("")) {
			params.add(new PMultiPart(PMultiPart.MULTI_TYPE_FILE,"photo",UserControl_ManagerIF.Instance().getUserPhotoPath(), null) );
		}
		
		 return params;
		


	}

	private UserControl_RequestBase() {
	}

	public int getM_iPassedParam() {
		return m_iPassedParam;
	}

	public void setM_iPassedParam(int m_iPassedParam) {
		this.m_iPassedParam = m_iPassedParam;
	}

	public int getM_strPassedParam() {
		return m_strPassedParam;
	}

	public void setM_strPassedParam(int m_strPassedParam) {
		this.m_strPassedParam = m_strPassedParam;
	}

	public long getM_iUserRequestID() {
		return m_cResponse.getM_lUserRequestID();
	}

	public void setM_iUserRequestID(long m_iUserRequestID) {
		this.m_iUserRequestID = m_iUserRequestID;
	}

}
