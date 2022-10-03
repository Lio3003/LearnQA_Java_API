import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "Compare actual query type with the type from the parameter", "Compare actual1","Compare actual"})

    public void testTextLength(String text) {
        Map<String, String> queryParams = new HashMap<>();

        if(text.length() >= 15)
        {
            queryParams.put("text", text);
        }

        assertEquals(text.length() >= 15, true, "The length of text more than 15 symbols!");
    }


}

