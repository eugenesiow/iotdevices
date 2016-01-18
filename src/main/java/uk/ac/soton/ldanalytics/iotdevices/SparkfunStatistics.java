package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONObject;

public class SparkfunStatistics {

	public static void main(String[] args) {
		String folderPath = "/Users/eugene/Documents/Programming/sparkfun/";
		String outputPath = "/Users/eugene/Documents/Programming/sparkfun/things/";
		File folder = new File(folderPath);
		Set<String> things = new HashSet<String>();
		Map<Integer,Integer> counter = new TreeMap<Integer,Integer>(); 
		for(File file:folder.listFiles()) {
			try {
				String tempFileName = file.getName();
				if(tempFileName.startsWith("job") && tempFileName.endsWith(".json")) {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line = "";
					while((line=br.readLine())!=null) {
						JSONObject row = new JSONObject(line);
						String thing = row.getString("url").replace("/latest.json", "").replace("https://data.sparkfun.com/output/", "");
						if(!things.contains(thing)) {
							JSONObject content = row.getJSONObject("content");
							BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath + thing + ".json"));
							bw.write(content.toString());
							bw.close();
							Integer count = counter.get(content.length());
							if(count==null) {
								count = new Integer(0);
							}
							counter.put(content.length(), ++count);
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
			for(Entry<Integer,Integer> entry:counter.entrySet()) {
				bw.append(entry.getKey()+"="+entry.getValue()+"\n");
			}
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}
