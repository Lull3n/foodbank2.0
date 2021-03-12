import com.google.gson.*;

import java.io.FileReader;



/**
 * This class runs the application. First sends the array for cleaning, then for insertion into the database.
 */
public class Controller {
    public static void main(String[] args) {
        Database db = new Database();
        JsonCleaner.init();
        try {
            JsonArray array  = (JsonArray) JsonParser.parseReader(new FileReader("files/coopSort-2021-03-12.json"));
            JsonCleaner.cleanArray(array);
            db.insertJsonArray(array);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
