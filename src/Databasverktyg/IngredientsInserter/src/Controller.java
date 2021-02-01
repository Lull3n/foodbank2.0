import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Controller {
    public static void main(String[] args) {
        Database db = new Database();
        JsonCleaner.init();
        try {
            JsonArray array  = (JsonArray) JsonParser.parseReader(new FileReader("files/coopSort.json"));
            JsonCleaner.cleanArray(array);



            db.insertJsonArray(array);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
