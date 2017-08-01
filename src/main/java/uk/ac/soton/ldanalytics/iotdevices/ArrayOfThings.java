package uk.ac.soton.ldanalytics.iotdevices;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ArrayOfThings {
	public static void main(String[] args) {		
		String path = "/Users/eugene/Documents/Programming/data/array_of_things/";
		String fileName = path + "2017-07-31T10-50-00.950945-array_of_things_chicago.csv";
		String[] sensors = {"apds-9006-020", 
		        "bmi160", 
		        "bmp180", 
		        "chemsense", 
		        "hih4030", 
		        "hih6130", 
		        "hmc5883l", 
		        "htu21d", 
		        "lps25h", 
		        "ml8511", 
		        "mlx75305", 
		        "mma8452q", 
		        "pr103j2", 
		        "sht25", 
		        "tmp112", 
		        "tmp421", 
		        "tsl260rd", 
		        "tsys01"};
		String[] nodes = {"0000001e0610b9e7", 
		        "0000001e0610b9fd", 
		        "0000001e0610ba72", 
		        "0000001e0610ba89"};
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			for(String sensor:sensors) {
				BufferedWriter bw = new BufferedWriter(new FileWriter(path + "ts/" + sensor+".txt"));
				Long previous = 0L;
				Long delta = 0L;
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				String line = "";
				Boolean printNext = false;
				String[] header =br.readLine().split(",");
				while((line=br.readLine())!=null) {
					if(printNext==true) {
						header = line.split(",");
						printNext = false;
					} else if(line.equals("")) {
						printNext = true;					
					} else {
						String[] parts = line.split(",");
						if(parts[3].equals(sensor)) {
							Long current = sdf.parse(parts[1]).getTime();
							if(previous>0L) {
								delta = previous - current;
								bw.append(delta.toString());
								bw.newLine();
							}
							previous = current;
						}
					}
				}
				bw.close();
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
}
