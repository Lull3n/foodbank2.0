package rai;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Controller {
	private Connector connector;
	private LinkedList<Item> items;
	private LinkedList<String> itemStrings;
	private LinkedList<PriceItem> priceItems;
	private LinkedList<PriceItem> finalPriceItems;
	private ExcludeMap excludeMap;
	private WordParser wordParser;
	private WordList wordList;
	private WordMap wordMap;
	private UnitMap unitMap;


	
	private int currentRecipeId = 8;
	
	private int recipePortions;
	
	public static void main(String[] args) {
		Controller cont = new Controller();
		cont.getRecipeIngredients();
	}
	
	public Controller() {
		connector = new Connector();
		items = new LinkedList<Item>();
		priceItems = new LinkedList<PriceItem>();
		finalPriceItems = new LinkedList<PriceItem>();
		excludeMap = new ExcludeMap();
		wordParser = new WordParser();
		wordList = new WordList();
		wordMap = new WordMap();
		unitMap = new UnitMap(wordMap, wordList, wordParser);
		itemStrings = new LinkedList<String>();
	}
	
	private void getRecipeIngredients() {
	 	RecipeEntity recipe = connector.loadRecipe(currentRecipeId);
	 	if(recipe != null) {
			recipePortions = recipe.getPortions();
			String[] ingredientsArray = recipe.getIngredients();
			for (String s : ingredientsArray) {
				getIngredients(s);
			}
			combineLists();
		}
	}
	
	public int getRecipeId() {
		return currentRecipeId;
	}
	
	private void getIngredients(String title) {
		if (title == null)
			return;
		System.out.println(title);
		String[] array = title.split(" ");
		String result = "";
		String[] resultArray;
		for (int i = 0; i < array.length; i++) {
			if (wordParser.boolParse(array[i]) || wordList.contains(array[i].toLowerCase())) {
				System.out.println(array[i]);
			} else {
				String tempString = wordParser.removeChar(array[i], ',');
				if(wordList.contains(tempString))
					System.out.println(tempString);
				else
					result += wordParser.removeChar(array[i], ',') + " ";
				
			}
		}
		if (result != "") {
			result = result.substring(0, result.length() - 1).toLowerCase();
			result = wordMap.get(result);
			System.out.println("Result: " + result);
			String parsedUnits = parseItemUnits(title);
			parseItemUnits(parsedUnits, result, recipePortions);
			getDatabaseIngredients(result);
		}
	}
	
	private void combineLists() {
		int index = 1;
		for(String s : itemStrings) {
			System.out.println("Final selected item units " + index + ": " + s);
			index++;
		}
		index = 1;
		for(PriceItem pi : finalPriceItems) {
			System.out.println("Final selected item " + index + ": " + pi.toString());
			index++;
		}
		for(int i = 0 ; i < finalPriceItems.size() ; i++) {
			String tempItemString = itemStrings.get(i);
			String tempTitle = tempItemString.substring(0, tempItemString.indexOf(":"));
			float tempUnit = Float.parseFloat(tempItemString.substring(tempItemString.indexOf(":")+1, tempItemString.length()));
			items.add(new Item(tempTitle, tempUnit, finalPriceItems.get(i), finalPriceItems.get(i).getPriceType()));
		}
		index = 1;
		for(Item i : items) {
			System.out.println("Final item " + index + ": " + i.toString());
			connector.sendRelation(i, currentRecipeId);
			index++;
		}
		
	}

	private String parseItemUnits(String title) {
		return unitMap.parseUnits(title);
	}
	
	private void parseItemUnits(String units, String title, int portions) {
		String parsedItem = unitMap.parseUnits(units, title, portions);
		String[] parsedItemArray = parsedItem.split(":");
//		if(parsedItemArray[0].equals("salt") || parsedItemArray[0].equals("peppar")) {
//			System.out.println("Is salt or pepper");
//			return;
//		}
		itemStrings.add(parsedItem);
	}

	private void getDatabaseIngredients(String query) {
		System.out.println("Getting ingredients like: " + query);
		priceItems = connector.getDatabaseIngredients(query);

		if(priceItems != null) excludeItems(query);
	}
	
	private void excludeItems(String item) {
		String excludeItems = excludeMap.get(item);
		if(excludeItems != null) {
			String[] excludeItemsArray = excludeItems.split(",");
			for(String s : excludeItemsArray) {
				Iterator iter = priceItems.iterator();
				while(iter.hasNext()) {
					String str = iter.next().toString();
					if(str.toLowerCase().contains(s))
						iter.remove();
				}
			}
			
		}
		System.out.println("--------");
		for(PriceItem pi : priceItems)
			System.out.println(pi.toString());
		if(!priceItems.isEmpty())
			finalPriceItems.add(priceItems.getFirst());
		else
			finalPriceItems.add(new PriceItem(0, "EMPTY", 0, "EMPTY"));	
	}
}
