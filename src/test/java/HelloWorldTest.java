import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;


public class HelloWorldTest {
    @Test

    public void testRestAssured() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

    }
}
