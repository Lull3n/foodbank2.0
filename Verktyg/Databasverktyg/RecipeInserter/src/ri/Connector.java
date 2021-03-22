package ri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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
			connection = DriverManager.getConnection(dbURL);
			System.out.println("Successfully connected");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Inserts a recipe to the database
	 * @param category Category of the recipe
	 * @param title Title of the recipe
	 * @param description Description of the recipe
	 * @param portions Amount of portions with the recipe
	 * @param link Link to the recipe on the web
	 * @param imageLink Picture of the recipe
	 * @param ingredients The ingredients of the recipe
	 * @param instructions How to cook the recipe
	 * @return boolean value
	 */
	public boolean query(int category, String title, String description, int portions, String link, String imageLink,
			String ingredients, String instructions)
	{
		try
		{
			String query = "INSERT INTO recipes (category,title,portions,descr,ingredients,instructions,image,link) VALUES (?,?,?,?,?,?,?,?)";

			try
			{
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
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
