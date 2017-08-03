package uk.ac.soton.ldanalytics.iotdevices;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class FitbitStatistics {
	public static void main(String[] args) {
		String path = "/Users/eugene/Documents/Programming/OpenHumans/";
		String inputPath = path + "data/17820fitbit-data.json";
		String outputPath = path + "schema/";
		try {
			JSONObject data = new JSONObject(FileUtils.readFileToString(new File(inputPath),StandardCharsets.UTF_8));	
			for(String key:data.keySet()) {
				JSONObject prop = data.getJSONObject(key);
				if(prop.has("2017")) {
					JSONObject year = prop.getJSONObject("2017");
					for(String subKey:year.keySet()) {
						if(!subKey.equals("errors")) {
							FileUtils.writeStringToFile(new File(outputPath+"fitbit_"+subKey+".json"), prop.toString(), StandardCharsets.UTF_8);
							JSONObject tsEntry = year.getJSONArray(subKey).getJSONObject(0);
							System.out.println("fitbit,"+subKey+",true,"+(tsEntry.length()-1)+",1,true,true");
						}
					}
				} else if(prop.has("2017-01")) {
					System.out.println(key);
				} else if(prop.has("2016-12")) {
					System.out.println(key);
				}
			}					
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
