import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    Connection connection;
    String dbURL = "jdbc:sqlite:../../database/sqliteDb.db";

    public Database(){

    }



    public boolean insertJsonArray(JsonArray array){
        String sql = "INSERT INTO ingredients2 (title, price, pricetype, pricePerUnit, compUnit) VALUES(?,?,'kr',?,?)";

        try {
            connection = DriverManager.getConnection(dbURL);
            for (int i = 0; i < array.size(); i++) {
                JsonObject cur = array.get(i).getAsJsonObject();
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, cur.get("prod_name").getAsString());
                try {
                    statement.setString(2, cur.get("prod_price").getAsString());
                } catch (NumberFormatException e){

                    System.out.println(array.get(i));
                }
                statement.setString(3, cur.get("prod_compPrice").getAsString());
                statement.setString(4, cur.get("prod_compUnit").getAsString());
                System.out.println(((JsonObject) array.get(i)).get("prod_name").getAsString() + " price: " + ((JsonObject) array.get(i)).get("prod_price"));
                statement.execute();
            }
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

}
