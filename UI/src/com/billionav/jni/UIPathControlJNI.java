package com.billionav.jni;

public class UIPathControlJNI {
	
	/// max routes count that can be saved.
	public static final int BACKUP_ROUTES_MAX = 10;		
	
	// Path find condition, standard.
	public static final int STD_RECOMMMENT = 0;
	public static final int STD_DISTANCE = 1;
	public static final int STD_TRUNK = 2;
	public static final int STD_CUSTOM = 6;
	public static final int STD_TIME = 7;
	
	
	// Path find condition, highway.
	public static final int HWY_NORMAL = 0;
	public static final int HWY_PRIORITY = 1;
	public static final int HWY_AVOIDANCE = 2;
	// Path find condition, toll.
	public static final int TOL_NORMAL = 0;
	public static final int TOL_PRIORITY = 1;
	public static final int TOL_AVOIDANCE = 2;
	// Path find condition, ferry.
	public static final int FRY_NORMAL = 0;
	public static final int FRY_PRIORITY = 1;
	public static final int FRY_AVOIDANCE = 2;
	
	// Path finding type for UI, used when call StartUIPathFinding.
	public static final int UIC_PT_COMMON_REROUTE = 0;			// common reroute, there is nothing specially 
	public static final int UIC_PT_GO_HERE_FINDING = 1;			// Get point_info from POI, Set it as destination, start calculation.
	public static final int UIC_PT_MULTI_GO_HERE_FINDING = 2;	// Get point_info from POI, Set it as destination, to calculate 6 routes.
	public static final int UIC_PT_MULTI_REROUTE = 3;	        // Use old destination, to calculate 6 routes.
	public static final int UIC_PT_ADD_VIA_FINDING = 4;			// Add a via before destination, start calculation.
	public static final int UIC_PT_INSERT_VIA_FINDING = 5;		// Insert a guide point as first via, start calculation.
	public static final int UIC_PT_VIA_EDITED_FINDING = 6;		// Start calculation after DestList edited.
	public static final int UIC_PT_SET_START_FINDING = 7;		// Get point_info from POI, Set it as start, start calculation.
	public static final int UIC_PT_DEL_IC_ENTER_FINDING = 8;	// Delete IC enter and start route calculation
	public static final int UIC_PT_DEL_IC_EXIT_FINDING = 9;		// Delete IC exit and start route calculation
	public static final int UIC_PT_DEL_IC_ALL_FINDING = 10;		// Delete IC enter and exit and start route calculation
	public static final int UIC_PT_SKIP_VIA_FINDING = 11;		// Skip next via, start calculation.
	public static final int UIC_PT_GO_HOME_FINDING = 12;		// Set home as destination, start calculation.
	public static final int UIC_PT_MULTI_GO_HOME_FINDING = 13;	// Set home as destination, to calculate 6 routes.
	public static final int UIC_PT_DATA_UPDATE_FINDING = 14;	// Reroute after data update when opening process.
	public static final int UIC_PT_ROUTE_DOWNLOAD = 15;			// Download a route from web server.
	
	// Path point ID.
	public static final int PDM_DEST_ID_ORG_START = 0; 			///< ��Ҏ̽���r�γ��k��
	public static final int PDM_DEST_ID_START = 1; 				///< ���k��
	public static final int PDM_DEST_ID_FIRST_DEST = 2; 		///< ���k�ؤ����˒���
	public static final int PDM_DEST_ID_NEXT_DEST = 3; 			///< ���k�ؤ�ͨ�^�g�ߤ����˒���
	public static final int PDM_DEST_ID_LAST_DEST = 4; 			///< GOAL������β������ǰ�˒���
	public static final int PDM_DEST_ID_GOAL = 5; 				///< Ŀ�ĵأ�����β��
	public static final int PDM_DEST_ID_RGRD_START = 6; 		///< ���h��ʼ�㣨����β����ǰ��
	public static final int PDM_DEST_ID_RGRD_END = 7; 			///< ���h���K�㣨����β����ǰ��
	public static final int PDM_DEST_ID_RGRD_START_FIRST = 8;	///< ���h��ʼ�㣨���k�ؤ���?
	public static final int PDM_DEST_ID_RGRD_END_FIRST = 9;		///< ���h���K�㣨���k�ؤ���?
	public static final int PDM_DEST_ID_RGRD_START_NEXT = 10;	///< ���h��ʼ�㣨ͨ�^�g�ߤ���?
	public static final int PDM_DEST_ID_RGRD_END_NEXT = 11;		///< ���h���K�㣨ͨ�^�g�ߤ���?
	public static final int PDM_DEST_ID_IC_ENTER = 12; 			///< ��ڣɣ�
	public static final int PDM_DEST_ID_IC_EXIT = 13; 			///< ���ڣɣ�
	public static final int PDM_DEST_ID_VIA_1 = 14;				///< ���ĵأ�
	public static final int PDM_DEST_ID_VIA_2 = 15;				///< ���ĵأ�
	public static final int PDM_DEST_ID_VIA_3 = 16;				///< ���ĵأ�
	public static final int PDM_DEST_ID_VIA_4 = 17;				///< ���ĵأ�
	public static final int PDM_DEST_ID_VIA_5 = 18;				///< ���ĵأ�
	public static final int PDM_DEST_ID_VIA_6 = 19;				///< ���ĵأ�
	public static final int PDM_DEST_ID_VIA_7 = 20;				///< ���ĵأ�
	public static final int PDM_DEST_ID_VIA_8 = 21;				///< ���ĵأ�
	public static final int PDM_DEST_ID_VIA_9 = 22;				///< ���ĵأ�
	public static final int PDM_DEST_ID_VIA_10 = 23;				///< ���ĵأ�
	public static final int PDM_DEST_ID_PARKING = 24;				///< ���ĵأ�
	public static final int PDM_DEST_ID_DROP_IN = 25;			///< �Ĥ��
	public static final int PDM_DEST_ID_INVALID	= 26;	        ///< �o��
	
	// �O����`�Ȥ���ɶ� 
	public static final int UIC_PT_ROUTE_STATUS_NONE = 0;		/**< �O����`�Ȥʤ� */
	public static final int UIC_PT_ROUTE_STATUS_COMP = 1;		/**< �O����`�Ȥ��� */
	public static final int UIC_PT_ROUTE_STATUS_HALF = 2; 		/**< �O����`�Ȱ뤢�� */
	
	// Demo run status
	public static final int UIC_PT_DEMO_STATUS_OFF = 0;		///< demo run off
	public static final int UIC_PT_DEMO_STATUS_ON = 1;  	///< demo run on
	
	// Route guidance status
	public static final int UIC_PT_CURRENT_GUIDE_OFF = 0;	///< guide off
	public static final int UIC_PT_CURRENT_GUIDE_ON = 1;	///< guide on
	
	// ̽�����֥�������ID
	public static final int UIC_PT_FIND_OBJ_UI = 0;				///< Menu�����ˤ��̽��
	public static final int UIC_PT_FIND_OBJ_AUTOREROUTE = 1;    ///< Guide����Υȥꥬ�`�ˤ�륪�`�ȥ��`��̽��
	public static final int UIC_PT_FIND_OBJ_ROUTEADVISER = 2;   ///< Guide����Υȥꥬ�`�ˤ���`�ȥ��ɥХ����`̽��
	public static final int UIC_PT_FIND_OBJ_VICS = 3;           ///< VICS����Υȥꥬ�`�ˤ��̽��
	public static final int UIC_PT_FIND_OBJ_INTERNAVI = 4;      ///< ���󥿩`�ʥӤ���Υȥꥬ�`�ˤ��̽��
	public static final int UIC_PT_FIND_OBJ_DRIVEPLAN = 5;      ///< �ɥ饤�֥ץ�󤫤�Υȥꥬ�`�ˤ��̽��
	public static final int UIC_PT_FIND_OBJ_AUTODIVERSION = 6;  ///< �Ԅ��ػ�̽��Object
	public static final int UIC_PT_FIND_OBJ_TEST = 7;           ///< �ƥ����ä�̽��
	public static final int UIC_PT_FIND_OBJ_DEMO = 8;           ///	�ǥ����и����Υ�`��̽��
	public static final int UIC_PT_FIND_OBJ_INVALID = 9;	    ///< �o����		
	
	// ���������û�������
	public static final int UIC_PT_SCREEN_ATTR_NG = 0;			///< �ǌg�У����S�ɣ�
	public static final int UIC_PT_SCREEN_ATTR_OK = 1;			///< �g�У��S�ɣ�
	
	// Path point sort option.
	public static final int SORT_WAYPOINTS = 0;					///< sort way points in the order of distance.
	public static final int SORT_ALL_DESTS = 1;					///< sort all path points in the order of distance.
	public static final int SORT_WAYPOINTS_START_SPECIFY = 2;	///< sort way points with start point specified.
	public static final int SORT_ALL_DESTS_START_SPECIFY = 3;	///< sort all path points with start point specified.
	
	// Option of getting the number of path points.
	public static final int UIC_PT_NUM_POINT_ALL = 0;
	public static final int UIC_PT_NUM_POINT_EXCEPT_PASSED = 1;
	public static final int UIC_PT_NUM_POINT_ALL_VIA = 2;
	public static final int UIC_PT_NUM_POINT_ALL_VIA_EXCEPT_IC = 3;
	public static final int UIC_PT_NUM_POINT_VIA_EXCEPT_PASSED = 4;
	public static final int UIC_PT_NUM_POINT_VIA_EXCEPT_PASSED_UI = 5;
	public static final int UIC_PT_NUM_POINT_EXCEPT_PASSED_UI = 6;
	public static final int UIC_PT_NUM_POINT_VIA_AND_GOAL = 7;
	public static final int UIC_PT_NUM_POINT_VIA_AND_GOAL_EXCEPT_PASSED = 8;
	
	// ����·�������_ʼ�����Y��
	public static final int UIC_PT_POINT_LINK_EXIST = 0;
	public static final int UIC_PT_POINT_LINK_TRIGGER = 1;
	public static final int UIC_PT_POINT_LINK_ERROR = 2;
	
	public static final int UIC_PT_SELECT_ROAD_NONE_MASK		=0x00000000;		///< ����·�ϟo�������ϥ��?
	public static final int UIC_PT_SELECT_ROAD_NORMAL_MASK		=0x00000001;		///< ����һ���������
	public static final int UIC_PT_SELECT_ROAD_HWY_MASK			=0x00000002;		///< ���˸��ٵ�������
	public static final int UIC_PT_SELECT_ROAD_RAPID_MASK		=0x00000004;		///< ���˿��ٵ�������	
	
	/// ��·�N�e
	public static final int UIC_PT_PATH_ROAD_UNKNOWN			= 0;	// ����
	public static final int UIC_PT_PATH_ROAD_NORMAL				= 1;	// һ���
	public static final int	UIC_PT_PATH_ROAD_HWY				= 2;	// ���ٵ�
	public static final int	UIC_PT_PATH_ROAD_RAPID				= 3;	// ���ٵ�
	public static final int UIC_PT_PATH_ROAD_NOTHING			= 4;	// ��·���ʤ�
	
	public static final int UIC_PT_ADV_FT_NORMAL    = 0;		///< ͨ���K�ˣ������ॢ���Ȥ��x�k��		
	public static final int UIC_PT_ADV_FT_OPERATION = 1;		///< �����ǽK�ˣ��F�ڵء���˥�`���`�����Ѻ�£�
	public static final int UIC_PT_ADV_FT_OFF_ROUTE = 2; 		///< ���ե�`�ȤǽK��
	public static final int UIC_PT_ADV_FT_INTERRUPT	= 3;	 	///< ���Ƹ���z�߽K��
	
	// ���ɥХ����`�N�e
	public static final int UIC_PT_ADV_DEFAULT      = 0;		///< ��`�ȥ��ɥХ����`
	public static final int UIC_PT_ADV_AJA_REROUTE  = 1;		///< �i�Ϳ��]���Ԅӣ�
	public static final int UIC_PT_ADV_AJM_REROUTE  = 2; 		///< �i�Ϳ��]���քӣ�
	public static final int UIC_PT_ADV_ROAD_CLOSED	= 3;	 	///< ͨ��ֹ��
	public static final int UIC_PT_ADV_OTHER_ROUTE	= 4;	 	///< �e��`��
	public static final int UIC_PT_ADV_DRV_PLAN		= 5;	 	///< �ɥ饤�֥ץ��
	
	// �¾ɥ�`�����N�e
	public static final int UIC_PT_NEWOLD_DISTANCE  = 0;		///< ���x
	public static final int UIC_PT_NEWOLD_TIME   	= 1;		///< ��Ҫ�r�g
	public static final int UIC_PT_NEWOLD_DISTORG   = 2; 		///< ̽���_ʼ�ص㤫������ص�ޤǤξ��x
	public static final int UIC_PT_NEWOLD_TOLL		= 3;	 	///< ���Ͻ���link flag
	
	public static final int UIC_PT_NEWOLD_TOLLNOTUSE = 0;		///< Toll not use
	public static final int UIC_PT_NEWOLD_TOLLUSE    = 1;		///< Toll use

	
	public static final int PathFinderRequest_Weight_Param_WEIGHT_DEFAULT = 0;
	public static final int PathFinderRequest_Weight_Param_WEIGHT_SHORTEST = 1;
	public static final int PathFinderRequest_Weight_Param_WEIGHT_AVOIDHIGHWAY = 2;
	public static final int PathFinderRequest_Weight_Param_WEIGHT_WALK = 3;
	
	//path type define
	public static final int UIC_PT_ROUTE_KIND_ROUTE_DEFAULT			 = 0;
	public static final int UIC_PT_ROUTE_KIND_ROUTE_SHORTEST		 = 1;
	public static final int UIC_PT_ROUTE_KIND_ROUTE_AVOIDHIGHWAY	 = 2;
	public static final int UIC_PT_ROUTE_KIND_ROUTE_WIDTH			 = 3;
	public static final int UIC_PT_ROUTE_KIND_ROUTE_ECO				 = 4;
	public static final int UIC_PT_ROUTE_KIND_ROUTE_OLD				 = 5;
	public static final int UIC_PT_ROUTE_KIND_ROUTE_NEW				 = 6;
	public static final int UIC_PT_ROUTE_KIND_ROUTE_MAX				 = 7;

	/**
	 * ��̽���r�˱�Ҫ�ʥ�`�Ȥ������O������
	 *
	 * ��̽�����Ф�ǰ�ˡ���`������̽���ص�ꥹ�Ȥ��O�����ޤ���
	 * ��̽�����Ф����Ϥϱؤ�����˺�����¤�����
	 *
	 * @return	0 if ��K��, otherwise �I���ФΤ���g�в���
	 * @attention
	 * ̽���С������_ʼ����ޤǤόg�в��ɤ�����ޤ���
	 */
	public native int SetRerouteInfo();
	
	public native int SetPathPoint(int DestID);
	
	public native int SetPathPoint(int DestID, int NumPointData, long[] DispLonLat, long[] FlagLonLat, long[] LonLat, String PointName, String Address, String Tel);
	
	/**
	 * Start path finding from UI(User's operation)
	 * 
	 * @param UIFindType
	 * @return 0 if OK, otherwise some error occurs. 
	 */
	public native int StartUIPathFinding(int UIFindType);
	
	
	public native int StartUIPathFindingForRouteInfo();
	/**
	* �O����`�Ȥ���ɶ�ȡ��
	*
	* �O����`�Ȥ���ɶȣ�̽�����ɤ��ޤǽK��äƤ��뤫����ȡ�ä��ޤ���
	*
	* @return ��`�Ȥ���ɶ�
	* - UIC_PT_ROUTE_STATUS_NONE: �O����`�Ȥʤ�
	* - UIC_PT_ROUTE_STATUS_COMP: �O����`�Ȥ���
	* - UIC_PT_ROUTE_STATUS_HALF: �O����`�Ȱ뤢��
	*
	* @attention
	* �O����`�Ȱ뤢��Ȥ����Τϡ�̽���ϽK�ˤ��Ƥ��뤬����Ҏ�T���_ʼ��
	* �ФäƤ��ʤ�״�B�Τ��ȤǤ���
	*
	*/
	public native int GetRouteStatus();
	
	/**
	 * Get demo run status.
	 * @return UIC_PT_DEMO_STATUS_ON if demo run on, UIC_PT_DEMO_STATUS_OFF: demo run off.
	 */
	public native int GetDemoStatus();
	
	/**
	 * Stop demo run.
	 */
	public native void StopDemoDriving();
	
	/**
	 * Get current guidance status.
	 * @return UIC_PT_CURRENT_GUIDE_ON: guide on, UIC_PT_CURRENT_GUIDE_OFF: guide off.
	 */
	public native int GetCurrentGuide();
	
	/**
	* ���������û������Ԥ��O������
	* 
	* ��̽�����饹�ˌ����ơ����������äλ������Ԥ��O�����ޤ���
	*
	* @param ObjID; [in]: ��������
	* UIC_PT_FIND_OBJ_AUTOREROUTE  :���`�ȥ��`��̽��
	* UIC_PT_FIND_OBJ_ROUTEADVISER :��`�ȥ��ɥХ����`̽��
	* UIC_PT_FIND_OBJ_VICS			:�i�Ϳ��]���`�ȥ��`��̽��
	* @param Attr; [in]: ��������
	* - UIC_PT_SCREEN_ATTR_NG: �ǌg�У����S�ɣ�
	* - UIC_PT_SCREEN_ATTR_OK: �g�У��S�ɣ�
	* @return	�ʤ�
	* @attention
	*  ��ӛ�����̽�����֥������Ȥ�ʹ�ä��ʤ��褦�ˤ��Ƥ�������
	*	UIC_PT_SCREEN_ATTR_NG ���O���������֥������ȤϤ��λ���Ǥ�̽��
	*����g�Ф��ʤ��ʤ�ޤ���
	*/
	public native void SetScreenAttribute(int ObjID, int Attr);
	
	/**
     * ��`�ȤΏͻ�Ȥγ��ڄI����Ф��ޤ�
	*/
	public native void RouteInitialize();
	
	/**
	 * Delete route.
	 */
	public native void DeleteRoute();
	
	/**
	 * send message to start guide.
	 */
	public native void GuideStart();
	
	/**
	* ̽���ص�ꥹ�Ȥ��քӤǁK���椨��
	*
	* @param     NumPoint    [IN]:	̽���ص����
	* @param     pList       [IN]:	�ص�ꥹ��λ�ä�����
	* 
	* @retval    0: ��K��
	* @retval    ������:           ����`
	*
	* @note �����
	* ����k�ء����ĵأ������ĵأ������ĵأ���Ŀ�ĵأ�
	* �Έ��ϡ�
	* pList = {0, 1, 2, 3, 4} �ǁK���椨�o��
	* pList = {0, 3, 2, 1, 4} �ǡ����ĵأ��ȣ�������椨
	* pList = {0, 2, 1, 3, 4} �ǡ����ĵأ��򣲤�����椨
	* note : the above list{0,2,1,3,4}==>����k�ء����ĵأ������ĵأ������ĵأ���Ŀ�ĵأ�
	* 
	* @note
	* ���¤Τ褦��������Ǻ��Ф줿���Ϥϡ�����`�򷵤���
	* �ꥹ�Ȥ��󤭤����Ф�ɤ��줿����
	* �ꥹ�Ȥ��С�������Ф�ɤ��줿����
	* ���k�ؤ��ƄӤ��褦�Ȥ�������
	* ͨ�^�g�ߤεص���ƄӤ��褦�Ȥ�������
	* ����IC�����IC���ǰ�ˤ��褦�Ȥ�������	
	*/
	public native int MovePathPoint(int NumPoint, int[] pList);
	
	/**
	* ָ������̽���ص㥪�֥������Ȥ�̽���ص�ꥹ�Ȥ��������
    *�����`�Щ`��`�ɣ�
    *
    * @param	DestNum     [IN]:    �ص�ꥹ��λ��
    * 	
	* @retval	0:    ��K��
	* @retval	������:              ����`
    *
	* @note
	* DestNum:0,1,2......,N ==> PathPoint: Original Start,Via1,Via2......,GoalPoint ��
	* �ص����� GetNumPathPoint() ��ȡ�ä��뤳�ȡ�
	*/
	public native int DeletePathPoint(int DestNum);
	
	/**
	 * ��ʾ�õص�ꥹ�Ȥεص����ȡ�ä���
	 * @param ObjID		̽�����֥�������ID
	 * @param Option	UIC_PT_NUM_POINT_ALL, ...
	 * @return	number of path points.
	 */
	public native int GetNumPathPoint(int ObjID, int Option);
	
	/**
	 * Get the  area info of one route.
	 * @param ObjID		̽�����֥�������ID
	 * @return	[0]&[1]: lonlat of right-top, [2]&[3]:lonlat of left-bottom.
	 */
	public native long[] GetRouteArea(int ObjID);
	
	public native long[] GetCenterLonLat(int ObjID, int param, boolean indexflag);
	
	public native String GetPointName(int ObjID, int param, boolean indexflag);
	
	/**
	 * start demo run.
	 * @param runtimes, if value < 0, demo run repeatedly.
	 */
	public native void StartDemoDriving(int runtimes);
	
	/**
	 * Get distance of specified route.
	 * @param ObjID		Path find object ID (UIC_PT_FIND_OBJ_UI,...).
	 * @param RouteNo	0,1,2...5
	 * @return	unit: meter.
	 */
	public native long GetRouteDistance(int ObjID, int RouteNo);
	
	/**
	 * Get required time of specified route.
	 * @param ObjID
	 * @param RouteNo
	 * @return
	 */
	public native long GetRouteTime(int ObjID, int RouteNo);
	
	public native int SetStartSpecify(boolean bValue);
	
	public native boolean IsStartSpecify();
	
	public native int InitRouteInfo();
	
	/**
	* UI�����̽������ֹ����
	*
	* @param	�ʤ�
	* @return	�ʤ�
	*/
	public native void AbortUIFinding();
	
	/**
     * Pass the path of the file of the route loading from web server  
     *
     *
     * @param FilePath
     * @return �ʤ�
 	 */
	public native void SetDownloadFilePath(String FilePath);
	
	public native void setRouteCondition(int condition);
	public native void setAvoidItem(int item);

	public native void registerUnKnownPlaceNameStr(String str);
}
