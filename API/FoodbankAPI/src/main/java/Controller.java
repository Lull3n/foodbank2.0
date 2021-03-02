import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
        for(Ingredient ingredient:ingredientsList) {
            ingredientHashMap.put(ingredient.getIngredient_id(), ingredient);
        }

        for (Recipe rec : recipeList) {
            DataReturn newDataReturn = new DataReturn();
            newDataReturn.setTitle(rec.getTitle());
            newDataReturn.setPortions(rec.getPortions());
            newDataReturn.setDescription(rec.getDescription());
            newDataReturn.setInstructions(rec.getInstructions());
            newDataReturn.setImageLink(rec.getImageLink());
            double price=0;

            ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();

            for (int i = 0; i < relationsList.size(); i++) {
                if (rec.getRecipe_id() == relationsList.get(i).getRecipe_id()) {
                    Ingredient ingredient = ingredientHashMap.get(relationsList.get(i).getIngredient_id());
                    ingredientList.add(ingredient);
                    price+=(relationsList.get(i).getPrice());
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


 //           JsonElement ingredients = gson.toJsonTree(dataFromDb.get(i).getIngredientsArray());

            JsonArray ingredientsArray=new JsonArray();

            int nbrOfIngredientsInRecipe=dataFromDb.get(i).getIngredientsArray().size();
            for (int j=0; j<nbrOfIngredientsInRecipe; j++) {
                JsonObject ingredientObject=new JsonObject();
                JsonElement ingredientName=gson.toJsonTree(dataFromDb.get(i).getIngredientsArray().get(j).getIngredientTitle());
                JsonElement ingredientUnits=gson.toJsonTree(dataFromDb.get(i).getIngredientsArray().get(j).getCompUnit());
                JsonElement ingredientPrice=gson.toJsonTree(dataFromDb.get(i).getIngredientsArray().get(j).getPrice());
                ingredientObject.add("name", ingredientName);
                ingredientObject.add("units", ingredientUnits);
                ingredientObject.add("price", ingredientPrice);
                ingredientsArray.add(ingredientObject);
            }

            //JsonElement unit = gson.toJsonTree(dataFromDb.get(i).getUnit());
            JsonElement price = gson.toJsonTree(dataFromDb.get(i).getPrice());
            JsonElement imageLink = gson.toJsonTree(dataFromDb.get(i).getImageLink());

            JsonObject recipeObject = new JsonObject();
            recipeObject.add("title", title);
            recipeObject.add("portions", portions);
            recipeObject.add("description", description);
            recipeObject.add("instructions", instructions);
            recipeObject.add("ingredients", ingredientsArray);
            recipeObject.add("price", price);
            recipeObject.add("image link", imageLink);

            allRecipes.add(recipeObject);
        }

         System.out.println(allRecipes);
        return allRecipes;
    }

    public static void main(String[] args) {
        Controller.convertAllRecipesToJson();
    }
}
