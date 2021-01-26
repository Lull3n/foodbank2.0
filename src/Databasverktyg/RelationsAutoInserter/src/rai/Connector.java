package rai;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connector {
private Connection connection;
	private String dbURL = "jdbc:sqlite:../../database/sqliteDb.db";
	
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
	
	public ResultSet loadRecipe(int id) {
		try {
			connection = DriverManager.getConnection(dbURL);

			String query = "SELECT * FROM aj1757.recipes WHERE recipe_id =" + id;
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getDatabaseIngredients(String like) {
		try {
			String query = "SELECT * FROM aj1757.ingredients2 WHERE title LIKE '%" + like + "%'";
			System.out.println(query);
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(query);
			return set;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getRecipePortions(String title) {
		try {
			String query = "SELECT * FROM aj1757.recipes WHERE title='" + title + "'";
			System.out.println(query);
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(query);
			return set;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendRelation(Item i, int recipeId) {
		try {
			String query = "INSERT INTO aj1757.relations (recipe_id,ingredients_id,units) VALUES (?,?,?)";
			String queryTest;
			queryTest = "INSERTING: " + recipeId + " " + i.getPriceItem().getId() + " " + i.getUnits();
			System.out.println(queryTest);
			PreparedStatement statement = connection.prepareStatement(query);
//			statement.setInt(1, recipeId);
//			statement.setInt(2, i.getPriceItem().getId());
//			statement.setFloat(3, i.getUnits());
//			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
