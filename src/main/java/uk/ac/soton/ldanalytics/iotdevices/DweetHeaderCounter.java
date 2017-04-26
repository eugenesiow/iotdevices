package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class DweetHeaderCounter {
	public static void main(String[] args) {
		String outputClassifier = "/Users/eugene/Documents/Programming/dweetTOIT/header.txt";

		String folderPath = "/Users/eugene/Documents/Programming/dweetTOIT/flat/";
		File folder = new File(folderPath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputClassifier));
			for(File file:folder.listFiles()) {
				String header = "";
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".json")) {
					JSONObject content = new JSONObject(FileUtils.readFileToString(file,"utf8"));
					JSONObject innermostContent = checkIsFlat(content);
					for(String key:innermostContent.keySet()) {
						header+=key.toLowerCase().trim()+";";
					}
					bw.append(header+"\n");
				}
			}	
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static JSONObject checkIsFlat(JSONObject content) {
		for(String key:content.keySet()) {
			if(content.get(key) instanceof JSONObject) {
				return checkIsFlat(content.getJSONObject(key));
			}
		}
		return content;
	}

}
