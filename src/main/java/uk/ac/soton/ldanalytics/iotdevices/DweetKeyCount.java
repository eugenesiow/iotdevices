package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class DweetKeyCount {

	public static void main(String[] args) {
		String folderPath = "/Users/eugene/Documents/Programming/dweet/flat/4/";
		String outputWordCount = "/Users/eugene/Documents/Programming/dweet/word_count.txt";
		String outputHeaderCount = "/Users/eugene/Documents/Programming/dweet/header_count.txt";
		File folder = new File(folderPath);
		Map<String,Integer> wordCount = new HashMap<String,Integer>();
		Map<String,Integer> headerCount = new HashMap<String,Integer>();
		ValueComparator bvc = new ValueComparator(wordCount);
		ValueComparator bvc_head = new ValueComparator(headerCount);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
        TreeMap<String, Integer> sorted_header_map = new TreeMap<String, Integer>(bvc_head);
		for(File file:folder.listFiles()) {
			try {
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".json")) {
					JSONObject content = new JSONObject(FileUtils.readFileToString(file));
					JSONObject innermostContent = checkIsFlat(content);
					for(String key:innermostContent.keySet()) {
						Integer count = wordCount.get(key);
						if(count==null) {
							count = new Integer(0);
						}
						wordCount.put(key, ++count);
					}
					Integer count = headerCount.get(innermostContent.keySet().toString());
					if(count==null) {
						count = new Integer(0);
					}
					headerCount.put(innermostContent.keySet().toString(), ++count);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		
		try {
			sorted_map.putAll(wordCount);
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputWordCount));
			for(Entry<String, Integer> entry:sorted_map.entrySet()) {
				bw.append(entry.getKey()+":"+entry.getValue()+"\n");
			}
			bw.close();
			
			sorted_header_map.putAll(headerCount);
			bw = new BufferedWriter(new FileWriter(outputHeaderCount));
			for(Entry<String, Integer> entry:sorted_header_map.entrySet()) {
				bw.append(entry.getKey()+":"+entry.getValue()+"\n");
			}
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

class ValueComparator implements Comparator<String> {
    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
