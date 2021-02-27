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
     */
    public static Connection connect() throws ClassNotFoundException {
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
        }

        return null;
    }

    /**
     * This method selects all recipes from the database and sets the Recipe object
     * with the collected data.
     *
     * @return Arraylist</ Recipe> of recipes
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
                recipe.setDescr(resultSet.getString(5));

                //todo finn en løsning på ingredienser
               // recipe.setIngredientsString(resultSet.getString(6));
                //recipe.setIngredients((Ingredients) resultSet.getObject(6));



                recipe.setInstructions(resultSet.getString(7));
                recipe.setImage(resultSet.getString(8));
                recipe.setLink(resultSet.getString(9));
               // getI(recipe, recipe.getTitle());
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
     *
     * @return Arraylist</ Relations> of relations data
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

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }



    /**
     * This method selects all ingredietns from the database
     *
     * @return Arraylist</ Ingredient> of ingredient data
     */
    public static ArrayList<Ingredient> selectIngredients() {
        ArrayList<Ingredient> list = new ArrayList<Ingredient>();
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
                list.add(ingredient);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * This method gets the recipe by the title of the recipe
     */
  /*  public static void getRecipeByTitle(String title) {
        ArrayList<Object> fullList = new ArrayList<>();

        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            // String query = "select * from FoodBank.dbo.relations";
            String queryRecipe = "select * from FoodBank.dbo.recipes WHERE title =" + title + "";
            // ResultSet resultSet = stmt.executeQuery(query);
            ResultSet resultSet = stmt.executeQuery(queryRecipe);

            while (resultSet.next()) {
                //Relations relations = new Relations();
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
                fullList.add(recipe);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }*/

    /**
     * This method gets the ID of the ingredients by the recipe ID
     */

   /* public static ArrayList<Ingredients> ingredientsRelation(int id) {

        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            String query = "select * from FoodBank.dbo.relations WHERE recipe_id =" + id + "";
            ResultSet resultSet = stmt.executeQuery(query);


            while (resultSet.next()) {
                Relations relations = new Relations();
                relations.setIngredient_id(resultSet.getInt(3));
                return getIngredients(id);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * This method gets the ingredients from the ingredient table by the ingredient ID
     */
   /* public static ArrayList<Ingredients> getIngredients(int ingredientID) {
        ArrayList<Ingredients> list = new ArrayList<>();
        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            String query = "select * from FoodBank.dbo.ingredients2  WHERE id= " + ingredientID + "";
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Ingredients ingredients = new Ingredients();
                ingredients.setId(resultSet.getInt(1));
                ingredients.setTitle(resultSet.getString(2));
                ingredients.setPrice(resultSet.getFloat(3));
                ingredients.setPricetype(resultSet.getString(4));
                ingredients.setCompUnit(resultSet.getString(5));
                list.add(ingredients);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }*/

   /**
    * Kaller på stored procedure getIngredientAndTitle
    * */
/*
    public static Ingredients getI (Recipe recipe, String title){

        try {
            Connection connection = connect();
            Statement stmt = connection.createStatement();
            String query = "{call FoodBank.dbo.getIngredientsAndTitle(" + title + ") }";
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()){
               Ingredients ingredients = new Ingredients();
               ingredients.setTitle(resultSet.getString(2));
               recipe.addToArray(ingredients);
               return ingredients; //todo test!
            }


        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

*/
}
