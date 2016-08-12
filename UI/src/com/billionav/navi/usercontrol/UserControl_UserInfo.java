package com.billionav.navi.usercontrol;

import java.util.Arrays;
 
public class UserControl_UserInfo {
	public static final int USER_LOGIN_TYPE_INVAILD 	= 0;
	public static final int USER_LOGIN_TYPE_PHONE 		= 1;
	public static final int USER_LOGIN_TYPE_EMAIL 		= 2;
	public static final int USER_LOGIN_TYPE_NANE		= 3;
	public static final int USER_LOGIN_TYPE_IMEI		= 4;
	
	public static final int WEIBO_TYPE_INVAILD			= 0;
	public static final int WEIBO_TYPE_SINA				= 1;
	public static final int WEIBO_TYPE_TECENT			= 2;
	
	public static final int WEIBO_STATUS_UNBINDED		= 0;
	public static final int WEIBO_STATUS_BINDED			= 1;
	
	//Remember Password
	public static final String PASSWORD_NEED_REMEMBER = "1";
	public static final String PASSWORD_NO_NEED_REMEMBER = "0";
	
	//AutoLogin 
	public static final String LOGIN_NO_NEED_AUTO_LOGIN = "0";
	public static final String LOGIN_NEED_AUTO_LOGIN = "1";
	
	public static final int WEIBO_TYPE_MAXLEN 		= 2;
	
	public String m_strUserId = "";
	public String m_strUserPassWord = "";
	public String m_strEmail = "";
	public String m_strPhoneNum = "";
	public String m_strNickName = "";//part 1
	public String m_strDeviceID = "";
	public String m_strHomeAddress = "";//part 2
	public String m_strBirthday = "";//part 3
	public String m_strSex = ""; //part 4
	public byte[] m_byPhoto = null;
	public String m_strMicroBlogURL = "";//part 14
	public String m_strAutoLogin = LOGIN_NO_NEED_AUTO_LOGIN;
	public String m_strRememberPwd = PASSWORD_NO_NEED_REMEMBER;
	/////////////////////////////////adding new
	public String m_strCompany = "";
	public String m_strDescription = "";
	public String m_strEducation = "";
	public String m_strSchool = "";
	public String m_strRealname = "";
	public String m_strOccupation = "";
	public String m_strMSN = "";
	public String m_strQQ = "";
	public String m_strInvitecode = "";
	
	public int m_iLoginType = USER_LOGIN_TYPE_INVAILD; 
	
	public int[] m_iWeiBoStatus = {WEIBO_STATUS_UNBINDED,WEIBO_STATUS_UNBINDED};
	public String[] m_strWeiBoToken = {"",""};
	public String[] m_strWeiBoVaildPeriod = {"",""};
	
	
	
	
	//////////////////////////
	//
	public UserControl_UserInfo() {
	}
	
	public UserControl_UserInfo(UserControl_UserInfo userInfo) {
		m_strUserId = userInfo.m_strUserId;
		m_strUserPassWord = userInfo.m_strUserPassWord;
		m_strEmail = userInfo.m_strEmail;
		m_strPhoneNum = userInfo.m_strPhoneNum;
		m_strNickName = userInfo.m_strNickName;//part 1
		m_strHomeAddress = userInfo.m_strHomeAddress;//part 2
		m_strBirthday = userInfo.m_strBirthday;//part 3
		m_strSex = userInfo.m_strSex; //part 4
		if (null != userInfo.GetHeadPortraitArray()) {
			ReadHeadPortraitFromArray(userInfo.GetHeadPortraitArray(), userInfo.GetHeadPortraitArray().length);//part 5
		}
		m_strMicroBlogURL = userInfo.m_strMicroBlogURL;//part 14
		m_strCompany = userInfo.m_strCompany;
		m_strDescription = userInfo.m_strDescription;
		m_strEducation = userInfo.m_strEducation;
		m_strSchool = userInfo.m_strSchool;
		m_strRealname = userInfo.m_strRealname;
		m_strOccupation = userInfo.m_strOccupation;
		m_strMSN = userInfo.m_strMSN;
		m_strQQ = userInfo.m_strQQ;
		m_strInvitecode = userInfo.m_strInvitecode;
		
		m_iLoginType = userInfo.m_iLoginType;
		m_strAutoLogin = userInfo.m_strAutoLogin;
		m_strRememberPwd = userInfo.m_strRememberPwd;
		
		m_iWeiBoStatus = new int[WEIBO_TYPE_MAXLEN];
		m_strWeiBoToken = new String[WEIBO_TYPE_MAXLEN];
		m_strWeiBoVaildPeriod = new String[WEIBO_TYPE_MAXLEN];
		
		
		for (int i = 0; i < WEIBO_TYPE_MAXLEN; i ++) {
			m_iWeiBoStatus[i] = userInfo.m_iWeiBoStatus[i];
			m_strWeiBoToken[i] = userInfo.m_strWeiBoToken[i];
			m_strWeiBoVaildPeriod[i] = userInfo.m_strWeiBoVaildPeriod[i];
		}
		
	}

	
	//copy array to m_byPhoto
	public void ReadHeadPortraitFromArray(byte[] array,int size) {
		if(0 >= size || null == array) {
			return;
		}
		m_byPhoto = new byte[size];
		ClearHeadPortrait();
		System.arraycopy(array, 0, m_byPhoto, 0, size);
	}
	
	protected void ClearHeadPortrait() {
		if(null != m_byPhoto) {
			Arrays.fill(m_byPhoto, (byte) 0);
		}
	}
	
	public byte[] GetHeadPortraitArray() {
		return m_byPhoto;
	}
}
