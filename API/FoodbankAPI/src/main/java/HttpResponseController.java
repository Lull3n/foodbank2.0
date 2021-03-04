import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class HttpResponseController {

/**
 * FIXME: ERROR METHOD
 * The intent is that the end user will recieve a message to remeber to change the password for the database,
 * if they haven't already. They will recieve an error 500 by default.
 *
 * Why doesn't it work?
 * Some nullpointer exception that I can't catch in try-catch, and a spark exception that I can't find the Maven dependency for.
 * I think this is the problem.
 * */
    public static JsonObject error500() {
        JsonObject obj = new JsonObject();
        Gson gson = new Gson();
        JsonElement message = gson.toJsonTree("Have you changed the username and password to the database? ");
        obj.add("Error", message);

        return obj;
    }

    /**
     * This method returns a message to the user if the recipe they searched for does not exist.
     * The actual HTTP status code is 200, but it should be 404 page not found.
     */
    public static JsonObject error404() {
        JsonObject obj = new JsonObject();
        Gson gson = new Gson();
        JsonElement message = gson.toJsonTree("Recipe does not exist");
        obj.add("Error", message);
       ;

        return obj;

    }
}
