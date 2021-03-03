package rpi;

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
		ArrayList<String> strings = connector.loadAllRelations();
		if(strings != null) {
			for (String string : strings) {
				relationStrings.add(string);
			}
		}
	}
	
	private void insertPrices() throws SQLException {
		for(String s : relationStrings) {
			String[] array = s.split(" ");
			relationPrices = connector.loadIngredient(Integer.parseInt(array[2]), array);
			System.out.println("Fetching ingredient id: " + array[2]);
			//for (String string : prices) relationPrices.add(string);

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
