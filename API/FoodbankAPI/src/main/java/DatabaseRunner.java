import java.sql.*;
import java.util.ArrayList;

/**
 * This class connects to the SQL database
 * The data is requested by the API class
 */
public class DatabaseRunner {

/**
 * This method creates a connection to the database
 * Remember to change username and password to your own :)
 * */
    public Connection connect() throws ClassNotFoundException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //fixme Skriv inn ditt brukernavn og passord til databasen
            String user = "sa";
            String pw = "MauHjelp1!";

            String dbURL = "jdbc:sqlserver://localhost";

            Connection conn = DriverManager.getConnection(dbURL, user, pw);
            if (conn != null) {
                System.out.println("Connected");
                return conn;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * This method selects all recipes from the database and sets the Recipe object
     * with the collected data.
     * @return Arraylist</Recipe> of recipes
     * */
    public ArrayList<Recipe> selectRecipe() {
        ArrayList<Recipe> list = new ArrayList<Recipe>();
        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            String query = "select * from FoodBank.dbo.recipes";
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Recipe recipe = new Recipe();
                recipe.setCategory(resultSet.getInt(2));
                recipe.setTitle(resultSet.getString(3));
                recipe.setPortions(resultSet.getInt(4));
                recipe.setDescr(resultSet.getString(5));
                //todo finn en løsning på ingredienser
                recipe.setIngredientsString(resultSet.getString(6));
                //recipe.setIngredients((Ingredients) resultSet.getObject(6));
                recipe.setInstructions(resultSet.getString(7));
                recipe.setImage(resultSet.getString(8));
                recipe.setLink(resultSet.getString(9));
                list.add(recipe);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * This method selects all relations from the database and sets the Relations object
     * with the collected data.
     * @return Arraylist</Relations> of relations data
     * */
    public ArrayList<Relations> selectRelations() {
        ArrayList<Relations> list = new ArrayList<Relations>();
        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            String query = "select * from FoodBank.dbo.relations";
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Relations relations = new Relations();
                relations.setRelations_id(resultSet.getInt(1));
                relations.setRecipe_id(resultSet.getInt(2));
                relations.setIngredient_id(resultSet.getInt(3));
                relations.setUnits(resultSet.getInt(4));
                relations.setPrice(resultSet.getFloat(5));
                list.add(relations);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }




}
