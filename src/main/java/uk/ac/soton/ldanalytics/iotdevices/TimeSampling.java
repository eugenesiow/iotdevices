package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeSampling {
	public static void main(String[] args) {
		String folderPath = "/Users/eugenesiow/Documents/Programming/iot_data/data/";
		String outputPath = "/Users/eugenesiow/Documents/Programming/iot_data/";
		File folder = new File(folderPath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath + "statistics.txt"));
		
			for(File file:folder.listFiles()) {

				String tempFileName = file.getName();
				if(tempFileName.endsWith(".csv")) {
					BufferedReader br = new BufferedReader(new FileReader(file));
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
					int count = 0;
					long totalDiff = 0;
					Boolean error = false;
					while((line=br.readLine())!=null) {
						String[] parts = line.split(",");
						if(parts.length>=header.length) {
							try {
								long currentTime = sdf.parse(parts[timestampCol]).getTime();
								if(previousTime>0) {
									totalDiff += previousTime - currentTime;
									count++;
								}
								previousTime = currentTime;
							} catch (ParseException e) {
								error = true;
//								e.printStackTrace();
//								System.out.println(tempFileName+"|"+line+"|"+count);
							}
						}
					}
					
					if(count>0) {
//						if(error) {
//							System.out.println("ERROR!"+count);
//							System.out.println(tempFileName + "," + ((totalDiff/count)/1000.0)+"\n");
//						}
						bw.append(tempFileName + "," + ((totalDiff/count)/1000.0)+"\n");
					}
					br.close();
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("ioexception");
			e.printStackTrace();
		}
	}
}
