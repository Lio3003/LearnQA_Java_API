import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthTest {

    @Test
    public void testHeader(){

        Response responseGetHeader = RestAssured

                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        //System.out.println(responseGetHeader.getHeaders());

          assertTrue(responseGetHeader.getHeaders().hasHeaderWithName("x-secret-homework-header"), "Response doesn't have 'x-secret-homework-header'");
          String valueHeader = responseGetHeader.getHeader("x-secret-homework-header");
          assertEquals(valueHeader, "Some secret value", "Value 'x-secret-homework-header' not equal 'Some secret value'");


    }
}

