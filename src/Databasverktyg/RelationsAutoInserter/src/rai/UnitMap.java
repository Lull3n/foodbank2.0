package rai;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class UnitMap {
	private static final String filepathWeight = "files/weightmap.txt";
	private static final String filepathType = "files/typemap.txt";
	private HashMap<String, String> weightMap = new HashMap<String, String>();
	private HashMap<String, String> typeMap = new HashMap<String, String>();
	
	private static final String STYCK = "styck";
	private static final String KILOGRAM = "kilogram";
	private static final String GRAM = "gram";
	private static final String DECILITER = "deciliter";
	private static final String LITER = "liter";
	private static final String MATSKED = "matsked";
	private static final String TESKED = "tesked";
	private static final String PORTION = "portion";
	private static final String KRYDDMÅTT = "kryddmått";
	
	private WordMap wordMap;
	private WordList wordList;
	private WordParser wordParser;
	
	public UnitMap(WordMap wordMap, WordList wordList, WordParser wordParser) {
		this.wordMap = wordMap;
		this.wordList = wordList;
		this.wordParser = wordParser;
		readMaps();
	}

	private void readMaps() {
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filepathWeight)));
			weightMap.clear();
			while(true) {
				String value = reader.readLine();
				if(value == null)
					break;
				if(value != "-") {
					while(true) {
						String key = reader.readLine();
						if(key == null || key.equals("-"))
							break;
						System.out.println("Key: " + key + " Value: " + value);
						weightMap.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filepathType)));
			typeMap.clear();
			while(true) {
				String value = reader.readLine();
				if(value == null)
					break;
				if(value != "-") {
					while(true) {
						String key = reader.readLine();
						if(key == null || key.equals("-"))
							break;
						System.out.println("Key: " + key + " Value: " + value);
						typeMap.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String parseUnits(String string) {
		String[] stringArray = string.split(" ");
		for(String s : stringArray) {
			System.out.println("Got array: " + s);
		}
		if(stringArray[0].equals("ca") || stringArray[0].equals("circa")) { // Removes "ca" and "circa" from array
			String[] tempStringArray = new String[stringArray.length-1];
			for(int i = 1 ; i < stringArray.length ; i++) {
				tempStringArray[i-1] = stringArray[i];
			}
			stringArray = tempStringArray.clone();
		}
		String unit = null;
		String unitType = null;
		if(wordParser.boolParse(stringArray[0])) {
			if(stringArray[0].contains("-"))
				unit = stringArray[0].substring(0, stringArray[0].indexOf("-"));
			else 
				unit = stringArray[0];
			System.out.println("Pre-cleanup: " + unit);
			for(String s : wordList.getValuesList()) {
				if(unit.contains(s)) {
					unitType = unit.substring(unit.indexOf(s.charAt(0)), unit.length());
					if(typeMap.containsKey(unitType))
						unitType = typeMap.get(unitType);
					System.out.println("Grabbed Unit Type: " + unitType);
					unit = unit.substring(0, unit.indexOf(s.charAt(0)));
				}
			}
			System.out.println("Post-cleanup: " + unit);
		}
		if(unitType == null) {
			if(stringArray.length > 1 ) {
				if(typeMap.containsKey(stringArray[1]))
					unitType = typeMap.get(stringArray[1]);
				else {
					unitType = UnitMap.STYCK;
				}
			} else {
				if(typeMap.containsKey(stringArray[0]))
					unitType = typeMap.get(stringArray[0]);
				else {
					unitType = UnitMap.STYCK;
				}
			}
		}
		if(unit == null)
			unit = "1";
		System.out.println("Unit Type: " + unitType);
		return unit + ":" + unitType;
	}

	public String parseUnits(String units, String title, int portions) {
		System.out.println("Checking Units for Ingredient: " + title);
		String[] unitsArray = units.split(":");
		for(String s : unitsArray) {
			System.out.println("Got array: " + s);
		}
		float weight = 0;
		String unitsNumber = unitsArray[0].replaceAll(",", ".");
		System.out.println("Parsed Unit Number: " + unitsNumber);
		switch(unitsArray[1]) {
		case UnitMap.STYCK:
			if(weightMap.containsKey(title))
				weight = Integer.parseInt(weightMap.get(title)) * Float.parseFloat(unitsNumber);
			System.out.println(title + ": " + weight);
			break;
		case UnitMap.GRAM:
			weight = Float.parseFloat(unitsNumber);
			break;
		case UnitMap.KILOGRAM:
			weight = Float.parseFloat(unitsNumber) * 1000;
			break;
		case UnitMap.DECILITER:
			weight = Float.parseFloat(unitsNumber) * 100;
			break;
		case UnitMap.LITER:
			weight = Float.parseFloat(unitsNumber) * 1000;
			break;
		case UnitMap.MATSKED:
			weight = Float.parseFloat(unitsNumber) * 15;
			break;
		case UnitMap.TESKED:
			weight = Float.parseFloat(unitsNumber) * 5;
			break;
		case UnitMap.KRYDDMÅTT:
			weight = Float.parseFloat(unitsNumber) * 3;
		case UnitMap.PORTION:
			if(weightMap.containsKey(title))
				weight = Math.round(Integer.parseInt(weightMap.get(title)) * Float.parseFloat(unitsNumber) / portions);
			break;
		default:
			weight = Math.round(Float.parseFloat(unitsNumber));
			break;
		}
		System.out.println("Calculated weight units: " + weight);
		return title + ":" + weight;
	}
}
