package com.billionav.voicerecog;

import java.util.List;

/**
 * it is used to notify UI events
 */
public interface VrListener {
	public static final int TYPE_HOME 	= 1;
	public static final int TYPE_CORP 	= 2;
	
	/**
	 * It will be called when VR go to registered POI,such as home,corp
	 * @param type POI type,such as home,corp
	 */
	void onVrGotoRegPOI(int type);
	
	/**
	 * It will be called when VR decide return VR main UI
	 * @param clear whether clear previous dialog history
	 */
	void onVrSwitchUI(boolean clear);
	
	/**
	 * It will be called when VR decide goto the selected POI
	 * @param poi POI target
	 */
	void onVrGotoTarget(POIInfo poi, List<POIInfo> wayPoints);
	
	/**
	 * It will be called when VR get POI search results
	 * @param results POI result list
	 */
	void onPOIResults(List<POIInfo> results);
	
	/**
	 * it will be called when VR mode start
	 */
	void onVrStarted();
	
	/**
	 * It will be called when VR request zoom in/out map
	 * @param zoomIn Zoom In map if it's true.
	 * 
	 * @return true if operation is success
	 */
	boolean onVrZoomMap(boolean zoomIn);
	
	/**
	 * it will be called when VR mode exit is complete
	 */
	void onVrTerminated(boolean back);
	
	void onContactResults(List<ContactInfo> results);
}

