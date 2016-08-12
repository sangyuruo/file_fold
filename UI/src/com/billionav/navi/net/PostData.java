package com.billionav.navi.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import android.util.Log;

public class PostData
{
	public static final int POSTDATA_TYPE_ERROR = 0;
	public static final int POSTDATA_TYPE_NAMEVALUE = 1;
	public static final int POSTDATA_TYPE_STRING = 2;
	public static final int POSTDATA_TYPE_BUFFER = 3;
	public static final int POSTDATA_TYPE_FILE = 4;
	public static final int POSTDATA_TYPE_MULTIDATA = 5;

	
	private static final String TEXT_PLAIN = "text/plain";
	private static final String LINE_END = "\r\n";
	private static final String TWO_HYPHENS= "--";
	private static final String BOUNDARY= "/******************/";
	
	public PostData()
	{
		m_iType = 0;
		m_cEntity = null;
	}
	
	public int getPostDataType()
	{
		return m_iType;
	}
	
	public String getBoundary()
	{
		return BOUNDARY;
	}

	public void setPostData(List<NameValuePair> data)
	{
		try {
			m_cEntity = new UrlEncodedFormEntity(data,HTTP.UTF_8);
			m_iType = POSTDATA_TYPE_NAMEVALUE;
		}     		 
		catch(Exception e)
		{
			PNetLog.e("setPost NameValuePair Error:"+e.getMessage());
		} 
	}
	
	public void setPostData(byte buf[])
	{
		m_cEntity = new ByteArrayEntity(buf);
		m_iType = POSTDATA_TYPE_BUFFER;
	}
	
	public void setPostFile(String filename)
	{
		try {
			File file = new File(filename);
			m_cEntity = new FileEntity(file, "application/octet-stream");
			//m_cEntity = new FileEntity(file, "text/plain; charset=\"UTF-8\"");
			m_iType = POSTDATA_TYPE_FILE;
		}     		 
		catch(Exception e)
		{
			PNetLog.e("setPost File Error:"+e.getMessage());
		}
	}
	
	/*
	public void setPostMultiData(int type[], String[] title, String[] content, String[] encoding )
	{
    	List<PMultiPart> params = new ArrayList<PMultiPart>();
    	for(int i =0; i< title.length ; i++)
    	{
    		params.add(new PMultiPart(type[i], title[i], content[i], encoding[i]));
    	}
    	setPostMultiData(params);
	}
	*/
	
	public void setPostMultiData(List<PMultiPart> data)
	{
    	if (null == data || 0 == data.size()) {
    		return;
    	}
    	try {

        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	DataOutputStream dos = new DataOutputStream(bos);

	    	for (int i=0; i<data.size(); ++i) {
	    		PMultiPart item = data.get(i);
	    		switch(item.getType())
	    		{
	    		case PMultiPart.MULTI_TYPE_STRING:
	    		case PMultiPart.MULTI_TYPE_BUFFER:
	    			MultiString(dos, item.getTitle(), item.getByteContent(), item.getEncoding());
	    			break;
	    		case PMultiPart.MULTI_TYPE_FILE:
	    			MultiFile(dos, item.getTitle(), item.getFileName(), item.getEncoding(), 0, Integer.MAX_VALUE);
	    			break;
	    		case PMultiPart.MULTI_TYPE_FILE_PART:
	    			MultiFile(dos, item.getTitle(), item.getFileName(), item.getEncoding(), item.getFileOffset(), item.getLength());
	    			break;
	    		case PMultiPart.MULTI_TYPE_FILE_BUF:
	    			MultiFileFromBuf(dos, item.getTitle(), item.getFileName(), item.getByteContent(), item.getEncoding());
	    			break;
	    		default:
	    			PNetLog.e("Item:"+i+" setPostMultiData Error Type:"+item.getType());
	    			break;
	    		}
	    	}
	    	dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END);
	    	dos.flush();
	    	
	    	m_cEntity = new ByteArrayEntity(bos.toByteArray());

	    	dos.close();
	    	
	   		m_iType = POSTDATA_TYPE_MULTIDATA;
    	}
   		catch (Exception e)
    	{
    		PNetLog.e("setPostMultiData Error:"+e.getMessage());
    	}
		
	}
	
	public HttpEntity getPostData()
	{
		return m_cEntity;
	}
	
	protected void MultiFile(DataOutputStream dos, String name, String fileName, String mineType, int offset, int length) throws IOException
	{
	  	File file = new File(fileName);
    	
	  	//FileInputStream fis = new FileInputStream(file);
	  	RandomAccessFile fis = new RandomAccessFile(file, "r");
	  	
	  	fis.seek(offset);
    	
    	dos.writeBytes(TWO_HYPHENS+BOUNDARY+LINE_END);
    	dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" +
				file.getName()+"\"" + LINE_END);
    	
		if (null == mineType || mineType.length() < 1) {
			mineType = TEXT_PLAIN;
		}
			
		dos.writeBytes("Content-Type: " + mineType + LINE_END);
		dos.writeBytes("Content-Transfer-Encoding: binary" + LINE_END);
		dos.writeBytes(LINE_END);

		int bytesRead = 0;
		int bufsize = 8*1024;
		byte[] buffer = null;;
		int remain = length;
	    int read_size = Math.min(remain, bufsize);
	    buffer = new byte[read_size];
		while( (bytesRead = fis.read(buffer, 0, read_size)) != -1 )
		{
			dos.write(buffer, 0, bytesRead);
			//Log.e("NET", new String(buffer));
			remain = remain - bytesRead;
			if(remain <= 0)
			{
				break;
			}
			read_size = Math.min(remain, bufsize);
		    //buffer = new byte[read_size];
		}
		
		dos.writeBytes(LINE_END);
		dos.flush();
   		fis.close();

	}

	protected void MultiFileFromBuf(DataOutputStream dos, String name, String fileName, byte[] data, String mineType) throws IOException
	{
	  	File file = new File(fileName);
    	
    	dos.writeBytes(TWO_HYPHENS+BOUNDARY+LINE_END);
    	dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" +
				file.getName()+"\"" + LINE_END);
    	
		if (null == mineType || mineType.length() < 1) {
			mineType = TEXT_PLAIN;
		}
			
		dos.writeBytes("Content-Type: " + mineType + LINE_END);
		dos.writeBytes("Content-Transfer-Encoding: binary" + LINE_END);
		dos.writeBytes(LINE_END);
		
		dos.write(data);
		
		dos.writeBytes(LINE_END);
		dos.flush();
	}

	protected void MultiString(DataOutputStream dos, String name, byte[] text, String charsetName) throws IOException
	{
		dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
		dos.writeBytes("Content-Disposition: form-data; name=\"" 
				+name + "\"" + LINE_END);
		
		// Write content type
		if (null == charsetName || charsetName.length() < 1) {
			charsetName = TEXT_PLAIN;
		}
		dos.writeBytes("Content-Type: " + charsetName + LINE_END);
		dos.writeBytes(LINE_END);
		
		// Write text content
		dos.write(text);
		
		dos.writeBytes(LINE_END);
		dos.flush();
	}
	
	private HttpEntity m_cEntity;
	private int m_iType;
}