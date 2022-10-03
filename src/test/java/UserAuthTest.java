import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthTest {

    @Test
    public void testCookie(){

        Response responseGetAuth = RestAssured

                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

                 assertTrue(responseGetAuth.getCookies().containsKey("HomeWork"), "Response doesn't have 'HomeWork' cookie");
                 String valueCookie = responseGetAuth.getCookie("HomeWork");
                 assertEquals(valueCookie, "hw_value", "Value HomeWork cookie not equal 'hw_value'");

//        if(responseGetAuth.getCookies().containsKey("HomeWork1"))
//        {
//            String valueCookie = responseGetAuth.getCookie("HomeWork");
//            assertEquals(valueCookie, "hw_value", "Value HomeWork cookie not equal 'hw_value'");
//        }
//        else System.out.println("Response doesn't have 'HomeWork' cookie");;


    }
}

