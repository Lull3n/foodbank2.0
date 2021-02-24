import java.util.ArrayList;

public class DataReturn {

    private String title;
    private int portions;
    private String descr;
    private String instructions;
    private String amount;
    private String ingredientString;
    private int unit;
    private double price;
    private Ingredients ingredients;
    public DataReturn() {//ArrayList<Recipe> recipe, ArrayList<Relations> relations
        ingredients = new Ingredients();
    }

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

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIngredientString() {
        return ingredientString;
    }

    public void setIngredientString(String ingredientString) {
        this.ingredientString = ingredientString;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Ingredients getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients ingredients) {

//        ingredients.setUnit(getUnit());
  //      ingredients.setName(getTitle());
        this.ingredients = ingredients;
    }
}
