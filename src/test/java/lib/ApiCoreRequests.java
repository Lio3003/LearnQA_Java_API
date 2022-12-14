package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url,  String cookie){
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithToken(String url, String token){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }
    @Step("Make a POST-authData-request")
    public Response makePostRequestAuthData(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a POST-authData1-request")
    public Response makePostRequestAuthData1(String url, Map<String, String> authData1){
        return given()
                .filter(new AllureRestAssured())
                .body(authData1)
                .post(url)
                .andReturn();
    }

    @Step("Make a POST-userData-request")
    public Response makePostRequestUserData(String url, Map<String, String> userData){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Make a POST-request without one field")
    public Response makePostRequestUserDataWithoutOneField(String url, Map<String, String> userDataWithoutOneField){
        return given()
                .filter(new AllureRestAssured())
                .body(userDataWithoutOneField)
                .post(url)
                .andReturn();
    }
    @Step("Make a POST-userData-request with Json")
    public JsonPath makePostRequestJson(String url, Map<String, String> userData){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .jsonPath();
    }

    @Step("Make a POST-userData1-request with Json")
    public JsonPath makePostRequestJson1(String url, Map<String, String> userData1){
        return given()
                .filter(new AllureRestAssured())
                .body(userData1)
                .post(url)
                .jsonPath();
    }
    @Step("Make a GET-request with user_agent data")
    public JsonPath makeGetRequestWithUserAgentData(String url, String userAgent){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("user-agent", userAgent))
                .get(url)
                .jsonPath();
    }

    @Step("Make a GET-request for long time job URL")
    public JsonPath makeGetRequestLongTimeJob(String url){
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .jsonPath();
    }

    @Step("Make a GET-request without token and cookie")
    public Response makeGetRequestWithoutTokenAndCookie(String url){
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make a DELETE-request with token and auth cookie")
    public Response makeDeleteRequest(String url, String token, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .delete(url)
                .andReturn();
    }

    @Step("Make a PUT-request with token and auth cookie and editData")
    public Response makePutRequestWithEditData(String url, String token, String cookie, Map<String, String> editData){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a PUT-request with only editData")
    public Response makePutRequestWithOnlyEditData(String url, Map<String, String> editData){
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a GET-request with token and auth cookie and editData")
    public Response makeGetRequestWithEditData(String url, String token, String cookie, Map<String, String> editData){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(editData)
                .get(url)
                .andReturn();
    }
}