package ri;

import java.text.MessageFormat;

public class Controller
{
	public static void main(String[] args) {
		new Controller();
	}
	
	private Connector connector;

	public Controller()
	{
		GUI gui = new GUI();
		gui.setController(this);
		connector = new Connector();
	}

	/** Adjusting the user input into the correct message format and sends it to the Connector class
	 * @param category Category of the recipe
	 * @param title Title of the recipe
	 * @param description Description of the recipe
	 * @param portions Amount of portions with the recipe
	 * @param link Link to the recipe on the web
	 * @param imageLink Picture of the recipe
	 * @param ingredients The ingredients of the recipe
	 * @param instructions How to cook the recipe
	 */
	public void submit(int category, String title, String description, int portions, String link, String imageLink,
			String ingredients, String instructions)
	{
		System.out.println("Formatting strings...");
		String ingredientsFix;
		String instructionsFix;
		ingredientsFix = ingredients.replaceAll("\n", "\\\\");
		instructionsFix = instructions.replaceAll("\n", "\\\\");
		System.out.println("Formatted strings");
		System.out.println(
				"Issuing statement to MySQL database: " + MessageFormat.format("{0} {1} {2} {3} {4} {5} {6} {7}",
						category, title, description, portions, link, imageLink, ingredientsFix, instructionsFix));
		connector.query(category, title, description, portions, link, imageLink, ingredientsFix, instructionsFix);
	}
}
