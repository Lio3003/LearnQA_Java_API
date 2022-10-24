package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import lib.Assertions;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAuthTest extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;


    @BeforeEach
    public void loginUser(){
                Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        this.cookie = this.getCookie(responseGetAuth,"auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    public void testAuthUser(){
        Response responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }


    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition){


        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")){
            spec.cookie("auth_sid", this.cookie);
        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token", this.header);
        }
        else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }

        Response responseForCheck = spec.get().andReturn();
        Assertions.assertJsonByName(responseForCheck, "user_id", 0);
    }

    @ParameterizedTest
    @CsvSource({
            "'Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30', Mobile, No, Android",
            "'Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1', Mobile, Chrome, iOS",
            "'Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)', Googlebot, Unknown, Unknown",
            "'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0', Web, Chrome, No",
            "'Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1', Mobile, No, iPhone"
    })
    public void testUserAgent(String userAgent, String platform, String browser, String device) {
        JsonPath responseUAData = RestAssured
                .given()
                .header("user-agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        assertEquals(responseUAData.getString("user_agent"), userAgent,"UserAgent " + responseUAData.getString("user_agent") + " is differs from " + userAgent);
        assertEquals(responseUAData.getString("platform"), platform,"Platform " + responseUAData.getString("platform") + " is differs from " + platform);
        assertEquals(responseUAData.getString("browser"), browser,"Browser " + responseUAData.getString("browser") + " is differs from " + browser);
        assertEquals(responseUAData.getString("device"), device,"Device " + responseUAData.getString("device") + " is differs from " + device);

    }


}