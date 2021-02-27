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
    //private DatabaseRunner dbo;

    /**
     * This method may be excessive and could possible be done in the setDataReturn method
     * nevertheless, it gets the arraylist of recipes from the DatabaseRunner
     * */
    public static ArrayList<Recipe> getRecipeFromDatabase() {
       // dbo = new DatabaseRunner();

        ArrayList<Recipe> list =DatabaseRunner.selectRecipe();

        return list;
    }
    /**
     * This method may be excessive and could possible be done in the setDataReturn method
     * nevertheless, it gets the arraylist of relations from the DatabaseRunner
     * */
    public static ArrayList<Relations> getRelationsFromDatabase() {
        //dbo = new DatabaseRunner();
        ArrayList<Relations> list = DatabaseRunner.selectRelations();
        return list;
    }

    public static ArrayList<Ingredient> getIngredientsFromDatabase(){
        ArrayList<Ingredient> list = DatabaseRunner.selectIngredients();
        return list;
    }

    public static ArrayList<DataReturn> setDataReturn() {
        ArrayList<Recipe> recipes = getRecipeFromDatabase();
        ArrayList<Relations> relations = getRelationsFromDatabase();
        ArrayList<Ingredient> ingredientsInDb = getIngredientsFromDatabase();
        ArrayList<DataReturn> dataReturnObjects = new ArrayList<>();
        HashMap<Integer, Ingredient> ingredientHashMap = new HashMap<Integer, Ingredient>();
        for(Ingredient ingredient:ingredientsInDb) {
            ingredientHashMap.put(ingredient.getIngredient_id(), ingredient);
        }

        for (Recipe rec : recipes) {
            DataReturn newDataReturn = new DataReturn();
            newDataReturn.setTitle(rec.getTitle());
            newDataReturn.setPortions(rec.getPortions());
            newDataReturn.setDescr(rec.getDescr());
            newDataReturn.setInstructions(rec.getInstructions());

            ArrayList<Ingredient> ingredientList = new ArrayList<>();

            for (int i = 0; i < relations.size(); i++) {
                if (rec.getRecipe_id() == relations.get(i).getRecipe_id()) {
                    Ingredient ingredient = ingredientHashMap.get(relations.get(i).getIngredient_id());
                    ingredientList.add(ingredient);
                }
            }

            newDataReturn.setIngredientsArray(ingredientList);
            dataReturnObjects.add(newDataReturn);
        }

          return dataReturnObjects;
    }


    public static JsonArray javaToJson() {
        ArrayList<DataReturn> dataFromDb = setDataReturn();
        Gson gson = new Gson();

        JsonArray jsonArray=new JsonArray();

        for(int i=0; i<dataFromDb.size(); i++) {
            JsonElement title = gson.toJsonTree(dataFromDb.get(i).getTitle());
            JsonElement portions = gson.toJsonTree(dataFromDb.get(i).getPortions());
            JsonElement description = gson.toJsonTree(dataFromDb.get(i).getDescr());
            JsonElement instructions = gson.toJsonTree(dataFromDb.get(i).getInstructions());
            JsonElement ingredients = gson.toJsonTree(dataFromDb.get(i).getIngredientsArray());
            System.out.println(dataFromDb.get(i).getIngredientsArray());
            JsonElement unit = gson.toJsonTree(dataFromDb.get(i).getUnit());
            JsonElement price = gson.toJsonTree(dataFromDb.get(i).getPrice());

            JsonObject obj = new JsonObject();
            obj.add("title", title);
            obj.add("portions", portions);
            obj.add("description", description);
            obj.add("instructions", instructions);
            // obj.add("amount", amount); //fixme
            obj.add("ingredients", ingredients); //fixme ARRAY
            obj.add("unit", unit);//fixme
            obj.add("price", price);

            jsonArray.add(obj);
        }


        //JsonElement amount = gson.toJsonTree(data.getAmount()); //fixme
       // JsonElement ingredients = gson.toJsonTree(data.getIngredientString()); //fixme
       // JsonElement ingredient = gson.toJsonTree(data.getIngredientsArray()); // fixme ARRAY


         System.out.println(jsonArray);
        return jsonArray;
    }

    public static void main(String[] args) {
        Controller.javaToJson();
    }

}
