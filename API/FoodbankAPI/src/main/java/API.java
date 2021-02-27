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
 *
 *
 * */
    public static void initAPI() {

        // bare et utkast
        get("/recipe", (request, response) -> {
           // Controller controller = new Controller();
            System.out.println("Get all recipes");
            JsonArray recipeData = Controller.javaToJson();

            response.header("Content-Type", "application/json");
            return recipeData;
        });

        get("/recipe/:id", (request, response) -> {

            String id = request.params("id");
            //Controller controller = new Controller();
            System.out.println("Get recipe by id, but why would you?");
            JsonArray recipeData = Controller.javaToJson();

            response.header("Content-Type", "application/json");
            return recipeData;
        });
    }
}