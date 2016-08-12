package com.billionav.navi.trafficrealtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

/**
 * this class is used to parse XML file,and
 * extract information
 * @author wenjianjun
 *
 */
public class TrafficXmlParser {

	public static String TagName = "Traffic";
	private XmlPullParser parser;
	
	public TrafficXmlParser() throws XmlPullParserException
	{
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		parser = factory.newPullParser();
	}
	
	/**
	 * set data source
	 * @param inputStream
	 * @param encoding
	 * @throws XmlPullParserException
	 */
	public void setInput(InputStream inputStream ,String encoding) throws XmlPullParserException
	{
		parser.setInput(inputStream,encoding);
	}
	
	/**
	 * set data source
	 * @param content
	 * @throws XmlPullParserException
	 */
	public void setInput(String content) throws XmlPullParserException
	{
		parser.setInput(new StringReader(content) );
	}
	
	/**
	 * parse the data
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public ArrayList<RoadwayFlowItemInfo> parse() throws XmlPullParserException, IOException
	{
		ArrayList<RoadwayFlowItemInfo> roadwayInfoList= new ArrayList<RoadwayFlowItemInfo>();
		
		RoadwayFlowItemInfo roadwayFlowItemInfo = null;
		FlowItemInfo flowItemInfo = null;
		TravelTimeInfo travelTimeInfo = null;
		ArrayList<FlowItemInfo> flowItemInfoList = null;
		ArrayList<TravelTimeInfo> travelTimeInfoList = null;
		
		String timestamp = null;
		String tagName = null;
		
		//parse the XML file
		int eventType = parser.getEventType();
		while( eventType != XmlPullParser.END_DOCUMENT )
		{
			switch(eventType)
			{
			case XmlPullParser.START_DOCUMENT:
				{
					Log.i(TagName,"start document");
				}
				break;
			case XmlPullParser.START_TAG:
				{
					tagName = parser.getName();
					
					//Log.i(TagName,"tagName = " + tagName);
					if(XmlFields.Tag_TrafficML_RealTime.equals(tagName))
					{
						/*int size = parser.getAttributeCount();
						Log.w(TagName,"attr count = " + size );
						for(int index = 0; index < size; index ++)
						{
							String attrName = parser.getAttributeName(index);
							String attrValue = parser.getAttributeValue(index);
							Log.w(TagName,attrName + " = " + attrValue);
						}*/
						timestamp = parser.getAttributeValue(2);
					}
					else if( XmlFields.Tag_Roadway_Flow_Item.equals(tagName))
					{
						roadwayFlowItemInfo = new RoadwayFlowItemInfo();
						flowItemInfoList = new ArrayList<FlowItemInfo>();
						
						roadwayFlowItemInfo.setTimestamp(timestamp);
					}
					else if( XmlFields.Tag_RoadWayID.equals(tagName))
					{
						roadwayFlowItemInfo.setRoadwayID(parser.nextText() );
					}
					else if( XmlFields.Tag_Desc.equals(tagName))
					{
						roadwayFlowItemInfo.setDescription(parser.nextText());
					}
					else if( XmlFields.Tag_Flow_Items.equals(tagName))
					{
						roadwayFlowItemInfo.setFlowItemsDirection(parser.getAttributeValue(0));	
					}
					else if( XmlFields.Tag_Flow_Item.equals(tagName))
					{
						flowItemInfo = new FlowItemInfo();
					}
					else if( XmlFields.Tag_ID.equals(tagName))
					{
						flowItemInfo.setID(parser.nextText() );
					}
					else if( XmlFields.Tag_Ebu_Country_Code.equals(tagName) )
					{
						flowItemInfo.setEbu_country_code(parser.nextText() );
					}
					else if( XmlFields.Tag_Table_ID.equals(tagName))
					{
						flowItemInfo.setTable_ID(parser.nextText() );
					}
					else if( XmlFields.Tag_Location_ID.equals(tagName))
					{
						flowItemInfo.setLocation_ID(parser.nextText());
					}
					else if( XmlFields.Tag_Location_Desc.equals(tagName))
					{
						flowItemInfo.setLocation_desc(parser.nextText());
					}
					else if( XmlFields.Tag_Rds_Direction.equals(tagName))
					{
						flowItemInfo.setRds_direction(parser.nextText() );
					}
					else if( XmlFields.Tag_Length.equals(tagName))
					{
						flowItemInfo.setRds_length_units(parser.getAttributeValue(0));
						flowItemInfo.setRds_length(parser.nextText());
					}
					else if( XmlFields.Tag_Travel_Times.equals(tagName))
					{
						travelTimeInfoList = new ArrayList<TravelTimeInfo>();
					}
					else if( XmlFields.Tag_Lane_type.equals(tagName))
					{
						flowItemInfo.setLane_type(parser.getAttributeValue(0));
					}
					else if( XmlFields.Tag_Travel_Time.equals(tagName))
					{
						travelTimeInfo = new TravelTimeInfo();
						travelTimeInfo.setType(parser.getAttributeValue(0) );
					}
					else if( XmlFields.Tag_Duration.equals(tagName))
					{
						travelTimeInfo.setDuration_units(parser.getAttributeValue(0) );
						travelTimeInfo.setDuration(parser.nextText());
					}
					else if( XmlFields.Tag_Average_Speed.equals(tagName))
					{
						travelTimeInfo.setAverage_speed_units(parser.getAttributeValue(0));
						travelTimeInfo.setAverage_speed(parser.nextText() );
					}
					else if( XmlFields.Tag_Jam_Factor.equals(tagName))
					{
						flowItemInfo.setJam_factor(parser.nextText() );
					}
					else if( XmlFields.Tag_Jam_Factor_Trend.equals(tagName))
					{
						flowItemInfo.setJam_factor_trend(parser.nextText() );
					}
					else if(  XmlFields.Tag_Confidence.equals(tagName))
					{
						flowItemInfo.setConfidence(parser.nextText() );
					}
					else
					{
						//do nothing
					}
					
				}
				break;
			case XmlPullParser.END_TAG:
			{
				String endTagName = parser.getName();
				
				if(XmlFields.Tag_Flow_Item.equals(endTagName))
				{
					flowItemInfoList.add(flowItemInfo);
				}
				else if( XmlFields.Tag_Roadway_Flow_Item.equals(endTagName))
				{
					roadwayFlowItemInfo.setFlowItemInfoList(flowItemInfoList);
					roadwayInfoList.add(roadwayFlowItemInfo);
//					System.gc();
				}
				else if(XmlFields.Tag_Travel_Time.equals(endTagName))
				{
					travelTimeInfoList.add(travelTimeInfo);
				}
				else if( XmlFields.Tag_Travel_Times.equals(endTagName) )
				{
					flowItemInfo.setTravelTimeInfoList(travelTimeInfoList);
				}
				else
				{
					//do nothing
				}
				//Log.i(TagName,"end tag: " + parser.getName());
			}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}
		return roadwayInfoList;
	}
}
