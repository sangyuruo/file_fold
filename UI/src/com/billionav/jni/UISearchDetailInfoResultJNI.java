package com.billionav.jni;

public class UISearchDetailInfoResultJNI {
	
	private int m_type;

	public int GetType() {
		return m_type;
	}

	public void SetType(int m_type) {
		this.m_type = m_type;
	}
	/**
	 * Default constructor
	 */
	public UISearchDetailInfoResultJNI() {
		SetType(UISearchControlJNI.UIC_SCM_SRCH_TYPE_INVALID);
	}
	
	/**
	 * GetListItemNameAt
	 * getter function of list item name
	 *
	 * @param   idx the item index in the list
	 * @return  list item name
	 */
	public native String GetListItemNameAt(long idx, int listid);
	
	
	/*-----------------------------for  POIListItem only----------------------------------------------------------------------*/
	/**
	 * GetPOIListItemAddressAt
	 * getter function of list item address
	 *
	 * @param   idx the item index in the list
	 * @return  address
	 */
	public native String GetPOIListItemAddressAt(long idx, int listid);
	/**
	 * GetPOIListItemTelNoAt
	 * getter function of list item TelNo
	 *
	 * @param   idx the item index in the list
	 * @return  telephone number string
	 */
	public String GetPOIListItemTelNoAt(long idx, int listid){
		return GetPOIListItemTelNoAt(idx,0,listid);
	}
	/**
	 * GetPOIListItemTelNoAt
	 * getter function of list item TelNo
	 *
	 * @param   listidx the item index in the list
	 * @param   telNoIdx the item index in the phonelist
	 * @return  telephone number string
	 */
	public 	native String GetPOIListItemTelNoAt(long listidx, long telNoIdx, int listid);

	/**
	 * GetPOIListItemCommentAt
	 * getter function of list item Comment
	 *
	 * @param   idx the item index in the list
	 * @return  comment
	 */
	public native String GetPOIListItemCommentAt(long idx, int listid);
	/**
	 * GetPOIListItemDistanceAt
	 * getter function of list item distance (meter)
	 *
	 * @param  listid the id of specific list
	 * @return  distance
	 */
	public native long GetPOIListItemDistanceAt(long idx, int listid);



	/**
	 * GetPOIListItemOpeningTimeAt
	 * getter function of list item opening time
	 *
	 * @param  listid the id of specific list
	 * @return  opening time
	 */	
	public native String GetPOIListItemOpeningTimeAt(long idx, int listid);




	/**
	 * GetPOIListItemLonLatAt
	 * getter function of list item lonlat
	 *
	 * @param  listid the id of specific list
	 * @return  long[0]:Lon;long[1]:Lat;
	 */	
	public native long[] GetPOIListItemLonLatAt(long idx, int listid);

	
	/**
	 * GetTouchedPOIIndex
	 * getter function of touched poi index
	 *
	 * @param   VOID
	 * @return  touched poi index
	 */
	
	public native long GetTouchedMarkListCount();
	/*-----------to del start-------------------*/
	public long GetTouchedPOIIndex(){
		return GetTouchedMarkIndex(0);
	}
	public int GetTouchedMarkIndex(){
		return GetTouchedMarkIndex(0);
	}
	public int GetTouchedMarkType(){
		return GetTouchedMarkType(0);
	}
	public String GetTouchedMarkName(){
		return GetTouchedMarkName(0);
	}
	public long[] GetTouchedMarkLonLat(){
		return GetTouchedMarkLonLat(0);
	}
	/*-----------to del end-------------------*/
	public native int GetTouchedMarkIndex(long idx);
	public native int GetTouchedMarkCategory(long idx);
	public native int GetTouchedMarkType(long idx);
	public native String GetTouchedMarkName(long idx);
	public native String GetTouchedMarkAddress(long idx);
	public native String GetTouchedMarkTime(long idx);
	public native long[] GetTouchedMarkLonLat(long idx);
	
	public native String GetPinPointName();
	public native String GetPinPointAddress();
	public native String GetPinPointTel();
	public native long GetPinPointDistance();
	public native long[] GetPinPointLonLat();
}
