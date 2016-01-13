package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class DweetStatistics {

	public static void main(String[] args) {
		String folderPath = "/Users/eugene/Documents/Programming/dweet/";
		String outputFlatPath = "/Users/eugene/Documents/Programming/dweet/flat/";
		String outputComplexPath = "/Users/eugene/Documents/Programming/dweet/complex/";
		File folder = new File(folderPath);
		Set<String> things = new HashSet<String>();
		int flat = 0;
		for(File file:folder.listFiles()) {
			try {
				String tempFileName = file.getName();
				if(tempFileName.startsWith("job") && tempFileName.endsWith(".json")) {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line = "";
					while((line=br.readLine())!=null) {
						JSONObject row = new JSONObject(line);
						String thing = row.getString("thing");
						if(!things.contains(thing)) {
							JSONObject content = row.getJSONObject("content");
							Boolean isFlat=checkIsFlat(content);
							String path = outputComplexPath;
							if(isFlat) {
								path = outputFlatPath;
								flat++;
							}
							BufferedWriter bw = new BufferedWriter(new FileWriter(path + thing + ".json"));
							bw.write(content.toString());
							bw.close();
							things.add(thing);
						}
					}
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try { 
			BufferedWriter bw = new BufferedWriter(new FileWriter(folderPath + "statistics.txt"));
			bw.append("total_things="+things.size()+"\n");
			bw.append("total_flat="+flat+"\n");
			bw.append("total_complex="+(things.size()-flat)+"\n");
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Boolean checkIsFlat(JSONObject content) {
		for(String key:content.keySet()) {
			if(content.get(key) instanceof JSONObject) {
				if(content.length()>1) {
					return false;
				} else {
					checkIsFlat(content.getJSONObject(key));
				}
			} else if(content.get(key) instanceof JSONArray) {
				if(content.length()>1) {
					if(content.getJSONArray(key).length()>1 && !(content.getJSONArray(key).get(0) instanceof JSONObject)) {
//						System.out.println(content.toString());
						return false;
					}
					
				}
			}
		}
		return true;
	}

}
