package ri;

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
			//Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:sqlite:../../database/sqliteDb.db");
			System.out.println("Successfully connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean query(int category, String title, String description, int portions, String link, String imageLink,
			String ingredients, String instructions) {
		try {
			String query = "INSERT INTO recipes (category,title,portions,descr,ingredients,instructions,image,link) VALUES (?,?,?,?,?,?,?,?)";
			try {
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, category);
				statement.setString(2, title);
				statement.setInt(3, portions+2);
				statement.setString(4, description);
				statement.setString(5, ingredients);
				statement.setString(6, instructions);
				statement.setString(7, imageLink);
				statement.setString(8, link);
				statement.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
