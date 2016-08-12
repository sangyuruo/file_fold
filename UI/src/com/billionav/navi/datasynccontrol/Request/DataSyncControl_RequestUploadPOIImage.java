package com.billionav.navi.datasynccontrol.Request;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.billionav.navi.net.PMultiPart;
import com.billionav.navi.net.PostData;

public class DataSyncControl_RequestUploadPOIImage extends DataSyncControl_RequestFileBase{
	private String strPOIurl = "";		//TODO SET THE POIURL
	private String strPOISerUrl = "";
	
	public DataSyncControl_RequestUploadPOIImage(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean uploadImage(String strUUID, String strRecordKind, String strImgPath) {
		String url = strPOISerUrl + strPOIurl;
		setM_bAuth(true);
		List<PMultiPart> listData = new ArrayList<PMultiPart>();
		
		PMultiPart cUUIDItem = new PMultiPart(PMultiPart.MULTI_TYPE_STRING, "uuid", strUUID, "text/plain");
		listData.add(cUUIDItem);
		PMultiPart cRecordItem = new PMultiPart(PMultiPart.MULTI_TYPE_STRING, "recordkind", strRecordKind, "text/plain");
		listData.add(cRecordItem);
		byte[] image = getBytesFromFile(strImgPath);
		if(null != image)
		{
			PMultiPart cImageItem = new PMultiPart("image", image, "application/octet-stream");
			listData.add(cImageItem);
		}
		
		PostData post = new PostData();
		post.setPostMultiData(listData);
		
		SendRequestByPostResponseStream(url,post);
		return true;
	}

	public String getStrPOIurl() {
		return strPOIurl;
	}

	public void setStrPOIurl(String strPOIurl) {
		this.strPOIurl = strPOIurl;
	}

	public String getStrPOISerUrl() {
		return strPOISerUrl;
	}

	public void setStrPOISerUrl(String strPOISerUrl) {
		this.strPOISerUrl = strPOISerUrl;
	}
	
	private byte[] getBytesFromFile(String filePath) {
		FileInputStream in;
		try {
			in = new FileInputStream(filePath);

			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			//System.out.println("bytes available:" + in.available());
			byte[] temp = new byte[1024];
			int size = 0;
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
			in.close();
			byte[] bytes = out.toByteArray();
			//System.out.println("bytes size got is:" + bytes.length);
			return bytes;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
