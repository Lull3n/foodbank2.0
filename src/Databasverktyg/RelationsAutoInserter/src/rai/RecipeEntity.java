package rai;

public class RecipeEntity {
    private String[] ingredients;
    private int portions;

    public RecipeEntity(String[] ingredients, int portions){
        this.ingredients = ingredients;
        this.portions = portions;
    }

    public int getPortions() {
        return portions;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }
}
