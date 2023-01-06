package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import lib.Assertions;
import lib.ApiCoreRequests;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;

@Epic("Authorization cases")
@Feature("Authorization")
public class UserAuthTest extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;
    //добавить это тоже перед тестами!!!
private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    public void loginUser(){
                Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth,"auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    @Severity(SeverityLevel.CRITICAL)
    public void testAuthUser(){
        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/auth",
                        this.header,
                        this.cookie
                );

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }


    @Description("This test checks authorization status w/o sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @Severity(SeverityLevel.CRITICAL)
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition){


        if (condition.equals("cookie")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.cookie
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);

        } else if(condition.equals("headers")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.header
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }
    }

    @Description("This test compare data in response with list of expected values of user_agent")
    @DisplayName("Test compare user_agent data")
    @Severity(SeverityLevel.NORMAL)
    @ParameterizedTest
    @CsvSource({
            "'Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30', Mobile, No, Android",
            "'Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1', Mobile, Chrome, iOS",
            "'Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)', Googlebot, Unknown, Unknown",
            "'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0', Web, Chrome, No",
            "'Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1', Mobile, No, iPhone"
    })
    public void testUserAgent(String userAgent, String platform, String browser, String device) {

        JsonPath responseUAData = apiCoreRequests.makeGetRequestWithUserAgentData("https://playground.learnqa.ru/ajax/api/user_agent_check",
                userAgent);

        assertEquals(userAgent, responseUAData.getString("user_agent"),"UserAgent " + responseUAData.getString("user_agent") + " is differs from expected " + userAgent);
        assertEquals(platform, responseUAData.getString("platform"), "Platform " + responseUAData.getString("platform") + " is differs from expected " + platform);
        assertEquals(browser, responseUAData.getString("browser"), "Browser " + responseUAData.getString("browser") + " is differs from expected " + browser);
        assertEquals(device, responseUAData.getString("device"), "Device " + responseUAData.getString("device") + " is differs from expected " + device);

    }
    @Description("This test check status before and after job")
    @DisplayName("Test check long job status")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void testLongTimeJob() throws InterruptedException {
        JsonPath responseGetToken = apiCoreRequests.makeGetRequestLongTimeJob("https://playground.learnqa.ru/ajax/api/longtime_job");

        String token = responseGetToken.get("token");
        int seconds = responseGetToken.get("seconds");
        //System.out.print(seconds);

        JsonPath responsePutToken = apiCoreRequests.makeGetRequestLongTimeJob("https://playground.learnqa.ru/ajax/api/longtime_job?token=" + token);
        assertEquals("Job is NOT ready", responsePutToken.getString("status"), "Status before job " + responsePutToken.getString("status") + " - is incorrect");

        Thread.sleep(seconds*10);

        JsonPath responseAfterJob = apiCoreRequests.makeGetRequestLongTimeJob("https://playground.learnqa.ru/ajax/api/longtime_job?token=" + token);
        assertNotNull(responseAfterJob.get("result"), "Result does not exist");
        assertEquals("Job is ready", responseAfterJob.getString("status"), "Status after job " + responseAfterJob.getString("status") + " - is incorrect");
    }
}