package com.billionav.jni;

import com.billionav.navi.uitools.SystemTools;

public class UISearchResultJNI {
	
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
	public UISearchResultJNI() {
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

	/**
	 * GetListItemGenreIDAt
	 * getter function of list item genre id
	 *
	 * @param   idx the item index in the list
	 * @return  list item name
	 */
	public native long GetListItemGenreIDAt(long idx, int listid);
	
	public int getDefaultGenreCount() {
		if(SystemTools.EDITION_LUXGEN.equals(SystemTools.getApkEdition())){
			return 9;
		}else{			
			return 8;
		}
	}

	/**
	 * GetListItemGenreIDAt
	 * getter function of list item genre id
	 *
	 * @param   idx the item index in the list
	 * @return  list item name
	 */
	public native boolean GetListItemHasNextFlag(long idx, int listid);
	
	/**
	 * GetListItemCount
	 * getter function of list item count
	 *
	 * @param  listid the id of specific list
	 * @return  list item count
	 */
	public native long GetListItemCount(int listid);
	
		
		
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

	/*-----------------------------for  TabInfo only----------------------------------------------------------------------*/
	
}
