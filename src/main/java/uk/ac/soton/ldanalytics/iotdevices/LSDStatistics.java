package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

public class LSDStatistics {

	public static void main(String[] args) {
		String folderPath = "/Users/eugene/Downloads/knoesis_observations_csv_date_sorted/";
		File folder = new File(folderPath);
		int wide_count = 0;
		int number_count = 0;
		int total = 0;
		int total_attribute = 0;
		for(File file:folder.listFiles()) {
			try {
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".csv")) {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line=br.readLine();
					String[] cols = line.split(",");
					if(cols.length>2) 
						wide_count++;
					for(int i=1;i<cols.length;i++) {
						if(!cols[i].endsWith("_bool"))
							number_count++;
						total_attribute++;							
					}
					total++;
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Double wide = wide_count / (total * 1.0);
		Double num = number_count / (total_attribute * 1.0);
		System.out.println(wide_count);
		System.out.println(number_count);
		System.out.println(wide);
		System.out.println(num);
	}

}
