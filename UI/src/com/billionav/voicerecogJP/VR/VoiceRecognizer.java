package com.billionav.voicerecogJP.VR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

public class VoiceRecognizer {
	private static final int MAX_RESULT_COUNT = 10;
	
	private static final String COMMAND_DELIM = ";";
	
	private static final int [][] COMMANDS = {
//		{R.string.vr_go_home, Command.CMD_HOME},
//		{R.string.vr_traffic_jam, Command.CMD_JAM},
//		{R.string.vr_search_rest_area, Command.CMD_REST_AREA},
//		{R.string.vr_search_station_building, Command.CMD_STATION_BUILDING},
//		{R.string.vr_search_station, Command.CMD_STATION},
//		{R.string.vr_search_gas_station, Command.CMD_GAS_STATION},
//		{R.string.vr_search_park, Command.CMD_PARK},
//		
//		{R.string.vr_search_id_store, Command.CMD_ID_STORE},
//		{R.string.vr_search_dept_store, Command.CMD_DEPT_STORE},
//		{R.string.vr_search_conv_store, Command.CMD_CONV_STORE},
//		{R.string.vr_search_supermarket, Command.CMD_SUPERMARKET},
//		{R.string.vr_search_shopping_mall, Command.CMD_SHOPPING_MALL},
//		{R.string.vr_search_outlet_mall, Command.CMD_OUTLET_MALL},
//		{R.string.vr_search_amusement, Command.CMD_AMUSEMENT},
//		{R.string.vr_search_theater, Command.CMD_THEATER},
//		
//		{R.string.vr_search_business_hotel, Command.CMD_BUSINESS_HOTEL},
//		{R.string.vr_search_hotel, Command.CMD_HOTEL},
//		{R.string.vr_search_inn, Command.CMD_INN},
//		{R.string.vr_search_other_stay, Command.CMD_OTHER_STAY},
//	
//		{R.string.vr_search_fast_food, Command.CMD_FAST_FOOD},
//		{R.string.vr_search_fam_rest, Command.CMD_FAM_REST},
//		{R.string.vr_search_cafe, Command.CMD_CAFE},
//		{R.string.vr_search_hospital, Command.CMD_HOSPITAL},
//		{R.string.vr_search_bank, Command.CMD_BANK},
//		
//		{R.string.vr_search_docomo_shop, Command.CMD_DOCOMO_SHOP}
	};
	
	private Map<Integer, List<Pattern>> patterns = null;
	
	/**
	 * Intialize command patterns
	 * @param ctx Context
	 * @return command if match success,else return null
	 */
	public boolean initialize(Context ctx) {
		patterns = buildCommands(ctx, COMMANDS);
		return (null != patterns && patterns.size() > 0);
	}
	
	/**
	 * Parse VR engine recognized text list
	 * @param recognized text list
	 * @return command if match success,else return null
	 */
    public Command parseSpeechResult(List<String> results) {
    	if (null == patterns) {
    		return null;
    	}
    	
    	if (null == results || 0 == results.size()) {
    		return null;
    	}
    	
    	int i = 0;
    	Iterator<String> it = results.iterator();
    	while (i<MAX_RESULT_COUNT && it.hasNext()) {
    		String text = it.next();
    		Command cmd = matchCommand(text);
    		if (null != cmd) {
    			return cmd;
    		}
    		++i;
    	}
    	return null;
    }
    
    private Map<Integer, List<Pattern>> buildCommands(Context ctx, int [][] commands) {
		Map<Integer, List<Pattern>> patternMap = new LinkedHashMap<Integer, List<Pattern>>(commands.length);
		for (int i=0; i<commands.length; ++i) {
			String command = ctx.getResources().getString(commands[i][0]);
			if (null == command || 0 == command.length()) continue;
			
			String[] values = command.split(COMMAND_DELIM);
        	List<String> cmds = new ArrayList<String>(values.length);
        	for (int j=0; j<values.length; ++j) {
        		cmds.add(values[j]);
        	}
			patternMap.put(commands[i][1], buildCommand(cmds));
		}
		return patternMap;
	}
    
	private List<Pattern> buildCommand(List<String> command) {
		List<Pattern> patterns = new ArrayList<Pattern>(command.size());
		for (int j=0; j<command.size(); ++j) {
			Pattern p = Pattern.compile(command.get(j));
			patterns.add(p);
		}
		return patterns;
	}
	
    private Command matchCommand(String text) {
		if (null != patterns) {
			Iterator<Entry<Integer, List<Pattern>>> it = patterns.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, List<Pattern>> entry = it.next();
				Command command = findCommand(entry.getKey(), entry.getValue(), text);
				if (null != command) {
					return command;
				}
			}
		}
		return null;
    }
    
	private Command findCommand(Integer type, List<Pattern>pattern, String text) {
		for (int j=0; j<pattern.size(); ++j) {
			Matcher m = pattern.get(j).matcher(text);
			if (m.matches()) {
				String content = m.group(m.groupCount());
				return new Command(type, content);
			}
		}
		return null;
	}
    
	// Singletone and consturtor
	public static VoiceRecognizer instance() {
		if (null != sInstance) {
			return sInstance;
		}
		
		synchronized(VoiceRecognizer.class) {
			if (null == sInstance) {
				sInstance = new VoiceRecognizer();
			}
		}

		return sInstance;
	}
	
	private VoiceRecognizer() {
		this.patterns = null;
	}
	
	// Singletone
	private static VoiceRecognizer sInstance = null;
}
