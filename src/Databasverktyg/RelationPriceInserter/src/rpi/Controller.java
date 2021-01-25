package rpi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Controller {
	private Connector connector;
	
	private LinkedList<String> relationStrings;
	private ArrayList<String> relationPrices;
	
	public Controller() {
		connector = new Connector();
		
		relationStrings = new LinkedList<String>();
		relationPrices = new ArrayList<String>();
	}
	
	private void loadRelations() throws SQLException {
		ResultSet set = connector.loadAllRelations();
		
		while(set.next()) {
			relationStrings.add(set.getInt("relation_id") + " " + set.getInt("recipe_id") + " " + set.getInt("ingredients_id") + " " + set.getInt("units"));
		}
	}
	
	private void insertPrices() throws SQLException {
		for(String s : relationStrings) {
			String[] array = s.split(" ");
			ResultSet set = connector.loadIngredient(Integer.parseInt(array[2]));
			System.out.println("Fetching ingredient id: " + array[2]);
			while(set.next()) {
				System.out.println("Calculating price for ingredient id: " + array[2]);
				float relationUnits = Float.parseFloat(array[3]);
				System.out.println("Relation units before division: " + relationUnits);
				float relationUnitsDivided = (relationUnits / 1000);
				System.out.println("Relation units after division: " + relationUnitsDivided);
				float relationPrice = relationUnitsDivided * set.getFloat("price");
				System.out.println("Calculated price: " + relationPrice);
				System.out.println("Inserting string into array: " + array[0] + " " + relationPrice);
				relationPrices.add(array[0] + " " + relationPrice);
				System.out.println("------------------------------------------------");
			}
		}
		System.out.println("Successfully added " + relationPrices.size() + " items:");
		
		System.out.println("Adding prices to Database...");
		
		int count = 0;
		for(String s : relationPrices) {
			String[] priceArray = s.split(" ");
			System.out.print("Inserting relation price: " + s + " ... ");
			if(connector.insertRelationPrice(Integer.parseInt(priceArray[0]), Float.parseFloat(priceArray[1]))) {
				count++;
				System.out.println("DONE");
			} else
				System.out.println("FAILED");
		}
		
		System.out.println("Added " + count + " items to database.");
	}
	
	public static void main(String[] args) throws SQLException {
		Controller cont = new Controller();
		cont.loadRelations();
		cont.insertPrices();
	}
}
