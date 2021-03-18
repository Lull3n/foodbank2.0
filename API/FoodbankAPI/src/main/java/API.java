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
         * To handle CORS-policy
         */
        options("/*",
                (request, response) -> {
                    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");

                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                    }

                    return "OK";
                }
        );

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        Controller.setTablesAndProcedures("FoodBank.dbo.recipes",
                "FoodBank.dbo.ingredients2",
                "FoodBank.dbo.getRelationsForRecipe" );

        /**
         * Returns a JsonArray with ALL the recipes
         * */
        get("/foodbank/api/recipe", (request, response) -> {
            System.out.println("Get all recipes");


            JsonArray recipeData = Controller.convertAllRecipesToJson();

            response.header("Content-Type", "application/json");
            if (response.status() == 500) {
                System.out.println("Error 500");
                return HttpResponseController.error500();
            }
            return recipeData;
        });
        /**
         * Expects a string with the recipes name and returns a
         * recipe with that name
         * */
        get("/foodbank/api/recipe/:title", (request, response) -> {
            response.header("Content-Type", "application/json");
            String title = request.params("title");


            JsonObject recipe = SingleController.convertASingleRecipeToJson();

            return recipe;
        });

        /**
         * expects a string with a number 0-5, kött, kyckling, fisk, pasta, vegetariskt or veganskt
         * returns all recipes with the specified category
         */
        get("/foodbank/api/category/:category", (request, response) ->{
            response.header("Content-Type","application/json");
            String category = request.params("category").toLowerCase();
            System.out.println("Get all recipes by categories");
            switch (category){
                case "kött" :
                    category = "0";
                    break;
                case "kyckling" :
                    category = "1";
                    break;
                case "fisk" :
                    category = "2";
                    break;
                case "pasta" :
                    category = "3";
                    break;
                case "vegetariskt" :
                    category = "4";
                    break;
                case "veganskt" :
                    category = "5";
                    break;
            }
            JsonArray categoryData = Controller.convertAllRecipesByCategoryToJson(Integer.parseInt(category));

            return categoryData;
        });

    }
}