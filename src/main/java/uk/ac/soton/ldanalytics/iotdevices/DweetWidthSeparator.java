package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class DweetWidthSeparator {

	public static void main(String[] args) {
		String folderPath = "/Users/eugene/Documents/Programming/dweet/flat/";
		String outputPath = "/Users/eugene/Documents/Programming/dweet/flat/4/";
		File folder = new File(folderPath);
		for(File file:folder.listFiles()) {
			try {
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".json")) {
					JSONObject content = new JSONObject(FileUtils.readFileToString(file));
					int isFlat=checkIsFlat(content);
					if(isFlat>0) {
//						System.out.println(isFlat);
						if(isFlat==4) {
							BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath + tempFileName + ".json"));
							bw.write(content.toString());
							bw.close();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static int checkIsFlat(JSONObject content) {
		for(String key:content.keySet()) {
			if(content.get(key) instanceof JSONObject) {
				if(content.length()>1) {
					return -1;
				} else {
					return checkIsFlat(content.getJSONObject(key));
				}
			} else if(content.get(key) instanceof JSONArray) {
				if(content.length()>1) {
					if(content.getJSONArray(key).length()>1 && !(content.getJSONArray(key).get(0) instanceof JSONObject)) {
//						System.out.println(content.toString());
						return -1;
					}
					
				}
			}
		}
		return content.length();
	}

}
