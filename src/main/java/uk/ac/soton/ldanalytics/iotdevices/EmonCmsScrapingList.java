package uk.ac.soton.ldanalytics.iotdevices;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

public class EmonCmsScrapingList {
	public static void main(String[] args) {
		try {
			Random rand = new Random();
			String outputPath = "/Users/eugene/Documents/Programming/EmonCms/catalog/";
			int max_user = 24169;
			for(int i=1;i<=max_user;i++) {
				String url = "http://emoncms.org/feed/list.json?userid=" + i;
				try {
					String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
					if(!json.equals("[]"))
						FileUtils.writeStringToFile(new File(outputPath + i + ".json"), json, 	StandardCharsets.UTF_8);
				} catch(IOException e1) {
					e1.printStackTrace();
				}
				Thread.sleep(1000 + rand.nextInt(1000));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
