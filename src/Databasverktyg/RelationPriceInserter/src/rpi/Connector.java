package rpi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Connector {
	private Connection connection;
	
	public Connector() {
		try {
			System.out.println("Connecting to MySQL database...");
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://195.178.232.16:3306/aj1757","aj1757","foodbank123");
			System.out.println("Successfully connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet loadRelations(int recipeId) {
		try {
			String query = "SELECT * FROM aj1757.relations WHERE recipe_id=" + recipeId;
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet loadAllRelations() {
		try {
			String query = "SELECT * FROM aj1757.relations";
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet loadIngredient(int id) {
		try {
			String query = "SELECT * FROM aj1757.ingredients2 WHERE id=" + id;
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean insertRelationPrice(int id, float price) {
		try {
			String query = "UPDATE aj1757.relations SET price=" + price + " WHERE relation_id=" + id;
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
