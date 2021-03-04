import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a controller
 * The purpose of this class is to get objects from DatabaseConnect and parse the objects
 * to JSON objects. The API class will then call upon these json objects whenever an
 * end user is sending request to the API.
 */
public class Controller {

    /**
     * Creates an arrayList with Recipe-objects, representing the recipe table in the database
     **/
    public static ArrayList<Recipe> getRecipeFromDatabase() {
        ArrayList<Recipe> list =DatabaseRunner.selectRecipe();
        return list;
    }

    /**
     * Creates an arrayList with Relations-objects, representing the relations table in the database
     **/
    public static ArrayList<Relations> getRelationsFromDatabase() {
        ArrayList<Relations> list = DatabaseRunner.selectRelations();
        return list;
    }

    /**
     * Creates an arrayList with Ingredient-objects, representing the ingredient table in the database
     **/
    public static ArrayList<Ingredient> getIngredientsFromDatabase(){
        ArrayList<Ingredient> list = DatabaseRunner.createIngredientList();
        return list;
    }

    /**
     * Creates an arrayList with representations of all recipes and included relations from the database
     **/
    public static ArrayList<DataReturn> createDataReturnAllRecipes() {
        ArrayList<Recipe> recipeList = getRecipeFromDatabase();
        ArrayList<Relations> relationsList = getRelationsFromDatabase();
        ArrayList<Ingredient> ingredientsList = getIngredientsFromDatabase();
        ArrayList<DataReturn> dataReturnObjects = new ArrayList<>();
        HashMap<Integer, Ingredient> ingredientHashMap = new HashMap<Integer, Ingredient>();
        for (Ingredient ingredient : ingredientsList) {
            ingredientHashMap.put(ingredient.getIngredient_id(), ingredient);
        }

        for (Recipe rec : recipeList) {
            DataReturn newDataReturn = new DataReturn();
            newDataReturn.setTitle(rec.getTitle());
            newDataReturn.setPortions(rec.getPortions());
            newDataReturn.setDescription(rec.getDescription());
            newDataReturn.setInstructions(rec.getInstructions());
            newDataReturn.setImageLink(rec.getImageLink());
            double price = 0;

            ArrayList<IngredientsInRecipe> ingredientList = new ArrayList<IngredientsInRecipe>();

            for (int i = 0; i < relationsList.size(); i++) {
                if (rec.getRecipe_id() == relationsList.get(i).getRecipe_id()) {
                    IngredientsInRecipe ingredientsInRecipe = new IngredientsInRecipe();
                    ingredientsInRecipe.setIngredientName(ingredientHashMap.get(relationsList.get(i).getIngredient_id()).getIngredientTitle());
                    ingredientsInRecipe.setUnitsOfIngredient(relationsList.get(i).getUnits());
                    ingredientsInRecipe.setIngredientPriceInRecipe(relationsList.get(i).getPrice());
                    ingredientList.add(ingredientsInRecipe);
                    price += (relationsList.get(i).getPrice());
                }
            }

            newDataReturn.setPrice(price);
            newDataReturn.setIngredientsArray(ingredientList);
            dataReturnObjects.add(newDataReturn);
        }

        return dataReturnObjects;
    }

    public static JsonArray convertAllRecipesToJson() {
        ArrayList<DataReturn> dataFromDb = createDataReturnAllRecipes();
        Gson gson = new Gson();
        JsonArray allRecipes=new JsonArray();

        for(int i=0; i<dataFromDb.size(); i++) {
            JsonElement title = gson.toJsonTree(dataFromDb.get(i).getTitle());
            JsonElement portions = gson.toJsonTree(dataFromDb.get(i).getPortions());
            JsonElement description = gson.toJsonTree(dataFromDb.get(i).getDescription());
            JsonElement instructions = gson.toJsonTree(dataFromDb.get(i).getInstructions());
            JsonElement price = gson.toJsonTree(dataFromDb.get(i).getPrice());
            JsonElement imageLink = gson.toJsonTree(dataFromDb.get(i).getImageLink());

            JsonArray ingredientsArray=new JsonArray();

            int nbrOfIngredientsInRecipe=dataFromDb.get(i).getIngredientsArray().size();
            for (int j=0; j<nbrOfIngredientsInRecipe; j++) {
                JsonObject ingredientObject=new JsonObject();
                JsonElement ingredientName=gson.toJsonTree(dataFromDb.get(i).getIngredientsArray().get(j).getIngredientName());
                JsonElement ingredientUnits=gson.toJsonTree(dataFromDb.get(i).getIngredientsArray().get(j).getUnitsOfIngredient());
                JsonElement ingredientPrice=gson.toJsonTree(dataFromDb.get(i).getIngredientsArray().get(j).getIngredientPriceInRecipe());
                ingredientObject.add("name", ingredientName);
                ingredientObject.add("units", ingredientUnits);
                ingredientObject.add("price", ingredientPrice);
                ingredientsArray.add(ingredientObject);
            }
            JsonObject ingArr = new JsonObject();
            ingArr.add("ingredients", ingredientsArray);

            JsonObject recipeObject = new JsonObject();
            recipeObject.add("title", title);
            recipeObject.add("portions", portions);
            recipeObject.add("description", description);
            recipeObject.add("instructions", instructions);
            recipeObject.add("listOfIngredients", ingArr);
            recipeObject.add("totalPrice", price);
            recipeObject.add("imageLink", imageLink);

            allRecipes.add(recipeObject);
        }

         System.out.println(allRecipes);
        return allRecipes ;
    }

    public static void main(String[] args) {
        Controller.convertAllRecipesToJson();
    }
}
