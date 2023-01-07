package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.HashMap;
import java.util.Map;

@Epic("User's cases")
@Feature("Create user")
public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test checks creation user with existing email")
    @DisplayName("Negative test create new user - email")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequestUserData("https://playground.learnqa.ru/api/user/",
                        userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @Description("This test checks creation user with invalid email")
    @DisplayName("Negative test create new user - email")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithInvalidEmail() {
        String email = DataGenerator.getInvalidRandomEmail();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequestUserData("https://playground.learnqa.ru/api/user/",
                        userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Test
    @Description("This test checks successfully creation new user")
    @DisplayName("Positive test create new user")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequestUserData("https://playground.learnqa.ru/api/user/",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @Description("This test checks inability to create a user with a name length of 1 character")
    @DisplayName("Negative test create new user - short name")
    @Severity(SeverityLevel.MINOR)
    public void testCreateUserWithNameInOneSymbol() {
        String email = DataGenerator.getRandomEmail();
        String shortUsername = DataGenerator.getNameWithShortUsername();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", shortUsername);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequestUserData("https://playground.learnqa.ru/api/user/",
                userData);
        Assertions.assertJsonHasShortUsername(responseCreateAuth, "The value of 'username' field is too short");
    }

    @Test
    @Description("This test checks inability to create a user with a long name")
    @DisplayName("Negative test create new user - long name")
    @Severity(SeverityLevel.MINOR)
    public void testCreateUserWithLongUsername() {
        String email = DataGenerator.getRandomEmail();
        String longUsername = DataGenerator.getNameWithLongUsername();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", longUsername);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequestUserData("https://playground.learnqa.ru/api/user/",
                userData);
        Assertions.assertJsonHasShortUsername(responseCreateAuth, "The value of 'username' field is too long");
    }

    @ParameterizedTest
    @Description("This test checks inability to create a user without required fields")
    @DisplayName("Negative test create new user without required fields")
    @Severity(SeverityLevel.NORMAL)
    @CsvSource({
            "  , 123, learnqa, learnqa, learnqa",
            "'hkkkg@example.com', , learnqa, learnqa, learnqa",
            "'nkkj@example.com', 123, , learnqa, learnqa",
            "'/;kk2@example.com', 123, learnqa, , learnqa",
            "'kkk2@example.com', 123, learnqa, learnqa, "
    })

    public void testCreateUserWithoutAnyFields(String email, String password, String username, String firstName, String lastName) {
        Map<String, String> userDataWithoutOneField = new HashMap<>();

        userDataWithoutOneField.put("email", email);
        userDataWithoutOneField.put("password", password);
        userDataWithoutOneField.put("username", username);
        userDataWithoutOneField.put("firstName", firstName);
        userDataWithoutOneField.put("lastName", lastName);

        Response responseCreateAuth = apiCoreRequests.makePostRequestUserDataWithoutOneField("https://playground.learnqa.ru/api/user/",
                        userDataWithoutOneField);

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

