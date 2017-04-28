package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class DweetTimeResolution {
	public static void main(String[] args) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String folderPath = "/Users/eugene/Documents/Programming/dweetTOIT/content/";
			String outputLog = "/Users/eugene/Documents/Programming/dweetTOIT/time_spacing.txt";
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputLog));
			File folder = new File(folderPath);
			for(File file:folder.listFiles()) {
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".json")) {
					JSONObject content = new JSONObject(FileUtils.readFileToString(file));
					JSONArray dweets = content.getJSONArray("with");
					String thing = "";
					String created = "";
					Date previous = null;
					for(int i=0;i<dweets.length();i++) {
						JSONObject dweet = dweets.getJSONObject(i);
						created = dweet.getString("created");
						thing = dweet.getString("thing");
						Date current = df.parse(created);
						if(previous!=null) {
							long time = previous.getTime() - current.getTime();
							bw.append(time+";");
						}
						previous = current;
					}
					bw.append(thing+"\n");
				}
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
}
