package rpi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
			System.out.println("Connecting to MySQL database...");

			System.out.println("Successfully connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet loadRelations(int recipeId) {
		try {
			String query = "SELECT * FROM Foodbank.dbo.relations WHERE recipe_id=" + recipeId;
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> loadAllRelations() {
		try {
			connection = DriverManager.getConnection(dbURL, user, password);
			String query = "SELECT * FROM Foodbank.dbo.relations";
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(query);
			ArrayList<String> ret = null;
			while (set.next()){
				if(ret == null) ret = new ArrayList<>();
				String string = set.getInt("relation_id") + " " + set.getInt("recipe_id") + " " + set.getInt("ingredients_id") + " " + set.getInt("units");
				ret.add(string);
			}
			connection.close();
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<String> loadIngredient(int id, String[] array) {
		try {
			connection = DriverManager.getConnection(dbURL);
			String query = "SELECT * FROM Foodbank.dbo.ingredients2 WHERE id=" + id;
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(query);
			ArrayList<String> relationPrices = null;
			while(set.next()) {
				if(relationPrices == null) relationPrices = new ArrayList<>();
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

			connection.close();
			return relationPrices;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean insertRelationPrice(int id, float price) {
		try {
			connection = DriverManager.getConnection(dbURL);
			String query = "UPDATE Foodbank.dbo.relations SET price=" + price + " WHERE relation_id=" + id;
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
