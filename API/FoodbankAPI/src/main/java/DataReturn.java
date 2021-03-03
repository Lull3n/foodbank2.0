import java.util.ArrayList;

/**
 * Class representing an recipe object as it should be returned to a client
 */
public class DataReturn {

    private String title;
    private int portions;
    private String description;
    private String instructions;
    private String ingredientString;
    //private int unit;
    private double price;
    private ArrayList<IngredientsInRecipe> ingredientsArray = new ArrayList<IngredientsInRecipe>();
    private String imageLink;

    public String getImageLink() { return imageLink;}

    public void setImageLink(String imageLink) { this.imageLink = imageLink;}

    public ArrayList<IngredientsInRecipe> getIngredientsArray() {
        return ingredientsArray;
    }

    public void setIngredientsArray(ArrayList<IngredientsInRecipe> ingredientsArray) { this.ingredientsArray = ingredientsArray;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getIngredientString() {
        return ingredientString;
    }

    public void setIngredientString(String ingredientString) {
        this.ingredientString = ingredientString;
    }
//
//    public int getUnit() {
//        return unit;
//    }
//
//    public void setUnit(int unit) {
//        this.unit = unit;
//    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
