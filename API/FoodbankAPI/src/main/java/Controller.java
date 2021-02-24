import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * This class is a controller
 * The purpose of this class is to get objects from DatabaseConnect and parse the objects
 * to JSON objects. The API class will then call upon these json objects whenever an
 * end user is sending request to the API.
 */
public class Controller {
    private DatabaseRunner dbo;

    /**
     * This method may be excessive and could possible be done in the setDataReturn method
     * nevertheless, it gets the arraylist of recipes from the DatabaseRunner
     * */
    public ArrayList<Recipe> getRecipeFromDatabase() {
        dbo = new DatabaseRunner();

        ArrayList<Recipe> list = dbo.selectRecipe();

        return list;
    }
    /**
     * This method may be excessive and could possible be done in the setDataReturn method
     * nevertheless, it gets the arraylist of relations from the DatabaseRunner
     * */
    public ArrayList<Relations> getRelationsFromDatabase() {
        dbo = new DatabaseRunner();
        ArrayList<Relations> list = dbo.selectRelations();
        return list;
    }

    public DataReturn setDataReturn() {
        ArrayList<Recipe> recipes = getRecipeFromDatabase();
        ArrayList<Relations> relations = getRelationsFromDatabase();
        DataReturn dataReturn = new DataReturn();
        for (Recipe rec : recipes) {
            dataReturn.setTitle(rec.getTitle());
            dataReturn.setPortions(rec.getPortions());
            dataReturn.setDescr(rec.getDescr());
            dataReturn.setInstructions(rec.getInstructions());
            dataReturn.setIngredientString(rec.getIngredientsString());// fixme + fix amount

        }

        for (Relations rel : relations) {
            dataReturn.setUnit(rel.getUnits());
            dataReturn.setPrice(rel.getPrice());
        }


        return dataReturn;
    }


    public JsonObject javaToJson() {
        DataReturn data = setDataReturn();

        Gson gson = new Gson();
        JsonElement title = gson.toJsonTree(data.getTitle());
        JsonElement portions = gson.toJsonTree(data.getPortions());
        JsonElement description = gson.toJsonTree(data.getDescr());
        JsonElement instructions = gson.toJsonTree(data.getInstructions());
        //JsonElement amount = gson.toJsonTree(data.getAmount()); //fixme
        JsonElement ingredients = gson.toJsonTree(data.getIngredientString()); //fixme
        JsonElement unit = gson.toJsonTree(data.getUnit()); //fixme
        JsonElement price = gson.toJsonTree(data.getPrice());


        JsonObject obj = new JsonObject();
        obj.add("title", title);
        obj.add("portions", portions);
        obj.add("description", description);
        obj.add("instructions", instructions);
       // obj.add("amount", amount); //fixme
        obj.add("ingredients", ingredients); //fixme
        obj.add("unit", unit);//fixme
        obj.add("price", price);

        System.out.println(obj);
        return obj;
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.javaToJson();
    }
}
