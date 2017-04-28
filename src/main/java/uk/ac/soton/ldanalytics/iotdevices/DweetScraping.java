package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;

public class DweetScraping {
	public static void main(String[] args) {
		try {
			Random rand = new Random();
			String inputCatalog = "/Users/eugene/Documents/Programming/dweetTOIT/catalog.txt";
			String outputPath = "/Users/eugene/Documents/Programming/dweetTOIT/content/";
			BufferedReader br = new BufferedReader(new FileReader(inputCatalog));
			String line="";
			int count = 0;
			while((line=br.readLine())!=null) {
				if(line.endsWith(".json")) {
					System.out.println(count+++":"+line);
					BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath + line));
					String url = "http://dweet.io/get/dweets/for/" + line.replace(".json", "");
					try {
						String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
						bw.write(json);
						bw.flush();
					} catch(IOException e1) {
						e1.printStackTrace();
					}
					bw.close();
					Thread.sleep(1000 + rand.nextInt(1000));
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
}
