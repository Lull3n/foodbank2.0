import com.google.gson.*;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class ControllerTest {
    private Controller testController;

    @BeforeEach
    void setUp() {
        testController=new Controller();
        testController.setTablesAndProcedures("FoodBank.dbo.testRecipes", "FoodBank.dbo.testIngredients",
                "FoodBank.dbo.getTestRelationsForRecipe");
    }

    @Test
    @DisplayName("Checking first element in testRecipe-table")
    public void getRecipeFromDatabaseFirstElement() {
        Assert.assertEquals(1,testController.getRecipeFromDatabase().get(0).getRecipe_id());
    }

    @Test
    @DisplayName("Checking last element in testRecipe-table")
    public void getRecipeFromDatabaseTestLastElement() {
        Assert.assertEquals("link3", testController.getRecipeFromDatabase().get(2).getLink());
    }

    @Test
    @DisplayName("Checking first element in testIngredients-table")
    public void getIngredientsFromDatabaseTestFirstElement() {
        Assert.assertEquals(1, testController.getIngredientsFromDatabase().get(0).getIngredient_id());
    }

    @Test
    @DisplayName("Checking last element in testIngredients-table")
    public void getIngredientsFromDatabaseTestLastElement() {
        Assert.assertEquals("compUnit3", testController.getIngredientsFromDatabase().get(2).getCompUnit());
    }

    @Test
    @DisplayName("Checking the returned number of dataReturn-objects")
    public void createDataReturnAllRecipesTestNbrOfObj() {
        Assert.assertEquals(3,testController.createDataReturnAllRecipes().size());
    }

    @Test
    @DisplayName("Checking the title of the firs dataReturn-object")
    public void createDataReturnAllRecipesTestRecipeTitle() {
        Assert.assertEquals("tite1",testController.createDataReturnAllRecipes().get(0).getTitle());
    }

    @Test
    @DisplayName("Checking the title of the firs dataReturn-object")
    public void createDataReturnAllRecipesTestRecipeImage() {
        Assert.assertEquals("image3",testController.createDataReturnAllRecipes().get(2).getImageLink());
    }

    @Test
    @DisplayName("Checking the title of the firs recipe in the json-object")
    public void convertAllRecipesToJsonTestTitle() {
        try {
            JsonArray jsonArray = testController.convertAllRecipesToJson();     // Alla recept
            JsonElement recipe=jsonArray.get(0);
            JsonParser parser = new JsonParser();
            JsonElement element=parser.parse(recipe.toString());
            JsonObject rootObject=element.getAsJsonObject();
            String title=rootObject.get("title").getAsString();

            Assert.assertEquals("tite1",title);

        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Checking the title of the firs recipe in the json-object")
    public void convertAllRecipesToJsonTestImage() {
        try {
            JsonArray jsonArray = testController.convertAllRecipesToJson();     // Alla recept
            JsonElement recipe=jsonArray.get(2);
            JsonParser parser = new JsonParser();
            JsonElement element=parser.parse(recipe.toString());
            JsonObject rootObject=element.getAsJsonObject();
            String title=rootObject.get("imageLink").getAsString();

            Assert.assertEquals("image3",title);

        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // todo testa ogiltiga värden
}