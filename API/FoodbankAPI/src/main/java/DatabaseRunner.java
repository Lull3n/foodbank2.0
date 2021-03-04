import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class connects to the SQL database
 * The data is requested by the API class
 */
public class DatabaseRunner {

    /**
     * This method creates a connection to the database
     * Remember to change username and password to your own :)
     */
    public static Connection connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //fixme Skriv inn ditt brukernavn og passord til databasen
            String user = "javaConnection";
            String pw = "hejDatabasenFood";

            String dbURL = "jdbc:sqlserver://localhost";

            Connection conn = DriverManager.getConnection(dbURL, user, pw);
            if (conn != null) {
                System.out.println("Connected");
                return conn;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Selects all recipes from the database and sets the Recipe object
     * with the collected data.
     *
     * @return Arraylist</ Recipe> of recipe objects
     */
    public static ArrayList<Recipe> selectRecipe() {
        ArrayList<Recipe> list = new ArrayList<Recipe>();
        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            String query = "select * from FoodBank.dbo.recipes";
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Recipe recipe = new Recipe();
                recipe.setRecipe_id(resultSet.getInt(1));
                recipe.setCategory(resultSet.getInt(2));
                recipe.setTitle(resultSet.getString(3));
                recipe.setPortions(resultSet.getInt(4));
                recipe.setDescription(resultSet.getString(5));
                recipe.setIngredientsString(resultSet.getString(6));
                recipe.setInstructions(resultSet.getString(7));
                recipe.setImageLink(resultSet.getString(8));
                recipe.setLink(resultSet.getString(9));

                list.add(recipe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Selects all relations from the database and sets the Relations object
     * with the collected data.
     *
     * @return Arraylist</ Relations> of relations objects
     */
    public static ArrayList<Relations> selectRelations() {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Selects all ingredients from the database
     *
     * @return Arraylist</ Ingredient> of ingredient objects
     */
    public static ArrayList<Ingredient> createIngredientList() {
        ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            String query = "select * from FoodBank.dbo.ingredients2";
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredient_id(resultSet.getInt(1));
                ingredient.setIngredientTitle(resultSet.getString(2));
                ingredient.setPrice(resultSet.getInt(3));
                ingredient.setPriceType(resultSet.getString(4));
                ingredient.setPricePerUnit(resultSet.getFloat(5));
                ingredient.setCompUnit(resultSet.getString(6));

                ingredientList.add(ingredient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredientList;
    }

   /**
    * Kaller på stored procedure getIngredientAndTitle
    * */
    public static Recipe getRecipe (String title){
        Recipe recipe = new Recipe();
        String query = "{ call FoodBank.dbo.recipeByTitle(?) }";
        ResultSet resultSet;

        try (Connection connection = connect();
             CallableStatement callStmt = connection.prepareCall(query)) {

            callStmt.setString(1,title);
            resultSet = callStmt.executeQuery();

            while (resultSet.next()) {
                recipe.setRecipe_id(resultSet.getInt(1));
                recipe.setCategory(resultSet.getInt(2));
                recipe.setTitle(resultSet.getString(3));
                recipe.setPortions(resultSet.getInt(4));
                recipe.setDescription(resultSet.getString(5));
                recipe.setIngredientsString(resultSet.getString(6));
                recipe.setInstructions(resultSet.getString(7));
                recipe.setImageLink(resultSet.getString(8));
                recipe.setLink(resultSet.getString(9));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return recipe;
    }
}
