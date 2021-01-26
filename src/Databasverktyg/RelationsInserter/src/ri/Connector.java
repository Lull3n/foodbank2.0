package ri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

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
			String query = "SELECT * FROM recipes WHERE title='" + selectedRecipe + "'";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet != null) {
				resultSet.next();
				return resultSet.getInt("recipe_id");
			}
				
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

	public ResultSet loadDatabaseIngredient(String result) {
		try {
			connection = DriverManager.getConnection(dbURL);
			String query = "SELECT * FROM ingredients2 WHERE title LIKE '%" + result + "%'";
			Statement statement = connection.createStatement();
			System.out.println("Got ingredient with query: " + query);
			return statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
