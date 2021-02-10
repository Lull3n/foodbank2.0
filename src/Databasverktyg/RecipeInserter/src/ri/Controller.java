package ri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Date;

public class Controller {
	public static void main(String[] args) {
		new Controller();
	}
	
	private Connector connector;

	public Controller() {
		GUI gui = new GUI();
		gui.setController(this);
		connector = new Connector();
	}

	public String submit(int category, String title, String description, int portions, String link, String imageLink,
			String ingredients, String instructions) {
		System.out.println("Formatting strings...");
		String ingredientsFix;
		String instructionsFix;
		ingredientsFix = ingredients.replaceAll("\n", "\\\\");
		instructionsFix = instructions.replaceAll("\n", "\\\\");
		System.out.println("Formatted strings");
		System.out.println(
				"Issuing statement to MySQL database: " + MessageFormat.format("{0} {1} {2} {3} {4} {5} {6} {7}",
						category, title, description, portions, link, imageLink, ingredientsFix, instructionsFix));
		 boolean test = connector.query(category, title, description, portions, link, imageLink, ingredientsFix, instructionsFix);
		 if (test)
		 	return "Passed";
		 else
		 	return "Failed";
	}
}
