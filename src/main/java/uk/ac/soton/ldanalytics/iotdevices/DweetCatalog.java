package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;

public class DweetCatalog {
	public static void main(String[] args) {
		try {
			Random rand = new Random();
			String folderPath = "/Users/eugene/Documents/Programming/dweetTOIT/flat/";
			String outputCatalog = "/Users/eugene/Documents/Programming/dweetTOIT/catalog.txt";
			String outputPath = "/Users/eugene/Documents/Programming/dweetTOIT/content/";
			File folder = new File(folderPath);
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputCatalog));
			for(File file:folder.listFiles()) {
				String tempFileName = file.getName();
//				if(tempFileName.endsWith(".json")) {
//					BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath + tempFileName));
//					String url = "http://dweet.io/get/dweets/for/" + tempFileName.replace(".json", "");
//					String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
//					bw.write(json);
//					bw.flush();
//					bw.close();
//					Thread.sleep(1000 + rand.nextInt(1000));
//				}
				bw.append(tempFileName+"\n");
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
}
