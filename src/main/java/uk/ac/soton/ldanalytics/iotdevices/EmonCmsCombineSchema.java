package uk.ac.soton.ldanalytics.iotdevices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class EmonCmsCombineSchema {
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
		
		String folderPath = "/Users/eugene/Documents/Programming/EmonCms/catalog/";
		String outputPath = "/Users/eugene/Documents/Programming/EmonCms/catalog.txt";

		File folder = new File(folderPath);
		int wide = 0;
		int tall = 0;
		try {
			BufferedWriter bw =new BufferedWriter(new FileWriter(outputPath));
			for(File file:folder.listFiles()) {
				Map<String,Map<String,String>> schemas = new HashMap<String,Map<String,String>>();
				String tempFileName = file.getName();
//				System.out.println(tempFileName);
				if(tempFileName.endsWith(".json")) {
					String userId = tempFileName.replace(".json", "");
					JSONArray feeds = new JSONArray(FileUtils.readFileToString(file,StandardCharsets.UTF_8));
					for(int i=0;i<feeds.length();i++) {
						JSONObject feed = feeds.getJSONObject(i);
						String key = feed.get("time").toString() + "," + feed.getString("tag");
//						String key = feed.get("time").toString();
//						String key = "1";
						Map<String,String> schema = schemas.get(key);
						if(schema==null) {
							schema = new HashMap<String,String>();
						}
						String id = feed.getString("id");
						String name = feed.getString("name");
						schema.put(id, name);
						schemas.put(key, schema);
						String value = feed.get("value").toString();
						Matcher m = p.matcher(value);
						if (!m.matches()){
							System.out.println(value);
						}
					}
					for(Entry<String, Map<String, String>> schema:schemas.entrySet()) {
						if(schema.getValue().size()==1) 
							tall++;
						else
							wide++;
						bw.append(userId+","+schema.getKey()+","+schema.getValue().size()+",");
						for(String name:schema.getValue().values()) {
							bw.append(name+",");
						}
						bw.newLine();
					}
				}
			}
			System.out.println("tall:"+tall+",wide:"+wide);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
