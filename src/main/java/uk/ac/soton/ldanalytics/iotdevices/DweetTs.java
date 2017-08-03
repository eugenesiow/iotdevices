package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class DweetTs {
	public static void main(String[] args) {
		try {
			String inputCatalog = "/Users/eugene/Documents/Programming/dweetTOIT/time_spacing.txt";
			String path = "/Users/eugene/Documents/Programming/dweetTOIT/ts/";
			int count = 0;
			BufferedReader br = new BufferedReader(new FileReader(inputCatalog));
			String line="";
			while((line=br.readLine())!=null) {
				String[] parts = line.split(";");
				if(parts.length>2) {
					BufferedWriter bw = new BufferedWriter(new FileWriter(path+count+".csv"));
					for(int i=0;i<parts.length-1;i++) {
						bw.append(parts[i]);
						bw.newLine();
					}
					bw.close();
					count++;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
}
