package com.billionav.navi.gps;

import com.billionav.jni.jniLocInfor;

public class GpsIF {
	
	public static final int MAX_GPS_SATELLITE = 32;

	
	//get the number of the satellite used for fix;
	//the max number is MAX_GPS_SATELLITE(32)
	public int GetSatelliteNum()
	{
		return jniLocInfor.getLocInfor().numSatellite;
	}
	
	//get the time of to first fix.(milliseconds)
	public float GetFirstFixTime()
	{
		return jniLocInfor.getLocInfor().ttff;
	}
	
	//get the SatelliteID by index;
	//the index must between 0 and the number of the satellite used for fix;(GetSatelliteNum)
	
	public int GetSatelliteID(int index)
	{
		if (index >= 0 && index < MAX_GPS_SATELLITE)
		{
		    return jniLocInfor.getLocInfor().SatelliteID[index];
		}
		else
		{
		    return 0;
		}
	}
	
	//get the SignalStrength by index;
	//the index must between 0 and the number of the satellite used for fix;(GetSatelliteNum)
	public float GetSignalStrength(int index)
	{
		if (index >= 0 && index < MAX_GPS_SATELLITE)
		{
		    return jniLocInfor.getLocInfor().SignalStrength[index];
		}
		else
		{
		    return 0;
		}
	}
	
	//get the SignalStrength by index;
	//the index must between 0 and the number of the satellite used for fix;(GetSatelliteNum)
	public float GetAzimuth(int index)
	{
		if (index >= 0 && index < MAX_GPS_SATELLITE)
		{
		    return jniLocInfor.getLocInfor().Azimuth[index];
		}
		else
		{
		    return 0;
		}
	}
	
	//get the SignalStrength by index;
	//the index must between 0 and the number of the satellite used for fix;(GetSatelliteNum)
	public float GetElevationAngle(int index)
	{
		if (index >= 0 && index < MAX_GPS_SATELLITE)
		{
		    return jniLocInfor.getLocInfor().ElevationAngle[index];
		}
		else
		{
		    return 0;
		}
	}
	
	//get used In Fix flag by index;
	//true if the satellite was used by the GPS engine when calculating the most recent GPS fix.
	//the index must between 0 and the number of the satellite used for fix;(GetSatelliteNum)
	public boolean GetBeUsedFlag(int index)
	{
		if (index >= 0 && index < MAX_GPS_SATELLITE)
		{
		    return jniLocInfor.getLocInfor().usedInFix[index];
		}
		else
		{
		    return false;
		}
	}

	
	//get has almanace flag by index;
	//true if the GPS engine has almanac data for the satellite.
	//the index must between 0 and the number of the satellite used for fix;(GetSatelliteNum)
	public boolean GetHasAlmanacFlag(int index)
	{
		if (index >= 0 && index < MAX_GPS_SATELLITE)
		{
		    return jniLocInfor.getLocInfor().hasAlmanac[index];
		}
		else
		{
		    return false;
		}
	}
	
	//get has Ephemeris flag by index;
	//true if the GPS engine has almanac data for the satellite.
	//the index must between 0 and the number of the satellite used for fix;(GetSatelliteNum)
	public boolean GetHasEphemerisFlag(int index)
	{
		
		if (index >= 0 && index < MAX_GPS_SATELLITE)
		{
		    return jniLocInfor.getLocInfor().hasEphemeris[index];
		}
		else
		{
		    return false;
		}
	}
}

