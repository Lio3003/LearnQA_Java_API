package tests;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.HashMap;
import java.util.Map;


public class UserRegisterTest extends BaseTestCase {

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    public void testCreateUserWithInvalidEmail() {
        String email = DataGenerator.getInvalidRandomEmail();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Test
    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    public void testCreateUserWithNameInOneSymbol() {
        String email = DataGenerator.getRandomEmail();
        String shortUsername = DataGenerator.getNameWithShortUsername();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", shortUsername);
        userData = DataGenerator.getRegistrationData(userData);


        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        Assertions.assertJsonHasShortUsername(responseCreateAuth, "The value of 'username' field is too short");

    }

    @Test
    public void testCreateUserWithLongUsername() {
        String email = DataGenerator.getRandomEmail();
        String longUsername = DataGenerator.getNameWithLongUsername();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", longUsername);
        userData = DataGenerator.getRegistrationData(userData);


        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertJsonHasShortUsername(responseCreateAuth, "The value of 'username' field is too long");

    }

    @ParameterizedTest
    @CsvSource({
            ", 123, learnqa, learnqa, learnqa",
            "'hkkkg@example.com',, learnqa, learnqa, learnqa",
            "'nkkj@example.com', 123,, learnqa, learnqa",
            "'/;kk2@example.com', 123, learnqa, , learnqa",
            "'kkk2@example.com', 123, learnqa, learnqa,"
    })

    public void testCreateUserWithoutAnyFields(String email, String password, String username, String firstName, String lastName) {
        Map<String, String> userDataWithoutOneField = new HashMap<>();

        userDataWithoutOneField.put("email", email);
        userDataWithoutOneField.put("password", password);
        userDataWithoutOneField.put("username", username);
        userDataWithoutOneField.put("firstName", firstName);
        userDataWithoutOneField.put("lastName", lastName);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userDataWithoutOneField)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        if (email == null) {
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: email");
        }
        else if (password == null)
        {
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: password");
        }
        else if (username == null)
        {
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: username");
        }
        else if (firstName == null)
        {
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: firstName");
        }
        else {
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: lastName");
        }
    }
}

