package com.billionav.navi.usercontrol;

import android.util.Log;

import com.billionav.navi.menucontrol.NSTriggerID;

public class UserControl_CommonVar {
	public static final int UC_RESPONSECODE_SUCCESS = 1;
	public static final int UC_RESPONSECODE_FAILED  = 0;
	
	public static final int UIC_MN_TRG_UC_INVLIDATE = -1;

//WeiBo Name
	public static final String APP_WEIBO_SINA	= "sina";
	public static final String APP_WEIBO_TECENT = "tecent";
	
//VALIDATE
	public static final int VALIDATE_INPUT_OK			=	0;
	public static final int VAILDATE_INPUT_TOO_SHORT	= 	1;
	public static final int VAILDATE_INPUT_TOO_LONG		=	2;
	public static final int VAILDATE_INPUT_FORMAT_ERROR	=	3;
	
//Login Identify of Source
	public static final int LOGIN_INDETITY_OF_SOURCE_OTHER			=	0;
	public static final int LOGIN_INDETITY_OF_SOURCE_UI				=	1;	//When UI Action to Login
	public static final int LOGIN_INDETITY_OF_SOURCE_AUTO			=	2;	//When SetAutoLogin
	public static final int LOGIN_INDETITY_OF_SOURCE_RELOGIN		=	3;	//When The Token outOf Time will reLogin
	public static final int LOGIN_INDETITY_OF_SOURCE_REGIST_LOGIN	=	4; //When Register finish then Login
	
//SNS Part
	//Invite Person  person Info Type
	public static final int PERSON_INFO_TYPE_USER_ID = 1;
	public static final int PERSON_INFO_TYPE_USER_EMAIL = 2;
	public static final int PERSON_INFO_TYPE_USER_CELLPHONE = 3;
	
	//SNS-7-1 GetResource
	public static final int GET_RESOURCE_TYPE_POSTER_PHOTO = 1;
	public static final int GET_RESOURCE_TYPE_POSTER_VOICE = 2;
	public static final int GET_RESOURCE_TYPE_USER_PICTURE = 3;
	
	
	//Join Group 
	/**
	 *  0 - already invited , wait for conform 			
	 *  1 - already post request , wait for conform
	 *  2 -	already conform	
	 *  3 -	refuse		
	 *
	 */
	public static final int REQUESR_JOIN_CYCLE_INVITED_NOT_CONFORMED = 0;
	public static final int REQUESR_JOIN_CYCLE_REQUEST_NOT_CONFORMED = 1;
	public static final int REQUESR_JOIN_CYCLE_ALREADY_CONFORED		 = 2;
	public static final int REQUESR_JOIN_CYCLE_REFUSE				 = 3;
	
	//Group Type
	public static final int CYCLE_TYPE_PUBLIC = 1;
	public static final int CYCLE_TYPE_PROTECTED = 2;
	public static final int CYCLE_TYPE_PRIVATE = 3;
	
	//poster search constant
	public static final int POST_QUERY_DEFAULT_OFFLEN = 0;
	public static final int POST_QUERY_DEFAULT_MAXLEN_100= 100;
	public static final int POST_QUERY_DEFAULT_MAXLEN_20 = 20;
	
	//Update Poster Accident Level 
	public static final int ROAD_INFO_ACCIDENT_LEVEL_SLIGHT = 0;
	public static final int ROAD_INFO_ACCIDENT_LEVEL_MODERATELY = 1;
	public static final int ROAD_INFO_ACCIDENT_LEVEL_SEVERE = 2;
	
	//Update Poster Road Info
	public static final int ROAD_INFO_FIXING	= 0;
	public static final int ROAD_INFO_WITH_WATER = 1;
	public static final int ROAD_INFO_DAMAGED	= 2;
	
	//Poster type
	public static final int POST_QUERY_POST_TYPE_OTHER	= 0;
	public static final int POST_QUERY_POST_TYPE_TRAFFIC_CONGESTION = 1;
	public static final int POST_QUERY_POST_TYPE_TRAFFIC_ACCIDENT = 2;
	public static final int POST_QUERY_POST_TYPE_ACCESS_DIFFICULT = 3;
	public static final int POST_QUERY_POST_TYPE_DANGER_REPORT = 4;
	public static final int POST_QUERY_POST_TYPE_POLICE	= 5;
	public static final int POST_QUERY_POST_TYPE_CAMERA = 6;
	
	//MyData
	public static final String MYDATA_GETSAFERANK_TYPE_TOTLE = "totle";
	public static final String MYDATA_GETSAFERANK_TYPE_MONTH = "month";
	public static final String MYDATA_GETSAFERANK_TYPE_PERIOD = "period";
	
	//TriggleID
	//GroupManager:: Build Group
	public static final int UIC_MN_TRG_UC_BUILD_GROUP = NSTriggerID.UIC_MN_TRG_UC_BUILD_GROUP;			//Build Group Triggle ID
	
	//GroupManager:: Delete Group
	public static final int UIC_MN_TRG_UC_DEL_GROUP  = NSTriggerID.UIC_MN_TRG_UC_DEL_GROUP;			//Del Group Triggle ID
	
	//GroupManager:: Edit Group
	public static final int UIC_MN_TRG_UC_EDIT_GROUP  = NSTriggerID.UIC_MN_TRG_UC_EDIT_GROUP;			//Edit Group Info Triggle ID
	
	//GroupManager:: Query Groups
	public static final int UIC_MN_TRG_UC_QUERY_GROUPS  = NSTriggerID.UIC_MN_TRG_UC_QUERY_GROUPS;		//Query Groups Info Triggle ID
	
	//GroupMemberManger:: Add To BlackList
	public static final int UIC_MN_TRG_UC_ADDTOBLACKLIST = NSTriggerID.UIC_MN_TRG_UC_ADDTOBLACKLIST;		
	
	//GroupMemberManger:: Move out BlackList
	public static final int UIC_MN_TRG_UC_MOVEOUT_BLACKLIST = NSTriggerID.UIC_MN_TRG_UC_MOVEOUT_BLACKLIST;
	
	//GroupMemberManger:: Query BlackList
	public static final int UIC_MN_TRG_UC_QUERY_BLACKLIST = NSTriggerID.UIC_MN_TRG_UC_QUERY_BLACKLIST;
	
	//GroupMemberManger::Exit Group 
	public static final int UIC_MN_TRG_UC_EXITGROUP		= NSTriggerID.UIC_MN_TRG_UC_EXITGROUP;
	
	//GroupMemberManger::Invite Person
	public static final int UIC_MN_TRG_UC_INVITEPERSON		= NSTriggerID.UIC_MN_TRG_UC_INVITEPERSON;
	
	//GroupMemberManger::KickOffPerson
	public static final int UIC_MN_TRG_UC_KICKOFF_PERSON	= NSTriggerID.UIC_MN_TRG_UC_KICKOFF_PERSON;
	
	//GroupMemberManger::JoinGroup
	public static final int UIC_MN_TRG_UC_JOIN_GROUP		= NSTriggerID.UIC_MN_TRG_UC_JOIN_GROUP;
	
	//GroupMemberManager::Approval Join Group
	public static final int UIC_MN_TRG_UC_APPROVAL_JOIN_GROUP		= NSTriggerID.UIC_MN_TRG_UC_APPROVAL_JOIN_GROUP;
	
	//InformatinManager::Blog Sharing
	public static final int UIC_MN_TRG_UC_BLOG_SHARING		= NSTriggerID.UIC_MN_TRG_UC_BLOG_SHARING;
	
	//InformatinManager::Group Sharing
	public static final int UIC_MN_TRG_UC_GROUP_SHARING		= NSTriggerID.UIC_MN_TRG_UC_GROUP_SHARING;
	
	//PosterManager:: UPLOAD VOICE OR IMAGE
	public static final int UIC_MN_TRG_UC_PM_UPLOADDATA	= NSTriggerID.UIC_MN_TRG_UC_PM_UPLOADDATA;
	
	//PosterManager::Report Position
	public static final int UIC_MN_TRG_UC_PM_REPORTPOS = NSTriggerID.UIC_MN_TRG_UC_PM_REPORTPOS;
	
	//PosterManager::User feedback
	public static final int UIC_MN_TRG_UC_PM_USER_FEEDBACK		= NSTriggerID.UIC_MN_TRG_UC_PM_USER_FEEDBACK;
	
	//PosterManager::Get photo
	public static final int UIC_MN_TRG_UC_PM_GET_PHOTO			= NSTriggerID.UIC_MN_TRG_UC_PM_GET_PHOTO;
	
	//PosterManager::Query Collections list
	public static final int UIC_MN_TRG_UC_PM_QUERY_COLLECTION_LIST		= NSTriggerID.UIC_MN_TRG_UC_PM_QUERY_COLLECTION_LIST;
	
	//PosterManager:: Delete Collection 
	public static final int UIC_MN_TRG_UC_PM_DEL_COLLECTION		= NSTriggerID.UIC_MN_TRG_UC_PM_DEL_COLLECTION;
	
	//PosterManager:: Query Poster List
	public static final int UIC_MN_TRG_UC_PM_QUERY_POSTER_LIST	= NSTriggerID.UIC_MN_TRG_UC_PM_QUERY_POSTER_LIST;
	
	//PosterManager:: Query Poster Details
	public static final int UIC_MN_TRG_UC_PM_QUERY_POSTER_DETAILS = NSTriggerID.UIC_MN_TRG_UC_PM_QUERY_POSTER_DETAILS;
	
	//GetResource
	public static final int UIC_MN_TRG_UC_GR_GET_RESOURCE	=	NSTriggerID.UIC_MN_TRG_UC_GR_GET_RESOURCE;
	
	//MyData Part
	public static final int UIC_MN_TRG_UC_MYDATA_GET_RANKLIST = NSTriggerID.UIC_MN_TRG_UC_MYDATA_GET_RANKLIST;
	
	public static final int UIC_MN_TRG_UC_MYDATA_GET_USERDATA = NSTriggerID.UIC_MN_TRG_UC_MYDATA_GET_USERDATA;
	
	//Error Code
	public static final int UIC_MN_ERRORCODE_EMAIL_BEREGISTED 		=	100;
	public static final int UIC_MN_ERRORCODE_EMAIL_INVALIDATE		=	101;
	public static final int UIC_MN_ERRORCODE_CELLPHONE_BEREGISTED	=	102;
	public static final int UIC_MN_ERRORCODE_CELLPHONE_INVALIDATE	=	103;
	public static final int UIC_MN_ERRORCODE_PASSWORD_INVALIDATE	=	104;
	public static final int UIC_MN_ERRORCODE_LOGIN_TOKEN_INVALIDATE	=	105;
	public static final int UIC_MN_ERRORCODE_ERROR_USERINFO			=	106;
	public static final int UIC_MN_ERRORCODE_ERROR_PASSWORD			=	107;
	public static final int UIC_MN_ERRORCODE_USER_NOEXIST			=	108;
	public static final int UIC_MN_ERRORCODE_NICKNAME_INVALIDATE	=	109;
	public static final int UIC_MN_ERRORCODE_USERID_INVALIDATE		=	110;
	public static final int UIC_MN_ERRORCODE_DATA_FORMAT_ERROR		=	111;
	public static final int UIC_MN_ERRORCODE_SEX_FORMAT_ERROR		=	112;
	public static final int UIC_MN_ERRORCODE_LONLAT_FORMAT_ERROR	=	113;
	public static final int UIC_MN_ERRORCODE_REGISTE_FORMAT_ERROR	=	114;
	public static final int UIC_MN_ERRORCODE_NICKNAME_BEREGISTED	=	115;
	public static final int UIC_MN_ERRORCODE_REGISTED_NOT_ACTIVE	=	116;
	public static final int UIC_MN_ERRORCODE_CANNOT_ACTIVE_MUTIPUAL	=	117;
	public static final int UIC_MN_ERRORCODE_NO_USER_AVATAR			=	118;
	public static final int UIC_MN_ERRORCODE_USER_BLOCKED			=	119;
	
	public static final int UIC_MN_ERRORCODE_REPEATED_CYCLENAME		=	200;
	public static final int UIC_MN_ERRORCODE_CYCLE_ID_INVALIDATE	=	201;
	public static final int UIC_MN_ERRORCODE_CYCLE_TYPE_INVALIDATE	=	202;
	public static final int UIC_MN_ERRORCODE_NO_PERMISSION_UPDATE_CYCLE	=	203;
	public static final int UIC_MN_ERRORCODE_NO_PERMISSION_DEL_CYCLE	=	204;
	public static final int UIC_MN_ERRORCODE_CYCLE_NO_EXIST			=	205;
	public static final int UIC_MN_ERRORCODE_CYCLE_NAME_INVALIDATE	=	206;
	public static final int UIC_MN_ERRORCODE_CYCLETYPE_INVALIDATE	=	207;
	public static final int UIC_MN_ERRORCODE_USER_ALREADY_IN_CYCLE	=	208;
	public static final int UIC_MN_ERRORCODE_INVALIDATE_INVITE		=	209;
	public static final int UIC_MN_ERRORCODE_REFUSE_REQUEST			=	210;
	public static final int UIC_MN_ERRORCODE_CANNOT_KICKOFF_OWNER_SELF	=	211;
	public static final int UIC_MN_ERRORCODE_USER_NOT_IN_CYCLE		=	212;
	public static final int UIC_MN_ERRORCODE_CANNOT_BLOCK_SELF		=	213;
	public static final int UIC_MN_ERRORCODE_USER_ALREADY_IN_BLOCKLIST	=	214;
	public static final int UIC_MN_ERRORCODE_CANNOT_INVITE_FOR_NOT_IN_CYCLE	= 215;
	public static final int UIC_MN_ERRORCODE_USER_NOT_IN_BLOCKLIST	=	216;
	public static final int UIC_MN_ERRORCODE_COLLECTION_NOT_EXIST	=	217;
	
	public static final int UIC_MN_ERRORCODE_POSTER_NO_EXIST		=	300;
	public static final int UIC_MN_ERRORCODE_POSTER_ID_FORMAT_ERROR	=	301;
	public static final int UIC_MN_ERRORCODE_POSTER_NO_PHOTO		=	302;
	public static final int UIC_MN_ERRORCODE_POSTER_PHOTOID_FORMAT_ERROR	=	303;
	public static final int UIC_MN_ERRORCODE_COLLECTION_ID_INVALIDATE	=	304;
	public static final int UIC_MN_ERRORCODE_POSTER_TYPE_INVALIDATE		=	305;
	public static final int UIC_MN_ERRORCODE_POSTER_ALREADY_COLLECTED	=	306;
	
	public static final int UIC_MN_ERRORCODE_SYNC_NOTOKEN_OR_TOKENED_PASSED_TIME	=	401;
	
	
	public static final int SNS_LOG_STEP_START = 1;
	public static final int SNS_LOG_STEP_SEND = 2;
	public static final int SNS_LOG_STEP_RECEIVE = 3;
	public static final int SNS_LOG_STEP_TRIGGLE = 4;
	public static final int SNS_LOG_STEP_EXCEPTION = 5;
	public static final int SNS_LOG_STEP_END = 6;
	
	public static void SNSLog(String strModel, int step, String msg) {
		String strMainModel = "SNSLog";
		if (SNS_LOG_STEP_START == step) {
			Log.d(strMainModel, strModel + "::Start		" + msg);
		} else if (SNS_LOG_STEP_SEND == step) {
			Log.d(strMainModel, strModel + "::URL		" + msg);
		} else if (SNS_LOG_STEP_RECEIVE == step) {
			Log.d(strMainModel, strModel + "::Receive		" + msg);
		} else if (SNS_LOG_STEP_TRIGGLE == step) {
			Log.d(strMainModel, strModel + "::Triggle		" + msg);
		} else if (SNS_LOG_STEP_EXCEPTION == step) {
			Log.e(strMainModel, strModel + "::Exception		" + msg);
		} else if (SNS_LOG_STEP_END == step) {
			Log.d(strMainModel, strModel + "::End		" + msg);
		}
	}

	
	
}
