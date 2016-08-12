package com.billionav.jni;


public class jniCalcDistance {
	static public class MapLonLat {
		public long	Longitude;
	    public long	Latitude;
	}
	static public class PreciseDistance {
		public long mDistance;
		public int  mDirection;
	}

	public static native int CalcPreciseDistance(long orgLog, long orgLat, 
			long destLog, long destLat, long[] outParams);

	public static int CalcPreciseDistance(final MapLonLat org, final MapLonLat dest, 
			PreciseDistance pd) {
		long[] buff = new long[2];
			
		int rtn = CalcPreciseDistance(org.Longitude, org.Latitude,
				dest.Longitude, dest.Latitude, buff);
		pd.mDistance = buff[0];
		pd.mDirection = (int) buff[1];

		return rtn;
	}
}
