package rai;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

public class Connector {
	private Connection connection; //com.microsoft.sqlserver:mssql-jdbc:8.4.1.jre14
	private String dbURL;
	private String user, password;
	public Connector() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			user = "onlinestore";
			password = "project50";

			dbURL = "jdbc:sqlserver://localhost";
			System.out.println("Successfully connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public RecipeEntity loadRecipe(int id) {
		RecipeEntity ret = null;
		try {
			connection = DriverManager.getConnection(dbURL, user, password);

			String query = "SELECT * FROM Foodbank.dbo.recipes WHERE recipe_id =" + id;
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
	
	public LinkedList<PriceItem> getDatabaseIngredients(String like) {
		LinkedList<PriceItem> ret = new LinkedList<>();
		try {
			connection = DriverManager.getConnection(dbURL);
			String query = "SELECT * FROM Foodbank.dbo.ingredients2 WHERE title LIKE '%" + like + "%'";
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
	
	public ResultSet getRecipePortions(String title) {
		try {
			String query = "SELECT * FROM Foodbank.dbo.recipes WHERE title='" + title + "'";
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
			connection = DriverManager.getConnection(dbURL);
			String query = "INSERT INTO Foodbank.dbo.relations (recipe_id,ingredients_id,units) VALUES (?,?,?)";
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
