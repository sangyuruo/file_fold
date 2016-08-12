package com.billionav.navi.system;

import java.io.*;

public class VersionControl {
	
	static private String s_AplVersion = null;
	static private String s_MapdataVersion = null;
	
	static private String filename = "/sdcard/version.txt";
	
	static public void setAplVerion(String ver) {
		if(s_AplVersion != ver)
		{
			s_AplVersion = ver;
			updateVersionInfo();
		}
	};
	
	static public String getAplVerion()
	{
		if( null == s_AplVersion)
		{
			loadVersionInfo();
		}
		return s_AplVersion;
	};
	
	static public void setMapdataVersion(String ver)
	{
		if(s_MapdataVersion != ver)
		{
			s_MapdataVersion = ver;
			updateVersionInfo();
		}
	};
	
	static public String getMapdataVersion()
	{
		if( null == s_MapdataVersion)
		{
			loadVersionInfo();
		}
		return s_MapdataVersion;
	};
	
	static private void createVersionFile()
	{
		File versionFile = new File(filename);
		try 
		{
			versionFile.createNewFile();
		}
		catch (IOException e2)
		{
			// log
		}
	}
	
	static private void updateVersionInfo()
	{
		BufferedWriter bw = null;
		// open output stream
		try 
		{
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		} 
		catch (FileNotFoundException e1 )
		{
			createVersionFile();
		}
		
		try 
		{
			bw.write(s_AplVersion);
			bw.write("\n");
			bw.write(s_MapdataVersion);
			bw.write("\n");
			bw.close();
		}
		catch (IOException e2)
		{
			
		}
	}
	
	static private void loadVersionInfo()
	{
		BufferedReader br = null;
		String data = null;
		
		// open input stream
		try 
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		} 
		catch (FileNotFoundException e1 )
		{
			createVersionFile();
		}
		
		try 
		{
			if ((data = br.readLine())!= null)
			{
				s_AplVersion = data;
			}

			if ((data = br.readLine())!= null)
			{
				s_MapdataVersion = data;
			}
			br.close();
		}
		catch (IOException e2)
		{
			
		}
	}
}
