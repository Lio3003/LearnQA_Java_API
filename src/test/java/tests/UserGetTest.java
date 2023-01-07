package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

@Epic("User's cases")
@Feature("Get user")
public class UserGetTest extends BaseTestCase {
    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("This test checks getting only the username when requesting without authorization")
    @DisplayName("Test get username only")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserDataNotAuth(){
        Response responseUserData = apiCoreRequests.makeGetRequestWithoutTokenAndCookie("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData,  "lastName");
        Assertions.assertJsonHasNotField(responseUserData,  "email");
    }

    @Test
    @Description("This test checks getting the username when requesting with authorization")
    @DisplayName("Test get authorized user name")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequestAuthData("https://playground.learnqa.ru/api/user/login",
                        authData);

        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/2",
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"),
                this.cookie = this.getCookie(responseGetAuth,"auth_sid"));

        Assertions.assertJsonHasField(responseUserData, "username");

    }

    @Test
    @Description("This test checks getting the username only when requesting user data by another user")
    @DisplayName("Test getting another user's data")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUserDetailsAuthAsAnotherUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequestAuthData("https://playground.learnqa.ru/api/user/login",
                        authData);

        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/1",
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"),
                this.cookie = this.getCookie(responseGetAuth,"auth_sid"));

        if(responseUserData.getStatusCode() == 404)
        {
            System.out.println(responseUserData.asString());
        }
        else {
            Assertions.assertJsonHasField(responseUserData, "username");
            Assertions.assertJsonHasNotField(responseUserData, "firstName");
            Assertions.assertJsonHasNotField(responseUserData,  "lastName");
            Assertions.assertJsonHasNotField(responseUserData,  "email");
        }
    }
}
