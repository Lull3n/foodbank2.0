package rai;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *  Class that manages the interaction between the java code and the Microsoft SQL Server database
 */
public class Connector {
private Connection connection;
	//com.microsoft.sqlserver:mssql-jdbc:8.4.1.jre14
	private String dbURL = "jdbc:sqlserver://localhost:1433;" +
			"databaseName=FoodBank;user=javaConnection;password=hejDatabasenFood;";
	
	public Connector() {
		try {
			//System.out.println("Connecting to MySQL database...");
			//Class.forName("com.mysql.jdbc.Driver");
			//connection = DriverManager.getConnection("jdbc:mysql://195.178.232.16:3306/aj1757","aj1757","foodbank123");
			//System.out.println("Successfully connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that loads a specific recipe
	 * @param id Unique id of the recipe
	 * @return RecipeEntity object including ingredients and portions
	 */
	public RecipeEntity loadRecipe(int id) {
		RecipeEntity ret = null;
		try {
			connection = DriverManager.getConnection(dbURL);

			String query = "SELECT * FROM recipes WHERE recipe_id =" + id;
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(query);
			if(set.next()) {
				ret = new RecipeEntity( set.getString("ingredients").split("\\\\"), set.getInt("portions"));
				connection.close();
				return ret;
			}
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Method that loads a specific ingredient
	 * @param like The ingredient name
	 * @return PriceItem object including the ingredient id, name, price and pricetype
	 */
	public LinkedList<PriceItem> getDatabaseIngredients(String like) {
		LinkedList<PriceItem> ret = new LinkedList<>();
		try {
			connection = DriverManager.getConnection(dbURL);
			String query = "SELECT * FROM ingredients2 WHERE title LIKE '%" + like + "%'";
			System.out.println(query);
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(query);


			if(!set.next()) {
				ret.add(new PriceItem(0, "EMPTY", 0, "EMPTY"));
			} else {
				do {
					ret.add(new PriceItem(set.getInt("id"), set.getString("title"), set.getFloat("price"), set.getString("pricetype")));
				} while(set.next());
			}
			connection.close();
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method that inserts a relation to the database
	 * @param i Item object
	 * @param recipeId Unique recipe id
	 */
	public void sendRelation(Item i, int recipeId) {
		try {
			connection = DriverManager.getConnection(dbURL);
			String query = "INSERT INTO relations (recipe_id,ingredients_id,units) VALUES (?,?,?)";
			String queryTest;
			queryTest = "INSERTING: " + recipeId + " " + i.getPriceItem().getId() + " " + i.getUnits();
			System.out.println(queryTest);
			PreparedStatement statement = connection.prepareStatement(query);
//			statement.setInt(1, recipeId);
//			statement.setInt(2, i.getPriceItem().getId());
//			statement.setFloat(3, i.getUnits());
//			statement.executeUpdate();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
