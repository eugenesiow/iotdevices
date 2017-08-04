package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class RunkeeperTsExtract {
	public static void main(String[] args) {
		String path = "/Users/eugene/Documents/Programming/OpenHumans/";
		String folderPath = path + "data/";
		String outputPath = path + "ts_runkeeper/";
		File folder = new File(folderPath);
		try {
			for(File file:folder.listFiles()) {
				String tempFileName = file.getName();
				int counter = 0;
				if(tempFileName.endsWith(".json") && tempFileName.contains("Runkeeper-activity-data")) {
					String jsonStr = FileUtils.readFileToString(file,StandardCharsets.UTF_8);
					if(!jsonStr.endsWith("}"))
						break;
					JSONObject data = new JSONObject(jsonStr);	
					JSONArray activity = data.getJSONArray("fitness_activities");
					if(activity.length()>0) {
						for(int i=0;i<activity.length();i++) {
							BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath + tempFileName.replace(".json", "") + counter++ + ".txt"));
							JSONArray actPath = activity.getJSONObject(i).getJSONArray("path");
							double previousTs = -1.0;
							for(int j=0;j<actPath.length();j++) {
								double currentTs = actPath.getJSONObject(j).getDouble("timestamp");
								if(previousTs>-1.0) {
									bw.append((currentTs - previousTs)+"");
									bw.newLine();
								}
								previousTs = currentTs;
							}
							bw.close();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
