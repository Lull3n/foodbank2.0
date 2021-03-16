package ri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * Class that manages the interaction between the java code and the Microsoft SQL Server database
 */
public class Connector
{
	private Connection connection;
	private String dbURL = "jdbc:sqlserver://localhost:1433;" +
			"databaseName=FoodBank;user=javaConnection;password=hejDatabasenFood;";

	public Connector()
	{
		try
		{
			System.out.println("Connecting to MySQL database...");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Loads all the recipes from the database into an ArrayList
	 * @return The recipe names
	 */
	public ArrayList<String> loadRecipes()
	{
		ArrayList<String> result = null;
		try
		{
			connection = DriverManager.getConnection(dbURL);
			System.out.println("connection established");
			String query = "SELECT * FROM recipes";
			Statement statement = connection.createStatement();
			ArrayList<String> results = new ArrayList();

			ResultSet set = statement.executeQuery(query);

			while (set.next())
			{
				results.add(set.getString("title"));
			}
			connection.close();
			result = results;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Loads all the ingredients from a specific recipe
	 * @param title The recipe name
	 * @return The ingredients
	 */
	public String loadRecipeIngredients(String title)
	{
		String ret = "";
		try
		{
			connection = DriverManager.getConnection(dbURL);
			String query = "SELECT * FROM recipes WHERE title='" + title + "'";
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(query);

			if(set.next())
			{
				ret = set.getString("ingredients");
			}

			connection.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Loads a specific recipe and returns the recipe id
	 * @param selectedRecipe The recipe
	 * @return The unique recipe id
	 */
	public int getRecipeId(String selectedRecipe)
	{
		try
		{
			connection = DriverManager.getConnection(dbURL);
			String query = "SELECT * FROM recipes WHERE title='" + selectedRecipe + "'";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if(resultSet != null)
			{
				resultSet.next();
				return resultSet.getInt("recipe_id");
			}
			connection.close();
				
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Inserts a relation into the database
	 * @param recipeId
	 * @param ingredientId
	 * @param ingredientUnit
	 * @return
	 */
	public int sendRelation(int recipeId, String ingredientId, String ingredientUnit)
	{
		int ret = 0;

		try
		{
			connection = DriverManager.getConnection(dbURL);
			String query = "INSERT INTO relations (recipe_id,ingredients_id,units) VALUES (?,?,?)";
			try
			{
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, recipeId);
				statement.setInt(2, Integer.parseInt(ingredientId));
				statement.setInt(3, Integer.parseInt(ingredientUnit));
				ret = statement.executeUpdate();
				connection.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * Loads a specifc ingredient and returns the column
	 * @param result The name of the ingredient
	 * @return The ingredient column
	 */
	public LinkedList<String> loadDatabaseIngredient(String result)
	{
		try
		{
			connection = DriverManager.getConnection(dbURL);
			String query = "SELECT * FROM ingredients2 WHERE title LIKE '%" + result + "%'";
			Statement statement = connection.createStatement();
			ResultSet ingredient = statement.executeQuery(query);
			LinkedList<String> ingredientsList = new LinkedList<>();

			while (ingredient.next())
			{
				System.out.println("Got ingredient: " + ingredient.getString("title"));
				String res = "#";
				res += ingredient.getInt("id") + " ";
				res += ingredient.getString("title") + " - ";
				res += ingredient.getFloat("price") + " ";
				res += ingredient.getString("pricetype");
				ingredientsList.add(res);
				System.out.println("Got ingredient result: " + res);
			}

			System.out.println("Got ingredient with query: " + query);
			connection.close();
			return ingredientsList;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
