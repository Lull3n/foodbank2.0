/**
 * Class to handle database connection.
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;

public class Database {
    Connection connection;
    //com.microsoft.sqlserver:mssql-jdbc:8.4.1.jre14
    private String dbURL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=FoodBank;user=FoodConnection;password=hejdatabasenfood;";

    public Database(){

    }

    /**
     * Takes a JsonArray of ingredients, and adds it to the database.
     * @param array Cleaned array of ingredients.
     */
    public void insertJsonArray(JsonArray array){
        String sql = "INSERT INTO ingredients2 (title, price, pricetype, pricePerUnit, compUnit) VALUES(?,?,'kr',?,?)";

        try {
            connection = DriverManager.getConnection(dbURL);

            for (int i = 0; i < array.size(); i++) {

                JsonObject cur = array.get(i).getAsJsonObject();
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, cur.get("prod_name").getAsString());
                statement.setString(2, cur.get("prod_price").getAsString());
                try{
                    Float.parseFloat(cur.get("prod_compPrice").getAsString());
                } catch (NumberFormatException e){
                    System.out.println(cur);
                }

                statement.setString(3, cur.get("prod_compPrice").getAsString());
                statement.setString(4, cur.get("prod_compUnit").getAsString());

                System.out.println("ADDED: " + ((JsonObject) array.get(i)).get("prod_name").getAsString() +
                        " price: " + ((JsonObject) array.get(i)).get("prod_price"));
                statement.execute();
            }
            connection.close();
        } catch (SQLException e){

            e.printStackTrace();
        }

    }

}
