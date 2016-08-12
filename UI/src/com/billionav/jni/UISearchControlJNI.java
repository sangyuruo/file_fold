package com.billionav.jni;

import java.util.UUID;

import android.util.Log;

import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.uitools.SystemTools;


public class UISearchControlJNI {
	
	/**
	 * Execution status code
	 * To describe current execution status
	 */
	public static final long UIC_SCM_SCREEN_NOWAIT			= 0;
	public static final long UIC_SCM_SCREEN_WAIT				= 1;
	public static final long UIC_SCM_SCREEN_BUSY				= 2;
	public static final long UIC_SCM_ERROR_NOWAIT				= -1;
	
	public static final int UIC_SCM_TOUCHED_MARK_TYPE_LOGOMARK 	= 0;
	public static final int UIC_SCM_TOUCHED_MARK_TYPE_FREEWORD 	= 1;	
	public static final int UIC_SCM_TOUCHED_MARK_TYPE_VICINITY 	= 2;
	public static final int UIC_SCM_TOUCHED_MARK_TYPE_POINT 	= 3;
	public static final int UIC_SCM_TOUCHED_MARK_TYPE_TRAFFIC 	= 4;
	public static final int UIC_SCM_TOUCHED_MARK_TYPE_USER_REPORT = 5;
	public static final int UIC_SCM_TOUCHED_MARK_TYPE_MAP_SYMBOL  = 6;	

	
	public static final int UIC_SCM_TOUCHED_MARK_TYPE_INVALID = -1;
	/**
	 * define the max length of input characters
	 *
	 */
	public static final long MAX_INPUT_NAME_LENGTH 			= 64;
	
	/**
	 * define the max number of custom logo mark list
	 *
	 */
	public static final long UIC_SCM_MAX_LOGOMARK_GENRE_NUMBER 			= 30;
	/**
	 * define the max size of search result
	 *
	 */
	public static final int MAX_SEARCH_RESULT_SIZE 			= 100;
	
	/**
	 * define mode version. 
	 */
	public static final int SRCH_CUSTOM_MADE_VERSION_LUXGEN = 0;
	public static final int SRCH_CUSTOM_MADE_VERSION_OTHER = 1;
	/**
	 * define searching sort type. 
	 */
	public static final int SRCH_SORT_TYPE_DISTANCE = 0;
	public static final int SRCH_NET_TYPE_ALPHABET = 1;
	public static final int SRCH_NET_TYPE_ROUTESIDE = 2;
	/**
	 * define searching net type. 
	 * 
	 */
//	public static final int SRCH_NET_TYPE_NONE = -1;
	public static final int SRCH_NET_TYPE_OFFLINE = 0;
	public static final int SRCH_NET_TYPE_ONLINE = 1;
	/**
	 * Search type
	 * Used to specify a kind of search procedures
	 */	
	public static final int UIC_SCM_SRCH_TYPE_LOGOMARK_NORMAL	= 0;	
	public static final int UIC_SCM_SRCH_TYPE_FREEWORD 			= 1;
	public static final int UIC_SCM_SRCH_TYPE_NEARBY			= 2;
	public static final int UIC_SCM_SRCH_TYPE_MAP_SYMBOL		= 3;	
	public static final int UIC_SCM_SRCH_TYPE_CHECK_TOUCH_POINT	= 4;
	public static final int UIC_SCM_SRCH_TYPE_PIN_POINT			= 5;

	public static final int UIC_SCM_SRCH_TYPE_INVALID 		= -1;

	/**
	 * Action id 
	 * Used to describe user input action, such as long press or double click
	 */
	public static final int	UIC_SCM_ACT_ID_NORMAL 			= 0;
	public static final int	UIC_SCM_ACT_ID_LONG_PRESS		= 1;
	public static final int	UIC_SCM_ACT_ID_INVALID			= -1;

	/**
	 * Button id
	 * Used to specify a kind of buttons
	 */
	public static final int	UIC_SCM_BTN_ID_SEARCH 			= 0;	/**< id for [Search] button*/
	public static final int	UIC_SCM_BTN_ID_AREA				= 1;	/**< id for [Area] button*/
	public static final int	UIC_SCM_BTN_ID_POICATE			= 2;	/**< id for [POI Category] button*/
	public static final int UIC_SCM_BTN_ID_POICATE_FAVORITE	= 3;	/**< id for [Favorite POI Category] button*/
	public static final int UIC_SCM_BTN_ID_NEAR_CAR			= 4;	/**< id for [Near Car] button*/
	public static final int UIC_SCM_BTN_ID_NEAR_DEST		= 5;	/**< id for [Near Dest] button*/
	public static final int UIC_SCM_BTN_ID_NEAR_CURSOR		= 6;	/**< id for [Near Cursor] button*/
	public static final int UIC_SCM_BTN_ID_NEAR_ROUTE		= 7;	/**< id for [Near Route] button*/
	public static final int UIC_SCM_BTN_ID_DEL_ALL_HISTORY  = 8;	/**< id for [Delete All History] button*/
	public static final int UIC_SCM_BTN_ID_ADD_GENRE  		= 9;	/**< id for [Add Genre] button*/
	
	public static final int	UIC_SCM_BTN_ID_INVALID			= -1;	/**< invalid button id*/

	/**
	 * Change direction
	 * Used to specify a kind of change directions
	 */
	public static final int	UIC_SCM_CHANGE_FORWARD 			= 0;	/**< forward change direction*/
	public static final int	UIC_SCM_CHANGE_BACKWARD			= 1;	/**< default backward change direction*/
	public static final int	UIC_SCM_CHANGE_SPEC_BACKWARD	= 2;	/**< specific backward change direction*/
	public static final int	UIC_SCM_CHANGE_NONE				= 3;	/**< node does not change*/
	public static final int	UIC_SCM_CHANGE_INVALID			= -1;	/**< invalid change direction*/

	/**
	 * Screen id
	 * Used to specify a kind of screens
	 */
	public static final int	UIC_SCM_SCREEN_ID_NAME_INPUT 			= 0;
	public static final int UIC_SCM_SCREEN_ID_CUSTOM_GENRE			= 1;	
	public static final int	UIC_SCM_SCREEN_ID_POI_LIST				= 2;	
	public static final int	UIC_SCM_SCREEN_ID_POI					= 3;	
	
	//to del
	public static final int	UIC_SCM_SCREEN_ID_ADDR_INPUT 			= 4;
	public static final int	UIC_SCM_SCREEN_ID_AREA_LEV1				= 5;
	public static final int	UIC_SCM_SCREEN_ID_AREA_LEV2				= 6;
	public static final int	UIC_SCM_SCREEN_ID_AREA_LEV3				= 7;
	public static final int	UIC_SCM_SCREEN_ID_AREA_LEV4				= 8;
	public static final int UIC_SCM_SCREEN_ID_POICATE_FAVORITE		= 9;
	public static final int	UIC_SCM_SCREEN_ID_POICATE_LEV1			= 10;
	public static final int	UIC_SCM_SCREEN_ID_POICATE_LEV2			= 11;
	public static final int	UIC_SCM_SCREEN_ID_POICATE_LEV3			= 12;
	
	public static final int	UIC_SCM_SCREEN_ID_INVALID				= -1;
	
	/**
	 * list id
	 * Used to specify a kind of list
	 */
	public static final int	UIC_SCM_LIST_ID_NORMAL 				= 0;
	public static final int UIC_SCM_LIST_ID_CATEGORY_NEAR_CAR	= 1;
	public static final int UIC_SCM_LIST_ID_CATEGORY_NEAR_DEST	= 2;
	public static final int UIC_SCM_LIST_ID_CATEGORY_NEAR_CURSOR= 3;
	public static final int UIC_SCM_LIST_ID_CATEGORY_NEAR_ROUTE = 4;
	

	public static final int UIC_SCM_LIST_ID_LOGOMARK_GENRE 			= 8;
	public static final int UIC_SCM_LIST_ID_INPUT_HELP 				= 9;
	public static final int UIC_SCM_LIST_ID_INPUT_HISTORY 			= 10;
	public static final int UIC_SCM_LIST_ID_POI						= 11;
	public static final int UIC_SCM_LIST_ID_CUSTOM_GENRE			= 12;
	public static final int UIC_SCM_LIST_ID_MAP_SYMBOL			= 13;

	public static final int	UIC_SCM_LIST_ID_INVALID				= -1;

	/**
	 * error type
	 * Used to specify a kind of error type
	 */
	public static final int	UIC_SCM_ERROR_TYPE_NO_ERROR = 0;
	public static final int	UIC_SCM_ERROR_TYPE_SERVER 	= 1; 
	public static final int	UIC_SCM_ERROR_TYPE_NET		= 2;
	public static final int	UIC_SCM_ERROR_TYPE_DATA		= 3;
	public static final int	UIC_SCM_ERROR_TYPE_OPER		= 4;
	public static final int	UIC_SCM_ERROR_TYPE_INNER	= 5;
	/**
	 * error code
	 * Used to specify a kind of error code
	 */
	public static final int	UIC_SCM_CODE_INNER_OK 			= 0xef000001;
	public static final int	UIC_SCM_CODE_INNER_ERROR 		= 0xef000002;
	public static final int	UIC_SCM_CODE_OPER_CANCEL_OK 	= 0xef010001;
	public static final int	UIC_SCM_CODE_OPER_CANCEL_ERROR	= 0xef010002;
	public static final int	UIC_SCM_CODE_DATA_OVERFLOW  	= 0xff040001;
	public static final int	UIC_SCM_CODE_NET_DISCONNECTED 	= 0xff030001;
	
	public static final int SearchResult_UIC_SCM_LIST_ID_POI = 1;   //检索结果
	public static final int SearchResult_UIC_SCM_LIST_ID_INPUT_HELP = 2;  //输入辅助
	public static final int SearchResult_UIC_SCM_LIST_ID_INPUT_HISTORY = 3; //检索历史
	public static final int SearchResult_UIC_SCM_LIST_ID_GENRE = 4;  //检索类别列表
	public static final int SearchResult_UIC_SCM_LIST_ID_AREA = 5;   //????????
	
	public static class AL_LonLat{
		public AL_LonLat(long lon,long lat){
			this.lon = lon;
			this.lat = lat;
		}
		private long lon;
		private long lat;
	}
	public static class UIC_SCM_POIReqParam{
		public UIC_SCM_POIReqParam(){
			
			genre_id = 0;
			area_id = 0;
			keyword =  "";
			genrename = "";
			areaname = "";
			type = UIC_SCM_SRCH_TYPE_INVALID;
			btn_id = UIC_SCM_BTN_ID_INVALID;
			act_id = UIC_SCM_ACT_ID_NORMAL;
			list_id = UIC_SCM_LIST_ID_INVALID;
			nettype = SRCH_NET_TYPE_OFFLINE;
			radius = 16000;
		}
		public int type;
		public int radius;
		public int btn_id;
		public int act_id;
		public int list_id;
		public int genre_id;
		public int area_id;
		public int nettype; 
		public String keyword;
		public String genrename;
		public String areaname;
	}
	/**
	 * Instance 
	 * synchronized method
	 */
	public static synchronized UISearchControlJNI Instance() {
		if (m_instance == null) {
			m_instance = new UISearchControlJNI();
		}
		return m_instance;
	}
	
	/**
	 * Private constructor
	 * for singleton pattern
	 */
	private UISearchControlJNI() {
		m_srchResult = new UISearchResultJNI();
		m_detailInfoResult = new UISearchDetailInfoResultJNI();
	}
	
	
	public long StartSearch(int type, int netType){
		return StartForSearch(type,netType,getVer());
	}
	/**
	 * Start specific type of search
	 *
	 * @param   type specific search type
	 * @return  search status code
	 */
	public native long StartForSearch(int type, int netType, int ver);
	
	/**
	 * Destroy specific type of search
	 *
	 * @param   type specific search type
	 * @return  search status code
	 */
	public native long DestroySearch(int type);

	private int getVer(){
		if(SystemTools.EDITION_LUXGEN.equals(SystemTools.getApkEdition())){			
			return SRCH_CUSTOM_MADE_VERSION_LUXGEN;
		}else{
			return SRCH_CUSTOM_MADE_VERSION_OTHER;
		}
	}
	
	/**
	 * recovery specific type of search
	 *
	 * @param   type specific search type
	 * @return  search status code
	 */
	public long OnTouchMap(AL_LonLat sCenterPos){
		return OnTouchMap(sCenterPos.lon,sCenterPos.lat,UIC_SCM_ACT_ID_NORMAL);
	}
	public long OnTouchMap(AL_LonLat sCenterPos,long actid){
		return OnTouchMap(sCenterPos.lon,sCenterPos.lat,actid);
	}
	private native long OnTouchMap(long lon,long lat,long actid);
	
	public long ReqPinPoint(AL_LonLat sCenterPos){
		Log.i("LonLat","UISearchControlJNI:ReqPinPoint");
		return ReqPinPoint(sCenterPos.lon,sCenterPos.lat);
	}
	private native long ReqPinPoint(long lon,long lat);
	
	/**
	 * [Overload]To interpret and accomplish the "press detail info" request from UI
	 *
	 * @param   type specific search type
	 * @param	bid specific button id
	 * @return  search status code
	 */
	public native long OnUpdateDetailInfo(int type,long idx);

	/**
	 * [Overload]To interpret and accomplish the "press button" request from UI
	 *
	 * @param   type specific search type
	 * @param	bid specific button id
	 * @return  search status code
	 */
	public long OnPressBtn(UIC_SCM_POIReqParam param){
		Log.i(NaviConstant.TAG,"OnPressBtn");
		StringBuilder sb = new StringBuilder();
		sb.append("type:").append(param.type).append("\n")
			.append("radius:").append(param.radius).append("\n")
			.append("btn_id:").append(param.btn_id).append("\n")
			.append("act_id:").append(param.act_id).append("\n")
			.append("list_id:").append(param.list_id).append("\n")
			.append("genre_id:").append(param.genre_id).append("\n")
			.append("area_id:").append(param.area_id).append("\n")
			.append("keyword:").append(param.keyword).append("\n")
			.append("genrename:").append(param.genrename).append("\n")
			.append("areaname:").append(param.areaname).append("\n")
			.append("nettype:").append(param.nettype);
		Log.i(NaviConstant.TAG,"param:"+sb.toString());
		return OnPressBtn(param.type,
							param.radius,
							param.btn_id,
							param.act_id,
							param.list_id,
							param.genre_id,
							param.area_id,
							param.keyword,
							param.genrename,
							param.areaname,
							param.nettype
							);
	}
	private native long OnPressBtn(int type,
									int radius,
									int btn_id,
									int act_id,
									int listid,
									int genre_id,
									int area_id,
									String keyword,
									String genrename,
									String areaname,
									int nettype
									);
	
	//to del ---------------end---------------
	public long PrepareForAddPoint(int type,long idx){
		return PrepareForAddPoint(type,idx,UUID.randomUUID().toString());
	}
	public long PrepareForCalcRoute(int type,long idx){
		return PrepareForCalcRoute(type,idx,UUID.randomUUID().toString());
	}
	public native long PrepareForAddPoint(int type,long idx,String uuid);	
	public native long PrepareForCalcRoute(int type,long idx,String uuid);	
	/**
	 * To interpret and accomplish the "cancel" request from UI
	 *
	 * @param   type specific search type
	 * @return  search status code
	 */
	public native long OnCancel(int type);
	
	/**
	 * To interpret and accomplish the "back" request from UI
	 *
	 * @param   type specific search type
	 * @return  search status code
	 */
	public native long OnBack(int type);
	/**
	 * [Overload]To interpret and accomplish the "input help" request from UI
	 *
	 * @param   type specific search type
	 * @param	topidx the top index
	 * @return  search status code
	 */
	public long OnInput(int type,String input,AL_LonLat sCenterpos,int netType){
		return OnInput(type,input,sCenterpos.lon,sCenterpos.lat,netType);
	}
	public native long OnInput(int type,String input,long lon,long lat,int netType);
	
	/**
	 * [Overload]To interpret and accomplish the "select list item" request from UI
	 *
	 * @param   type specific search type
	 * @param	idx the selected item index
	 * @param	listid specific list id
	 * @return  search status code
	 */
	public long OnSelectListItem(int type, long idx,int nettype) {
		return OnSelectListItem(type, idx, UIC_SCM_LIST_ID_NORMAL,nettype);
	}
	public long OnSelectListItem(int type, long idx, int listid,int nettype) {
		return OnSelectListItem(type, idx, listid, UIC_SCM_ACT_ID_NORMAL, nettype ,SRCH_SORT_TYPE_DISTANCE,-1);
	}
	//for sort nearby search
	public long OnSelectListItemNearby(long idx,int nettype,int sort) {
		return OnSelectListItem(UIC_SCM_SRCH_TYPE_NEARBY, 
				idx, UIC_SCM_LIST_ID_NORMAL, UIC_SCM_ACT_ID_NORMAL,nettype,SRCH_SORT_TYPE_DISTANCE,-1);
	}
	//for rapid nearby search
	public long OnSelectListItemRapidParkNearby(long lon,long lat) {
		return RapidParkNearbySearch(UIC_SCM_SRCH_TYPE_NEARBY, "car park",
				lon, lat,
				UIC_SCM_LIST_ID_NORMAL, UIC_SCM_ACT_ID_NORMAL,
				SRCH_NET_TYPE_OFFLINE, 3,SRCH_SORT_TYPE_DISTANCE, 500);
	}
	public native long OnSelectListItem(int type, 
							  			long idx, 
							  			int listid, 
							  			int aid,int nettype,int sort, int radius);
	
	public native long RapidParkNearbySearch(int type, 
  			String name, 
  			long lon,long lat,
  			int listid, 
  			int aid,int nettype,int genreid, int sort, int radius);
		
	
	/**
	 * Get search result instance
	 *
	 * @param   type specific search type
	 * @return  search result instance, NULL on error
	 */
	public UISearchResultJNI GetSearchResult(int type) {
		m_srchResult.SetType(type);
		return m_srchResult;
	}
	
	/**
	 * Get detail info result instance
	 *
	 * @param   type specific search type
	 * @return  detail info result instance, NULL on error
	 */
	public UISearchDetailInfoResultJNI GetDetailInfoResult(int type) {
		m_detailInfoResult.SetType(type);
		return m_detailInfoResult;
	}
	/**
	 * Update the specific search result
	 *
	 * @param   type specific search type
	 * @return  NP_FALSE on Error
	 */
	public native boolean UpdateResult(int type);
	
	
	/**
	 * Get latest request id
	 *
	 * @param   type specific search type
	 * @return  latest request id
	 */
	public native long GetReqID(int type); 
	public native long UseURLMaps(boolean bValue);
	
	public native void reqTraditionalItemList(int genreid, String genreName, int netType);
	
	
	public native void addPOIGenre(int position);
	public native void addCustomPOIGenre(String poiName);
	public native void deletePOIGenre(int position);
	
	
	public native void reqDefaultAreaInfo();
	public native String getDefaultArea();
	public native void reqAreaList(String name);
	public native AreaInfo[] getAreaInfoArrays();
	public native void reqSaveAreaInfo(String name, String areacode, long lon, long lat);
	
	public static class AreaInfo{
		public String name;
		public String areacode;
		public boolean nextlevelflag;
		public long lon;
		public long lat;
		
		public AreaInfo(String name, String areacode, boolean nextlevelflag, long lon, long lat) {
			this.name = name;
			this.areacode = areacode;
			this.nextlevelflag = nextlevelflag;
			this.lon = lon;
			this.lat = lat;
		}
	}
	
	private static UISearchControlJNI m_instance = null;
	private UISearchResultJNI m_srchResult = null;
	private UISearchDetailInfoResultJNI m_detailInfoResult = null;

	public native void ReqDelInputHistory(int iIndex);
	
	public static native void SetOrbisSwtch(boolean isOpened);
	
	public static native void sendDestNationPoint(String name, String address, String tel, int lon, int lat);

	public static native byte[] getDestNationPointProtoByte(String name, String address, String tel, int lon, int lat);
}
