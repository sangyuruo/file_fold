package com.billionav.navi.dataupdate;


public class DataVersionInfo{   
    //private int    m_formatVersion;       //��ʽ�汾
    //private int    m_curDataVersion;      //��ǰ���ݰ汾
    //private String m_mapDataVersion;      //��ͼ���ݰ汾��Ϣ
    //private String m_curOrbisDataVersion; //��ǰOrbis���ݰ汾��Ϣ
    private String m_checkOfDrawingNumber; //��ͼ��   
    private String m_publishingNumber;     //�����   
    	
	public DataVersionInfo(){	
		/*
        	m_formatVersion = 0; 
        	m_curDataVersion = 0;    
        	m_mapDataVersion = new String();    
        	m_curOrbisDataVersion = new String();  */
        	m_checkOfDrawingNumber = new String(); 
        	m_publishingNumber = new String(); 
	}
	/*	
	public int getFormatVersion(){
		return m_formatVersion;
	}
	
	public void setFormatVersion( int formatVersion ){
		m_formatVersion = formatVersion;
	}
	
	public int getCurDataVersion(){
		return m_curDataVersion;
	}
	
	public void setCurDataVersion( int curDataVersion ){
		m_curDataVersion = curDataVersion;
	}

    public String getMapDataVersion(){
		return m_mapDataVersion;
	}
	
	public void setMapDataVersion( String mapDataVersion ){
		if( null != mapDataVersion ) {
			m_mapDataVersion = mapDataVersion;			
		}		
	}

	public String getCurOrbisDataVersion(){
		return m_curOrbisDataVersion;
	}
	
	public void setCurOrbisDataVersion( String curOrbisDataVersion ){
		if( null != curOrbisDataVersion ) {
			m_curOrbisDataVersion = curOrbisDataVersion;		
		}		
	}
	*/
	public String getCheckOfDrawingNumber(){
		return m_checkOfDrawingNumber;
	}
	
	public void setCheckOfDrawingNumber( String checkOfDrawingNumber ){
		if( null != checkOfDrawingNumber ) {
			m_checkOfDrawingNumber = checkOfDrawingNumber;		
		}		
	}
	
	public String getPublishingNumber(){
		return m_publishingNumber;
	}
	
	public void setPublishingNumber( String publishingNumber ){
		if( null != publishingNumber ) {
			m_publishingNumber = publishingNumber;	
		}		
	}
}
