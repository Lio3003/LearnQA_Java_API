package lib;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;

public class DataGenerator {
    public static String getRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "@example.com";

    }
    public static String getInvalidRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "example.com";

    }

    public static String getNameWithShortUsername(){
        String shortUsername = RandomStringUtils.randomAlphabetic(1);
        return shortUsername;}

    public static String getNameWithLongUsername(){
        String longUsername = RandomStringUtils.randomAlphabetic(251);
        return longUsername;
    }
}
