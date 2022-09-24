import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.http.Headers;

import java.util.HashMap;
import java.util.Map;


public class HelloWorldTest {
    @Test

    public void testRestAssured() {
        Map<String, String> data = new HashMap<>();
                data.put("login", "secret_login");
                data.put("password", "secret_pass");

            Response responseForGet = RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post("https://playground.learnqa.ru/api/get_auth_cookie")
                    .andReturn();

            String responseCookie = responseForGet.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            if(responseCookie !=null){
            cookies.put("auth_cookie", responseCookie);
            }

            Response responseForCheck = RestAssured
                .given()
                .body(data)
                    .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

            responseForCheck.print();

//            System.out.println("\nPretty text:");
//            response.prettyPrint();
//
//            System.out.println("\nHeaders:");
//            Headers responseHeaders = response.getHeaders();
//            System.out.println(responseHeaders);
//
//            System.out.println("\nCookies:");
//            Map<String, String> responseCookies = response.getCookies();
//            System.out.println(responseCookies);

//        String responseCookie = response.getCookie("auth_cookie");
//        System.out.println(responseCookie);
    }

        }

