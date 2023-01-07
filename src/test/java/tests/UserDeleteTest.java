package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

@Epic("User's cases")
@Feature("Delete user")
public class UserDeleteTest extends BaseTestCase {
    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test checks if the user with id = 2 can't be deleted")
    @DisplayName("Test delete previously created user with id = 2")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteUserWithId2(){

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequestAuthData("https://playground.learnqa.ru/api/user/login",
                        authData);

        //DELETE
        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user/2",
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"),
                this.cookie = this.getCookie(responseGetAuth, "auth_sid")
        );

        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

        //GET
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/2",
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"),
                this.cookie = this.getCookie(responseGetAuth, "auth_sid")
        );

        Assertions.assertJsonByName(responseUserData, "firstName", "Vitalii");

    }

    @Test
    @Description("This test checks if the new user was deleted successfully")
    @DisplayName("Test delete new user")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserSuccessfulDeleted(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.makePostRequestJson("https://playground.learnqa.ru/api/user/",
                        userData);
        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequestAuthData("https://playground.learnqa.ru/api/user/login",
                        authData);

        //DELETE

        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId,
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"),
                this.cookie = this.getCookie(responseGetAuth, "auth_sid")
        );

        //GET
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/"  + userId,
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"),
                this.cookie = this.getCookie(responseGetAuth, "auth_sid")
        );

        Assertions.assertResponseTextEquals(responseUserData, "User not found");
    }
    @Test
    @Description("This test checks that without the correct authorization data cannot be deleted user")
    @DisplayName("Negative test delete user with another auth data")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteUserAnotherUserAuth(){

//GENERATE USER1
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.makePostRequestJson("https://playground.learnqa.ru/api/user/",
                        userData);

        String userId = responseCreateAuth.getString("id");
        String userName = userData.get("username");

        //GENERATE USER2
        Map<String, String> userData1 = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth1 =apiCoreRequests.makePostRequestJson1("https://playground.learnqa.ru/api/user/",
                userData1);

        String userId1 = responseCreateAuth1.getString("id");
        String userName1 = userData1.get("username");

        //LOGIN USER1 WITH USER2 DATA
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData1.get("email"));
        authData.put("password", userData1.get("password"));

        Response responseGetAuth1 = apiCoreRequests.makePostRequestAuthData("https://playground.learnqa.ru/api/user/login",
                        authData);

        //DELETE USER1

        Response responseDeleteUser1 = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId,
                this.header = this.getHeader(responseGetAuth1, "x-csrf-token"),
                this.cookie = this.getCookie(responseGetAuth1, "auth_sid")
        );

        //GET USER1
        Response responseUserData1 = apiCoreRequests.makeGetRequestWithoutTokenAndCookie("https://playground.learnqa.ru/api/user/"  + userId);
        Assertions.assertResponseTextEquals(responseUserData1, "{\"username\":\"" + userName + "\"}");

        //GET USER2
        Response responseUserData2 = apiCoreRequests.makeGetRequestWithoutTokenAndCookie("https://playground.learnqa.ru/api/user/"  + userId1);
        Assertions.assertResponseTextEquals(responseUserData2, "{\"username\":\"" + userName1 + "\"}");
    }

}
