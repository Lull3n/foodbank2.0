import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleController {

    /**
    * Creates an arrayList with Recipe-objects, representing the recipe table in the database
    */
    public static Recipe singleRecipe(String title) {
        Recipe recipe = DatabaseRunner.getRecipe(title);
        System.out.println(recipe);

        return recipe;
    }

    /**
    * Creates an arrayList with Ingredient-objects, representing the ingredient table in the database
    */
    public static DataReturn createDataReturnSingle(String title) {

        Recipe singleRecipe=singleRecipe(title);
        DataReturn singleDataReturn=new DataReturn();

        singleDataReturn.setTitle(singleRecipe.getTitle());
        singleDataReturn.setPortions(singleRecipe.getPortions());
        singleDataReturn.setDescription(singleRecipe.getDescription());
        singleDataReturn.setInstructions(singleRecipe.getInstructions());
        singleDataReturn.setIngredientString(singleRecipe.getIngredientsString());
        singleDataReturn.setImageLink(singleRecipe.getImageLink());


        ArrayList<Ingredient> ingredientsList=Controller.getIngredientsFromDatabase();
        HashMap<Integer, Ingredient> ingredientHashMap = new HashMap<Integer, Ingredient>();
        for(Ingredient ingredient:ingredientsList) {
            ingredientHashMap.put(ingredient.getIngredient_id(), ingredient);
        }

        ArrayList<Relations> relationsList=Controller.getRelationsFromDatabase();
        ArrayList<IngredientsInRecipe> list = new ArrayList<>();
        double price=0;
        for (int i = 0; i < relationsList.size(); i++) {
            if (singleRecipe.getRecipe_id() == relationsList.get(i).getRecipe_id()) {
                IngredientsInRecipe ingredientInRecipe = new IngredientsInRecipe();
                ingredientInRecipe.setIngredientName(ingredientHashMap.get(relationsList.get(i).getIngredient_id()).getIngredientTitle());
                ingredientInRecipe.setUnitsOfIngredient(relationsList.get(i).getUnits());
                ingredientInRecipe.setIngredientPriceInRecipe(relationsList.get(i).getPrice());
                list.add(ingredientInRecipe);
                price += (relationsList.get(i).getPrice());
            }
        }

        singleDataReturn.setIngredientsArray(list);
        singleDataReturn.setPrice(price);

        return singleDataReturn;
    }

    public static JsonObject convertASingleRecipeToJson(String recipeTitle) {
        DataReturn dataFromDb = createDataReturnSingle(recipeTitle);

        if(dataFromDb.getTitle() ==null){
            return HttpResponseController.error404();
        }
        else {
            Gson gson = new Gson();

            JsonArray singleRecipe = new JsonArray();

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

    public static void main(String[] args) {
        SingleController.convertASingleRecipeToJson("Fisksoppa");
    }

}
