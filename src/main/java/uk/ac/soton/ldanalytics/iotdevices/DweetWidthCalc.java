package uk.ac.soton.ldanalytics.iotdevices;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class DweetWidthCalc {

	public static void main(String[] args) {
		String folderPath = "/Users/eugene/Documents/Programming/dweetTOIT/flat/";
		File folder = new File(folderPath);
		Map<Integer,Integer> widthCounter = new TreeMap<Integer,Integer>();
		for(File file:folder.listFiles()) {
			try {
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".json")) {
					JSONObject content = new JSONObject(FileUtils.readFileToString(file));
					int isFlat=checkIsFlat(content);
					if(isFlat>0) {
						Integer count = widthCounter.get(isFlat);
						if(count==null) 
							count = 0;
						count++;
						widthCounter.put(isFlat, count);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		for(Entry<Integer,Integer> width:widthCounter.entrySet()) {
			System.out.println(width.getKey()+"\t"+width.getValue());
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
