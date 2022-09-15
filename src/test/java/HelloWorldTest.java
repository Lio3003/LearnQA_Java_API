import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;


public class HelloWorldTest {
    @Test

    public void testRestAssured() {

        JsonPath response = RestAssured

                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        String message = response.get("messages.message[1]");
        System.out.println(message);

        String timestamp = response.get("messages.timestamp[1]");
        System.out.println(timestamp);

    }
}
