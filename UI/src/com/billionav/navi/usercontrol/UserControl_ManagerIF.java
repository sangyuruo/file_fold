package com.billionav.navi.usercontrol;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.CountDownTimer;
import android.util.Log;

import com.billionav.jni.W3JNI;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.net.PHost;
import com.billionav.navi.net.PLogin;
import com.billionav.navi.net.PNetLog;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.usercontrol.Request.UserControl_RequestChangePass;
import com.billionav.navi.usercontrol.Request.UserControl_RequestCheckRegisted;
import com.billionav.navi.usercontrol.Request.UserControl_RequestDeleteUser;
import com.billionav.navi.usercontrol.Request.UserControl_RequestDownLoadUserInfo;
import com.billionav.navi.usercontrol.Request.UserControl_RequestFactory;
import com.billionav.navi.usercontrol.Request.UserControl_RequestForgetPass;
import com.billionav.navi.usercontrol.Request.UserControl_RequestGetAccessTokenStatus;
import com.billionav.navi.usercontrol.Request.UserControl_RequestGetCode;
import com.billionav.navi.usercontrol.Request.UserControl_RequestGetPassByMail;
import com.billionav.navi.usercontrol.Request.UserControl_RequestGetPassByPhone;
import com.billionav.navi.usercontrol.Request.UserControl_RequestGetResource;
import com.billionav.navi.usercontrol.Request.UserControl_RequestId;
import com.billionav.navi.usercontrol.Request.UserControl_RequestInviteUserIntoCircle;
import com.billionav.navi.usercontrol.Request.UserControl_RequestRegByMail;
import com.billionav.navi.usercontrol.Request.UserControl_RequestRegByPhone;
import com.billionav.navi.usercontrol.Request.UserControl_RequestReqValidateCode;
import com.billionav.navi.usercontrol.Request.UserControl_RequestSetAccessToken;
import com.billionav.navi.usercontrol.Request.UserControl_RequestUpLoadUserInfo;
import com.billionav.navi.usercontrol.Request.UserControl_RequestUserLogin;
import com.billionav.navi.usercontrol.Request.UserControl_RequestUserLogout;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestGRGetResource;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestGroupBuildGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestGroupDelGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestGroupEditGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestGroupQueryGroups;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestInfoBlogSharing;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestInfoGroupSharing;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberAddToBlackList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberApprovalJoinGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberExitGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberInvitePerson;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberJoinGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberKickOffPerson;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberMoveOutBlackList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberQueryBlackList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMyDataQueryRankList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMyDataQueryUserStyle;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterDelCollection;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterGetPhoto;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterQueryColList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterQueryPosterDetails;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterQueryPosterList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterReportPos;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterUploadData;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterUserFeedback;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseFactory;

public class UserControl_ManagerIF {
	private static UserControl_ManagerIF m_sInstance = null;
	
	private UserControl_UserInfo m_cLoginStoredInfo = null;
	private UserControl_UserInfo m_cLogInUserInfo = null;
	private UserControl_UserInfo m_cOtherUserInfo = null;
	private UserControl_DesCrypt m_cDesCrypt = null;
	private boolean m_bTrial = false;
	private String m_strServerUrl = null;
	private String m_strAuthServerUrl = null;
//	private boolean IsLogin=false;
	private int i_LoginState = LOGIN_STATE_LOGOUT;
	public String m_strPhotoPath="";
	public static long INVALIDATE_REQUESTID = -1;
	public int m_iLoginIdentityTag = UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_OTHER;
	
	public static final int		LOGIN_STATE_LOGOUT = 0;
	public static final int 	LOGIN_STATE_LOGGINGOUT= 1;
	public static final int		LOGIN_STATE_LOGGING = 2;
	public static final int		LOGIN_STATE_LOGGED = 3;

	public static final int 	MAX_STORED_USER_INFO = 3;
	public static final String 	STORAGE_PARAM_SPLIT_STRING = ",";
	//public static final String 	STORAGE_OBJECT_SPLIT_STRING = "|";
	public static final String 	STORAGE_Array_SPLIT_STRING = "|";
	public static final String 	STORAGE_Array_SPLIT_ESCAPE_STRING = "\\|";
	
	public static final int WEIBO_TYPE_SINA = 1;
	public static final int WEIBO_TYPE_TECENT = 2;
	
	
	private ArrayList<UserControl_CycleInfo> m_cListCycleInfo = null;
	private UserControl_BlackList m_cBlackList = null;
	private int m_iCycleOffset = 0;
	
	private UserControl_PosterListInfo m_cPosterListInfo = null;
	
	private UserControl_PositionResData m_cPositionResData = null;
	
	private UserControl_UserFeedBack m_cUserFeedBack = null;
	
	private UserControl_FavoriteColData m_cFavoriteCollectionData = null;
	
	private ArrayList<UserControl_InviteFailedUserInfo> m_cInviteFailedUserList	= 	null;
	
	private UserControl_UserFeedBack m_cUpdateDataResponseInfo = null;
	
	private byte[]	m_bytePosterVoiceInfo = null;
	
	private byte[]  m_bytePosterUserHeaderPicInfo = null;
	
	private byte[] m_bytePosterPictureInfo = null;
	
	private UserControl_RankListInfo m_cRankListInfo;
	
	private UserControl_UserStyleInfo m_cUserStyleInfo;
	
	private UserControl_PosterDetail m_cPosterDetails;
	
	private UserControl_WeiBoBindStatus m_cWeiBoBindStatus;
	
	public final int CUSTOM_DIALOG_MILLISEINFUTURE = 30000;  
	
	public final int CUSTOM_DIALOG_COUNTDOWNINTERVAL = 1000; 
	
	private CustomTimer customTimer;// = new CustomTimer(CUSTOM_DIALOG_MILLISEINFUTURE, CUSTOM_DIALOG_COUNTDOWNINTERVAL);

	////////////////////////////////////////
	/***********Main method****************/
	///////////////////////////////////////
	  
	private UserControl_ManagerIF() {
		if (m_cDesCrypt == null) {
			m_cDesCrypt=new UserControl_DesCrypt();
		}

		if (m_cLogInUserInfo == null) {
			m_cLogInUserInfo=new UserControl_UserInfo();
		}
		
		InitUrlFromIni();
		
		NSViewManager.GetViewManager().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				customTimer = new CustomTimer(CUSTOM_DIALOG_MILLISEINFUTURE, CUSTOM_DIALOG_COUNTDOWNINTERVAL);
				
			}
		});
	}
	
	//get an instance of UserControl_ManagerIF,this provide convenince to use this manager
	public static UserControl_ManagerIF Instance() {
		if (m_sInstance == null) {
			m_sInstance = new UserControl_ManagerIF();
		}

		return m_sInstance;
	}
	
	public CustomTimer getCustomTimer(){
		return customTimer;
	}
	
	public void setCustomTimerListener(onCountingListener handler){
		customTimer.setListener(handler);
	}
	public void destroyCustomTimerListener(){
		customTimer.destroyListener();
	}
	public void startCustomTimer(){
		customTimer.start();
	}
	public boolean isfindpassworddelaying(){
		return customTimer.isCounting();
	}
	public int getCurCount(){
		return customTimer.getCurCount();
	}
	public interface onCountingListener{
		public void onFinish();
		public void onTick(int countDown);
	}
	private class CustomTimer extends CountDownTimer{
		private int countDown = 0;
//		private Handler handler = null;
		private onCountingListener listener;
		private boolean iscounting = false;
		public CustomTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
			countDown = (int) (millisInFuture/countDownInterval);
		}

		public void setListener(onCountingListener listener){
			this.listener = listener;
		}
		
		public void destroyListener() {
			this.listener = null;
		}
		
		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			iscounting = false;
			countDown = 0;
			if(listener != null) {
				listener.onFinish();
			}
		}

		
		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			iscounting = true;
			countDown = (int) millisUntilFinished/1000;
			if(listener != null) {
				listener.onTick(countDown);
			}
		}
		
		public boolean isCounting(){
			return iscounting;
		}
		public int getCurCount(){
			return countDown;
		} 
	}
	
	
	//Add Cancel UserRequest IF
	public boolean CancleUserRequest(long lRequestID) {
		return UserControl_ResponseFactory.Instance().CancleUserRequest(lRequestID);
	}
	
	//get verification code
	public long getVerificationCode(UserControl_UserInfo userInfo, String strValidate){
		//get a request from  request factory by providing request type
		UserControl_RequestGetCode cRequest = 
			(UserControl_RequestGetCode)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_GET_VERIFICATION_CODE);

		if (null != cRequest) {
			//request to server for register 
			 cRequest.getVerificationCode(userInfo, strValidate);
			 return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	//if you forget password,this can change your password by phonenumber and code
	public long setNewPasswordByCode(UserControl_UserInfo userInfo, String loginName,  String password, String strValidate){
		//get a request from  request factory by providing request type
		UserControl_RequestForgetPass cRequest = 
			(UserControl_RequestForgetPass)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SET_NEW_PASSWORD);

		if (null != cRequest) {
			//request to server for register 
			 cRequest.ChangePassword(userInfo, loginName, password, strValidate);
			 return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	//register by cellphone number
	public long RegisterByPhone(UserControl_UserInfo userInfo, String strValidate) {
		//get a request from  request factory by providing request type
		UserControl_RequestRegByPhone cRequest = 
			(UserControl_RequestRegByPhone)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_REG_BY_PHONE);

		if (null != cRequest) {
			//request to server for register 
			 cRequest.RegisterByPhone(userInfo, strValidate);
			 return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	public long RegisterByMail(UserControl_UserInfo userInfo)
	{
		UserControl_RequestRegByMail cRequest = 
			(UserControl_RequestRegByMail)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_REG_BY_MAIL);

		if (null != cRequest) {
			//request to server for register 
			cRequest.RegisterByEmail(userInfo);
			return cRequest.getM_iUserRequestID();
		}
		return INVALIDATE_REQUESTID;
	}
	//when user has register,this method will return false
	public long HasRegistered(String phoneNum, String email, String nikeName) {
		UserControl_RequestCheckRegisted cRequest = 
			(UserControl_RequestCheckRegisted)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_CHECK_REGISTED);

		if (null != cRequest) {
			cRequest.HasRegistered(phoneNum, email, nikeName);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}

	//register for validate code,when user login on
	public long ReqValidateCode(String sCellPhoneNum) {
		UserControl_RequestReqValidateCode cRequest = 
			(UserControl_RequestReqValidateCode)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_VALIDATECODE);

		if (null != cRequest) {
			cRequest.ReqValidateCode(sCellPhoneNum);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}

	//login on by nickname 
	public long LoginByName(String nickName, String password) {
		UserControl_RequestUserLogin cRequest = 
			(UserControl_RequestUserLogin)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGIN);

		if (null != cRequest) {
			cRequest.LoginByName(nickName, password);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}

	public long LoginByPhone(String phoneNum, String password) {
		UserControl_RequestUserLogin cRequest = 
			(UserControl_RequestUserLogin)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGIN);

		if (null != cRequest) {
			cRequest.LoginByPhone(phoneNum, password);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	public long LoginByMail(String email, String password) {
		UserControl_RequestUserLogin cRequest = 
			(UserControl_RequestUserLogin)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGIN);

		if (null != cRequest) {
			cRequest.LoginByMail(email, password);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	//login by IMEI
	public long LoginByIMEI(String IMEI) {
//		UserControl_UserInfo[] userInfo = GetLoginInfoListFromFile();
//		userInfo[0].m_iLoginType = UserControl_UserInfo.USER_LOGIN_TYPE_PHONE;
		UserControl_RequestUserLogin cRequest = 
				(UserControl_RequestUserLogin)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGIN);
		
		Log.d("test","START LOG IN By IMEI");
		
		if(null != cRequest) {
			cRequest.LoginByIMEI(IMEI);
			Log.d("test","requestID="+cRequest.getM_iUserRequestID());
			return cRequest.getM_iUserRequestID();
		}
		return INVALIDATE_REQUESTID;
	}
	
	
	/**
	 * @Discrption	the DeviceNo : Input the IMEI if cann't get IMEI set the MAC
	 * @format		 DeviceNo	: "I" + IMEI
	 * 							  "M" + MAC
	 * @param nickName
	 * @param password
	 * @param DeviceNo
	 * @return boolean
	 */
	public long LoginByName(String nickName, String password,String DeviceNo) {
		UserControl_RequestUserLogin cRequest = 
			(UserControl_RequestUserLogin)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGIN);

		if (null != cRequest) {
			cRequest.LoginByName(nickName, password, DeviceNo);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}

	public long LoginByPhone(String phoneNum, String password,String DeviceNo) {
		UserControl_RequestUserLogin cRequest = 
			(UserControl_RequestUserLogin)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGIN);

		if (null != cRequest) {
			cRequest.LoginByPhone(phoneNum, password, DeviceNo);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	
	public long LoginByMail(String email, String password,String DeviceNo) {
		UserControl_RequestUserLogin cRequest = 
			(UserControl_RequestUserLogin)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGIN);

		if (null != cRequest) {
			cRequest.LoginByMail(email, password, DeviceNo);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	/**
	 * @Discrption	Add new param that prove that where the login from 
	 * @format		 
	 * 							  
	 * @param nickName
	 * @param password
	 * @param DeviceNo
	 * @param IdentityOfSource
	 * 
	 * UserControl_CommonVar
	 * 	LOGIN_INDETITY_OF_SOURCE_OTHER		=	0;
	 *	LOGIN_INDETITY_OF_SOURCE_UI			=	1;	//When UI Action to Login
	 *	LOGIN_INDETITY_OF_SOURCE_AUTO		=	2;	//When SetAutoLogin
	 *	LOGIN_INDETITY_OF_SOURCE_RELOGIN	=	3;	//When The Token outOf Time will reLogin
	 *  LOGIN_INDETITY_OF_SOURCE_REGIST_LOGIN	=	4; //When Register finish then Login
	 */
	
	public long LoginByName(String nickName, String password,String DeviceNo,int IdentityOfSource) {
		UserControl_RequestUserLogin cRequest = 
			(UserControl_RequestUserLogin)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGIN);

		if (null != cRequest) {
			cRequest.setM_iPassedParam(IdentityOfSource);
			cRequest.LoginByName(nickName, password, DeviceNo);
			setLoginStatus(LOGIN_STATE_LOGGING);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}

	public long LoginByPhone(String phoneNum, String password,String DeviceNo,int IdentityOfSource) {
		UserControl_RequestUserLogin cRequest = 
			(UserControl_RequestUserLogin)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGIN);

		if (null != cRequest) {
			cRequest.setM_iPassedParam(IdentityOfSource);
			cRequest.LoginByPhone(phoneNum, password, DeviceNo);
			setLoginStatus(LOGIN_STATE_LOGGING);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	public long LoginByMail(String email, String password,String DeviceNo,int IdentityOfSource) {
		UserControl_RequestUserLogin cRequest = 
			(UserControl_RequestUserLogin)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGIN);

		if (null != cRequest) {
			cRequest.setM_iPassedParam(IdentityOfSource);
			cRequest.LoginByMail(email, password, DeviceNo);
			setLoginStatus(LOGIN_STATE_LOGGING);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	
	

	public long LogOut() {
		UserControl_RequestUserLogout cRequest = 
			(UserControl_RequestUserLogout)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_USER_LOGOUT);

		if (null != cRequest) {
			cRequest.LogOut();
			setLoginStatus(LOGIN_STATE_LOGGINGOUT);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	public void CleanLoginSession(){
		setLoginStatus(LOGIN_STATE_LOGOUT);
		PLogin.setSessionToken("");
		PLogin.notifyUserLogout();
		PHost.initialize();
	}

	public long GetPassByPhone(String phoneNumber) {
		UserControl_RequestGetPassByPhone cRequest = 
			(UserControl_RequestGetPassByPhone)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_GET_PASS_BY_PHONE);

		if (null != cRequest) {
			cRequest.GetPassByPhone(phoneNumber);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}

	public long GetPassByEmail(String email) {
		UserControl_RequestGetPassByMail cRequest = 
			(UserControl_RequestGetPassByMail)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_GET_PASS_BY_MAIL);

		if (null != cRequest) {
			cRequest.GetPassByEmail(email);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}

	
	public long DeleteUserByEmail(String email) {
		UserControl_RequestDeleteUser cRequest = 
			(UserControl_RequestDeleteUser)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_DELETE_USER);

		if (null != cRequest) {
			cRequest.DeleteUserByEmail(email);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	public long DeleteUserByCellphone(String cellphone) {
		UserControl_RequestDeleteUser cRequest = 
			(UserControl_RequestDeleteUser)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_DELETE_USER);

		if (null != cRequest) {
			cRequest.DeleteUserByCellphone(cellphone);
			return cRequest.getM_iUserRequestID();
		}
		
		return INVALIDATE_REQUESTID;
	}
	
	
	/**************************************************************
	 * SNS Part
	 ***************************************************************/
	public long SNSCreateCycle(String strName
									, int iType
									, String strDescription
									, String strTags) {
		UserControl_CommonVar.SNSLog("SNSCreateCycle", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestGroupBuildGroup cRequest = (UserControl_RequestGroupBuildGroup)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GM_BUILD_GROUP);
		
		cRequest.setStrGroupName(strName);
		cRequest.setiGroupType(iType);
		cRequest.setStrGroupDiscription(strDescription);
		cRequest.setStrGroupTags(strTags);
		
		cRequest.SendBuildGroupRequest();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSDeleteGroup(String strCycleID) {
		UserControl_CommonVar.SNSLog("SNSDeleteGroup", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestGroupDelGroup cRequest = (UserControl_RequestGroupDelGroup)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GM_DEL_GROUP);
		
		cRequest.setM_strGroupID(strCycleID);
		
		cRequest.SendDelGroupRequest();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSUpdateCycle(String strCycleID
									, String strCycleName
									, String strCycleDescription
									, String strTags) {
		UserControl_CommonVar.SNSLog("SNSUpdateCycle", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestGroupEditGroup cRequest = (UserControl_RequestGroupEditGroup)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GM_EDIT_GROUP);
		
		cRequest.setM_StrGroupID(strCycleID);
		cRequest.setM_StrGroupName(strCycleName);
		cRequest.setM_StrGroupDiscription(strCycleDescription);
		cRequest.setM_StrGroupTags(strTags);
		
		cRequest.SendUpdateGroupRequest();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSQuerysCycle(UserControl_CycleInfo cQueryInfo) {
		UserControl_CommonVar.SNSLog("SNSQuerysCycle", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestGroupQueryGroups cRequest = (UserControl_RequestGroupQueryGroups)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GM_QUERY_GROUPS);
		
		cRequest.setM_strGroupID(cQueryInfo.getM_strID());
		cRequest.setM_strGroupName(cQueryInfo.getM_strName());
		cRequest.setM_strGroupOwner(cQueryInfo.getM_strOwner());
		cRequest.setM_strGroupOffset(cQueryInfo.getM_strOffset());
		cRequest.setM_strGroupTags(cQueryInfo.getM_strTags());
		cRequest.setM_strGroupType(cQueryInfo.getM_strType());
		cRequest.setM_strGroupMaxLen(cQueryInfo.getM_strMaxLen());
		
		cRequest.SendQueryGroupsRequest();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSAddToBlackList(String strUserID) {
		UserControl_CommonVar.SNSLog("SNSAddToBlackList", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestMemberAddToBlackList cRequest = (UserControl_RequestMemberAddToBlackList)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GMM_ADD_TO_BLACKLIST);
		cRequest.setM_strUserID(strUserID);
		cRequest.SendRequestAddToBlackList();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSMoveOutFromBlackList(String strUserID) {
		UserControl_CommonVar.SNSLog("SNSMoveOutFromBlackList", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestMemberMoveOutBlackList cRequest = (UserControl_RequestMemberMoveOutBlackList)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GMM_MOVEOUT_BLACKLIST);
		cRequest.setM_strUserID(strUserID);
		cRequest.SendRequestMoveOutFromBlackList();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSQueryBlackList(int iOffset, int iMaxlen){
		UserControl_CommonVar.SNSLog("SNSQueryBlackList", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestMemberQueryBlackList cRequest = (UserControl_RequestMemberQueryBlackList)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GMM_QUERY_BLACKLIST);
		cRequest.setM_iOffset(iOffset);
		cRequest.setM_Maxlen(iMaxlen);
		cRequest.SendRequestQueryBlackList();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSExitCycle(String strCycleID, String strUserID) {
		UserControl_CommonVar.SNSLog("SNSExitCycle", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestMemberExitGroup cRequest = (UserControl_RequestMemberExitGroup)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GMM_EXIT_GROUP);
		
		cRequest.setM_strCycleID(strCycleID);
		cRequest.setM_strUser(strUserID);
		
		cRequest.SendRequestExitGroup();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSInvitePerson(UserControl_InviteDataFormat cListInviteDataFormat) {
		UserControl_CommonVar.SNSLog("SNSInvitePerson", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestMemberInvitePerson cRequest = (UserControl_RequestMemberInvitePerson)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GMM_INVITE_PERSON);
		cRequest.setM_cListInviteDataFormat(cListInviteDataFormat);
		cRequest.SendRequestInvitePerson(cListInviteDataFormat);
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSApprovalJoinGroup(String strUserID) {
		UserControl_CommonVar.SNSLog("SNSApprovalJoinGroup", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestMemberApprovalJoinGroup cRequest = (UserControl_RequestMemberApprovalJoinGroup)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GMM_APPROVAL_PERSON);
		cRequest.setM_strID(strUserID);
		cRequest.SendRequestApprovalJoinGroup();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSJoinGroup(String strCycleID) {
		UserControl_CommonVar.SNSLog("SNSJoinGroup", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestMemberJoinGroup cRequest = (UserControl_RequestMemberJoinGroup)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GMM_JOIN_GROUP);
		cRequest.setM_strCycleID(strCycleID);
		cRequest.SendRequestJoinGroup();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSKickOffPerson(String strCycleID, String strUserID) {
		UserControl_CommonVar.SNSLog("SNSKickOffPerson", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestMemberKickOffPerson cRequest = (UserControl_RequestMemberKickOffPerson)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GMM_KICKOFF_PERSON);
		cRequest.setM_strCycleID(strCycleID);
		cRequest.setM_strUser(strUserID);
		cRequest.SendRequestKickOffPerson();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSBlogShare() {
		UserControl_RequestInfoBlogSharing cRequest = (UserControl_RequestInfoBlogSharing)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_IM_BLOG_SHARING);
		//TODO
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSCycleShare() {
		UserControl_RequestInfoGroupSharing cRequest = (UserControl_RequestInfoGroupSharing)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_IM_GROUP_SHARING);
		//TODO
		return cRequest.getM_iUserRequestID();
	}
	///////////////
	public long SNSDeleteCollectionPoster(String strPosterID) {
		UserControl_CommonVar.SNSLog("SNSDeleteCollectionPoster", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestPosterDelCollection cRequest = (UserControl_RequestPosterDelCollection)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_PM_DEL_COL);
		cRequest.setStrPostID(strPosterID);
		cRequest.SendRequestDelFavorite();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSGetPhotoOfPoster(String strPhotoID) {
		UserControl_CommonVar.SNSLog("SNSGetPhotoOfPoster", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestPosterGetPhoto cRequest = (UserControl_RequestPosterGetPhoto)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_PM_GET_PHOTO);
		cRequest.setStrPhotoID(strPhotoID);
		cRequest.SendRequestGetPhoto();
		return cRequest.getM_iUserRequestID();
	}
	
	public boolean SNSDeletePhotoOfPoster(String strPhotoID) {
		return UserControl_RequestPosterGetPhoto.DeletePhotoData(strPhotoID);
	}
	
	public byte[] SNSGetPhotoBytes(String strPhotoID) {
		return UserControl_RequestPosterGetPhoto.GetPhotoBytes(strPhotoID);
	}
	
	
	public long SNSQueryCollectionList(int iOffset, int iMaxLen) {
		UserControl_CommonVar.SNSLog("SNSQueryCollectionList", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestPosterQueryColList cRequest = (UserControl_RequestPosterQueryColList)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_PM_QUERY_COL_LIST);
		cRequest.setM_iMaxLen(iMaxLen);
		cRequest.setM_iOffset(iOffset);
		cRequest.SendRequestQueryCollectionList();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSQueryPosterDetails(String strPosterID) {
		UserControl_CommonVar.SNSLog("SNSQueryPosterDetails", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestPosterQueryPosterDetails cRequest = (UserControl_RequestPosterQueryPosterDetails)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_PM_QUERY_POST_DETAILS);
		cRequest.setStrPosterID(strPosterID);
		cRequest.SendRequestQueryPostDetails();
		return cRequest.getM_iUserRequestID();
	}
	
	public UserControl_PosterDetail GetPosterDetailsByID (String posterID) {
		return getM_cPosterDetails();
	}
	
	public boolean DeletePosterDetailsDataByID(String posterID) {
		return true;
	}
	
	
	public long SNSQueryPosterList(UserControl_PosterQueryInfo info) {
		UserControl_CommonVar.SNSLog("SNSQueryPosterList", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestPosterQueryPosterList cRequest = (UserControl_RequestPosterQueryPosterList)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_PM_QUERY_POST_LIST);
		cRequest.setInfo(info);
		cRequest.SendQueryPosterList();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSReportPosition(String strLon,String strLat) {
		UserControl_CommonVar.SNSLog("SNSReportPosition", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestPosterReportPos cRequest = (UserControl_RequestPosterReportPos)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_PM_REPORT_POS);
		cRequest.setStrLat(strLat);
		cRequest.setStrLon(strLon);
		cRequest.SendRequestReportPosition();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSUploadData(UserControl_UploadData cData) {
		UserControl_CommonVar.SNSLog("SNSUploadData", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestPosterUploadData cRequest = (UserControl_RequestPosterUploadData)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_PM_UPLOAD_DATA);
		cRequest.setM_cData(cData);
		cRequest.sendRequestUploadData();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSUserFeedback(String strPostID, String strCommand) {
		UserControl_CommonVar.SNSLog("SNSUserFeedback", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestPosterUserFeedback cRequest = (UserControl_RequestPosterUserFeedback)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_PM_USER_FEEDBACK);
		cRequest.setStrPostID(strPostID);
		cRequest.setStrCommand(strCommand);
		cRequest.SendRequestGetUserFeedBack();
		return cRequest.getM_iUserRequestID();
	} 
	
	public long SNSGetResource(int iType, String strId, String strEmail, String strCellPhone) {
		UserControl_CommonVar.SNSLog("SNSGetResource", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestGRGetResource cRequest = (UserControl_RequestGRGetResource)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GR_GET_RESOURCE);
		cRequest.setM_iType(iType);
		cRequest.setM_strID(strId);
		cRequest.setM_strEmail(strEmail);
		cRequest.setM_strCellPhone(strCellPhone);
		
		cRequest.SendRequestGetResource();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSMYDATAGetRankList(String strType, String strStart, String strEnd, int iLen, int iOffset) {
		UserControl_CommonVar.SNSLog("SNSMYDATAGetRankList", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestMyDataQueryRankList cRequest = (UserControl_RequestMyDataQueryRankList)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_MYDATA_GET_RANKLIST);
		
		cRequest.setM_iLen(iLen);
		cRequest.setM_strPriodStart(strStart);
		cRequest.setM_strPriodEnd(strEnd);
		cRequest.setM_strType(strType);
		cRequest.setM_iOffset(iOffset);
		
		cRequest.SendRequestQueryRankList();
		return cRequest.getM_iUserRequestID();
	}
	
	public long SNSMYDATAGetUserStyleInfo(String strSummryType ,String strStartData) {
		return SNSMYDATAGetUserStyleInfo(strSummryType,strStartData,"");
	}
	
	public long SNSMYDATAGetUserStyleInfo(String strSummryType ,String strStartData, String strEndData) {
		UserControl_CommonVar.SNSLog("SNSMYDATAGetUserStyleInfo", UserControl_CommonVar.SNS_LOG_STEP_START, "");
		UserControl_RequestMyDataQueryUserStyle cRequest = (UserControl_RequestMyDataQueryUserStyle)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_MYDATA_GET_USER_STYLE);
		cRequest.setM_strStartData(strStartData);
		cRequest.setM_strSummryType(strSummryType);
		cRequest.setM_strEndData(strEndData);
		
		cRequest.SendRequestGetUserStyleInfo();
		return cRequest.getM_iUserRequestID();
	}
	
	////////////////////////////////////////
	/***********Data check method****************/
	/********regular expression to check out data *********/
	///////////////////////////////////////
	public boolean IsValidEmail(String email) {	
		String expression = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		return IsValidString(email,expression);
	}
	
	public boolean IsValidPhoneNum(String number) {
		//must be 11 numbers
		String expression = "^(\\+86)?[0-9]{11}$";//from ^[0-9]{11}$ to ^(\+86)?[0-9]{11}$ change to Fit +86xxxxxxxx
		return IsValidString(number,expression);
	}
	
	public int IsValidNickName(String nickName) {
		//the first character must be letter,and the middle character can be letter ,number,or "_".
		//and the length of nickname is 4 at least,20 at most.
		int iLen = nickName.length();
		int iCount = 0;
		for (int index = 0; index < iLen; index++) {
			iCount += 1;
		}
		
		if (iCount < 4) {
			return UserControl_CommonVar.VAILDATE_INPUT_TOO_SHORT;
		}
		
		if (iCount > 20) {
			return UserControl_CommonVar.VAILDATE_INPUT_TOO_LONG;
		}
		
		String expression ="^[\u4e00-\u9fa5a-zA-Z0-9]+$"; 
		if (IsValidString(nickName,expression)) {
			return UserControl_CommonVar.VALIDATE_INPUT_OK;
		} else {
			return UserControl_CommonVar.VAILDATE_INPUT_FORMAT_ERROR;
		}
	}
	
	public static boolean isChinese(char c) {  

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

		|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

		|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

		|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

		|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

		|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		}

		return false;

    }  
	
	public int IsValidPassword(String password) {	
		//the length of password is 6 at least,16 at most. and there is no space character in password.
		int iLen = password.length();
		if (iLen < 6) {
			return UserControl_CommonVar.VAILDATE_INPUT_TOO_SHORT;
		}
		
		if (iLen > 16) {
			return UserControl_CommonVar.VAILDATE_INPUT_TOO_LONG;
		}
		String expression = "^[_a-zA-Z0-9]{6,16}$";

		if( IsValidString(password,expression)) {
			return UserControl_CommonVar.VALIDATE_INPUT_OK;
		} else {
			return UserControl_CommonVar.VAILDATE_INPUT_FORMAT_ERROR;
		}
	}

	private boolean IsValidString(String str, String sRegularExpression) {
		if((null == str)
			|| (null == sRegularExpression)
			|| (0 == str.compareTo(""))
			|| (0 == sRegularExpression.compareTo(""))){
			return false;
		}
		
		Pattern pattern = Pattern.compile(sRegularExpression);
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()){
			return true;
		}
		return false;
	}

	////////////////////////////////////////
	/***********Function method****************/
	///////////////////////////////////////

	public boolean IsTrial() {
		return m_bTrial;
	}

	public void SetTrial() {
		// TODO: Not Done
		m_bTrial = true;
	}

	public  UserControl_UserInfo GetLogInUserInfo() {
    		return m_cLogInUserInfo;
	}
	public boolean SetPhotoByteToUserInfo(byte[] photo)
	{
		m_cLogInUserInfo.m_byPhoto=photo;
		return true;
	}
	public  UserControl_UserInfo GetOtherUserInfo() {
		return m_cOtherUserInfo;
}
	//if user has login on ,then at least one of his email ,nickname  and  phone number will not be empty
	public boolean HasLogin() {
		if(SystemTools.getApkEdition().equals(SystemTools.EDITION_LUXGEN)){
			return true;
		}
		return getLoginStatus() == LOGIN_STATE_LOGGED;
	}
	
	
	public UserControl_UserInfo GetStoredUserInfo() {
		if (null != m_cLoginStoredInfo) {
			return new UserControl_UserInfo(m_cLoginStoredInfo);
		} else {
			return null;
		}
	}
	
	public void SetStoredRememberPwdInfo(boolean isRememberPwd) {
		Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:SetStoredRememberPwdInfo: isRememberPwd" + isRememberPwd);
		if (null != m_cLoginStoredInfo) {
			if (isRememberPwd) {
				m_cLoginStoredInfo.m_strRememberPwd = UserControl_UserInfo.LOGIN_NEED_AUTO_LOGIN;
			} else {
				m_cLoginStoredInfo.m_strRememberPwd = UserControl_UserInfo.LOGIN_NO_NEED_AUTO_LOGIN;
			}
			UpdateLoginInfoToFile(m_cLoginStoredInfo);
		} else{
			Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:SetStoredRememberPwdInfo: no m_cLoginStoredInfo");
		}
	}
	
	public void SetStoredAutoLoginInfo(boolean isAutoLogin) {
		Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:SetStoredAutoLoginInfo: isAutoLogin" + isAutoLogin);
		if (null != m_cLoginStoredInfo) {
			if (isAutoLogin) {
				m_cLoginStoredInfo.m_strAutoLogin = UserControl_UserInfo.PASSWORD_NEED_REMEMBER;
			} else {
				m_cLoginStoredInfo.m_strAutoLogin = UserControl_UserInfo.PASSWORD_NO_NEED_REMEMBER;
			}
			UpdateLoginInfoToFile(m_cLoginStoredInfo);
		} else {
			Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:SetStoredAutoLoginInfo: no m_cLoginStoredInfo");
		}
	}
	
	public void SetStoredTokenInfo(int iWeiboType, String token, String vaildPeriod) {
		Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:SetStoredTokenInfo: iWeiboType " + iWeiboType + " token " + token + " vaildPeriod " +vaildPeriod);
		if (null != m_cLoginStoredInfo) {
			if (WEIBO_TYPE_SINA == iWeiboType) {
				m_cLoginStoredInfo.m_strWeiBoToken[0] = token;
				m_cLoginStoredInfo.m_strWeiBoVaildPeriod[0] = vaildPeriod;
				if(null!= token && !"".equals(token.trim())) {
					m_cLoginStoredInfo.m_iWeiBoStatus[0] = UserControl_UserInfo.WEIBO_STATUS_BINDED;
				}else {
					m_cLoginStoredInfo.m_iWeiBoStatus[0] = UserControl_UserInfo.WEIBO_STATUS_UNBINDED;
				}
			} else if (WEIBO_TYPE_TECENT == iWeiboType) {
				m_cLoginStoredInfo.m_strWeiBoToken[1] = token;
				m_cLoginStoredInfo.m_strWeiBoVaildPeriod[1] = vaildPeriod;
				if(null!= token && !"".equals(token.trim())) {
					m_cLoginStoredInfo.m_iWeiBoStatus[1] = UserControl_UserInfo.WEIBO_STATUS_BINDED;
				} else {
					m_cLoginStoredInfo.m_iWeiBoStatus[1] = UserControl_UserInfo.WEIBO_STATUS_UNBINDED;
				}
			} else {
				
			}
			UpdateLoginInfoToFile(m_cLoginStoredInfo);
		} else {
			Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:SetStoredTokenInfo: no m_cLoginStoredInfo");
		}
	}
	
	////////////////////////////////////////
	/***********method for Local****************/
	///////////////////////////////////////
	
	private boolean JugeFileFormat(Object[] strobjs){
		if (!m_cDesCrypt.GetDecryptInfoFromFile(strobjs)){
			return false;
		}
		if (!UserControl_DesCrypt.STORAGE_VERSION.equals(strobjs[0])) {
			return false;
		}
		return true;
	}
	
	private int JugeLoginObjExistInStorage(UserControl_UserInfo cUserInfo,UserControl_UserInfo[] userInfoItems) {
		int iFindIndex = -1;
		
		for (int index = 0; index < userInfoItems.length; index++) {
			if (null != userInfoItems[index]) {
				if (cUserInfo.m_iLoginType == userInfoItems[index].m_iLoginType) {
					if (UserControl_UserInfo.USER_LOGIN_TYPE_PHONE == cUserInfo.m_iLoginType) {
						if (null != cUserInfo.m_strPhoneNum && cUserInfo.m_strPhoneNum.equals(userInfoItems[index].m_strPhoneNum)) {
							iFindIndex = index;
						}
					} else if (UserControl_UserInfo.USER_LOGIN_TYPE_EMAIL == cUserInfo.m_iLoginType) {
						if (null != cUserInfo.m_strEmail && cUserInfo.m_strEmail == userInfoItems[index].m_strEmail) {
							iFindIndex = index;
						}
					} else if (UserControl_UserInfo.USER_LOGIN_TYPE_NANE == cUserInfo.m_iLoginType) {
						if (null != cUserInfo.m_strNickName && cUserInfo.m_strNickName == userInfoItems[index].m_strNickName) {
							iFindIndex = index;
						}
					} else {
	
					}
				}
			}
		}
		
		return iFindIndex;
	}
	
	private int GetStoredItemCount(UserControl_UserInfo[] userInfoItems) {
		int iItemCount = 0;
		for (int iIndex = 0; iIndex < MAX_STORED_USER_INFO; iIndex++) {
			if (null != userInfoItems[iIndex]) {
				iItemCount++;
			}
		}
		return iItemCount;
	}
	
	private void ChangeObjectToString(Object[] objs, UserControl_UserInfo[] userInfoItems){
		StringBuffer stb;
		UserControl_UserInfo userInfoTemp;
		for (int iIndex = 0; iIndex < MAX_STORED_USER_INFO; iIndex++) {
			userInfoTemp = userInfoItems[iIndex];
			if (null != userInfoTemp) {
				stb = new StringBuffer();
				stb.append(userInfoTemp.m_iLoginType);
				stb.append(STORAGE_PARAM_SPLIT_STRING);
				stb.append(userInfoTemp.m_strNickName);
				stb.append(STORAGE_PARAM_SPLIT_STRING);
				stb.append(userInfoTemp.m_strUserPassWord);
				stb.append(STORAGE_PARAM_SPLIT_STRING);
				stb.append(userInfoTemp.m_strPhoneNum);
				stb.append(STORAGE_PARAM_SPLIT_STRING);
				stb.append(userInfoTemp.m_strEmail);
				stb.append(STORAGE_PARAM_SPLIT_STRING);
				stb.append(userInfoTemp.m_strAutoLogin);
				stb.append(STORAGE_PARAM_SPLIT_STRING);
				stb.append(userInfoTemp.m_strRememberPwd);
				stb.append(STORAGE_PARAM_SPLIT_STRING);
				for (int i = 0; i < UserControl_UserInfo.WEIBO_TYPE_MAXLEN; i++) {
					stb.append(userInfoTemp.m_iWeiBoStatus[i]);
					if (i != UserControl_UserInfo.WEIBO_TYPE_MAXLEN - 1) {
						stb.append(STORAGE_Array_SPLIT_STRING);
					}
				}
				stb.append(STORAGE_PARAM_SPLIT_STRING);
				for (int i = 0; i < UserControl_UserInfo.WEIBO_TYPE_MAXLEN; i++) {
					stb.append(userInfoTemp.m_strWeiBoToken[i]);
					if (i != UserControl_UserInfo.WEIBO_TYPE_MAXLEN - 1) {
						stb.append(STORAGE_Array_SPLIT_STRING);
					}
				}
				stb.append(STORAGE_PARAM_SPLIT_STRING);
				for (int i = 0; i < UserControl_UserInfo.WEIBO_TYPE_MAXLEN; i++) {
					stb.append(userInfoTemp.m_strWeiBoVaildPeriod[i]);
					if (i != UserControl_UserInfo.WEIBO_TYPE_MAXLEN - 1) {
						stb.append(STORAGE_Array_SPLIT_STRING);
					}
				}
				objs[iIndex] = stb.toString();
			} else {
				objs[iIndex] = "";
			}
		}
	}
	
	private void DebugPrintUserInfoList(UserControl_UserInfo[] userInfoItems){
		UserControl_UserInfo info = null;
		if (null != userInfoItems) {
			for (int index = 0; index < userInfoItems.length; index++) {
				info = userInfoItems[index];
				String str = "";
				if(null == info) {
					
				}else {
					if(UserControl_UserInfo.USER_LOGIN_TYPE_PHONE == info.m_iLoginType) {
						str = info.m_strPhoneNum;
					} else if (UserControl_UserInfo.USER_LOGIN_TYPE_EMAIL == info.m_iLoginType) {
						str = info.m_strEmail;
					} else if (UserControl_UserInfo.USER_LOGIN_TYPE_NANE == info.m_iLoginType) {
						str = info.m_strNickName;
					}
				}
				Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:UserInfo Index: " + index +" Item: "+ str);
			}
		}
	}
	
	private void DebugPrintSingleUserInfo(UserControl_UserInfo info,String part) {
		StringBuffer stb = null;

		if (null == info) {
			Log.d("[UserControl]",
					"[UserControl]--LoginInfoStorage:DebugPrintSingleUserInfo " + part + ":"
							+ "info == null");
		} else {
			stb = new StringBuffer();
			stb.append(" m_iLoginType:	" + info.m_iLoginType);
			stb.append(" m_strNickName:	" + info.m_strNickName);
			stb.append(" m_strUserPassWord:	" + info.m_strUserPassWord);
			stb.append(" m_strPhoneNum:	" + info.m_strPhoneNum);
			stb.append(" m_strEmail:	" + info.m_strEmail);
			stb.append(" m_strAutoLogin:	" + info.m_strAutoLogin);
			stb.append(" m_strRememberPwd:	" + info.m_strRememberPwd);
			stb.append(" WeiBo:");
			stb.append(" {");
			for (int i = 0; i < UserControl_UserInfo.WEIBO_TYPE_MAXLEN; i++) {
				stb.append(info.m_iWeiBoStatus[i]);
				stb.append(",");
			}
			stb.append(" } ");
			stb.append(" {");
			for (int i = 0; i < UserControl_UserInfo.WEIBO_TYPE_MAXLEN; i++) {
				stb.append(info.m_strWeiBoToken[i]);
				stb.append(",");
			}
			stb.append(" } ");
			stb.append(" {");
			for (int i = 0; i < UserControl_UserInfo.WEIBO_TYPE_MAXLEN; i++) {
				stb.append(info.m_strWeiBoVaildPeriod[i]);
				stb.append(",");
			}
			stb.append(" }");
			Log.d("[UserControl]",
					"[UserControl]--LoginInfoStorage:DebugPrintSingleUserInfo " + part + ":"
							+ stb.toString());
		}

	}
	
	
	public boolean UpdateLoginInfoToFile(UserControl_UserInfo cUserInfo) {

		Object[] strobjs = new Object[MAX_STORED_USER_INFO + 1];	//Version + key + Encryption Object String
		Object[] objs = new Object[MAX_STORED_USER_INFO];			//unEncryption  Object String
		
		if (!JugeFileFormat(strobjs)) {
			Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:UpdateLoginInfoToFile Data Format error ");
			m_cDesCrypt.CleanContextOfFile();
		}
		
		UserControl_UserInfo[] userInfoItems = GetLoginObjectInfoFromFile();
		int iFindIndex = JugeLoginObjExistInStorage(cUserInfo, userInfoItems);
		Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:UpdateLoginInfoToFile Login Info Status " + iFindIndex);
		
		UserControl_UserInfo userInfoTemp;
		if (-1 != iFindIndex) {
			userInfoTemp = userInfoItems[iFindIndex];
			userInfoTemp.m_iLoginType		=	cUserInfo.m_iLoginType;
			userInfoTemp.m_strNickName 		=	cUserInfo.m_strNickName;
			userInfoTemp.m_strUserPassWord 	= 	cUserInfo.m_strUserPassWord;
			userInfoTemp.m_strPhoneNum		= 	cUserInfo.m_strPhoneNum;
			userInfoTemp.m_strEmail			= 	cUserInfo.m_strEmail;
			userInfoTemp.m_strAutoLogin		= 	cUserInfo.m_strAutoLogin;
			userInfoTemp.m_strRememberPwd   = 	cUserInfo.m_strRememberPwd;
			for (int i = 0; i < UserControl_UserInfo.WEIBO_TYPE_MAXLEN; i ++) {	
				userInfoTemp.m_iWeiBoStatus[i] = cUserInfo.m_iWeiBoStatus[i];
				userInfoTemp.m_strWeiBoToken[i] = cUserInfo.m_strWeiBoToken[i];
				userInfoTemp.m_strWeiBoVaildPeriod[i] = cUserInfo.m_strWeiBoVaildPeriod[i];
			}
			
			DebugPrintSingleUserInfo(userInfoTemp, "UpdateLoginInfoToFile");
			ChangeObjectToString(objs,userInfoItems);
			return m_cDesCrypt.SaveEncryptInfoToFile(objs);
		} else {
			return false;
		}
	}
	//save login infomation to file by object stream
	public boolean SaveLoginInfoToFile(UserControl_UserInfo cUserInfo) {
		
		Object[] strobjs = new Object[MAX_STORED_USER_INFO + 1];	//Version + key + Encryption Object String
		Object[] objs = new Object[MAX_STORED_USER_INFO];			//unEncryption  Object String
		
		if (!JugeFileFormat(strobjs)) {
			Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:SaveLoginInfoToFile Data Format error ");
			m_cDesCrypt.CleanContextOfFile();
		}
		
		UserControl_UserInfo[] userInfoItems = GetLoginObjectInfoFromFile();
		int iFindIndex = JugeLoginObjExistInStorage(cUserInfo, userInfoItems);
		Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:SaveLoginInfoToFile Login Info Status " + iFindIndex);
		
		UserControl_UserInfo userInfoTemp;
		if (-1 != iFindIndex) {
			userInfoTemp = userInfoItems[iFindIndex];
			Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:SaveLoginInfoToFile AutoLogin Status " + userInfoItems[iFindIndex].m_strAutoLogin);
			// Update the info
			//Add Updata the PassWord
			userInfoTemp.m_strUserPassWord = cUserInfo.m_strUserPassWord;
			userInfoTemp.m_iLoginType = cUserInfo.m_iLoginType;
			
			for (int j = 0; j < UserControl_UserInfo.WEIBO_TYPE_MAXLEN; j++) {
				userInfoTemp.m_iWeiBoStatus[j] = cUserInfo.m_iWeiBoStatus[j];
				userInfoTemp.m_strWeiBoToken[j] = cUserInfo.m_strWeiBoToken[j];
				userInfoTemp.m_strWeiBoVaildPeriod[j] = cUserInfo.m_strWeiBoVaildPeriod[j];
				Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:SaveLoginInfoToFile " +
						"Update Storage Info Item " + j + " WeiBoStatus: "+cUserInfo.m_iWeiBoStatus[j] + ", WeiBoToken: "
						+ cUserInfo.m_strWeiBoToken[j] + " VaildPeriod: " + cUserInfo.m_strWeiBoVaildPeriod[j]);
			}
			// Move other Info
			for (int iIndex = iFindIndex - 1; iIndex >= 0; iIndex--) {
				userInfoItems[iIndex + 1] = userInfoItems[iIndex];
			}
			// Move to top
			userInfoItems[0] = userInfoTemp;
			DebugPrintUserInfoList(userInfoItems);
		} else {
			int iItemCount = GetStoredItemCount(userInfoItems);
			if (iItemCount >= MAX_STORED_USER_INFO) {
				for (int iIndex = MAX_STORED_USER_INFO - 2; iIndex >= 0; iIndex--) {
					userInfoItems[iIndex + 1] = userInfoItems[iIndex];
				}
				userInfoItems[0] = cUserInfo;
			} else {
				for (int iIndex = iItemCount - 1; iIndex >= 0; iIndex--) {
					userInfoItems[iIndex + 1] = userInfoItems[iIndex];
				}
				// else add to the end
				userInfoItems[0] = cUserInfo;
			}
			DebugPrintUserInfoList(userInfoItems);
		}
		
		ChangeObjectToString(objs,userInfoItems);
		return m_cDesCrypt.SaveEncryptInfoToFile(objs);
	}
	
	//login infomation was wrote to native file by object stream with encryption,
	//so this method will get login infomation by decrypting from file
	public String[] GetLoginInfoFromFile() {
		String [] tempStr = {"","","",""};
		UserControl_UserInfo[] cTempUserInfo = GetLoginObjectInfoFromFile();
		if (cTempUserInfo[0] != null) {
			if (cTempUserInfo[0].m_iLoginType == UserControl_UserInfo.USER_LOGIN_TYPE_PHONE) {
				tempStr = new String[3];
				tempStr[0] = cTempUserInfo[0].m_strNickName;
				tempStr[1] = cTempUserInfo[0].m_strUserPassWord;
				tempStr[2] = cTempUserInfo[0].m_strPhoneNum;
			} else if(cTempUserInfo[0].m_iLoginType == UserControl_UserInfo.USER_LOGIN_TYPE_EMAIL){
				tempStr = new String[4];
				tempStr[0] = cTempUserInfo[0].m_strNickName;
				tempStr[1] = cTempUserInfo[0].m_strUserPassWord;
				tempStr[2] = cTempUserInfo[0].m_strPhoneNum;
				tempStr[3] = cTempUserInfo[0].m_strEmail;
			} else {
				tempStr[0] = cTempUserInfo[0].m_strNickName;
				tempStr[1] = cTempUserInfo[0].m_strUserPassWord;
				tempStr[2] = cTempUserInfo[0].m_strPhoneNum;
				tempStr[3] = cTempUserInfo[0].m_strEmail;
			}
		}
		DebugPrintSingleUserInfo(cTempUserInfo[0], "GetLoginInfoFromFile");
		
		return tempStr;
	}
	
	public UserControl_UserInfo[] GetLoginInfoListFromFile() {
		UserControl_UserInfo[] cFileObjectitems = GetLoginObjectInfoFromFile();
		int iCount = 0;
		for (int index = 0; index < cFileObjectitems.length; index++) {
			if (null == cFileObjectitems[index]) {
				break;
			}
			iCount++;
		}
		Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:GetLoginInfoListFromFile Stored Account Size " + iCount);
		
		UserControl_UserInfo[] items = new UserControl_UserInfo[iCount];
		for (int index = 0; index < iCount; index++) {
			items[index] = new UserControl_UserInfo(cFileObjectitems[index]);
		}
		
		return items;
	}
	
	public UserControl_UserInfo[] GetLoginObjectInfoFromFile() {
		UserControl_UserInfo cUserInfo;
		Object[] objs = new Object[MAX_STORED_USER_INFO + 1];
		UserControl_UserInfo[] result = new UserControl_UserInfo[MAX_STORED_USER_INFO];
		
		if (!JugeFileFormat(objs)) {
			for (int i = 0; i < result.length; i++) {
				result[i] = null;
			}
		} else {
			for (int i = 0; i < result.length; i++) {
				String strObject = (String)objs[i + 1];
				if (null == strObject || "".equals(strObject)) {
					cUserInfo = null;
				} else {
					cUserInfo = new UserControl_UserInfo();
					String[] params = strObject.split(STORAGE_PARAM_SPLIT_STRING);
					if (null != params && params.length >= 1 && !"".equals(params[0])) {
						try {
							cUserInfo.m_iLoginType  		= Integer.parseInt(params[0]);
						}catch(Exception ex) {
							Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:GetLoginObjectInfoFromFile m_iLoginType Integer.parseInt Error");
							ex.printStackTrace();
						}
					}
					if (null != params && params.length >= 2) {
						cUserInfo.m_strNickName 		= params[1];
					}
					if (null != params && params.length >= 3) {
						cUserInfo.m_strUserPassWord 	= params[2];
					}
					if (null != params && params.length >= 4) {
						cUserInfo.m_strPhoneNum			= params[3];
					}
					if (null != params && params.length >= 5) {
						cUserInfo.m_strEmail			= params[4];
					}
					if (null != params && params.length >= 6) {
						cUserInfo.m_strAutoLogin		= params[5];
					}
					if (null != params && params.length >= 7) {
						cUserInfo.m_strRememberPwd		= params[6];
					}
					String[] WeiBoStatus = null;
					if (null != params && params.length >= 8) {
						WeiBoStatus =  params[7].split(STORAGE_Array_SPLIT_ESCAPE_STRING);
					}
					String[] WeiBoToken = null;
					if (null != params && params.length >= 9) {
						WeiBoToken =  params[8].split(STORAGE_Array_SPLIT_ESCAPE_STRING);
					}
					String[] WeiBoVaildPeriod = null;
					if (null != params && params.length >= 10) {
						WeiBoVaildPeriod =  params[9].split(STORAGE_Array_SPLIT_ESCAPE_STRING);
					}
					for (int j = 0; j < UserControl_UserInfo.WEIBO_TYPE_MAXLEN; j ++) {
						
						if (null != WeiBoToken && WeiBoToken.length > j) {
							cUserInfo.m_strWeiBoToken[j]		= WeiBoToken[j];
						}
						if (null != WeiBoVaildPeriod && WeiBoVaildPeriod.length > j) {
							cUserInfo.m_strWeiBoVaildPeriod[j]	= WeiBoVaildPeriod[j];
						}
					}
					try {
						for (int j = 0; j < UserControl_UserInfo.WEIBO_TYPE_MAXLEN; j ++) {
							if (null != WeiBoStatus && null != WeiBoStatus[j] && !"".equals(WeiBoStatus[j]) && WeiBoStatus.length > j) {
								cUserInfo.m_iWeiBoStatus[j]		= Integer.parseInt(WeiBoStatus[j]);
							}
						}
					}catch(Exception ex) {
						ex.printStackTrace();
						Log.d("[UserControl]", "[UserControl]--LoginInfoStorage:GetLoginObjectInfoFromFile Integer.parseInt Error");
					}
				}
				result[i] = cUserInfo;
			}
		}
		
		return result;
	}
	
	public void SetUserInfo(UserControl_UserInfo userInfo) {
		m_cLogInUserInfo = userInfo;
	} 
	public void SetOtherUserInfo(UserControl_UserInfo userInfo) {
		m_cOtherUserInfo = userInfo;
	} 
	
	public String GetSRVUrl() {
		return m_strServerUrl;
	}
	
	public String GetAuthSRVUrl() {
		return m_strAuthServerUrl;
	}
	private void InitUrlFromIni() {
		
		String requestRootUrl = W3JNI.getConfigValue("USERSRVURL");  
		String requestAuthUrl = W3JNI.getConfigValue("USERAUTHURL");
		if( requestRootUrl.length() == 0 )
		{
			m_strServerUrl = null;
			PNetLog.e( "UserControlManagerIF:: InitUrl error requestRootUrl=["+requestRootUrl + "]\n" );
		}
		else
		{
			m_strServerUrl = requestRootUrl;
		}
		
		if( requestAuthUrl.length() == 0 )
		{
			m_strAuthServerUrl = null;
			PNetLog.e( "UserControlManagerIF:: InitUrl error requestRootUrl=["+requestAuthUrl + "]\n" );
		}
		else
		{
			m_strAuthServerUrl = requestAuthUrl;
		}
		
	}

	//userID,email,cellphone. just need one of these three paragramer to show which user you want to invite
	//and other two can give ""
	public boolean InviteUserIntoCircle(String circleID,String userID ,String email,String cellphone){
		
		UserControl_RequestInviteUserIntoCircle cRequest = 
			(UserControl_RequestInviteUserIntoCircle)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_ASK_USER_IN_TO_CIRCLE);

		if (null != cRequest) {
			return cRequest.AskUserIntoCircle(circleID, userID, email, cellphone);
		}
		
		return false;
		
	}	
	public boolean UpdatePasswd(String oldPassword, String newPassword){
		
		UserControl_RequestChangePass cRequest = 
			(UserControl_RequestChangePass)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_CHANGE_PASS);

		if (null != cRequest) {
			return cRequest.ChangePassword(oldPassword, newPassword);
		}
		
		return false;
		
	}
	
	public boolean UploadUserInfo(UserControl_UserInfo userInfo){
		
		UserControl_RequestUpLoadUserInfo cRequest = 
			(UserControl_RequestUpLoadUserInfo)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_UPLOAD_USERINFO);

		if (null != cRequest) {
			return cRequest.UploadUserInfomation(userInfo);
		}
		
		return false;
		
	}

	
	private boolean DownloadUserInfoByPhone(String cellphone,boolean myself)
	{
		UserControl_RequestDownLoadUserInfo cRequest = 
			(UserControl_RequestDownLoadUserInfo)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_DOWNLOAD_USERINFO);

		if (null != cRequest) {
			return cRequest.DownloadByPhone(cellphone, myself);
		}
		
		return false;
		
	}
	private boolean DownloadUserInfoByEmail(String email,boolean myself)
	{
		UserControl_RequestDownLoadUserInfo cRequest = 
			(UserControl_RequestDownLoadUserInfo)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_DOWNLOAD_USERINFO);

		if (null != cRequest) {
			return cRequest.DownloadByMail(email, myself);
		}
		
		return false;
		
	}
	
	public boolean DownloadOtherUserInfo(String cellphone,String email)
	{
		if(0 != cellphone.compareTo(""))
		{
			return DownloadUserInfoByPhone(cellphone,true);
		}
		else
			if(0 != email.compareTo(""))
			{
				return DownloadUserInfoByEmail(email,true);
			}
		return false;
	}
	public boolean DownloadMyUserInfo(String cellphone,String email)
	{
		if(0 != cellphone.compareTo(""))
		{
			return DownloadUserInfoByPhone(cellphone,true);
		}
		else
			if(0 != email.compareTo(""))
			{
				return DownloadUserInfoByEmail(email,true);
			}
		return false;
	}
	public boolean SetAccessToken(String type,String token)
	{
		UserControl_RequestSetAccessToken cRequest = 
			(UserControl_RequestSetAccessToken)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SET_ACCESS_TOKEN);

		if (null != cRequest) {
			return cRequest.SetAccessToken(type, token);
		}
		
		return false;
	}
	
	public boolean SetAccessToken(String type,String token,String validPeriod)
	{
		UserControl_RequestSetAccessToken cRequest = 
			(UserControl_RequestSetAccessToken)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SET_ACCESS_TOKEN);

		if (null != cRequest) {
			return cRequest.SetAccessToken(type, token, validPeriod);
		}
		
		return false;
	}
	
	public boolean GetAccessTokenStatus()
	{
		UserControl_RequestGetAccessTokenStatus cRequest = 
			(UserControl_RequestGetAccessTokenStatus)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_GET_ACCESS_TOKEN);

		if (null != cRequest) {
			return cRequest.GetAccessTokenStatus();
		}
		return false;
	}
	
//	public boolean DownloadUserPhoto(String email,String cellphone,String userId)
//	{
//		UserControl_RequestGetPhoto cRequest = 
//			(UserControl_RequestGetPhoto)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_GET_PHOTO);
//
//		if (null != cRequest) {
//			return cRequest.DownloadUserPhoto(email, cellphone, userId);
//		}
//		return false;
//	}
	public boolean SNSGetResource(String type,String id,String email,String cellphone)
	{
		UserControl_RequestGetResource cRequest = 
			(UserControl_RequestGetResource)UserControl_RequestFactory.Instance().CreateRequest(UserControl_RequestId.UC_REQ_SNS_GET_RESOURCE);

		if (null != cRequest) {
			return cRequest.GetResource( type, id, email, cellphone);
		}  
		return false;
	}
	public boolean DownloadUserPhoto(String id,String email,String cellphone)
	{
		return SNSGetResource("3",id,email,cellphone);
	}
	public ArrayList<UserControl_CycleInfo> getM_cListCycleInfo() {
		return m_cListCycleInfo;
	}

	public void setM_cListCycleInfo(
			ArrayList<UserControl_CycleInfo> m_cListCycleInfo) {
		this.m_cListCycleInfo = m_cListCycleInfo;
	}

	public int getM_iCycleOffset() {
		return m_iCycleOffset;
	}
	public int getLoginStatus()
	{
		return i_LoginState;
	}
	
	/*
	 * false means logged out
	 * true means logged in
	 */
	public void setLoginStatus(boolean LoginStatus)
	{
		 if(LoginStatus){
			 setLoginStatus(LOGIN_STATE_LOGGED);
		 }else{
			 setLoginStatus(LOGIN_STATE_LOGOUT);
		 }
	}
	
	public boolean isLogging(){
		return i_LoginState == LOGIN_STATE_LOGGING || i_LoginState == LOGIN_STATE_LOGGINGOUT;
	}
	
	private void setLoginStatus(int LoginStatus)
	{
		i_LoginState = LoginStatus;
	}
	public void notifyLoginSucceed(){
		setLoginStatus(LOGIN_STATE_LOGGED);
	}
	
	public void setLoginIdentityTag(int IdentityTag) {
		m_iLoginIdentityTag = IdentityTag;
	}
	
	public int getLoginIdentityTag() {
		return m_iLoginIdentityTag;
	}
	
	public String getUserPhotoPath()
	{
		return m_strPhotoPath;
	}
	public void setUserPhotoPath(String UserPhotoPath)
	{
		m_strPhotoPath=UserPhotoPath;
	}
	public void setM_iCycleOffset(int m_iCycleOffset) {
		this.m_iCycleOffset = m_iCycleOffset;
	}

	public UserControl_BlackList getM_cBlackList() {
		return m_cBlackList;
	}

	public void setM_cBlackList(UserControl_BlackList m_cBlackList) {
		this.m_cBlackList = m_cBlackList;
	}

	public UserControl_PosterListInfo getM_cPosterListInfo() {
		return m_cPosterListInfo;
	}

	public void setM_cPosterListInfo(UserControl_PosterListInfo m_cPosterListInfo) {
		this.m_cPosterListInfo = m_cPosterListInfo;
	}

	public UserControl_PositionResData getM_cPositionResData() {
		return m_cPositionResData;
	}

	public void setM_cPositionResData(UserControl_PositionResData m_cPositionResData) {
		this.m_cPositionResData = m_cPositionResData;
	}

	public UserControl_UserFeedBack getM_cUserFeedBack() {
		return m_cUserFeedBack;
	}

	public void setM_cUserFeedBack(UserControl_UserFeedBack m_cUserFeedBack) {
		this.m_cUserFeedBack = m_cUserFeedBack;
	}

	public UserControl_FavoriteColData getM_cFavoriteCollectionData() {
		return m_cFavoriteCollectionData;
	}

	public void setM_cFavoriteCollectionData(
			UserControl_FavoriteColData m_cFavoriteCollectionData) {
		this.m_cFavoriteCollectionData = m_cFavoriteCollectionData;
	}

	public ArrayList<UserControl_InviteFailedUserInfo> getM_cInviteFailedUserList() {
		return m_cInviteFailedUserList;
	}

	public void setM_cInviteFailedUserList(
			ArrayList<UserControl_InviteFailedUserInfo> m_cInviteFailedUserList) {
		this.m_cInviteFailedUserList = m_cInviteFailedUserList;
	}


	public UserControl_UserFeedBack getM_cUpdateDataResponseInfo() {
		return m_cUpdateDataResponseInfo;
	}

	public void setM_cUpdateDataResponseInfo(
			UserControl_UserFeedBack m_cUpdateDataResponseInfo) {
		this.m_cUpdateDataResponseInfo = m_cUpdateDataResponseInfo;
	}

	public byte[] getM_bytePosterVoiceInfo() {
		return m_bytePosterVoiceInfo;
	}

	public void setM_bytePosterVoiceInfo(byte[] m_bytePosterVoiceInfo) {
		this.m_bytePosterVoiceInfo = m_bytePosterVoiceInfo;
	}

	public byte[] getM_bytePosterUserHeaderPicInfo() {
		return m_bytePosterUserHeaderPicInfo;
	}

	public void setM_bytePosterUserHeaderPicInfo(
			byte[] m_bytePosterUserHeaderPicInfo) {
		this.m_bytePosterUserHeaderPicInfo = m_bytePosterUserHeaderPicInfo;
	}

	public byte[] getM_bytePosterPictureInfo() {
		return m_bytePosterPictureInfo;
	}

	public void setM_bytePosterPictureInfo(byte[] m_bytePosterPictureInfo) {
		this.m_bytePosterPictureInfo = m_bytePosterPictureInfo;
	}

	public UserControl_RankListInfo getM_cRankListInfo() {
		return m_cRankListInfo;
	}

	public void setM_cRankListInfo(UserControl_RankListInfo m_cRankListInfo) {
		this.m_cRankListInfo = m_cRankListInfo;
	}

	public UserControl_UserStyleInfo getM_cUserStyleInfo() {
		return m_cUserStyleInfo;
	}

	public void setM_cUserStyleInfo(UserControl_UserStyleInfo m_cUserStyleInfo) {
		this.m_cUserStyleInfo = m_cUserStyleInfo;
	}

	public UserControl_PosterDetail getM_cPosterDetails() {
		return m_cPosterDetails;
	}

	public void setM_cPosterDetails(UserControl_PosterDetail m_cPosterDetails) {
		this.m_cPosterDetails = m_cPosterDetails;
	}

	public UserControl_WeiBoBindStatus getM_cWeiBoBindStatus() {
		return m_cWeiBoBindStatus;
	}

	public void setM_cWeiBoBindStatus(UserControl_WeiBoBindStatus m_cWeiBoBindStatus) {
		this.m_cWeiBoBindStatus = m_cWeiBoBindStatus;
	}
	
	public UserControl_UserInfo getM_cLoginStoredInfo() {
		return m_cLoginStoredInfo;
	}

	public void setM_cLoginStoredInfo(UserControl_UserInfo m_cLoginStoredInfo) {
		this.m_cLoginStoredInfo = m_cLoginStoredInfo;
	}
}
