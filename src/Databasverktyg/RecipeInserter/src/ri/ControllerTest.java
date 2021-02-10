package ri;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller c = new Controller();
    //Valid
    @Test
    void submit() {
        assertEquals("Passed", c.submit(2, "Stroganoff", "Prova laga stroganoffen med smaksatt crème fraiche",
                4, "https://www.ica.se/buffe/artikel/topp-10-middagsmat/", "http://image",
                "salt, ris", "Ta med detta"));
    }
    //Checking submit with Null values, test passed! 
    @Test
    void checkSubmit2(){
        assertEquals("Failed", c.submit(3, "B1", null,
                3, "https://www.ica.se/", "http://image",
                "", "Ta med detta"));
    }
}