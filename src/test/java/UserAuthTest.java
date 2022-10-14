import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;

public class UserAuthTest {

    @ParameterizedTest
    @ValueSource(strings = {"user_agent"})
    public void testUserAgent(){
        Map<String, String> userAgentData = new HashMap<>();
        userAgentData.put("user_agent", "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        userAgentData.put("_ym_uid", "1663500239248447327");
        userAgentData.put("_ym_d", "1663500239");
        userAgentData.put("HomeWork", "hw_value");
        userAgentData.put("MyCookie", "12345");

        Response responseUAData = RestAssured
                .given()
                .header("user_agent", userAgentData)
                .cookie("_ym_uid",userAgentData)
                .cookie("_ym_d", userAgentData)
                .cookie("HomeWork", userAgentData)
                .cookie("MyCookie", userAgentData)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();

        String platform = responseUAData.getBody().prettyPrint();}

    }
