package rpi;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Controller class
 */
public class Controller
{
	private Connector connector;

	private LinkedList<String> relationStrings;

	private ArrayList<String> relationPrices;
	
	public Controller()
	{
		connector = new Connector();
		relationStrings = new LinkedList<>();
		relationPrices = new ArrayList<>();
	}

	/**
	 * Reads all relations from the database and inserts into a LinkedList
	 */
	private void loadRelations()
	{
		ArrayList<String> strings = connector.loadAllRelations();

		if(strings != null)
		{
			for (String string : strings)
			{
				relationStrings.add(string);
			}
		}
	}

	/**
	 * Inserts the calculated prices into the database
	 */
	private void insertPrices()
	{
		int count = 0;

		for(String s : relationStrings)
		{
			String[] array = s.split(" ");
			relationPrices = connector.loadIngredient(Integer.parseInt(array[2]), array);
			System.out.println("Fetching ingredient id: " + array[2]);

			for(String ss : relationPrices)
			{
				String[] priceArray = ss.split(" ");
				System.out.print("Inserting relation price: " + ss + " ... ");
				if(connector.insertRelationPrice(Integer.parseInt(priceArray[0]), Float.parseFloat(priceArray[1])))
				{
					System.out.println(priceArray[0]  + " " + priceArray[1]);
					count++;
					System.out.println("DONE");
				}
				else
					System.out.println("FAILED");
			}

		}
		System.out.println("Successfully added " + relationPrices.size() + " items:");
		System.out.println("Adding prices to Database...");
		System.out.println("Added " + count + " items to database.");
	}
	
	public static void main(String[] args) {
		Controller cont = new Controller();
		cont.loadRelations();
		cont.insertPrices();
	}
}
