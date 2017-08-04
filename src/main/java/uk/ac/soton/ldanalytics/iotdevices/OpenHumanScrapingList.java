package uk.ac.soton.ldanalytics.iotdevices;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class OpenHumanScrapingList {
	public static void main(String[] args) {
		try {
			Random rand = new Random();
			String outputPath = "/Users/eugene/Documents/Programming/OpenHumans/";
			String uriBase = "https://www.openhumans.org/api/public-data/?format=json";
			String[] uris_extra = {"&source=fitbit","&source=moves","&source=runkeeper","&source=direct-sharing-14"};
			for(String uri_extra:uris_extra) {
				String url = uriBase + uri_extra;
				try {
					Boolean goNext = true;
					int counter = 0;
					while(goNext) {
						System.out.println(url);
						String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
						FileUtils.writeStringToFile(new File(outputPath + "catalog/"+ uri_extra.split("=")[1] + counter++ + ".json"), json, 	StandardCharsets.UTF_8);
						JSONObject page = new JSONObject(json);
						JSONArray results = page.getJSONArray("results");
						for(int i=0;i<results.length();i++) {
							JSONObject result = results.getJSONObject(i);
							String downloadUrl = result.getString("download_url");
							String baseName = result.getString("basename");
							String id = result.getInt("id") + "";
							String data = Jsoup.connect(downloadUrl).ignoreContentType(true).maxBodySize(0).execute().body();
							FileUtils.writeStringToFile(new File(outputPath + "data/"+ id + "_" + baseName), data, 	StandardCharsets.UTF_8);
						}
						String next = page.get("next").toString();
						if(!next.equals("null")) {
							url = next;
						}
						else {
							goNext = false;
						}
						Thread.sleep(1000 + rand.nextInt(1000));
					}
				} catch(IOException e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
