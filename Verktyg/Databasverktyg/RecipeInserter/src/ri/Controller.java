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
	 * @param category
	 * @param title
	 * @param description
	 * @param portions
	 * @param link
	 * @param imageLink
	 * @param ingredients
	 * @param instructions
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
