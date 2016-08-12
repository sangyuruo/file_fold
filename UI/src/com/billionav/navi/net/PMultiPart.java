package com.billionav.navi.net;

public class PMultiPart {
	
	public final static int MULTI_TYPE_STRING = 0;
	public final static int MULTI_TYPE_BUFFER = 1;
	public final static int MULTI_TYPE_FILE = 2;
	public final static int MULTI_TYPE_FILE_PART = 3;
	public final static int MULTI_TYPE_FILE_BUF = 4;
	
	public PMultiPart(int type, String title, String content, String encoding) 
	{
		switch(type)
		{
		case MULTI_TYPE_STRING:
		case MULTI_TYPE_BUFFER:
			m_bContent = content.getBytes();
			m_sFName = null;
			break;
		case MULTI_TYPE_FILE:
			m_sFName = content;
			m_bContent = null;
			break;
		default:
			PNetLog.e("PMultiPart Error Type:"+ type);
			break;
		}

		m_iType = type;
		m_sTitle = title;
		m_sEncoding = encoding;
		m_iOffset = 0;
		m_iLength = 0;
	}
	
	public PMultiPart(String title, byte[] buf, String encoding)
	{
		m_iType = MULTI_TYPE_STRING;
		m_bContent = buf;
		m_sFName = null;
		m_sTitle = title;
		m_sEncoding = encoding;
		m_iOffset = 0;
		m_iLength = 0;
	}
	
	public PMultiPart(String title, String filename, byte[] buf, String encoding)
	{
		m_iType = MULTI_TYPE_FILE_BUF;
		m_sTitle = title;
		m_sFName = filename;
		m_bContent = buf;
		m_sEncoding = encoding;
	}
	
	public PMultiPart(String title, String filename, String encoding, int offset, int length)
	{
		m_iType = MULTI_TYPE_FILE_PART;
		m_sTitle = title;
		m_sFName = filename;
		m_bContent = null;
		m_sEncoding = encoding;
		m_iOffset = offset;
		m_iLength = length;
	}
	
	public int getType()
	{
		return m_iType;
	}
	
	public String getTitle() {
		return m_sTitle;
	}
	
	public String getStringContent() {
		return new String(m_bContent);
	}
	
	public byte[] getByteContent() {
		return m_bContent;
	}
	
	public String getFileName()
	{
		return m_sFName;
	}
	
	public String getEncoding() {
		return m_sEncoding;
	}
	
	public int getFileOffset()
	{
		return m_iOffset;
	}
	
	public int getLength()
	{
		return m_iLength;
	}
	
	private int m_iType;
	private String m_sTitle;
	private String m_sFName;
	private byte[] m_bContent;
	private String m_sEncoding;
	private int m_iOffset;
	private int m_iLength;
}