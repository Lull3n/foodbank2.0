/**
 * This is the Foodbank API
 * <p>
 * GET
 */

import com.google.gson.JsonObject;

import static spark.Spark.*;

public class API {

    public void initAPI() {

        // bare et utkast
        get("/recipe", (request, response) -> {
            Controller controller = new Controller();
            JsonObject recipeData = controller.javaToJson();

            response.header("Content-Type", "application/json");
            return recipeData;
        });
    }
}
