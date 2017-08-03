package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ThingSpeakScrapingChannelList {
	public static void main(String[] args) {
		String path = "/Users/eugene/Documents/Programming/ThingSpeak/";
		String folderPath = path + "catalog/";
		String outputPath = path + "channels.txt";
		File folder = new File(folderPath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
			for(File file:folder.listFiles()) {
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".json")) {
					JSONObject page = new JSONObject(FileUtils.readFileToString(file,StandardCharsets.UTF_8));
					JSONArray channels = page.getJSONArray("channels");
					for(int i=0;i<channels.length();i++) {
						JSONObject channel = channels.getJSONObject(i);
						bw.append(channel.getInt("id")+"");
						bw.newLine();
					}
				}
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
