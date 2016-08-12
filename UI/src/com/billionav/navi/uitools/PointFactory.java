//package com.billionav.navi.uitools;
//
//
//import com.billionav.jni.jniLocationIF;
//import com.billionav.jni.jniCalcDistance.MapLonLat;
//import com.billionav.jni.jniLocationIF.CLonLat;
//import com.billionav.jni.UISearchControlJNI.AL_LonLat;
//
//public class PointFactory {
////	private AL_LonLat a;
////	private CLonLat b;
////	private MapLonLat c;
////	private int[] d;
////	private long[] e;
//
//	@SuppressWarnings("unchecked")
//	public static <T> T getLonlat(Object o, Class<T> cls){
//		if(o.getClass().equals(cls)) {
//			return (T) o;
//		}
//		
//		long x = -1;
//		long y = -1;
//		
//		if(o instanceof int[]) {
//			int[] temp = (int[]) o;
//			x = temp[0];
//			y = temp[1];
//		} else if(o instanceof long[]){
//			long[] temp = (long[]) o;
//			x = temp[0];
//			y = temp[1];
//		} else if(o instanceof AL_LonLat){
//			AL_LonLat temp = (AL_LonLat) o;
//			x = (Long) ReflectTools.getFiledValue(temp, "lon");
//			y = (Long) ReflectTools.getFiledValue(temp, "lat");
//		} else if(o instanceof CLonLat){
//			CLonLat temp = (CLonLat) o;
//			x = temp.Longitude;
//			y = temp.Latitude;
//		} else if(o instanceof MapLonLat){
//			MapLonLat temp = (MapLonLat) o;
//			x = temp.Longitude;
//			y = temp.Latitude;
//		} else {
//			throw new IllegalArgumentException("argument class must be one of AL_LonLat, CLonLat, MapLonLat, int[], long[]");
//		}
//		
//		if(cls.equals(int[].class)) {
//			return (T) new int[]{(int)x, (int)y};
//		} else if(cls.equals(long[].class)){
//			return (T) new long[]{x, y};
//		} else if(cls.equals(AL_LonLat.class)) {
//			return (T) new AL_LonLat(x, y);
//		} else if(cls.equals(CLonLat.class)) {
//			CLonLat lonlat = new jniLocationIF().new CLonLat();
//			lonlat.Longitude = x;
//			lonlat.Latitude = y;
//			return (T) new jniLocationIF().new CLonLat();
//		} else if(cls.equals(MapLonLat.class)) {
//			MapLonLat lonlat = new MapLonLat();
//			lonlat.Longitude = x;
//			lonlat.Latitude = y;
//			return (T) lonlat;
//		}
//		
//		return null;
//	}
//	
//	public static AL_LonLat getAL_LonLat(Object o){
//		return getLonlat(o, AL_LonLat.class);
//	}
//	
//	public static CLonLat getCLonLat(Object o){
//		return getLonlat(o, CLonLat.class);
//	}
//	
//	public static MapLonLat getMapLonLat(Object o){
//		return getLonlat(o, MapLonLat.class);
//	}
//	
//	public static int[] getIntArrayLonLat(Object o){
//		return getLonlat(o, int[].class);
//	}
//	
//	public static long[] getLongArrayLonLat(Object o){
//		return getLonlat(o, long[].class);
//	}
//}
