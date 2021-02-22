package ri;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectorTest {
    Connector connector = new Connector();

    @Test
    void insertTest() {
        assertAll(
                () -> assertEquals(true, connector.query(1, "TEST", "TEST", 2, "TEST", "TEST", "TEST", "TEST"))


        );


    }


}