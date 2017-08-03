package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ThingSpeakStatistics {
	public static void main(String[] args) {
		String path = "/Users/eugene/Documents/Programming/ThingSpeak/";
		String inputPath = path + "feeds/";
		String tsPath = path + "ts/";
		String outputPath = path + "statistics.txt";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		File folder = new File(inputPath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
			int tall = 0;
			int wide = 0;
			for(File file:folder.listFiles()) {
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".json")) {
					JSONObject data = new JSONObject(FileUtils.readFileToString(file,StandardCharsets.UTF_8));
					JSONObject channel = data.getJSONObject("channel");
					JSONArray feeds = data.getJSONArray("feeds");
					
					//count width
					int fields = 0;
					for(String key:channel.keySet()) {
						if(key.startsWith("field"))
							fields++;
					}
					if(fields>1)
						wide++;
					else
						tall++;
					
					//create ts
					if(feeds.length()>0) {
						String newFileName = tempFileName.replace(".json", ".txt");
						BufferedWriter tsBw = new BufferedWriter(new FileWriter(tsPath + newFileName));
						Long previousTime = 0L;
						for(int i=0;i<feeds.length();i++) {
							JSONObject feed = feeds.getJSONObject(i);
							String created = feed.getString("created_at");
							Long currentTime = sdf.parse(created).getTime();
							if(previousTime!=0) {
								tsBw.append((currentTime-previousTime)+"");
								tsBw.newLine();
							}
							previousTime = currentTime;
						}
						tsBw.close();
					}
				}
			}
			bw.append("tall:"+tall+",wide:"+wide+",wide%:"+(wide/((tall+wide)*1.0)));
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
