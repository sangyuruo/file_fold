package com.billionav.DRIR.GSensorLevel;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DRIRXmlContentHandler extends DefaultHandler{

	private List<DRIRGSensorLevel> levels;
	
	private DRIRGSensorLevel level;
	private String tempString;
	private static final String LEVELSTRING = "DRIRGSensorLevel";
	private static final String IDSTRING = "id";
	private static final String N1STRING = "N1";
	private static final String N2STRING = "N2";
	private static final String ASTRING = "A";
	private static final String BSTRING = "B";
	
	public List<DRIRGSensorLevel> getLevels()
	{
		return levels;
	}

	@Override
	public void startDocument() throws SAXException {
		levels = new ArrayList<DRIRGSensorLevel>();
		super.startDocument();		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (LEVELSTRING.equals(localName))
		{
			level = new DRIRGSensorLevel();
			level.setId(new Integer(attributes.getValue(IDSTRING)));
		}
		tempString = localName;
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (LEVELSTRING.equals(localName))
		{
			levels.add(level);
			level = null;
		}
		tempString = null;
		super.endElement(uri, localName, qName);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (null != level)
		{
			String valueString = new String(ch, start, length);
			if (N1STRING.equals(tempString))
			{
				level.setN1(new Integer(valueString).intValue());
			}
			else if (N2STRING.equals(tempString))
			{
				level.setN2(new Integer(valueString).intValue());
			}
			else if (ASTRING.equals(tempString))
			{
				level.setA(new Double(valueString).doubleValue());
			}
			else if (BSTRING.equals(tempString))
			{
				level.setB(new Double(valueString).doubleValue());
			}
		}
		super.characters(ch, start, length);
	}
	
	
}
