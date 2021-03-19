import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleController {

    private static String recipeTbl;
    private static String ingredientsTbl;
    private static String relationsProc;
    private static String recipeTitle;

    public static void setTablesAndProcedures(String recipeTbl, String ingredientsTbl, String relationsProc, String recipeTitle) {
        SingleController.recipeTbl=recipeTbl;
        SingleController.ingredientsTbl=ingredientsTbl;
        SingleController.relationsProc=relationsProc;
        SingleController.recipeTitle=recipeTitle;
    }

    public static void setRecipeTitle(String recipeTitle){
        SingleController.recipeTitle=recipeTitle;
    }


    /**
    * Gets a recipe object
     * @return the recipe
    */
    public static Recipe singleRecipe() {
        Recipe recipe = DatabaseRunner.getRecipe(recipeTitle);
        return recipe;
    }

    /**
     * Creates an arrayList with Ingredient-objects, representing the ingredient table in the database
     **/

    public static ArrayList<Ingredient> getIngredientsFromDatabase(){
        ArrayList<Ingredient> list = DatabaseRunner.createIngredientList(ingredientsTbl);
        return list;
    }

    /**
     * Creates a data return object representing a single recipe
     * @return the data return object representing a recipe
     */

    public static DataReturn createDataReturnSingle() {

        Recipe singleRecipe=singleRecipe();
        DataReturn singleDataReturn=new DataReturn();

        singleDataReturn.setTitle(singleRecipe.getTitle());
        singleDataReturn.setPortions(singleRecipe.getPortions());
        singleDataReturn.setDescription(singleRecipe.getDescription());
        singleDataReturn.setInstructions(singleRecipe.getInstructions());
        singleDataReturn.setIngredientString(singleRecipe.getIngredientsString());
        singleDataReturn.setImageLink(singleRecipe.getImageLink());

        ArrayList<Ingredient> ingredientsList=getIngredientsFromDatabase();
        HashMap<Integer, Ingredient> ingredientHashMap = new HashMap<Integer, Ingredient>();
        for(Ingredient ingredient:ingredientsList) {
            ingredientHashMap.put(ingredient.getIngredient_id(), ingredient);
        }

        ArrayList<IngredientsInRecipe> list = new ArrayList<>();
        double price=0;

        ArrayList<Relations> recipeRelations= DatabaseRunner.getRelationsForRecipe(singleRecipe.getRecipe_id(),
                relationsProc);
        for (Relations relation:recipeRelations) {
            IngredientsInRecipe ingredientsInRecipe = new IngredientsInRecipe();
            ingredientsInRecipe.setIngredientName(ingredientHashMap.get(relation.getIngredient_id()).getIngredientTitle());
            ingredientsInRecipe.setUnitsOfIngredient(relation.getUnits());
            ingredientsInRecipe.setIngredientPriceInRecipe(relation.getPrice());
            list.add(ingredientsInRecipe);
            price += (relation.getPrice());

        }

        singleDataReturn.setIngredientsArray(list);
        singleDataReturn.setPrice(price);

        return singleDataReturn;
    }

    public static JsonObject convertASingleRecipeToJson() {
        DataReturn dataFromDb = createDataReturnSingle();

        if(dataFromDb.getTitle() ==null){
            return HttpResponseController.error404();
        }
        else {
            Gson gson = new Gson();

            JsonElement title = gson.toJsonTree(dataFromDb.getTitle());
            JsonElement portions = gson.toJsonTree(dataFromDb.getPortions());
            JsonElement description = gson.toJsonTree(dataFromDb.getDescription());
            JsonElement instructions = gson.toJsonTree(dataFromDb.getInstructions());
            JsonElement price = gson.toJsonTree(dataFromDb.getPrice());
            JsonElement imageLink = gson.toJsonTree(dataFromDb.getImageLink());

            JsonArray ingredientsArray = new JsonArray();

            int nbrOfIngredientsInRecipe = dataFromDb.getIngredientsArray().size();

            for (int j = 0; j < nbrOfIngredientsInRecipe; j++) {
                JsonObject ingredientObject = new JsonObject();
                JsonElement ingredientName = gson.toJsonTree(dataFromDb.getIngredientsArray().get(j).getIngredientName());
                JsonElement ingredientUnits = gson.toJsonTree(dataFromDb.getIngredientsArray().get(j).getUnitsOfIngredient());
                JsonElement ingredientPrice = gson.toJsonTree(dataFromDb.getIngredientsArray().get(j).getIngredientPriceInRecipe());
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
            recipeObject.add("price", price);
            recipeObject.add("image link", imageLink);

            System.out.println(recipeObject);
            return recipeObject;
        }
    }
}

