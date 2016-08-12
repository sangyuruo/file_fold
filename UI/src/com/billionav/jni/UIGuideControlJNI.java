package com.billionav.jni;

public class UIGuideControlJNI {
	public static final int DPGUDEF_CMN_NOPARTS			= 0x00000000;				///< ���ڂ���ȫ�ѩ`�ķǱ�ʾ��
	public static final int DPGUDEF_CMN_ROUTEINFO		= 0x00000001;				///< ��`�ȥ���ե���`�����
	public static final int DPGUDEF_CMN_DISTANCE		= 0x00000002;				///< �о��x
	public static final int DPGUDEF_CMN_GUIDE_LANE		= 0x00000008;				///< ���ڵص��`��
	public static final int DPGUDEF_CMN_GUIDE_SIGNPOST  = 0x00000020;
	public static final int DPGUDEF_CMN_STREETNAME		= 0x00000010;				///< CurrentStreetName
	public static final int DPGUDEF_CMN_MAGILLUST		= 0x00000080;				///< ����㰸�ڇ�
	public static final int DPGUDEF_CMN_ETC				= 0x00000100;				// ETC
	public static final int DPGUDEF_CMN_ETAINFO			= 0x00000400;				///< ETA
	public static final int DPGUDEF_CMN_HWY_SIGNPOST    = 0x00002000;
	public static final int DPGUDEF_CMN_SPEED			= 0x00010000;				///< Speed
	//@}
	
	/*---------------------------------------------------------------------------*/
	/// @name ����ʸӡ�N�e(DPGUDEF_CMN_GuideArrowKind)
	//@{
	//CN JP OverSea
	public static final int DPGUDEF_CMN_GUIDEARROW_NONE = 0; 				///< ʸӡ�o�������ڂ�??�o������
	public static final int DPGUDEF_CMN_GUIDEARROW_STRAIGHT = 1;			///< ֱ�M
	public static final int DPGUDEF_CMN_GUIDEARROW_TURN_R = 2;				///< ����
	public static final int DPGUDEF_CMN_GUIDEARROW_TURN_L = 3; 				///< ����
	public static final int DPGUDEF_CMN_GUIDEARROW_TURN_FR = 4;				///< ��ǰ
	public static final int DPGUDEF_CMN_GUIDEARROW_TURN_FL = 5;  			///< ��ǰ
	public static final int DPGUDEF_CMN_GUIDEARROW_TURN_RR = 6;  			///< ���᷽
	public static final int DPGUDEF_CMN_GUIDEARROW_TURN_RL = 7;  			///< ���᷽
	public static final int DPGUDEF_CMN_GUIDEARROW_UTERN = 8;				///< U���`��
	//JP OverSea
	public static final int DPGUDEF_CMN_GUIDEARROW_UTERN_R = 9;
	public static final int DPGUDEF_CMN_GUIDEARROW_UTERN_L = 10;
	public static final int DPGUDEF_CMN_GUIDEARROW_BST_R = 11;
	public static final int DPGUDEF_CMN_GUIDEARROW_BST_L = 12;

	//OverSea(Roundabout)
	public static final int DPGUDEF_CMN_GUIDEARROW_R45DEG_OUT = 13;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R90DEG_OUT = 14;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R135DEG_OUT = 15;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R180DEG_OUT = 16;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R225DEG_OUT = 17;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R270DEG_OUT = 18;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R315DEG_OUT = 19;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R360DEG_OUT = 20;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R45DEG_IN = 21;      ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R90DEG_IN = 22;      ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R135DEG_IN = 23;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R180DEG_IN = 24;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R225DEG_IN = 25;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R270DEG_IN = 26;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R315DEG_IN = 27;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_R360DEG_IN= 28;     ///< 
	
	public static final int DPGUDEF_CMN_GUIDEARROW_L45DEG_OUT = 29;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L90DEG_OUT = 30;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L135DEG_OUT = 31;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L180DEG_OUT = 32;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L225DEG_OUT = 33;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L270DEG_OUT = 34;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L315DEG_OUT = 36;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L360DEG_OUT = 37;    ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L45DEG_IN = 38;      ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L90DEG_IN = 39;      ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L135DEG_IN = 40;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L180DEG_IN = 41;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L225DEG_IN = 42;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L270DEG_IN = 43;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L315DEG_IN= 44;     ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_L360DEG_IN = 45;     ///< 
	
	public static final int DPGUDEF_CMN_GUIDEARROW_BA_IN = 46;          ///< 
	public static final int DPGUDEF_CMN_GUIDEARROW_BA_OUT = 47;         ///< 
	
	public static final int DPGUDEF_CMN_GUIDEARROW_MAX = 48;					///< ʸӡ�N�e�t��
	//@}
	
	/*---------------------------------------------------------------------------*/
	/// @Distance �N�e
	//@{
	// OverSea
	public static final int DPGUDEF_CMN_DISTANCE_LONG = 0;						
	public static final int DPGUDEF_CMN_DISTANCE_MIDDLE = 1;						
	public static final int DPGUDEF_CMN_DISTANCE_CLOSE = 2;	
	public static final int DPGUDEF_CMN_DISTANCE_MAX = 3;
	//@}
		
	/*---------------------------------------------------------------------------*/
	/// @name ������N�e(DPGUDEF_CMN_FlagKind)
	//@{
	// CN JP OverSea
	public static final int DPGUDEF_CMN_FLAG_NONE = 0;						///< ��ʾ�o�������ڂ�??�o������
	public static final int DPGUDEF_CMN_FLAG_GOAL = 1;						///< Ŀ�ĵ�
	public static final int DPGUDEF_CMN_FLAG_VIA_1 = 2;						///< ���ĵأ�
	public static final int DPGUDEF_CMN_FLAG_VIA_2 = 3;						///< ���ĵأ�
	public static final int DPGUDEF_CMN_FLAG_VIA_3 = 4;						///< ���ĵأ�
	public static final int DPGUDEF_CMN_FLAG_VIA_4 = 5;						///< ���ĵأ�
	public static final int DPGUDEF_CMN_FLAG_VIA_5 = 6;						///< ���ĵ� 5	
	public static final int DPGUDEF_CMN_FLAG_GUIDEPOINT = 7;				///< ���ڵص�
	public static final int DPGUDEF_CMN_FLAG_ROUDABOUT = 8;					///< RoundAbout
	public static final int DPGUDEF_CMN_FLAG_TOLL = 9;						///< �Ͻ���
	public static final int DPGUDEF_CMN_FLAG_FERRY = 10;					///< �ե���`�\���
	public static final int DPGUDEF_CMN_FLAG_MAX = 11;						///< ��N�e��
	//@}
	
	/*---------------------------------------------------------------------------*/
	/// @name ETA��N�e(DPGUDEF_CMN_ETA_FlagKind)
	//@{
	// CN JP OverSea
	public static final int DPGUDEF_CMN_ETA_FLAG_NONE = 0;						///< ��ʾ�o�������ڂ�??�o������
	public static final int DPGUDEF_CMN_ETA_FLAG_GOAL = 1;						///< Ŀ�ĵ�
	public static final int DPGUDEF_CMN_ETA_FLAG_VIA_1 = 2;						///< ���ĵأ�
	public static final int DPGUDEF_CMN_ETA_FLAG_VIA_2 = 3;						///< ���ĵأ�
	public static final int DPGUDEF_CMN_ETA_FLAG_VIA_3 = 4;						///< ���ĵأ�
	public static final int DPGUDEF_CMN_ETA_FLAG_VIA_4 = 5;						///< ���ĵأ�
	public static final int DPGUDEF_CMN_ETA_FLAG_VIA_5 = 6;						///< ���ĵ� 5	
	public static final int DPGUDEF_CMN_ETA_FLAG_MAX = 7;						///< ��N�e��
	//@}
	
	/*---------------------------------------------------------------------------*/
	/// @Road �N�e(DPGUDEF_CMN_RoadKind)
	//@{
	public static final int DPGUDEF_CMN_ROAD_NONE = -1;						///< �N�e�ʤ������ڂ�??�o������//
	public static final int DPGUDEF_CMN_ROAD_NORMAL = 0;					///< һ���					//
	public static final int DPGUDEF_CMN_ROAD_NATIONAL = 1;					///< National Road
	public static final int DPGUDEF_CMN_ROAD_PREFECTURAL = 2;				///< Prefectural Road
	public static final int DPGUDEF_CMN_ROAD_HWY = 3;						///< ���ϵ�					//
	public static final int DPGUDEF_CMN_ROAD_FERRY = 4;						///< Ferry
	public static final int DPGUDEF_CMN_ROAD_URBAN_HWY = 5;					///< Urban Hwy
	public static final int DPGUDEF_CMN_ROAD_OTHER = 6;						///< other
	public static final int DPGUDEF_CMN_ROAD_MAX = 7;						///< �о��x���`���N�e�t��		//
	//@}
	
	/*---------------------------------------------------------------------------*/
	/// @Road �N�e(DPGUDEF_CMM_ILLUST_COLOR)
	//@{
	public static final int DPGUDEF_CMN_COLOR_DAY = 1;
	public static final int DPGUDEF_CMN_COLOR_NIGHT = 2;
	public static final int DPGUDEF_CMN_COLOR_SUNSET = 3;
	//@}
	
	/*---------------------------------------------------------------------------*/
	// ���饹�Ȓ����N�e
	/*---------------------------------------------------------------------------*/
	public static final int DPGUDEF_CMN_ILLUST_MK_NONE = 0;				///< No Illustration nor Magview
	public static final int DPGUDEF_CMN_ILLUST_MK_CANCELED = 1;
	public static final int DPGUDEF_CMN_ILLUST_MK_2D = 2;               ///< 2D magview (temp no use now)
	public static final int DPGUDEF_CMN_ILLUST_MK_DRV = 3;				///< �ɥ饤�Щ`���ӥ�`�����
	public static final int DPGUDEF_CMN_ILLUST_MK_AR = 4;				///< AR�ӥ�`�����
	public static final int DPGUDEF_CMN_ILLUST_MK_ARROW = 5;			///< ����`������
	public static final int DPGUDEF_CMN_ILLUST_MK_NORMAL_BRANCH = 6;	///< ���彻��㥤�饹��(JP)
	public static final int DPGUDEF_CMN_ILLUST_MK_REALILLUST = 7;		///< �ꥢ�뒈��?�饹��(JP,CN)
	public static final int DPGUDEF_CMN_ILLUST_MK_ENTRANCE = 8;			///< ���и�����ڥ��饹��(JP)
	public static final int DPGUDEF_CMN_ILLUST_MK_JUNCTION = 9;         ///< ���ٵ���᪥��饹��(JP,CN,OverSea)
	public static final int DPGUDEF_CMN_ILLUST_MK_JCT_PECULIAR = 10;		///< ����JCT���饹��(JP)
	public static final int DPGUDEF_CMN_ILLUST_MK_EXIT_BRANCH = 11;		///< ���ϵ������᷽�楤�饹��(JP)
	public static final int DPGUDEF_CMN_ILLUST_MK_ROUNDABOUT = 12;       ///< Roundabout (temp no use now)
	
	
	
	

	
	public static final int DPGUDEF_CMN_ILLUST_MK_MAX = 9;              ///< Number of kinds
	

	/*---------------------------------------------------------------------------*/
	/// @junction type only for OverSea
	//@{
	// OverSea
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_NONE = 0;			///< 
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_1 = 1;				///< UC/CANADA
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_2 = 2;				///< BELGIUM
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_3 = 3;				///< GERMANY
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_4 = 4;				///< FRANCE/SPAIN
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_5 = 5;				///< NETHERLANDS/AUSTRIA/NORWAY
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_6 = 6;				///< ITARY/DENMARK
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_7 = 7;				///< Switerlands
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_8 = 8;				///< Great Britten
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_9 = 9;				///< Portugal
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_10 = 10;				///< Finland/Slovak Republic
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_11 = 11;				///< Czech Republic
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_12 = 12;				///< Poland
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_13 = 13;				///< Greece/Hungary
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_14 = 14;				///< Ireland
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_15 = 15;				///< Sweden
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_16 = 16;				///< Russia
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_17 = 17;				///< Luxembourg
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_18 = 18;				///< Slovenia
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_19 = 19;				///< Others
	public static final int UIC_GU_SIGN_POST_COLOR_TYPE_MAX = 20;
	//@}

	/*---------------------------------------------------------------------------*/
	/// @junction type
	//@{
	// OverSea
	public static final int DPGUDEF_CMN_SIGN_POINT_TYPE_NONE = 0;						
	public static final int DPGUDEF_CMN_SIGN_POINT_TYPE_JCT = 1;						
	public static final int DPGUDEF_CMN_SIGN_POINT_TYPE_EXIT = 2;
	public static final int DPGUDEF_CMN_SIGN_POINT_TYPE_MAX = 3;
	//@}

	/*---------------------------------------------------------------------------*/
	/// @��`��t��
	public final static int	DPGUDEF_CMN_LANE_MAX = 16;	///< ��`�������[16]

	// Impl�� �ǩ`�����x
	public final static int	UIC_DPGU_LANE_NUM_MAX = 11;	///< ��`�������[11]
	
	/*---------------------------------------------------------------------------*/
	/// @name ������ƾt��
	public static final int	DPGUDEF_CMN_SIGNPOST_MAX = 3;	///< ������������[3]
	/*---------------------------------------------------------------------------*/	

	/*---------------------------------------------------------------------------*/
	/// @name ����Street�t�� (OverSea)
	public static final int	DPGUDEF_CMN_SIGNPOST_STREETNUM_MAX = 4;
	
	
	private static UIGuideControlJNI instance;
	
	/*---------------------------------------------------------------------------*/
	//@ReqVoiceGuideType
	//
	//
	public static final int NAVI_SRV_GUD_REQTYPE_SOUNDONLY = 0;		///< ��������Τ�
	public static final int NAVI_SRV_GUD_REQTYPE_WITHMAP = 1;		///< �؇��ʾ
	public static final int NAVI_SRV_GUD_REQTYPE_WITHNEXTMAP = 2;	///< �؇��ʾ�ͤ�
	
	/*---------------------------------------------------------------------------*/
	//Get Direction List Information
	//Message :UIC_MN_TRG_DIRECTIONLIST_CHANGE
	//param  the param 0 is the type NAVI_SRV_GUD_MSG_DIRECTION_LIST_ELIMINATE or NAVI_SRV_GUD_MSG_DIRECTION_LIST_REFRESH
	//param  the param 1 is the point ID that current PointID 
	//@{
	// Get Direction List Information
	public static final int NAVI_SRV_GUD_MSG_DIRECTION_LIST_ELIMINATE = 0;
	public static final int NAVI_SRV_GUD_MSG_DIRECTION_LIST_REFRESH = 1;
	// Value for the UI judge the Timing to change to the Route PreView page
	public static final int NAVI_SRV_GUD_MSG_DIRECTION_LIST_NOT_GOTOPREVIEW = 0;
	public static final int NAVI_SRV_GUD_MSG_DIRECTION_LIST_GOTOPREVIEW = 1;
	//@}
	/*---------------------------------------------------------------------------*/

	/*---------------------------------------------------------------------------*/
	/// @DepartureStatus
	//@{
	// 
	public static final int DPGUDEF_CMN_SIGN_DEPARTURESTATUS_INVALID = 0;						
	public static final int DPGUDEF_CMN_SIGN_DEPARTURESTATUS_STARTED = 1;						
	public static final int DPGUDEF_CMN_SIGN_DEPARTURESTATUS_FINISHED = 2;
	//@}

	/*---------------------------------------------------------------------------*/
	/// @ETADistanceKind
	//@{
	// 
	public static final int DPGUDEF_CMN_SIGN_DISTKIND_INVALID = 0;						
	public static final int DPGUDEF_CMN_SIGN_DISTKIND_STRAIGHT = 1;						
	public static final int DPGUDEF_CMN_SIGN_DISTKIND_OK = 2;
	//@}

	/*---------------------------------------------------------------------------*/
	/// @ETATimeKind
	//@{
	// 
	public static final int DPGUDEF_CMN_SIGN_TIMEKIND_INVALID = 0;						
	public static final int DPGUDEF_CMN_SIGN_TIMEKIND_CALCULATING = 1;						
	public static final int DPGUDEF_CMN_SIGN_TIMEKIND_OK = 2;
	//@}

	/*---------------------------------------------------------------------------*/
	/// @WeatherKind
	//@{
	// 
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_NONE = 0;						
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_SUNNY = 1;						
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_CLUD = 2;
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_LRAIN = 3;
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_LSNOW = 4;						
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_HRAIN = 5;						
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_HSNOW = 6;
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_STAR = 7;
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_UNKNOWN = 8;						
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_INVALID = 9;						
	public static final int DPGUDEF_CMN_SIGN_ETA_WEATHER_NUM = 10;
	//@}
	
	
	public static final int APP_START_BY_USER =	0;		
	public static final int APP_START_AUTO = 1;
	
	private UIGuideControlJNI(){
	}
	
	public static UIGuideControlJNI getInstance(){
		if(instance==null){
			instance=new UIGuideControlJNI();
		}
		return instance;
	}
	
	
	/*
	 * get display flag
	*/
	//
	public native int GetDispStatus();

	//Get Route Lenght 
	public native int GetRouteLen();
	
	public native String getDirctionSignSpotName();
	
	/*
	 * Get GuidePoint Information
	 */
	public native String GetGuidePointInfo_CrossName();
	
	public native String GetGuidePointInfo_NextStreetName();
	// ����ʸӡ�N�e: DPGUDEF_CMN_GuideArrowKind
	public native int GetGuidePointInfo_ArrowKind();
	// ������N�e: DPGUDEF_CMN_FlagKind
	public native int GetGuidePointInfo_FlagKind();
	// Exit Number
	public native int GetGuidePointInfo_RoundaboutExitNo();
	// StreetNumber
	public native String GetGuidePointInfo_StreetNumber();
	// ShieldID
	public native int GetGuidePointInfo_ShieldID();
	// DistanceKind
//	public native int GetGuidePointInfo_DistanceKind();
	// point kind
	public native int GetGuidePointInfo_PointKind();
	// Is Exist DistrictSignPost
	public native boolean GetGuidePointInfo_ExistDistrictSignPost();
	/*
	 * Get Direction List Information
	 */
	// All points number in the Route
	public native int GetDirectionListInfo_PointNum();
	// The point Distance 
	public native int GetDirectionListInfo_PointDistance(int index);
	// The point Direction 
	public native int GetDirectionListInfo_PointDirection(int index);
	// The point PointID 
	public native int GetDirectionListInfo_PointID(int index);
	// The point StreetName
	public native String GetDirectionListInfo_StreetName(int index);
	// The point LonLat Array : 0----Lon  1----Lat
	public native long[] GetDirectionListInfo_LonLat(int index);
	// The point kind
	public native int GetDirectionListInfo_PointKind(int index);
	// The Point District Name case: empty ->not Exist , case: not empty->Exist 
	public native String GetDirectionListInfo_DistrictName(int index);
	
	/*
	 *  Get Distance Information
	 */
	// long type distance
	public native int GetDistanceInfo_LDistance();
	// string type distance KM
	public native String GetDistanceInfo_StrDistance();
	// string type distance Mile
	public native String GetDistanceInfo_StrDistance_Mile();
	// string type distance Yard
	public native String GetDistanceInfo_StrDistance_Yard();
	// string type arrival time
	public native String GetDistanceInfo_StrArrialTime();
	
	/*
	 *  Get ETA Information
	 */
	// �U�ɵأ�NP_TRUE���U�ɵأ�
	public native boolean GetETAInfo_IsViaPoint();
	// Ŀ�ĵؤξ��x
	public native String GetETAInfo_StrDestDist();
	// ETA int DestDist
	public native int GetETAInfo_DestDist();
	//AL_String m_strDestDist_Maile;
	public native String GetETAInfo_StrDestDist_Maile();
	//AL_String m_strDestDist_Yard;
	public native String GetETAInfo_StrDestDist_Yard();
	// ���Ť���r��, e.g., 14:23
	public native String GetETAInfo_StrDestETA();
	// ���Ť���r�g, e.g., 03h34m
	public native String GetETAInfo_StrDestETR();
	// Dest Flag: DPGUDEF_CMN_ETA_FlagKind
	public native int GetETAInfo_DestFlag();
	// �U�ɵؤξ��x
	public native String GetETAInfo_StrSpotDist();
	// m_strSpotDist_Maile
	public native String GetETAInfo_StrSpotDist_Mile();
	// m_strSpotDist_Yard
	public native String GetETAInfo_StrSpotDist_Yard();
	// �U�ɵص��Ť���r��, e.g., 14:23
	public native String GetETAInfo_StrSpotETA();
	// �U�ɵص��Ť���r�g, e.g., 03h34m
	public native String GetETAInfo_StrSpotETR();
	// Waypiont Flag : DPGUDEF_CMN_ETA_FlagKind
	public native int GetETAInfo_SpotFlag();
	//Dest Timing Progress 0-100
	public native int GetETAInfo_ProgressForDest();
	//WayPoint Timing Progress 0-100
	public native int GetETAInfo_ProgressForWayPoint();
	//Dest Distance Progress 0-100
	public native int GetETAInfo_DisProgressForDest();
	//WayPoint Distance Progress 0-100
	public native int GetETAInfo_DisProgressForWayPoint();
	//DepartureStatus
	public native int GetETADepartureStatus();
	//ETATimeKind
	public native int GetETATimeKind();
	//ETADistanceKind
//	public native int GetETADistanceKind();
	//ETAWeatherKind
	public native int GetETAWeatherKind();
	
	/*
	 *  Get Current Road Name Information
	 */
	// current street name
	public native String GetCurrentRoadData_StrStreetName();
	// current street no
	public native String GetCurrentRoadData_StrStreetNo();
	// current street mark id
	public native int GetCurrentRoadData_StreetNo_MarkID();
	
	/*
	 *  Get Lane Information
	 */
	// ��`����
	public native int GetLaneInfo_LanNum();
	// ��`��ʸӡ�N�e : DPGUDEF_CMN_LaneArrowKind
	public native int[] GetLaneInfo_LaneArrowKind();
	// ��`��N�e : DPGUDEF_CMN_LaneKind
	public native int[] GetLaneInfo_LaneKind();
	// ���ӥ�`��N�e : DPGUDEF_CMN_LaneIncreaseKind
	public native int[] GetLaneInfo_LaneInCrease();
	
	/*
	 *  Get Illust Information
	 */
	// GuidePoint id
	public native int GetIllustInfo_GuidePointID();
	// DPGUDEF_CMN_RoadKind
	public native int GetIllustInfo_IllustRoadKind();
	// DPGUDEF_CMN_Illust_MagKind
	public native int GetIllustInfo_IllustMagKind();
	// DPGUDEF_CMM_ILLUST_COLOR
	public native int GetIllustInfo_IllustColor();
	
	// Get Day Vertical Image Type
	public native int GetIllustInfo_DayVerticalImageType();
	// Get Day Vertical Image Data Size
	public native int GetIllustInfo_DayVerticalDataSize();
	// Get Day Vertical Image Byte Buffer
	public native byte[] GetIllustInfo_DayVerticalImagByteBuffer();
	
	// Get Night Vertical Image Type
	public native int GetIllustInfo_NightVerticalImageType();
	// Get Night Vertical Image Data Size
	public native int GetIllustInfo_NightVerticalDataSize();
	// Get Night Vertical Image Byte Buffer
	public native byte[] GetIllustInfo_NightVerticalImagByteBuffer();
	
	// Get Day Horizontal Image Type
	public native int GetIllustInfo_DayHorizontalImageType();
	// Get Day Horizontal Image Data Size
	public native int GetIllustInfo_DayHorizontalDataSize();
	// Get Day Horizontal Image Byte Buffer
	public native byte[] GetIllustInfo_DayHorizontalImagByteBuffer();
	
	// Get Night Horizontal Image Type
	public native int GetIllustInfo_NightHorizontalImageType();
	// Get Night Horizontal Image Data Size
	public native int GetIllustInfo_NightHorizontalDataSize();
	// Get Night Horizontal Image Byte Buffer
	public native byte[] GetIllustInfo_NightHorizontalImagByteBuffer();
	
	/*
	 *  Get WEBGS Special Illust Information
	 */
	//Get Arrow Image Type
	public native int GetIllustInfo_ArrowImageType();
	//Get Arrow Image Size
	public native int GetIllustInfo_ArrowImageSize();
	//Get Arrow Image Buffer
	public native byte[] GetIllustInfo_ArrowImageBuffer();
	
	//Get BackGround Image Type
	public native int GetIllustInfo_BKImageType();
	//Get BackGround Image Size
	public native int GetIllustInfo_BKImageSize();
	//Get BackGround Image Buffer
	public native byte[] GetIllustInfo_BKImageBuffer();
	//Get the Car Position Index 0 : X offset 1: Y offset
	public native int[] GetIllustInfo_CarPosition();
	//Get the Car Dir
	public native float GetIllustInfo_CarDir();
	//Get the Car Zoom Size 
	public native float GetIllustInfo_CarMarkSize();
	
	/*
	 *  Get ETC Information
	 */
	// Get ETC Image Byte Size
	public native int GetETCIllustData_ImageByteSize();
	// Get ETC Image Byte Buffer
	public native byte[] GetETCIllustData_ImagByteBuffer();
	
	/*
	 * Get SignPost Information
	 */
	// sign post display or not
	public native boolean[] GetSignPostInfo_bValid();
	// sign post direction on route or not
	public native boolean[] GetSignPostInfo_bOnRoute();
	// sign post data
	public native String[] GetSignPostInfo_strIllustData();
	// m_strSignPostName
	public native String GetSignPostInfo_StrSignPostName();
	// m_uiDispStreetNum
	public native int GetSignPostInfo_DispStreetNum();
	// m_str_StreetNo
	public native String[] GetSignPostInfo_StrStreetNo();
	// m_uiStreetNo_MarkID
	public native int[] GetSignPostInfo_StreetNo_MarkID();
	// m_strRoadDirection
	public native String[] GetSignPostInfo_StrRoadDirection();
	// m_eSignPointKind
	public native int GetSignPostInfo_SignPostKind();
	// m_eSignContryCode
	public native int GetSignPostInfo_SignContryCode();
	// strExitNo
	public native String GetSignPostInfo_StrExitNo();
	// m_eExitDirectionKind
	public native int GetSignPostInfo_ExitDirectionKind();
	
	//ReqVoiceGuide()
	public native void ReqVoiceGuide();
	
	//ReqVoiceGuide With Params defined to the ReqVoiceGuideType
	/**
	*	NAVI_SRV_GUD_REQTYPE_SOUNDONLY  ///< ��������Τ�
	*	NAVI_SRV_GUD_REQTYPE_WITHMAP	///< �؇��ʾ
	*	NAVI_SRV_GUD_REQTYPE_WITHNEXTMAP///< �؇��ʾ�ͤ�
	*/
	public native void ReqVoiceGuide(int iReqGuideType);
	
	//RequestGuideFinishd
	public native void RequestGuideFinished();
	//DrawRequestGuide
	public native void DrawRequestGuide();
	
	public native boolean isIllustExist();
	
	/*
	 * Demo Speed ChangePart
	 */
	//SpeedUp
	public native void DemoSpeedAdjust_SpeedUp();
	//SpeedDown
	public native void DemoSpeedAdjust_SpeedDown();
	//SpeedAuto
	public native void DemoSpeedAdjust_SpeedAuto();
	//Get Demo Speed 0.1km unit
	public native int DemoSpeedAdjust_GetDemoSpeed();
	
	//SetAppStartMode
	public native void SetAppStartMode(int mode);
	
	public native void requestSearchParkingPotNearby();

}
