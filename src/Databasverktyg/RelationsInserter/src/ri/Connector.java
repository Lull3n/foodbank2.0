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

public class Connector {
	private Connection connection;
	String dbURL = "jdbc:sqlite:../../database/sqliteDb.db";
	public Connector() {

		try {
			System.out.println("Connecting to MySQL database...");
			//Class.forName("com.mysql.jdbc.Driver");
			//connection = DriverManager.getConnection("jdbc:sqlite:../../database/sqliteDb.db");
			//System.out.println("Successfully connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> loadRecipes() {
		try {
			connection = DriverManager.getConnection(dbURL);
			System.out.println("connection established");
			String query = "SELECT * FROM recipes";
			Statement statement = connection.createStatement();
			ArrayList<String> results = new ArrayList();

			ResultSet set = statement.executeQuery(query);
			while (set.next()){
				results.add(set.getString("title"));
			}
			connection.close();
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String loadRecipeIngredients(String title) {
		String ret = "";
		try {
			connection = DriverManager.getConnection(dbURL);
			String query = "SELECT * FROM recipes WHERE title='" + title + "'";
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(query);

			if(set.next()){
				ret = set.getString("ingredients");
			}

			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public ResultSet loadDatabaseIngredients(String result) {
		try {
			String query = "SELECT * FROM ingredients2 WHERE title LIKE '%" + result + "%'";
			Statement statement = connection.createStatement();
			System.out.println("Got ingredient with query: " + query);
			return statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getRecipeId(String selectedRecipe) {
		try {
			connection = DriverManager.getConnection(dbURL);
			String query = "SELECT * FROM recipes WHERE title='" + selectedRecipe + "'";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet != null) {
				resultSet.next();
				return resultSet.getInt("recipe_id");
			}
			connection.close();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void sendRelation(int recipeId, String ingredientId, String ingredientUnit) {
		try {
			String query = "INSERT INTO relations (recipe_id,ingredients_id,units) VALUES (?,?,?)";
			try {
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, recipeId);
				statement.setInt(2, Integer.parseInt(ingredientId));
				statement.setInt(3, Integer.parseInt(ingredientUnit));
				statement.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LinkedList<String> loadDatabaseIngredient(String result) {
		try {
			connection = DriverManager.getConnection(dbURL);
			String query = "SELECT * FROM ingredients2 WHERE title LIKE '%" + result + "%'";
			Statement statement = connection.createStatement();
			ResultSet ingredient = statement.executeQuery(query);
			LinkedList<String> ingredientsList = new LinkedList<>();
			while (ingredient.next()) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
