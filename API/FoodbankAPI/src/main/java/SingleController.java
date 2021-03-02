import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleController {

    /**
     * Creates an arrayList with Recipe-objects, representing the recipe table in the database
     **/
    public static ArrayList<Recipe> singleRecipe(String title) {
        //ArrayList<Recipe> list = DatabaseRunner.getRecipeByTitle(title);
        ArrayList<Recipe> list = DatabaseRunner.getRecipe(title);
        System.out.println(list);

        return list;
    }

    /**
     * Creates an arrayList with Relations-objects, representing the relations table in the database
     **/
    public static ArrayList<Relations> singleRelations(int id) {
        ArrayList<Relations> list = DatabaseRunner.selectRelationsById(id);
        return list;
    }

    /**
     * Creates an arrayList with Ingredient-objects, representing the ingredient table in the database
     **/
    public static ArrayList<Ingredient> singleIngreditent(int id) {
        ArrayList<Ingredient> list = DatabaseRunner.createIngredientListById(id);
        return list;
    }
/**
 *
 *
 * */
    public static ArrayList<DataReturn> createDataReturnSingle(String title) {
        ArrayList<DataReturn> list = new ArrayList<>();

        int idRec = 0;
        int idIng =0;
        ArrayList<Recipe> recipeList = singleRecipe(title);
        HashMap<Integer, Ingredient> ingredientHashMap = new HashMap();
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getTitle().equals(title)) {
                idRec = recipeList.get(i).getRecipe_id();
            }
        }
        ArrayList<Relations> relationsList = singleRelations(idRec);
        for (int j = 0; j<relationsList.size();j++){
            if(relationsList.get(j).getRecipe_id()==idRec){
                idIng = relationsList.get(j).getIngredient_id();
                System.out.println("Ing ID "+ idIng);
            }
        }

        ArrayList<Ingredient> ingList = singleIngreditent(idIng);

        for (Ingredient ing : ingList){
            ingredientHashMap.put(ing.getIngredient_id(), ing);

        }
        for (Recipe rec : recipeList) {
            DataReturn newDataReturn = new DataReturn();
            newDataReturn.setTitle(rec.getTitle());
            newDataReturn.setPortions(rec.getPortions());
            newDataReturn.setDescription(rec.getDescription());
            newDataReturn.setInstructions(rec.getInstructions());
            newDataReturn.setImageLink(rec.getImageLink());
            double price=0;

            ArrayList<IngredientsInRecipe> ingredientList = new ArrayList<IngredientsInRecipe>();

            for (int i = 0; i < relationsList.size(); i++) {
                if (rec.getRecipe_id() == relationsList.get(i).getRecipe_id()) {
                    IngredientsInRecipe ingredientsInRecipe=new IngredientsInRecipe();
                    ingredientsInRecipe.setIngredientName(ingredientHashMap.get(relationsList.get(i).getIngredient_id()).getIngredientTitle());
                    ingredientsInRecipe.setUnitsOfIngredient(relationsList.get(i).getUnits());
                    ingredientsInRecipe.setIngredientPriceInRecipe(relationsList.get(i).getPrice());
                    ingredientList.add(ingredientsInRecipe);
                    price+=(relationsList.get(i).getPrice());
                }
            }

            newDataReturn.setPrice(price);
            newDataReturn.setIngredientsArray(ingredientList);
            list.add(newDataReturn);
        }

        return list;
    }
    public static JsonArray convertAllRecipesToJson(String recipeTitle) {
        ArrayList<DataReturn> dataFromDb = createDataReturnSingle(recipeTitle);
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
                JsonElement ingredientName=gson.toJsonTree(dataFromDb.get(i).getIngredientsArray().get(j).getIngredientName());
                JsonElement ingredientUnits=gson.toJsonTree(dataFromDb.get(i).getIngredientsArray().get(j).getUnitsOfIngredient());
                JsonElement ingredientPrice=gson.toJsonTree(dataFromDb.get(i).getIngredientsArray().get(j).getIngredientPriceInRecipe());
                ingredientObject.add("name", ingredientName);
                ingredientObject.add("units", ingredientUnits);
                ingredientObject.add("price", ingredientPrice);
                ingredientsArray.add(ingredientObject);
            }

            //JsonElement unit = gson.toJsonTree(dataFromDb.get(i).getUnit());
            JsonElement price = gson.toJsonTree(dataFromDb.get(i).getPrice());
            JsonElement imageLink = gson.toJsonTree(dataFromDb.get(i).getImageLink());
            JsonObject recipeObject = new JsonObject();
            JsonObject ingArr = new JsonObject();
            recipeObject.add("title", title);
            recipeObject.add("portions", portions);
            recipeObject.add("description", description);
            recipeObject.add("instructions", instructions);

            //recipeObject.add("ingredients", ingredientsArray);
            ingArr.add("ingredients", ingredientsArray);
            recipeObject.add("listOfIngredients",ingArr);
            recipeObject.add("price", price);
            recipeObject.add("image link", imageLink);

            allRecipes.add(recipeObject);

            //allRecipes.add(ingArr);
        }

        System.out.println(allRecipes);
        return allRecipes ;
    }

    public static void main(String[] args) {
        SingleController.convertAllRecipesToJson("Pudding");
    }

}
