package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class DweetClassifier {
	public static void main(String[] args) {
		final String Digits     = "(\\p{Digit}+)";
		final String HexDigits  = "(\\p{XDigit}+)";
		// an exponent is 'e' or 'E' followed by an optionally 
		// signed decimal integer.
		final String Exp        = "[eE][+-]?"+Digits;
		final String fpRegex    =
			    ("[\\x00-\\x20]*"+ // Optional leading "whitespace"
			    "[+-]?(" +         // Optional sign character
			    "NaN|" +           // "NaN" string
			    "Infinity|" +      // "Infinity" string

			    // A decimal floating-point string representing a finite positive
			    // number without a leading sign has at most five basic pieces:
			    // Digits . Digits ExponentPart FloatTypeSuffix
			    // 
			    // Since this method allows integer-only strings as input
			    // in addition to strings of floating-point literals, the
			    // two sub-patterns below are simplifications of the grammar
			    // productions from the Java Language Specification, 2nd 
			    // edition, section 3.10.2.

			    // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
			    "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

			    // . Digits ExponentPart_opt FloatTypeSuffix_opt
			    "(\\.("+Digits+")("+Exp+")?)|"+

			    // Hexadecimal strings
			    "((" +
			    // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
			    "(0[xX]" + HexDigits + "(\\.)?)|" +

			    // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
			    "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

			    ")[pP][+-]?" + Digits + "))" +
			    "[fFdD]?))" +
			    "[\\x00-\\x20]*");// Optional trailing "whitespace"
		Pattern p = Pattern.compile(fpRegex);

		String outputClassifier = "/Users/eugene/Documents/Programming/dweet/classifier.txt";
		
		long stringCount = 0;
		long othersCount = 0;
		long booleanCount = 0;
		long arrayCount = 0;
		
//		String folderPath = "/Users/eugene/Documents/Programming/dweetTOIT/flat/";
//		File folder = new File(folderPath);
//		try {
//			BufferedWriter bw = new BufferedWriter(new FileWriter(outputClassifier));
//			for(File file:folder.listFiles()) {
//				
//					String tempFileName = file.getName();
//					if(tempFileName.endsWith(".json")) {
//						JSONObject content = new JSONObject(FileUtils.readFileToString(file,"utf8"));
//						JSONObject innermostContent = checkIsFlat(content);
//						for(String key:innermostContent.keySet()) {
//							Object val = innermostContent.get(key);
//							if(val instanceof String) {
//								bw.append(key+"||"+val+"\n");
//							} else if(val instanceof Boolean) {
//								booleanCount++;
//							} else if(val instanceof JSONArray) {
//								arrayCount++;
//							}
//							else {
//	//							System.out.println(key + ":" + val);
//								othersCount++;
//							}
//						}
//					}
//				
//			}	
//			bw.flush();
//			bw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("strings:"+stringCount+" bool:"+booleanCount+" array:"+arrayCount+" others:"+othersCount);
		
		booleanCount = 122;
		arrayCount = 93;
		othersCount = 40790;
		long idCount = 0;
		long categoricalCount = 0;
		long testCount = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(outputClassifier));
			String keycount = "/Users/eugene/Documents/Programming/dweet/_keylist.txt";
			BufferedWriter bw = new BufferedWriter(new FileWriter(keycount));
			String line = "";
			while((line=br.readLine())!=null) {
				String[] parts = line.split("\\|\\|");
				if(parts.length>1) {
					String key = parts[0];
					String s = parts[1];
					Matcher m = p.matcher(s);
					if (m.matches()){
						othersCount++;
					} else if(key.toLowerCase().contains("date") || key.toLowerCase().equals("ip") || key.toLowerCase().contains("time") || key.toLowerCase().contains("last")|| key.toLowerCase().contains("temperature") || key.toLowerCase().contains("level")|| key.toLowerCase().equals("ifconfig")) {
						othersCount++;
					} else if(s.toLowerCase().contains("mon") || s.toLowerCase().contains("jan") || s.contains("2016")) {
						othersCount++;
					} else if(key.toLowerCase().equals("core") || key.toLowerCase().equals("speed")|| key.toLowerCase().equals("firmware")|| key.toLowerCase().equals("cpu-freq")) { //with units
						othersCount++;
					} else if(key.toLowerCase().equals("status") || key.toLowerCase().contains("type") || key.toLowerCase().equals("sensor") || key.toLowerCase().equals("osver")|| key.toLowerCase().contains("version")|| key.toLowerCase().contains("model")|| key.toLowerCase().contains("dev")) {
						categoricalCount++;
					} else if(key.toLowerCase().contains("city") || key.toLowerCase().contains("country") || key.toLowerCase().contains("state") || key.toLowerCase().equals("smsstatus")) {
						categoricalCount++;
					} else if(key.toLowerCase().contains("id")) {
						idCount++;
					} else if(s.toLowerCase().equals("off") || s.toLowerCase().equals("on") || s.toLowerCase().equals("true") || s.toLowerCase().equals("false")|| s.toLowerCase().equals("yes") || s.toLowerCase().equals("no")) {
						booleanCount++;
					} else if(s.endsWith(" am") || s.endsWith(" pm")) {
						othersCount++;
					} else if(key.toLowerCase().contains("acc") || key.toLowerCase().contains("altitude")|| key.toLowerCase().contains("heading")) {//accelerometer
						othersCount++;
					} else if(key.toLowerCase().equals("foo") || key.toLowerCase().equals("hello")) {
						testCount++;
					} else if(s.contains("{")) {
						arrayCount++;
					} else if(s.contains(":")) { //time
						othersCount++;
					} else if(s.length()<2) { //single char
						othersCount++;
					}
					else {
//						System.out.println(key + ":" + s);
//						System.out.println(key);
						bw.append(key.toLowerCase().trim()+"\n");
						stringCount++;
					}
				}
			}
			bw.flush();
			bw.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("strings:"+stringCount+"\nbool:"+booleanCount+"\narray:"+arrayCount+"\ncategorical:"+categoricalCount+"\ntest:"+testCount+"\nid:"+idCount+"\nothers:"+othersCount);
		
	}
	
	public static JSONObject checkIsFlat(JSONObject content) {
		for(String key:content.keySet()) {
			if(content.get(key) instanceof JSONObject) {
				return checkIsFlat(content.getJSONObject(key));
			}
		}
		return content;
	}

}
