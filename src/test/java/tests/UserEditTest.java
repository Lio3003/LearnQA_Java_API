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
@Feature("Edit user")
public class UserEditTest extends BaseTestCase {
    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("This test checks successfully edition username")
    @DisplayName("Positive test edit user")
    @Severity(SeverityLevel.CRITICAL)
    @Flaky
 public void testEditJustCreatedTest(){
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

        //EDIT
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        editData.put("email", userData.get("email"));
        editData.put("password", userData.get("password"));

        System.out.print(editData);
        System.out.print(userData);

        Response responseEditUser = apiCoreRequests.makePutRequestWithEditData("https://playground.learnqa.ru/api/user/" + userId,
                this.cookie = this.getCookie(responseGetAuth,"auth_sid"),
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"),
                editData);
          Assertions.assertResponseCodeEquals(responseEditUser, 200);

        //GET
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                this.cookie = this.getCookie(responseGetAuth,"auth_sid"),
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"));

           Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Description("This test checks error if no authData in edit-request")
    @DisplayName("Negative test edit user - no authData")
    @Severity(SeverityLevel.CRITICAL)
    public void testEditJustCreatedTestNoAuth(){
//GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makePostRequestJson("https://playground.learnqa.ru/api/user/",
                        userData);

        String userId = responseCreateAuth.getString("id");
        //System.out.println(responseCreateAuth.getInt("id"));

        //EDIT
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequestWithOnlyEditData("https://playground.learnqa.ru/api/user/" + userId,
                        editData);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
    }

    @Test
    @Description("This test checks error when trying to edit user data with the authorization of another user")
    @DisplayName("Negative test edit user - another user authData")
    @Severity(SeverityLevel.CRITICAL)
    public void testEditUsernameAnotherAuthUserTest(){
//GENERATE USER1
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.makePostRequestJson("https://playground.learnqa.ru/api/user/",
                        userData);
        String userId = responseCreateAuth.getString("id");
        String userName = userData.get("username");

        //GENERATE USER2
        Map<String, String> userData1 = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth1 = apiCoreRequests.makePostRequestJson1("https://playground.learnqa.ru/api/user/",
                        userData1);

        String userId1 = responseCreateAuth1.getString("id");
        String userName1 = userData1.get("username");

        //LOGIN USER1
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequestAuthData("https://playground.learnqa.ru/api/user/login",
                authData);

        //LOGIN USER2
        Map<String, String> authData1 = new HashMap<>();
        authData1.put("email", userData1.get("email"));
        authData1.put("password", userData1.get("password"));

        Response responseGetAuth1 = apiCoreRequests.makePostRequestAuthData1("https://playground.learnqa.ru/api/user/login",
                        authData1);

        //EDIT
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("username", newName);

        Response responseEditUser = apiCoreRequests.makePutRequestWithEditData("https://playground.learnqa.ru/api/user/" + userId,
                this.cookie = this.getCookie(responseGetAuth1,"auth_sid"),
                this.header = this.getHeader(responseGetAuth1, "x-csrf-token"),
                editData);

        //GET USER1
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                this.cookie = this.getCookie(responseGetAuth,"auth_sid"),
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"));

        //GET USER2
        Response responseUserData1 = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + userId1,
                this.cookie = this.getCookie(responseGetAuth1,"auth_sid"),
                this.header = this.getHeader(responseGetAuth1, "x-csrf-token"));

        //System.out.println(responseUserData1.asString());
        Assertions.assertJsonByName(responseUserData, "username", userName);
        Assertions.assertJsonByName(responseUserData1, "username", userName1);
    }

    @Test
    @Description("This test changing the user's email, being authorized by the same user, to a new email without the @ symbol")
    @DisplayName("Negative test edit user - email without @")
    @Severity(SeverityLevel.NORMAL)
    public void testEditEmailTheSameAuthUserTest(){
//GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.makePostRequestJson("https://playground.learnqa.ru/api/user/",
                        userData);

        String userId = responseCreateAuth.getString("id");
        String email = userData.get("email");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequestAuthData("https://playground.learnqa.ru/api/user/login",
                        authData);

        //EDIT
        String newEmail = DataGenerator.getInvalidRandomEmail();;
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.makePutRequestWithEditData("https://playground.learnqa.ru/api/user/" + userId,
                this.cookie = this.getCookie(responseGetAuth,"auth_sid"),
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"),
                editData);


        //GET
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                this.cookie = this.getCookie(responseGetAuth,"auth_sid"),
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"));

        Assertions.assertJsonByName(responseUserData, "email", email);
    }

    @Test
    @Description("This test change the firstName of the user, being authorized by the same user, to a very short value of one character")
    @DisplayName("Negative test edit user - short firstName")
    @Severity(SeverityLevel.NORMAL)
    public void testEditFirstNameTheSameAuthUserTest(){
//GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.makePostRequestJson("https://playground.learnqa.ru/api/user/",
                        userData);

        String userId = responseCreateAuth.getString("id");
        String firstName = userData.get("firstName");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequestAuthData("https://playground.learnqa.ru/api/user/login",
                        authData);

        //EDIT
        String newFirstName = DataGenerator.getNameWithShortUsername();;
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        Response responseEditUser = apiCoreRequests.makePutRequestWithEditData("https://playground.learnqa.ru/api/user/" + userId,
                this.cookie = this.getCookie(responseGetAuth,"auth_sid"),
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"),
                editData);
       //GET
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                this.cookie = this.getCookie(responseGetAuth,"auth_sid"),
                this.header = this.getHeader(responseGetAuth, "x-csrf-token"));

        //System.out.println(responseUserData.asString());
        Assertions.assertJsonByName(responseUserData, "firstName", firstName);
    }
}
