package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;

public class DweetEvenUneven {
	public static void main(String[] args) {
		try {
			String inputCatalog = "/Users/eugene/Documents/Programming/dweetTOIT/time_spacing.txt";
			BufferedReader br = new BufferedReader(new FileReader(inputCatalog));
			String line="";
			int evenCount = 0;
			int unevenCount = 0;
			int neither = 0;
			while((line=br.readLine())!=null) {
				String[] parts = line.split(";");
				if(parts.length>2) {
					Boolean even = true;
					for(int i=1;i<parts.length-1;i++) {
						if(Integer.parseInt(parts[i])-Integer.parseInt(parts[i-1])!=0) {
							even = false;
						}
					}
					if(even)
						evenCount++;
					else
						unevenCount++;
				} else {
					neither++;
				}
			}
			System.out.println("even:"+evenCount+"\nuneven:"+unevenCount+"\nneither:"+neither);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
}
