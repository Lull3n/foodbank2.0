package ri;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectorTest {
    Connector connector = new Connector();

    @Test
    void loadRecipes() {
        assertEquals(6, connector.loadRecipes().size());
    }

    @Test
    void loadRecipeIngredients() {
        assertEquals("Köttfärs", connector.loadRecipeIngredients("Lasagne"));
    }



    @Test
    void getRecipeId() {
        assertEquals(1, connector.getRecipeId("Lasagne"));
    }

    @Test
    void sendRelation() {
        connector.sendRelation(1004, "6448", "500");
    }

    @Test
    void loadDatabaseIngredient() {
        assertEquals(3, connector.loadDatabaseIngredient("Köttfärs").size());
    }
}