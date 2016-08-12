package com.billionav.navi.trafficrealtime;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.xmlpull.v1.XmlPullParserException;


import android.util.Log;


public class Test {
	
	public static void test(InputStream input)
	{
		  long startTime = System.currentTimeMillis();
	        
	        ArrayList<RoadwayFlowItemInfo> roadwayInfoList = null;
	        //InputStream input = getResources().openRawResource(R.raw.realtimeflow);
	        try {
				TrafficXmlParser parser = new TrafficXmlParser();
				parser.setInput(input,"UTF-8");
				roadwayInfoList = parser.parse();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				//Log.i(TrafficXmlParser.TagName,e.getMessage());
				Log.i(TrafficXmlParser.TagName,"XmlPullParserException occurrs");
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				//Log.i(TrafficXmlParser.TagName,e.getMessage());
				Log.i(TrafficXmlParser.TagName,"IOException occurrs");
				e.printStackTrace();
			}
			
			long endTime = System.currentTimeMillis();
			
			Log.w(TrafficXmlParser.TagName,"spend time totally: " + (double)(endTime - startTime)/1000 +  "s");
			Log.w(TrafficXmlParser.TagName,"resolved " + roadwayInfoList.size() + " roadway_flow_items");
			
			Iterator it = roadwayInfoList.iterator();
			while(it.hasNext())
			{
				RoadwayFlowItemInfo item = (RoadwayFlowItemInfo)it.next();
				item.toString();
				//Log.v(TrafficXmlParser.TagName,item.toString());
			}
	}

}
