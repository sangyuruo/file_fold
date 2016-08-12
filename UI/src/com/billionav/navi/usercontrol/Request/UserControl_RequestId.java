package com.billionav.navi.usercontrol.Request;

public class UserControl_RequestId {
	public static final int UC_REQ_INVALID_ID = 0;
	public static final int UC_REQ_REG_BY_PHONE = 1;
	public static final int UC_REQ_CHECK_REGISTED = 2;
	public static final int UC_REQ_VALIDATECODE = 3;
	public static final int UC_REQ_USER_LOGIN = 4;
	public static final int UC_REQ_USER_LOGOUT = 5;
	public static final int UC_REQ_GET_PASS_BY_PHONE = 6;
	public static final int UC_REQ_CHANGE_PASS = 7;
	public static final int UC_REQ_UPLOAD_USERINFO = 8;
	public static final int UC_REQ_DOWNLOAD_USERINFO = 9;
	public static final int UC_REQ_REG_BY_MAIL = 10;
	public static final int UC_REQ_GET_PASS_BY_MAIL = 11;
	public static final int UC_REQ_DELETE_USER = 12;
	public static final int UC_REQ_ASK_USER_IN_TO_CIRCLE = 13;
	public static final int UC_REQ_SET_ACCESS_TOKEN = 14;
	public static final int UC_REQ_GET_ACCESS_TOKEN = 15;
	public static final int UC_REQ_GET_PHOTO = 16;
	public static final int UC_REQ_SNS_GET_RESOURCE = 17;
	
	public static final int UC_REQ_GET_VERIFICATION_CODE = 18;
	public static final int UC_REQ_SET_NEW_PASSWORD = 19;
	
//SNS 
	//GroupManager
	public static final int UC_REQ_SNS_GM_BUILD_GROUP 	= 101;
	public static final int UC_REQ_SNS_GM_DEL_GROUP		= 102;
	public static final int UC_REQ_SNS_GM_EDIT_GROUP 	= 103;
	public static final int UC_REQ_SNS_GM_QUERY_GROUPS 	= 104;
	
	//GroupMemberManger
	public static final int UC_REQ_SNS_GMM_ADD_TO_BLACKLIST = 111;
	public static final int UC_REQ_SNS_GMM_EXIT_GROUP		= 112;
	public static final int UC_REQ_SNS_GMM_INVITE_PERSON	= 113;
	public static final int UC_REQ_SNS_GMM_JOIN_GROUP		= 114;
	public static final int UC_REQ_SNS_GMM_KICKOFF_PERSON	= 115;
	public static final int UC_REQ_SNS_GMM_APPROVAL_PERSON  = 116;
	public static final int UC_REQ_SNS_GMM_MOVEOUT_BLACKLIST= 117;
	public static final int UC_REQ_SNS_GMM_QUERY_BLACKLIST	= 118;
	
	//InformatinManager
	public static final int UC_REQ_SNS_IM_BLOG_SHARING		= 121;
	public static final int UC_REQ_SNS_IM_GROUP_SHARING		= 122;
	
	//Poster Manager
	public static final int UC_REQ_SNS_PM_UPLOAD_DATA		= 131;
	public static final int UC_REQ_SNS_PM_REPORT_POS 		= 132;
	public static final int UC_REQ_SNS_PM_USER_FEEDBACK		= 133;
	public static final int UC_REQ_SNS_PM_GET_PHOTO			= 134;
	public static final int UC_REQ_SNS_PM_QUERY_COL_LIST	= 135;
	public static final int UC_REQ_SNS_PM_DEL_COL			= 136;
	public static final int UC_REQ_SNS_PM_QUERY_POST_LIST	= 137;
	public static final int UC_REQ_SNS_PM_QUERY_POST_DETAILS= 138;
	
	//Get Resource
	public static final int UC_REQ_SNS_GR_GET_RESOURCE		= 141;
	
	//MyData 
	public static final int UC_REQ_MYDATA_GET_RANKLIST		= 151;
	public static final int UC_REQ_MYDATA_GET_USER_STYLE	= 152;
}
