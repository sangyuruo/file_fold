package com.billionav.jni;


public class UIUserReportJNI {
	
	public static final int SNS_CATEGORY_TRAFFICJAM         = 1;
	public static final int SNS_CATEGORY_TRAFFICACCIDENT    = 2;
	public static final int SNS_CATEGORY_ROADCLOSE          = 3;
	public static final int SNS_CATEGORY_TRAFFICCONTROL     = 4;
	public static final int SNS_CATEGORY_ROADDAMAGE         = 5;
	public static final int SNS_CATEGORY_WATERFROZEN        = 6;
	public static final int SNS_CATEGORY_SPEEDCAMERA        = 7;        
	public static final int SNS_CATEGORY_POLICE             = 8;
	public static final int SNS_CATEGORY_OTHER              = 9;
	
	private UIUserReportJNI() {
	}
	
	public native void SetToken(String m_Token);
	
	public static class DetailInfo {
		public int		category; 
		public int		Type;
		public long		LinkID;
		public int		lon;      
		public int		lat;
		public String	LocName;
		public String	Text;
		public int		UserID;
		public String	NickName;
		public int		Stars;
		public String	PostTime;
		public String	PhotoID;
		public String	VoiceID;
		//don't delete or rename it, define for jni
		public DetailInfo(int category, int Type, long LinkID, int lon, int lat, String LocName, String Text, int UserID, String NickName,
				int Stars, String PostTime, String PhotoID, String VoiceID) {
			this.category	=	category; 
			this.Type		=	Type;
			this.LinkID		=	LinkID;
			this.lon		=	lon;      
			this.lat		=	lat;
			this.LocName	=	LocName;	// Shallow copy
			this.Text		=	Text;		// Shallow copy
			this.UserID		=	UserID;
			this.NickName	=	NickName;	// Shallow copy
			this.Stars		=	Stars;
			this.PostTime	=	PostTime;	// Shallow copy
			this.PhotoID	=	PhotoID;	// Shallow copy
			this.VoiceID	=	VoiceID;	// Shallow copy
		}
	}
	
	public static class QueryPostDetailRes {
		public String		PostID; 
		public boolean		Result;
		public int			ResCode;
		public DetailInfo	cDetailInfo;
		//don't delete or rename it, define for jni
		public QueryPostDetailRes(String PostID, boolean Result, int ResCode, DetailInfo cDetailInfo) {
			this.PostID 		=	PostID;			// Shallow copy
			this.Result			=	Result;
			this.ResCode		=	ResCode;
			this.cDetailInfo	=	cDetailInfo;	// Shallow copy
		}
	}
	
	public static class BroadcastResourceByID {
		public String		PostID; 
		public byte[]		ResourceBuffer	= null;
		public int			ResourceSize;
		public int			ResourceType;
		//don't delete or rename it, define for jni
		public BroadcastResourceByID(String PostID, byte[] ResourceBuffer, int ResourceSize, int ResourceType) {
			this.PostID 		=	PostID;			// Shallow copy
			this.ResourceSize	=	ResourceSize;
			this.ResourceType	=	ResourceType;
			
			this.ResourceBuffer	=	ResourceBuffer;	// Reference
			/*
			this.ResourceBuffer = new byte[ResourceSize];
			if (null != ResourceBuffer) {
				System.arraycopy(ResourceBuffer, 0, this.ResourceBuffer, 0, ResourceSize);
			}*/
		}
	}
	
	public static native void sendReport(long RequestID, int lon, int lat,
			int category, int type, String picPath, String voicePath, String contentText);
	
	public static native void sendUserReportDeatil(String postId);
	
	public static native QueryPostDetailRes getUserReportDeatil();
	
	public static native BroadcastResourceByID getBroadcastResourceByID();
}
