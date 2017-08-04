package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class HealthKitSchemata {
	public static void main(String[] args) {
		String path = "/Users/eugene/Documents/Programming/OpenHumans/";
		String folderPath = path + "data/";
		String outputPath = path + "schema_hk/";
		String tsOutputPath = path + "ts_hk/";
		File folder = new File(folderPath);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			int counter = 0;
			for(File file:folder.listFiles()) {
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".json") && tempFileName.contains("healthkit-export_")) {
					String jsonStr = FileUtils.readFileToString(file,StandardCharsets.UTF_8);
					JSONObject data = new JSONObject(jsonStr);	
					for(String key:data.keySet()) {
						if(key.startsWith("HK")) {
							JSONArray arr = data.getJSONArray(key);
							if(arr.length()>0) {
								JSONObject schema = arr.getJSONObject(0);
								FileUtils.writeStringToFile(new File(outputPath+key+".json"), schema.toString(), StandardCharsets.UTF_8);
								if(arr.length()>1 && schema.has("edate")) {
									BufferedWriter bw = new BufferedWriter(new FileWriter(tsOutputPath + key + "_"+  counter++ + ".txt"));
									for(int i=0;i<arr.length();i++) {
										JSONObject block = arr.getJSONObject(i);
										if(!block.has("edate"))
											break;
										long start = sdf.parse(block.getString("sdate")).getTime();
										long end = sdf.parse(block.getString("edate")).getTime();
										bw.append((end - start) + "");
										bw.newLine();
									}
									bw.close();
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
