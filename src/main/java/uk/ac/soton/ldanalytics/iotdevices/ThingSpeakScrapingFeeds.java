package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

public class ThingSpeakScrapingFeeds {
	public static void main(String[] args) {
		String path = "/Users/eugene/Documents/Programming/ThingSpeak/";
		String inputPath = path + "channels.txt";
		String outputPath = path + "feeds/"; 
		try {
			Random rand = new Random();
			BufferedReader br = new BufferedReader(new FileReader(inputPath));
			String line = "";
			while((line=br.readLine())!=null) {
				String channel = line.trim();
				String url = "https://api.thingspeak.com/channels/" + channel + "/feeds.json?results=100";
				try {
					String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
					FileUtils.writeStringToFile(new File(outputPath + channel + ".json"), json, StandardCharsets.UTF_8);
				} catch(IOException e1) {
					e1.printStackTrace();
				}
				Thread.sleep(2000 + rand.nextInt(1000));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
