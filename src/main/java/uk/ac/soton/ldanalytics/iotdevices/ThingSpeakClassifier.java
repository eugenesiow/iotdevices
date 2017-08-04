package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ThingSpeakClassifier {
	public static void main(String[] args) {
		int numerical = 0;
		int other = 0;
		int nullVal = 0;
		String[] dictionary = {"voltage","pressure","current","light","moisture","capacidad","ldr","power"};
		List<String> dictionaryList = Arrays.asList(dictionary);
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
		
		String path = "/Users/eugene/Documents/Programming/ThingSpeak/";
		String inputPath = path + "feeds/";
		String outputPath = path + "nullFields.txt";
		
		File folder = new File(inputPath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
			for(File file:folder.listFiles()) {
				String tempFileName = file.getName();
				if(tempFileName.endsWith(".json")) {
					JSONObject data = new JSONObject(FileUtils.readFileToString(file,StandardCharsets.UTF_8));
					JSONObject channel = data.getJSONObject("channel");
					JSONArray feeds = data.getJSONArray("feeds");
					//check fields
					if(feeds.length()>0) {
						JSONObject feed = feeds.getJSONObject(0);
						for(String key:feed.keySet()) {
							if(key.startsWith("field")) {
								String value = feed.get(key).toString().trim();
								Matcher m = p.matcher(value);
								if (m.matches()){
									numerical++;
								} else {
//									System.out.println(feed.get(key));
									if(value.equals("") || value.equals("null") || value.contains("NA") || value.contains("N/A")) {
										String fieldKey = channel.getString(key).trim().toLowerCase();
										if(dictionaryList.contains(fieldKey) || fieldKey.contains("temp") || fieldKey.contains("hum") ||  fieldKey.contains("count") || fieldKey.contains("time") || fieldKey.contains("point")) {
											numerical++;
										} else {
											nullVal++;
											bw.append(fieldKey);
											bw.newLine();
										}
									}
									else
										other++;
									
								}
							}
						}
					}
				}
			}
			bw.close();
			System.out.println("numerical:" + numerical + ",other:" + other +",null:"+nullVal);
			int total = other+numerical+nullVal;
			System.out.println("numerical%:" + numerical/(total*1.0));
			System.out.println("null%:" + nullVal/(total*1.0));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
