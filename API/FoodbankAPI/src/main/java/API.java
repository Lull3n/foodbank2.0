/**
 * This is the Foodbank API
 * <p>
 * GET
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static spark.Spark.*;

public class API {
    /**
     * Noting works
     * localhost:7000
     **/
    public static void initAPI() {

        /**
         * Returns a JsonArray with ALL the recipes
         * */
        get("/recipe", (request, response) -> {
            Controller APIController=new Controller();
            APIController.setTablesAndProcedures("FoodBank.dbo.recipes","FoodBank.dbo.ingredients2",
                    "FoodBank.dbo.getRelationsForRecipe");
            JsonArray recipeData = APIController.convertAllRecipesToJson();

            response.header("Content-Type", "application/json");
            if (response.status() == 500) {
                System.out.println("Error 500");
                return HttpResponseController.error500();
            }
            return recipeData;
        });

        /**
         * Expects a string with the recipes name and returns a recipe with the same name
         * */
        get("/recipe/:title", (request, response) -> {
            SingleController APIController=new SingleController();
            APIController.setTablesAndProcedures("FoodBank.dbo.recipes","FoodBank.dbo.ingredients2",
                    "FoodBank.dbo.getRelationsForRecipe");

            response.header("Content-Type", "application/json");
            String title = request.params("title");

            JsonObject recipe = APIController.convertASingleRecipeToJson(title);

            return recipe;

            //JsonArray recipeData = Controller.convertAllRecipesToJson();
        });
    }
}