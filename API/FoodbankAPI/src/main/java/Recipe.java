public class Recipe {
    private int recipe_id;
    private int category;
    private String title;
    private int portions;
    private String descr;
    private Ingredients ingredients;
    private String ingredientsString;
    private String instructions;
    private String image;
    private String link;
    private Ingredients [] ingredientsArray = new Ingredients[100];

    // price


    public Ingredients[] getIngredientsArray() {
        return ingredientsArray;
    }

    public void setIngredientsArray(Ingredients[] ingredientsArray) {
        this.ingredientsArray = ingredientsArray;
    }

    public void addToArray(Ingredients ingredient){
        for (int i =0; i<ingredientsArray.length;i++){
            ingredientsArray[i] = ingredient;
        }
    }


    public String getIngredientsString() {
        return ingredientsString;
    }

    public void setIngredientsString(String ingredientsString) {
        this.ingredientsString = ingredientsString;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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

    public Ingredients getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
