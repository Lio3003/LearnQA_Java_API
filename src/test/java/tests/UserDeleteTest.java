package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    @Test
    public void testDeleteUserWithId2(){

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //DELETE
        Response responseDeleteUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .delete("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonByName(responseUserData, "firstName", "Vitalii");

    }

    @Test
    public void testUserSuccessfulDeleted(){

//GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //DELETE
        Response responseDeleteUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .delete("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/"  + userId)
                .andReturn();

        Assertions.assertResponseTextEquals(responseUserData, "User not found");

    }
    @Test
    public void testDeleteUserAnotherUserAuth(){

//GENERATE USER1
        Map<String, String> userData1 = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth1 = RestAssured
                .given()
                .body(userData1)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId1 = responseCreateAuth1.getString("id");
        String userName1 = userData1.get("username");

        //GENERATE USER2
        Map<String, String> userData2 = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth2 = RestAssured
                .given()
                .body(userData2)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId2 = responseCreateAuth2.getString("id");
        String userName2 = userData2.get("username");

        //LOGIN USER1 WITH USER2 DATA
        Map<String, String> authData2 = new HashMap<>();
        authData2.put("email", userData2.get("email"));
        authData2.put("password", userData2.get("password"));

        Response responseGetAuth2 = RestAssured
                .given()
                .body(authData2)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String, String> authData1 = new HashMap<>();
        authData1.put("email", userData1.get("email"));
        authData1.put("password", userData1.get("password"));

        //DELETE USER1
        Response responseDeleteUser1 = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth2, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth2, "auth_sid"))
                .delete("https://playground.learnqa.ru/api/user/" + userId1)
                .andReturn();

        //GET USER1
        Response responseUserData1 = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/user/"  + userId1)
                .andReturn();

       Assertions.assertResponseTextEquals(responseUserData1, "{\"username\":\"" + userName1 + "\"}");

        //GET USER2
        Response responseUserData2 = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/user/"  + userId2)
                .andReturn();

        Assertions.assertResponseTextEquals(responseUserData2, "{\"username\":\"" + userName2 + "\"}");


    }

}
