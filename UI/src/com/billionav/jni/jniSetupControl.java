/**
 * 
 *
 */
package com.billionav.jni;


/**
 * @author yuxiaobo
 *
 */
public class jniSetupControl {
	
	/// common off
	public static final int STUPDM_COMMON_OFF = 0;
	/// common on
	public static final int STUPDM_COMMON_ON = 1;
	
	///Map Color Change
	///Map Color Change By Time
	public static final int STUPDM_MAP_COLOR_CHANGE_BY_TIME = 0;	
	///Daytime Map Color
	public static final int STUPDM_MAP_COLOR_CHANGE_DAY = 1;
	///Nighttime Map Color
	public static final int STUPDM_MAP_COLOR_CHANGE_NIGHT = 2;

	
	///Route Condition
	///Recommend
	public static final int STUPDM_CONDITION_STANDARD = 0;
	///Prioritize Distance
	public static final int STUPDM_CONDITION_DISTANCE = 1;
	///Prioritize Main Street
	public static final int STUPDM_CONDITION_MAINROAD = 2;
	
	public static final int STUPDM_CONDITION_ECO = 3;

	///ETA
	///Destination
	public static final int STUPDM_ARRIVAL_DESTINATION = 0;
	///next guidance point
	public static final int STUPDM_ARRIVAL_VIA = 1;		
	///OFF
	public static final int STUPDM_ARRIVAL_OFF = 2;	
			
	///Menu Language
	///English
	public static final int STUPDM_MENU_LANGUAGE_ENGLISH = 0;	
	///simplified Chinese
	public static final int STUPDM_MENU_LANGUAGE_CHINESE = 1;		
	///Japanese
	public static final int STUPDM_MENU_LANGUAGE_JAPANESE = 2;
	///Traditional Chinese
	public static final int STUPDM_MENU_LANGUAGE_TRADITIONAL_CHINESE = 3;

	
	/// Direction
	/// Headup
	public static final int STUPDM_DIRECTION_HEADUP = 0; 
	/// Northup
	public static final int STUPDM_DIRECTION_NORTHUP = 1;     

	///Clock Mode
	///24 mode
	public static final int STUPDM_TIME_DISPLAY_PATTERN_24H = 0;	
	///12 mode
	public static final int STUPDM_TIME_DISPLAY_PATTERN_12H = 1;	
	
	//last GPS setting
	//inside setting
	public static final int STUPDM_GPS_INSIDE = 0;
	//outside setting
	public static final int STUPDM_GPS_OUTSIDE = 1;
	//not setting the GPS
	public static final int STUPDM_GPS_NOTSET = 2;
	
	//ferry
	public static final int STUPDM_FERRY_STANDARD = 0;
	public static final int STUPDM_FERRY_PRORITORY = 1;
	public static final int STUPDM_FERRY_ADVOID = 2;
		
	/// Point Sort ID
	public static final int STUPDM_PNT_SORT_NAME = 0;         // Name (unicode order)
	public static final int STUPDM_PNT_SORT_TIME = 1; // Regist time
	public static final int STUPDM_PNT_SORT_MARK = 2;  // 2D mark
	public static final int STUPDM_PNT_SORT_COUNT = 3;  // Use count
	public static final int STUPDM_PNT_SORT_ACCESS = 4;  // Access time
	public static final int STUPDM_PNT_SORT_FAVORITE = 5; // Favorite Level 
	public static final int STUPDM_PNT_SORT_CUSTOM = 6;         // Custom
	
	//Gasoline Setting
	public static final int STUPDM_GASOLINE_REGULAR = 0;
	public static final int STUPDM_GASOLINE_HIGH_OCTANE = 1;
		
	public static final int STUPDM_ORDINARY_LOW_LIMIT = 20;
	// Default Speed Surface-Road value
	public static final int STUPDM_DEFAULT_ORDINARY = 30;
	public static final int STUPDM_ORDINARY_HIGH_LIMIT = 40;

	public static final int STUPDM_MAJOR_LOW_LIMIT = 40;
	// Default Speed - Expressway/Highway value
	public static final int STUPDM_DEFAULT_MAJOR = 60;
	public static final int STUPDM_MAJOR_LOW = 80;
	public static final int STUPDM_MAJOR_HIGH = 100;
	public static final int STUPDM_MAJOR_HIGH_LIMIT = 120;
	
	// STUPDM_MAGVIEW_GO_ILLUST_OR_CAMERA
	public static final int STUPDM_GO_ILLUST = 0;
	public static final int STUPDM_GO_CAMERA = 1;
	
	public static final int STUPDM_VOICE_NAVI_COMPLETION = 0;
	public static final int STUPDM_VOICE_NAVI_CONCISE = 1;
	
	public static final int STUPDM_RIDE_DISPLAY_SETTING_DISTANCE = 0;
	public static final int STUPDM_RIDE_DISPLAY_SETTING_TIME = 1;
	
	//STUPDM_ADJUST_G_SENSOR
	public static final int STUPDM_ADJUST_G_SENSOR_PITCH = 0;
	public static final int STUPDM_ADJUST_G_SENSOR_ROLL = 1;
	public static final int STUPDM_ADJUST_G_SENSOR_YAW = 2;
	
	public static final int STUPDM_SYNCHRONOUS_FREQUENCY_FIFTEEN = 0;
	public static final int STUPDM_SYNCHRONOUS_FREQUENCY_THRITY = 1;
	public static final int STUPDM_SYNCHRONOUS_FREQUENCY_SIXTY = 2;
	////////////////////////////////////////////////////////////////////
	/////added for AR
	////////////////////////////////////////////////////////////////////
	
	public static final int STUPDM_GPS_MODULE_EXTERNAL = 0;
	public static final int STUPDM_GPS_MODULE_INTERNAL = 1;
	public static final int STUPDM_GPS_MODULE_NOUSE = 2;
	
	public static final int STUPDM_MAP_DETAIL_LV_MOST = 0;
	public static final int STUPDM_MAP_DETAIL_LV_MORE = 1;
	public static final int STUPDM_MAP_DETAIL_LV_NORMAL = 2;
	public static final int STUPDM_MAP_DETAIL_LV_LESS = 3;
	public static final int STUPDM_MAP_DETAIL_LV_LEAST = 4;
	
	public static final int STUPDM_MAP_LOCATION_ONROADS = 0;
	public static final int STUPDM_MAP_LOCATION_GPSLOC = 1;
	
	public static final int STUPDM_MAP_COLOR_WORLD_RED = 0;
	public static final int STUPDM_MAP_COLOR_WORLD_BLUE = 1;
	public static final int STUPDM_MAP_COLOR_JAPAN_POPULAR_A = 2;
	public static final int STUPDM_MAP_COLOR_JAPAN_POPULAR_B = 3;
	public static final int STUPDM_MAP_COLOR_JAPAN_HI_END = 4;
	
	public static final float STUPDM_MAP_FONTSIZE_DEFAULT = (float)1.0;
	public static final float STUPDM_MAP_FONTSIZE_X1 = (float)1.5;
	public static final float STUPDM_MAP_FONTSIZE_X2 = (float)2.0;
	public static final float STUPDM_MAP_FONTSIZE_X3 = (float)2.5;
	public static final float STUPDM_MAP_FONTSIZE_X4 = (float)3.0;
	public static final float STUPDM_MAP_FONTSIZE_X5 = (float)3.5;
	public static final float STUPDM_MAP_FONTSIZE_X6 = (float)4.0;
	public static final float STUPDM_MAP_FONTSIZE_X7 = (float)4.5;
	
	public static final int STUPDM_MAP_ROADWIDTH_ADD_0DOT = 0;
	public static final int STUPDM_MAP_ROADWIDTH_ADD_1DOT = 1;
	public static final int STUPDM_MAP_ROADWIDTH_ADD_2DOT = 2;
	public static final int STUPDM_MAP_ROADWIDTH_ADD_3DOT = 3;
	public static final int STUPDM_MAP_ROADWIDTH_ADD_4DOT = 4;
	public static final int STUPDM_MAP_ROADWIDTH_ADD_5DOT = 5;
	public static final int STUPDM_MAP_ROADWIDTH_ADD_6DOT = 6;
	
	public static final int STUPDM_SKIN_ORIGIN = 0;
	public static final int STUPDM_SKIN_OVERSEA = 1;
	public static final int STUPDM_SKIN_RAKU_WHIDE = 2;
	public static final int STUPDM_SKIN_RAKU_BLACK = 3;
	public static final int STUPDM_SKIN_CYBER = 4;
	
	public static final int STUPDM_MENU_SIZE_LARGE = 0;
	public static final int STUPDM_MENU_SIZE_NORMAL = 1;
	
	public static final int STUPDM_KM = 0;            ///< Km
	public static final int STUPDM_MILE = 1;              ///< Mile
	public static final int STUPDM_MILE_YARDS = 2;        ///< Mile & Yards
	
	public static final int STUPDM_CAR_TYPE_MEDIUM = 0;	
	public static final int STUPDM_CAR_TYPE_NORMAL = 1;
	public static final int STUPDM_CAR_TYPE_LIGHT = 2;
	
	//Display location
	public static final int STUPDM_MAP_LOCATIONNAME_OFF  = 0;	
	public static final int STUPDM_MAP_LOCATIONNAME_AREANAME  = 1;
	public static final int STUPDM_MAP_LOCATIONNAME_CURRENTSTREET  = 2;
	
	//authenticated or unauthenticated
	public static final int STUPDM_UNKNOW = 0;
	public static final int STUPDM_NEGATIVE = 1;
	public static final int STUPDM_POSITIVE = 2;
	
	// Car Stop Limit

	// Car Stop Limit 
	public static final int CAR_DATA_LIMIT_NONE = 0x00;	/// �ʤ�
	public static final int CAR_DATA_LIMIT_3NO = 0x80;	/// ���ʥ�Щ`
	public static final int CAR_DATA_LIMIT_RV = 0x40;	/// �ң�
	public static final int CAR_DATA_LIMIT_1BOX = 0x20;	/// ���£ϣإ��`

	// Car Stop Limit

	public static final int CAR_LIMIT_STR_NONE = 0;	/// �O���ʤ�
	public static final int CAR_LIMIT_STR_3NO = 1;	/// ���ʥ�Щ` 
	public static final int CAR_LIMIT_STR_RV = 2;	/// �ң�
	public static final int CAR_LIMIT_STR_1BOX = 3;	/// ���£ϣ�

	//////////////////////////////////////////////////////////////////////////////
	// Car Size
	// Size ID
	public static final int CAR_SIZE_LENGTH = 0;	/// Length
	public static final int CAR_SIZE_WIDTH = 1;		/// Width
	public static final int CAR_SIZE_HEIGHT = 2;	/// Height
	public static final int CAR_SIZE_WEIGHT = 3;    /// Weight
	public static final int CAR_SIZE_EXHAUST = 4;   /// Exhaust
	
	//////////////////////////////////////////////////////////////////////////////
	//hybrid data server change to CDN server
	public static final int STUPDM_IPC_CDN_JAPAN = 0;		//change to Japan server
	public static final int STUPDM_PSET_SERVER = 1;			//change to pest server

	/////////////////////////////////////////////////////////////////////////////////
	public static final int STUPDM_GOURMET_SELECT_GOURNAVI = 0;
	public static final int STUPDM_GOURMET_SELECT_HOTPAPER = 1;
	//////////////////SETTING ITEMS										/////////////
	/////////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////////////////TEMP ROUTE CONDITION///////////////////////
	public static final int STUPDM_INVALID = 10;
	/////////////////////////////////////////////////////////////////////////////////
	
	/////////////////////////////////////IR SETTINGS////////////////////////////////
	public static final int	SETUPDM_DEBUG_LANE_DETECTION_OFF = 0;
	public static final int	SETUPDM_DEBUG_LANE_DETECTION_S = 1;
	public static final int	SETUPDM_DEBUG_LANE_DETECTION_V = 2;

	public static final int	SETUPDM_DEBUG_VEHICLE_DETECTION_OFF = 0;
	public static final int	SETUPDM_DEBUG_VEHICLE_DETECTION_S = 1;
	public static final int	SETUPDM_DEBUG_VEHICLE_DETECTION_V = 2;

	public static final int	SETUPDM_DEBUG_LANE_DEPARTURE_INDORMATION_OFF = 0;
	public static final int	SETUPDM_DEBUG_LANE_DEPARTURE_INDORMATION_S = 1;
	public static final int	SETUPDM_DEBUG_LANE_DEPARTURE_INDORMATION_V = 2;

	public static final int	SETUPDM_DEBUG_STOP_GO_ASSIST_OFF = 0;
	public static final int	SETUPDM_DEBUG_STOP_GO_ASSIST_S = 1;
	public static final int	SETUPDM_DEBUG_STOP_GO_ASSIST_V = 2;
	public static final int	SETUPDM_DEBUG_STOP_GO_ASSIST_N = 3;

	public static final int	SETUPDM_DEBUG_TRAFFICLIGHT_DETECTION_OFF = 0;
	public static final int	SETUPDM_DEBUG_TRAFFICLIGHT_DETECTION_S = 1;
	public static final int	SETUPDM_DEBUG_TRAFFICLIGHT_DETECTION_V = 2; 

	public static final int	SETUPDM_DEBUG_SPEED_AVOIDAN_OFF = 0;
	public static final int	SETUPDM_DEBUG_SPEED_AVOIDAN_S = 1;
	public static final int	SETUPDM_DEBUG_SPEED_AVOIDAN_V =2;

	public static final int	SETUPDM_DEBUG_DISTANCE_INDOMATION_OFF = 0;
	public static final int	SETUPDM_DEBUG_DISTANCE_INDOMATION_S = 1;
	public static final int	SETUPDM_DEBUG_DISTANCE_INDOMATION_V = 2;
	
	public static final int SETUPDM_DEBUG_CAMERAH_INPUT_CAR = 0;
	public static final int SETUPDM_DEBUG_CAMERAH_INPUT_GROUND = 1;
	
	public static final int SETUPDM_DEBUG_CAR_INFO_OFF = 0;
	public static final int SETUPDM_DEBUG_CAR_INFO_S = 1;
	public static final int SETUPDM_DEBUG_CAR_INFO_V = 2;
	
	
	//map scale setup values
	public static final  int MAP_HEIGHT_2000KM =	25296896;	//Scale0
	public static final  int MAP_HEIGHT_1600KM =	20237512;
	public static final  int MAP_HEIGHT_1200KM =	15178136;
	public static final  int MAP_HEIGHT_1000KM =	12648448;	//Scale1
	public static final  int MAP_HEIGHT_800KM =	10118756;
	public static final  int MAP_HEIGHT_600KM =	7589068;
	public static final  int MAP_HEIGHT_500KM =	6324224;	//Scale2
	public static final  int MAP_HEIGHT_350KM =	5404000;
	public static final  int MAP_HEIGHT_240KM =	3705600;
	public static final  int MAP_HEIGHT_200KM =	3162112;	//Scale3
	public static final  int MAP_HEIGHT_120KM =	1852800;
	public static final  int MAP_HEIGHT_100KM =	1581056;	//Scale4
	public static final  int MAP_HEIGHT_80KM =		1235200;
	public static final  int MAP_HEIGHT_65KM =		988200;
	public static final  int MAP_HEIGHT_50KM =		790528;		//Scale5
	public static final  int MAP_HEIGHT_40KM =		617600;
	public static final  int MAP_HEIGHT_32KM =		494080;
	public static final  int MAP_HEIGHT_24KM =		395264;     //Scale6
	public static final  int MAP_HEIGHT_20KM =		308800;
	public static final  int MAP_HEIGHT_16KM =		247040;
	public static final  int MAP_HEIGHT_12KM =		197632;     //Scale7
	public static final  int MAP_HEIGHT_10KM =		154400;
	public static final  int MAP_HEIGHT_8KM =		123520;
	public static final  int MAP_HEIGHT_6KM =		98816;      //Scale8
	public static final  int MAP_HEIGHT_5KM =		77200;
	public static final  int MAP_HEIGHT_4KM =		63200;
	public static final  int MAP_HEIGHT_3KM =		49408;      //Scale9
	public static final  int MAP_HEIGHT_2_5KM =	38600;
	public static final  int MAP_HEIGHT_2KM =		31600;
	public static final  int MAP_HEIGHT_1_6KM =	24704;      //Scale10
	public static final  int MAP_HEIGHT_1_2KM =	18528;
	public static final  int MAP_HEIGHT_1KM =		15800;
	public static final  int MAP_HEIGHT_800M =		12352;      //Scale11
	public static final  int MAP_HEIGHT_600M =		9264;
	public static final  int MAP_HEIGHT_500M =		7900;
	public static final  int MAP_HEIGHT_400M =		6176;       //Scale12
	public static final  int MAP_HEIGHT_350M =		5404;
	public static final  int MAP_HEIGHT_250M =		3950;
	public static final  int MAP_HEIGHT_200M =		3088;		//Scale13
	public static final  int MAP_HEIGHT_160M =		2470;
	public static final  int MAP_HEIGHT_120M =		1960;
	public static final  int MAP_HEIGHT_100M =		1544;		//Scale14
	public static final  int MAP_HEIGHT_80M =		1235;
	public static final  int MAP_HEIGHT_60M =		980;
	public static final  int MAP_HEIGHT_50M =		772;		//Scale15
	public static final  int MAP_HEIGHT_35M =		540;
	public static final  int MAP_HEIGHT_30M =		386;        //Scale16
	public static final  int MAP_HEIGHT_25M =		350;
	public static final  int MAP_HEIGHT_15M =		193;        //Scale17
	public static final  int MAP_HEIGHT_10M =		154;
	public static final  int MAP_HEIGHT_8M =		96;		    //Scale18
	public static final  int MAP_HEIGHT_4M =		48;			//Scale19
	public static final  int MAP_HEIGHT_INVALID =	0;

	
	
	/////////////////////////////////////////////////////////////////////////////////
	
	
	
	///Setup Setting Start
	public static final int STUPDM_SETUP_HEAD_ID = 0;
	///Clock Mode
	public static final int STUPDM_TIME_DISPLAY_PATTERN_TYPE = STUPDM_SETUP_HEAD_ID ;
	///Map Color
	public static final int STUPDM_MAPCOLOR_CHANGE= STUPDM_TIME_DISPLAY_PATTERN_TYPE + 1;		
	///MENU Language
	public static final int STUPDM_MENU_LANGUAGE= STUPDM_MAPCOLOR_CHANGE + 1;								

	///Close Up View
	public static final int STUPDM_EXPANDING_CROSSPOINT_ILLUST= STUPDM_MENU_LANGUAGE + 1;	
	///Speed - Surface  road	  default 30km/h
	public static final int STUPDM_AVERAGE_GEN= STUPDM_EXPANDING_CROSSPOINT_ILLUST + 1;	
	///Speed - Expressway/Highway default 60km/h
	public static final int STUPDM_AVERAGE_MAJ= STUPDM_AVERAGE_GEN + 1;		
	///Routing Criteria
	public static final int STUPDM_ROUTE_CONDITION= STUPDM_AVERAGE_MAJ + 1;	
	///Expressways
	public static final int STUPDM_HIGHWAY= STUPDM_ROUTE_CONDITION + 1;	
	///Ferry Conditions
	public static final int STUPDM_FERRY= STUPDM_HIGHWAY + 1;		
	///VICS Auto-Reroute
//	public static final int STUPDM_VICS_AUTOREROUTE= STUPDM_FERRY + 1;
	///speed camera
	public static final int STUPDM_SPEED_CAMERA= STUPDM_FERRY + 1;
	
	/// Point List Sort Type
	public static final int STUPDM_PNT_SORT = STUPDM_SPEED_CAMERA + 1;
	/// Point History Sort Type
	public static final int STUPDM_PNT_HIST_SORT = STUPDM_PNT_SORT + 1;
	
	///hybrid data server change to CDN server
	public static final int STUPDM_CDN_CONNECTION_SETTING = STUPDM_PNT_HIST_SORT + 1;
	/// new for ARNavi

	/// GPS Setting
	/// GPS activity
	public static final int STUPDM_GPS_ACTIVATION = STUPDM_CDN_CONNECTION_SETTING + 1;
	/// GPS Module Type
//	public static final int STUPDM_GPS_MODULE = STUPDM_GPS_ACTIVATION + 1;
	/// Open Without GPS Caution
	public static final int STUPDM_OPENING_GPS_ABORT_STATUS = STUPDM_GPS_ACTIVATION + 1;
	///Use External GPS
	public static final int STUPDM_OPENING_GPS_STATUS = STUPDM_OPENING_GPS_ABORT_STATUS + 1;
	///last GPS setting
	public static final int STUPDM_LAST_GPS_SETTING = STUPDM_OPENING_GPS_STATUS + 1;
	/// Navi Setting
	/// Car Type
	public static final int STUPDM_CAR_TYPE = STUPDM_LAST_GPS_SETTING + 1;
	/// Traffic
	public static final int STUPDM_TRAFFIC = STUPDM_CAR_TYPE + 1;
	/// Map Auto Zoom
	public static final int STUPDM_MAP_AUTO_ZOOM = STUPDM_TRAFFIC + 1;
	/// Map Day Color Type
	public static final int STUPDM_MAP_DAY_COLOR = STUPDM_MAP_AUTO_ZOOM + 1;
	/// Map Night Color Type
	public static final int STUPDM_MAP_NIGHT_COLOR = STUPDM_MAP_DAY_COLOR + 1;
	/// Map Draw Gps Station flag
	public static final int STUPDM_MAP_DRAW_GPS_STATION_FLAG = STUPDM_MAP_NIGHT_COLOR + 1;
	/// AR Setting
	/// AR Distance For Display
	public static final int STUPDM_AR_DISTANCE_FOR_DISPLAY = STUPDM_MAP_DRAW_GPS_STATION_FLAG + 1;
	/// AR Category
	public static final int STUPDM_AR_CATEGORY = STUPDM_AR_DISTANCE_FOR_DISPLAY + 1;
	
	/// System Setting
	
	/// Dist Setting
	/// railway intersection
	public static final int STUPDM_JUNCTION_INFO = STUPDM_AR_CATEGORY + 1;
	/// highway intersection
	public static final int STUPDM_CROSSING_INFO = STUPDM_JUNCTION_INFO + 1;
	/// Menu Skin
	public static final int STUPDM_SYS_MENU_SKIN = STUPDM_CROSSING_INFO + 1;

	/// drive mode
	public static final int STUPDM_DRIVE_MODE = STUPDM_SYS_MENU_SKIN + 1;

	//traffic line display
	public static final int STUPDM_TRAFFIC_LINE_DISPLAY = STUPDM_DRIVE_MODE + 1;

	public static final int STUPDM_TEMP_ROUTE_CONDITION = STUPDM_TRAFFIC_LINE_DISPLAY + 1;
	/// Temp Highway
	public static final int STUPDM_TEMP_HIGHWAY = STUPDM_TEMP_ROUTE_CONDITION + 1;
	/// Temp Ferry Conditions
	public static final int STUPDM_TEMP_FERRY = STUPDM_TEMP_HIGHWAY + 1;

	
	public static final int STUPDM_SMARTLOOP_MAPURL = STUPDM_TEMP_FERRY + 1;

	/// Km/Mile Setting
	public static final int STUPDM_KM_MILE = STUPDM_SMARTLOOP_MAPURL + 1;
	//magview go illust or camera screen
	public static final int STUPDM_MAGVIEW_GO_ILLUST_OR_CAMERA = STUPDM_KM_MILE + 1;
	//avoid toll road
	public static final int STUPDM_AVOID_TOLL_ROAD = STUPDM_MAGVIEW_GO_ILLUST_OR_CAMERA + 1;
	// demo mode
	public static final int STUPDM_DRIR_DEMO_MODE = STUPDM_AVOID_TOLL_ROAD + 1;
	// speed limit
	public static final int STUPDM_DRIR_SPEED_LIMIT = STUPDM_DRIR_DEMO_MODE + 1;
	// navi voice (COMPLETION, CONCISE)
	public static final int STUPDM_VOICE_NAVI = STUPDM_DRIR_SPEED_LIMIT + 1;

	public static final int STUPDM_FAVORITES = STUPDM_VOICE_NAVI + 1;
	public static final int STUPDM_SEARCH_HISTORY = STUPDM_FAVORITES + 1;
	public static final int STUPDM_SYNCHRONOUS_FREQUENCY = STUPDM_SEARCH_HISTORY + 1;
	public static final int STUPDM_ECO_DRIVE = STUPDM_SYNCHRONOUS_FREQUENCY + 1;
	
	public static final int  STUPDM_CAMERA_WANRING_TONE_INT_KEY  =  61;
	
	public static final int  STUPDM_CAMERA_WANRING_ICON_INT_KEY  = STUPDM_CAMERA_WANRING_TONE_INT_KEY+ 1;

	public static final int  STUPDM_FRIEND_LOACTION_INT_KEY = STUPDM_CAMERA_WANRING_ICON_INT_KEY+1;
	
	public static final int STUPDM_SURROUNDING_EVENT_INT_KEY= STUPDM_FRIEND_LOACTION_INT_KEY+1;
	
	
	public static final int STUPDM_AVOID_JAMS_INT_KEY  = STUPDM_SURROUNDING_EVENT_INT_KEY+1;
	
	public static final int STUPDM_SURROUNDING_EVENT_DISPLAY = STUPDM_AVOID_JAMS_INT_KEY+1;

	public static final int STUPDM_SPEED_CAMERA_SCALE = STUPDM_SURROUNDING_EVENT_DISPLAY+1;
	
	public static final int STUPDM_FAVORITE_DISPLAY_INT_KEY = STUPDM_SPEED_CAMERA_SCALE+1;

	public static final int STUPDM_DRIR_FUNCTION_INT_KEY    = STUPDM_FAVORITE_DISPLAY_INT_KEY +1;
	
	public static final int STUPDM_USER_REPORT_SCALE_INT_KEY  = STUPDM_DRIR_FUNCTION_INT_KEY +1;
	
	public static final int STUPDM_SNS_WARNING_TONE_INT_KEY  = STUPDM_USER_REPORT_SCALE_INT_KEY +1;
	/// develop debug
	public static final int STUPDM_DEBUG_DEVELOP_DBG= 1000;	

	public static final int STUPDM_DEBUG_BEACON_CONNECT=STUPDM_DEBUG_DEVELOP_DBG+1;
	/// Cradle Info On/Off
	public static final int STUPDM_DEBUG_CRADLE_INFO_OUTPUT = STUPDM_DEBUG_BEACON_CONNECT+1;

	public static final int STUPDM_DEBUG_SEARCH_INFO_OUTPUT = STUPDM_DEBUG_CRADLE_INFO_OUTPUT+1;
	

	/// Debug for CEATEC ON/OFF
	public static final int STUPDM_DEBUG_INFO_CEATEC = STUPDM_DEBUG_SEARCH_INFO_OUTPUT+1;
	/// Debug for China
	public static final int STUPDM_DEBUG_CHINA_DEMO = STUPDM_DEBUG_INFO_CEATEC+1;
	/// Debug for PARCEL
	public static final int STUPDM_DEBUG_PARCEL = STUPDM_DEBUG_CHINA_DEMO+1;
	/// Debug for Cradle
	public static final int STUPDM_DEBUG_MATCHINGLOG = STUPDM_DEBUG_PARCEL+1;
	/// Debug for Cradle
	public static final int STUPDM_DEBUG_LOCATIONWRITELOGS = STUPDM_DEBUG_MATCHINGLOG+1;
	
	public static final int STUPDM_DEBUG_SERVICE_MENU = STUPDM_DEBUG_LOCATIONWRITELOGS+1;
	
	public static final int STUPDM_DEBUG_NAME_COLLISION_STATUS = STUPDM_DEBUG_SERVICE_MENU+1;
	
	public static final int STUPDM_DEBUG_LANE_DETECTION_STATUS = STUPDM_DEBUG_NAME_COLLISION_STATUS+1;
	
	public static final int STUPDM_DEBUG_VEHICLE_DETECTION_STATUS = STUPDM_DEBUG_LANE_DETECTION_STATUS+1;
	
	public static final int STUPDM_DEBUG_LANE_DEPARTURE_INDORMATION_STATUS = STUPDM_DEBUG_VEHICLE_DETECTION_STATUS+1;
	
	public static final int STUPDM_DEBUG_STOP_GO_ASSIST_STATUS = STUPDM_DEBUG_LANE_DEPARTURE_INDORMATION_STATUS+1;
	
	public static final int STUPDM_DEBUG_TRAFFICLIGHT_DETECTION_STATUS = STUPDM_DEBUG_STOP_GO_ASSIST_STATUS+1;
	
	public static final int STUPDM_DEBUG_SPEED_AVOIDANC_STATUS = STUPDM_DEBUG_TRAFFICLIGHT_DETECTION_STATUS+1;
	
	public static final int STUPDM_DEBUG_DISTANCE_INDOMATION_STATUS = STUPDM_DEBUG_SPEED_AVOIDANC_STATUS+1;
	
	public static final int STUPDM_DEBUG_CAMERAH_INPUT_STATUS  = STUPDM_DEBUG_DISTANCE_INDOMATION_STATUS+1;
	
	public static final int STUPDM_DEBUG_CAR_INFORMATION_STATUS  = STUPDM_DEBUG_CAMERAH_INPUT_STATUS+1;
	
	public static final int STUPDM_DEBUG_RUN_LOOP_FILE_NAME_KEY  = STUPDM_DEBUG_CAR_INFORMATION_STATUS+1;

	public static final int STUPDM_DEBUG_TILE_ID_DISPLAY_INT_KEY  = STUPDM_DEBUG_RUN_LOOP_FILE_NAME_KEY+1;
	
	public static final int STUPDM_DEBUG_NETWORK_EXTERNIP_INT_KEY  = STUPDM_DEBUG_TILE_ID_DISPLAY_INT_KEY+1;

	// for float value
	public static final int STUPDM_MAP_SIZE = 0;
	public static final int STUPDM_DRIR_YELLOW_ALTER_TIME = 1;
	public static final int STUPDM_DRIR_RED_ALTER_TIME = 2;
	/**
	 * Get functional setting
	 *
	 * Get functional setting value, such as, route, map, VICS, sound, Debug,and some other settings
	 *
	 * @param   DWORD	byID	    : Function ID to get
	 * @return  DWORD				: Function setting value
	 */
	public native int GetInitialStatus(int dwID);

	/**
	 * Set functional setting
	 *
	 * Set functional setting value, such as, route, map, VICS, sound, Debug,and some other settings
	 *
	 * @param   DWORD byID		 : Function ID to Set
	 * @param   DWORD byNewStatus : New function value to set
	 * @return					 : none
	 */
	public native void SetInitialStatus(int dwID, int dwNewStatus);

	public native int GetCarTypeStatus();
	
	public native void SetCarTypeStatus(int NewStatus);

	public native byte GetCarLimitStatus();

	public native void SetCarLimitStatus(byte NewStatus);

	public native int GetCarSizeStatus(int DataType);

	public native void SetCarSizeStatus(int DataType, int NewStatus);
	
	public native void ResetCarData();

	public native float GetMapFontSizeStatus();
	
	public native void SetMapFontSizeStatus(float NewStatus);	
	
	public native byte[] GetDesignFilePath();
	
	public native void SetDesignFilePath(char[] bySeq, int arrayNum);
		
	public native short GetMapCanvasOffsetX();

	public native void SetMapCanvasOffsetX(short offsetX);

	public native short GetMapCanvasOffsetY();

	public native void SetMapCanvasOffsetY(short offsetY);
	
	public native void RestoreFactorySettings();
	
	public native float getFloat(int type);
//	
//	public native void setFloat(int type, float status);
	
	
	//assist method
	public static int get(int dwID){
		return instance.GetInitialStatus(dwID);
	}
	
	public static void set(int dwID, int dwNewStatus){
		instance.SetInitialStatus(dwID, dwNewStatus);
	}
	
	public static void setOn(int dwID){
		set(dwID, STUPDM_COMMON_ON);
	}
	
	public static void setOff(int dwID){
		set(dwID, STUPDM_COMMON_OFF);
	}
	
	public static boolean isOn(int dwID){
		return get(dwID) == STUPDM_COMMON_ON;
	}
	
	public static void converse(int dwID){
		if(isOn(dwID)) {
			setOff(dwID);
		} else {
			setOn(dwID);
		}
		
	}
	//assist method
	
	private static final jniSetupControl instance = new jniSetupControl();
	
	public static jniSetupControl getInstance(){
		return instance;
	}
	
	public static jniSetupControl createInstance(){
		return instance;
	}
	
	public jniSetupControl(){}
	
	public int GetInitialData(int dwID)
	{
		int value = 0;
		try{
			value = GetInitialStatus(dwID);
			return value;
		}
		catch(java.lang.UnsatisfiedLinkError e){
    		System.out.println("find jniSetupControl::GetInitialStatus() failed.");
    		System.out.println(e.toString());
    		System.out.println("----------------------------------.");
    		return -1;
		}
	}

	public void RestoreFactory()
	{
		try{
			RestoreFactorySettings();
		}
		catch(java.lang.UnsatisfiedLinkError e){
			System.out.println("find jniSetupControl::RestoreFactory() failed.");
    		System.out.println(e.toString());
    		System.out.println("----------------------------------.");
		}
	}
	
	
	
	public native String getStringByKey(int key);
	
	public native void  setString(int key,char[] str ,int charSize);
	
	public native int[]   getIntArrayByKey(int key);
	
	public native void setIntArray(int key,int[] value,int length);
		
	public native boolean getBooleanByKey(int key);
	
	public native void  setBoolean(int key,boolean value);
	
	public native int getIntByKey(int key);
	
	public native void setInt(int key ,int value);
	
	public native float getFloatByKey(int key);
	
	public native void setFloat(int key,float value);
	
	public native long getLongByKey(int key);
	
	public native void setLong(int key ,long value);
	
	
	
}
