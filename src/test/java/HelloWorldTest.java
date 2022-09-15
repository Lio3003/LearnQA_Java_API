import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;


public class HelloWorldTest {
    @Test

    public void testRestAssured() {
/*        Map<String, String> params = new HashMap<>();
        params.put("message", null);
        params.put("timestamp", "2021-06-04 16:41:51");*/

        JsonPath response = RestAssured
/*                .given()
                .queryParams(params)*/
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        String message = response.get("messages.message[1]");
        System.out.println(message);

        String timestamp = response.get("messages.timestamp[1]");
        System.out.println(timestamp);

/*        if (message == null) {
            System.out.println("The key 'messages.message[1]' is absent");
        } else {
            System.out.println(message);

        }
        if (timestamp == null) {
            System.out.println("The key 'messages.timestamp[1]' is absent");
        } else {
            System.out.println(timestamp);

        }*/
    }
}
