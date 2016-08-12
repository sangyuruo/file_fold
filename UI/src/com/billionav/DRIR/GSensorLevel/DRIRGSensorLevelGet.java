package com.billionav.DRIR.GSensorLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

import com.billionav.DRIR.jni.jniDRIR_MainControl;
import com.billionav.jni.FileSystemJNI;

public class DRIRGSensorLevelGet {
	public final static String CONFIG = (FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH))
			                            + (jniDRIR_MainControl.DRIRGetDrirRootPath()) + File.separator + "GSConfig.xml";
	
	public static List<DRIRGSensorLevel> getGSLevel()
	{
		DRIRXmlContentHandler handler = new DRIRXmlContentHandler();	
		FileReader fileReader;
		try {
			fileReader = new FileReader(CONFIG);
			SAXParserFactory spf = SAXParserFactory.newInstance(); 
			SAXParser sp;			
			try {
				sp = spf.newSAXParser();
				sp.parse(new InputSource(fileReader), handler);
				fileReader.close();
				return handler.getLevels();
			} catch (ParserConfigurationException e) {
				try {
					fileReader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				try {
					fileReader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				try {
					fileReader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//XMLReader xr = sp.getXMLReader();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
