package com.billionav.navi.uicommon;

import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.weibo.net.AccessToken;
import com.weibo.net.AsyncWeiboRunner;
import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class UIC_WeiboCommon{
	
	public static boolean publishWeibo(String imagePath, String content, RequestListener listener) {
		 UserControl_UserInfo userInfo = getUserInfo();
		 if(userInfo == null) {
			 return false;
		 }
		 int[] lonLat1 = UIMapControlJNI.GetCenterLonLat();
		 double[] worldLonLat = UIMapControlJNI.MapConvertLonlatToWorld(lonLat1[0], lonLat1[1]);
			AsynShareThumTask task = new AsynShareThumTask(
					NSViewManager.GetViewManager(),
					userInfo,
					worldLonLat, 
					imagePath, 
					content, 
					listener);
			task.execute(0);
		return true;
	}
	
	public static boolean publishWeibo(UserControl_UserInfo userInfo, String imagePath, String content, RequestListener listener) {
		 int[] lonLat1 = UIMapControlJNI.GetCenterLonLat();
		 double[] worldLonLat = UIMapControlJNI.MapConvertLonlatToWorld(lonLat1[0], lonLat1[1]);
			AsynShareThumTask task = new AsynShareThumTask(
					NSViewManager.GetViewManager(),
					userInfo,
					worldLonLat, 
					imagePath, 
					content, 
					listener);
			task.execute(0);
		return true;
	}
	
	public static UserControl_UserInfo getUserInfo() {
		if(!UserControl_ManagerIF.Instance().HasLogin()){
			return null;
		}
		
		UserControl_UserInfo userInfo = UserControl_ManagerIF.Instance().GetStoredUserInfo();
		if(userInfo == null) {
			return null;
		}
		
		String token = userInfo.m_strWeiBoToken[0];
		
		if(TextUtils.isEmpty(token)) {
			return null;
		}
		
		String expires = userInfo.m_strWeiBoVaildPeriod[0];
		
		if(TextUtils.isEmpty(expires)) {
			return null;
		}
		long temp_time = 0;
		try{
			temp_time = Long.parseLong(expires);
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		if(System.currentTimeMillis() > temp_time) {
			return null;
		}
		
		return userInfo;
	}

	public static class AsynShareThumTask extends AsyncTask<Integer, Integer, Integer> {

		private String imagePath;
		private String content;
		private String token;
		private String expires;
		private double[] lonLat;
		private Context context;
		private RequestListener listener;
		
		private AsynShareThumTask(Context context,UserControl_UserInfo userInfo, double[] lonLat, String imagePath, String content,RequestListener listener){
			this.token = userInfo.m_strWeiBoToken[0];
			this.expires = userInfo.m_strWeiBoVaildPeriod[0];
			this.lonLat = lonLat;
			this.imagePath = imagePath;
			this.content = content;
			this.context = context;
			this.listener = listener;
		}
		
		@Override
		protected synchronized Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			   Weibo weibo = Weibo.getInstance();
				AccessToken accessToken = new AccessToken(
						token,
						"9c1220859eed9e4efbca3ffb051750a8");
				accessToken.setExpiresIn(Long.valueOf(expires));
				weibo.setAccessToken(accessToken);

				if (!TextUtils.isEmpty(imagePath)) {
							try {
								
//								upload(weibo, weibo.getAppKey(),
//										pic_path, extraInfo
//												.getText().toString(),
//										String.valueOf(lonLat[0]),
//										String.valueOf(lonLat[1]));
							upload(weibo, weibo.getAppKey(),
									imagePath, content,
										String.valueOf(lonLat[0]),
										String.valueOf(lonLat[1]));
//								upload(weibo, weibo.getAppKey(),
//										pic_path, getString(R.string.STR_COM_016)+String.valueOf(System.currentTimeMillis()),
//										"",
//										"");
								
							} catch (WeiboException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

				} else {
							try {
								update(weibo, token,
										content, String
												.valueOf(lonLat[0]),
										String.valueOf(lonLat[1]));
//								update(weibo, token,
//										getString(R.string.STR_COM_016)+String.valueOf(System.currentTimeMillis()), "","");
							} catch (WeiboException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

			return null;
		}
		
		 private String upload(Weibo weibo, String source, String file, String status, String lon,
		            String lat) throws WeiboException {
		        WeiboParameters bundle = new WeiboParameters();
		        bundle.add("source", source);
		        bundle.add("pic", file);
		        bundle.add("status", status);
		        if (!TextUtils.isEmpty(lon)) {
		            bundle.add("lon", lon);
		        }
		        if (!TextUtils.isEmpty(lat)) {
		            bundle.add("lat", lat);
		        }
		        String rlt = "";
		        String url = Weibo.SERVER + "statuses/upload.json";
		        AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
		        weiboRunner.request(context, url, bundle, Utility.HTTPMETHOD_POST, listener);
	          
		        return rlt;
		    }

		    private String update(Weibo weibo, String source, String status, String lon, String lat)
		            throws MalformedURLException, IOException, WeiboException {
		        WeiboParameters bundle = new WeiboParameters();
		        bundle.add("source", source);
		       
		        bundle.add("status", status);
		        if (!TextUtils.isEmpty(lon)) {
		            bundle.add("lon", lon);
		        }
		        if (!TextUtils.isEmpty(lat)) {
		            bundle.add("lat", lat);
		        }
		        String rlt = "";
		        String url = Weibo.SERVER + "statuses/update.json";
		        AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
		        weiboRunner.request(context, url, bundle, Utility.HTTPMETHOD_POST, listener);
		     
		        return rlt;
		    }

	}
	
}
