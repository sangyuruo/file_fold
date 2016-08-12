package com.billionav.navi.uicommon;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.billionav.DRIR.Upload.UploadHandler;
import com.billionav.DRIR.jni.jniDRIR_MainControl;
import com.billionav.jni.FileSystemJNI;

public class UIC_ARVideoCommon {

	private static boolean isNeedReresh = false;
    private static boolean isAllSelected = false;
	private static final String[] videoSuffix = new String[]{"3gp"};
	private static final String[] picSuffix = new String[]{"jpg"};
	public final static String DRIR = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH)+"/";
	public static  ArrayList<VideoInfo> getVideoInfoes(Context context){

		ArrayList<VideoInfo> array0  = getALLVideoInfoes(getDMPath(),UploadHandler.UPLOAD_FILE_TYPE_DM,context);
		ArrayList<VideoInfo> array1 = getALLVideoInfoes(getDRPath(),UploadHandler.UPLOAD_FILE_TYPE_DR,context); 
		ArrayList<VideoInfo> array2 = getALLPicInfoes(getPICPath(), UploadHandler.UPLOAD_FILE_TYPE_PIC,context);
		array0.addAll(array1);
		array0.addAll(array2);
		Collections.sort(array0);
		return array0;
	}
	public static  void setIsAllSelected(boolean isAll){
		isAllSelected = isAll;
	}
	public static boolean getIsAllSelected(){
		return isAllSelected;
	}
	public static void setIsNeedReresh(boolean isNeed){
		isNeedReresh = isNeed;
	}
	public static boolean getIsNeedReresh(){
		return isNeedReresh;
	}
	public static String getDMPath(){
		return DRIR+jniDRIR_MainControl.DRIRGetDMFilePath();
	}
	
	public static String getDRPath(){
		return DRIR+jniDRIR_MainControl.DRIRGetDRFilePath();
	}
	public static String getPICPath(){
		return DRIR+jniDRIR_MainControl.DRIRGetPicFilePath();
		
	}
	
	public static void updateVideoInfoes(Context context,ArrayList<VideoInfo> videos,boolean isDm){
		ArrayList<VideoInfo> array0 = null;
		if(isDm){
			 array0  = getALLVideoInfoes(getDMPath(),UploadHandler.UPLOAD_FILE_TYPE_DM,context);
		}else{
			array0 = getALLVideoInfoes(getDRPath(), UploadHandler.UPLOAD_FILE_TYPE_DR,context);
		}
		
		for(VideoInfo video : array0){
			if(!videos.contains(video)){
				videos.add(video);
			}
		}
		Collections.sort(videos);		
	}
	
	
	public static ArrayList<VideoInfo> getALLVideoInfoes(String path,int type,Context context){
		File file = new File(path);
		ArrayList<VideoInfo> array = new ArrayList<UIC_ARVideoCommon.VideoInfo>();
		if( file.exists() && file.isDirectory()){
			for (File f : file.listFiles()) {
				if(f.isFile() && isVideoFile(f)){
					if(f.length()>0){
						VideoInfo vinfo = new VideoInfo(f.getPath(),f.getName(),new Date(f.lastModified()),f.length(),type,context);
						array.add(vinfo);
					}
				}else if(f.isDirectory()){
					ArrayList<VideoInfo> arr = getALLVideoInfoes(f.getPath(),type,context);
					array.addAll(arr);
				}
			}
		}
		
		return array;	
	}
	public static ArrayList<VideoInfo> getALLPicInfoes(String path,int type,Context context){
		File file = new File(path);
		ArrayList<VideoInfo> array = new ArrayList<UIC_ARVideoCommon.VideoInfo>();
		if( file.exists() && file.isDirectory()){
			for (File f : file.listFiles()) {
				if(f.isFile() && isPicFile(f)){
					if(f.length()>0){
						VideoInfo vinfo = new VideoInfo(f.getPath(),f.getName(),new Date(f.lastModified()),f.length(),type,context);
						array.add(vinfo);
					}
				}else if(f.isDirectory()){
					ArrayList<VideoInfo> arr = getALLVideoInfoes(f.getPath(),type,context);
					array.addAll(arr);
				}
			}
		}	
		return array;	
	}
	
	private static boolean isVideoFile(File file){
		String fileName = file.getName();
		
		int dotIndex = fileName.lastIndexOf(".");
		
		if(dotIndex > -1 && dotIndex <  fileName.length()-1 ){
			String suffix = fileName.substring(dotIndex+1);
			for(String str :videoSuffix){
				if(suffix.equals(str)){
					return true;
				}
			}
		}
		return false;	
	}
	private static boolean isPicFile(File file){
		String fileName = file.getName();
		
		int dotIndex = fileName.lastIndexOf(".");
		
		if(dotIndex > -1 && dotIndex <  fileName.length()-1 ){
			String suffix = fileName.substring(dotIndex+1);
			for(String str :picSuffix){
				if(suffix.equals(str)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static float b2m(long b){ 
		float fm = b*1.0f/(1024*1024);
		
		int im = (int)(fm*10 + 0.5f);
		
		return im*1.0f/10;
	}
	
	public static boolean deleteFiles(String path){
		File f = new File(path);
		if(f.exists() && f.isFile()){
			return  f.delete();
		}	
		return false;
	}
	public static String strEscape(String value){
		StringBuilder sb =new StringBuilder();
		for(int i =0;i<value.length();i++){
			if(value.charAt(i) == '/' ){
				if(i != value.length()-1){
					sb.append('/');
					sb.append('/');
				}
			}
			else{
				sb.append(value.charAt(i));
			}
		}
		
		return sb.toString();
	}
	
 
	 public static Bitmap getImageThumbnail(String imagePath, int width, int height) {  
	        Bitmap bitmap = null;  
	        BitmapFactory.Options options = new BitmapFactory.Options();  
	        options.inJustDecodeBounds = true;  
	        options.inJustDecodeBounds = false; //  false  
	        int h = options.outHeight;  
	        int w = options.outWidth;  
	        int beWidth = w / width;  
	        int beHeight = h / height;  
	        int be = 1;  
	        if (beWidth < beHeight) {  
	            be = beWidth;  
	        } else {  
	            be = beHeight;  
	        }  
	        if (be <= 0) {  
	            be = 1;  
	        }  
	        options.inSampleSize = be;  
	        bitmap = BitmapFactory.decodeFile(imagePath, options);  
	        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
	                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	        return bitmap;  
	    }  
	
	public static VideoInfo findItem(ArrayList<VideoInfo> list,String filePath){
		for(int i=0;i<list.size();i++){
			VideoInfo v = list.get(i);
			if(v.path.equals(filePath)){
				return v;
			}
		}
		return null;
	}
	
	
	public static class VideoInfo implements Comparable<VideoInfo>{
		private String path;
		private String name;
		private Date   date;
		private long  size;
		private Context context;
		private int type;
		private int status;
		private boolean isselected = false;
		 /* UPLOAD_FILE_TYPE_DM = 0;
		  UPLOAD_FILE_TYPE_DR = 1;
          UPLOAD_FILE_TYPE_PIC = 2;
          UPLOAD_FILE_TYPE_LOG = 3;*/


		
		public VideoInfo(String path ,String name,Date date,long size,int type,Context context){
			this.path = path;
			this.name = name;
			this.date = date;
			this.size = size;
			this.context = context;
			this.type = type;
			this.status = -1;
		}
		
		@Override
		public int compareTo(VideoInfo another) {
			long times = date.getTime() - another.getDate().getTime();
			
			if(times > 0){
				return -1;
			}else if(times <0){
				return 1;
			}else{
				return 0;
			}
		}
		
		@Override
		public boolean equals(Object o) {
			if(o instanceof VideoInfo){
				VideoInfo v = (VideoInfo)o;
				return path.equals(v.getPath());
			}
			return false;
		}
		
		public String getPath() {
			return path;
		}

		public String getName() {
			return name;
		}

		public Date getDate() {
			return date;
		}

		public long getSize() {
			return size;
		}
		
		public int getType(){
			return type;
		}
		public int getStatus(){
			return status;
		}
		public void setIsSelected(){
			isselected = !isselected;
		}
		public void setSelected(){
			isselected = true;
		}
		public boolean getIsSelected(){
			return isselected;
		}
		public void setStatus(int status){
			this.status = status;
		}
	}

	private static UIC_ARVideoCommon instance;
	
	private String pathOfWaitPaly;
	private UIC_ARVideoCommon(){
		
	}
	
	public static  UIC_ARVideoCommon getInstance(){
		if(instance == null){
			instance = new UIC_ARVideoCommon();
		}

		return instance;
	}
	
	public void setPathOfWaitPlay(String path){
		this.pathOfWaitPaly = path;
	}
	
	public String getPathOfWaitPlay(){
		return pathOfWaitPaly;
	}
	
	
	
}
