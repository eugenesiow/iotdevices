package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SparkfunTimeSampling {
	public static void main(String[] args) {
		String path = "/Users/eugene/Documents/Programming/iot_data/";
		String folderPath = path + "data/";
		String outputPath = path + "ts/";
		File folder = new File(folderPath);
		try {
			int counter = 0;
			for(File file:folder.listFiles()) {
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".csv")) {
					counter++;
					BufferedReader br = new BufferedReader(new FileReader(file));
					BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath + counter+".txt"));
					String line = "";
					line=br.readLine(); //read header
					if(line==null)
						line = "";
					String[] header = line.split(",");
					int timestampCol = header.length - 1;
					int headerCol = 0;
					for(String headerName:header) {
						if(headerName.trim().toLowerCase().equals("timestamp")) {
							timestampCol = headerCol;
						}
						headerCol++;
					}
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					long previousTime = 0;
					Boolean error = false;
					while((line=br.readLine())!=null) {
						String[] parts = line.split(",");
						if(parts.length>=header.length) {
							try {
								long currentTime = sdf.parse(parts[timestampCol]).getTime();
								if(previousTime>0) {
									bw.append((previousTime-currentTime)+"");
									bw.newLine();
									previousTime = currentTime;
								}
								previousTime = currentTime;
							} catch (ParseException e) {
								error = true;
//								e.printStackTrace();
//								System.out.println(tempFileName+"|"+line+"|"+count);
							}
						}
					}
					br.close();
					bw.flush();
					bw.close();
				}
			}
		} catch (IOException e) {
			System.out.println("ioexception");
			e.printStackTrace();
		}
	}
}
