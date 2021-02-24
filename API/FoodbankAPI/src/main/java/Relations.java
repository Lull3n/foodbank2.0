public class Relations {
    private int relations_id;
    private int recipe_id;
    private int ingredient_id;
    private int units;
    private double price;

    public int getRelations_id() {
        return relations_id;
    }

    public void setRelations_id(int relations_id) {
        this.relations_id = relations_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
