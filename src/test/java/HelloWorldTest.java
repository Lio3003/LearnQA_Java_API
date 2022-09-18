import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;


public class HelloWorldTest {
    @Test

    public void testRestAssured() {
        String locationHeader = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode = 301;
        int i = 0;
        while (statusCode !=200) {
            i++;
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(locationHeader)
                    .andReturn();
            response.prettyPrint();

            locationHeader = response.getHeader("Location");
            statusCode = response.getStatusCode();

            System.out.println(statusCode);
            System.out.println(locationHeader);
            System.out.println(i);
        }
/*        System.out.println(locationHeader);
        System.out.println(statusCode);
        System.out.println(i);*/
        }
    }
