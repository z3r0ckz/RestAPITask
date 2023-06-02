package utils;

import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;

public class ConfigTestData {
    public static String url;
    public static String urlPost;
    public static String testData_Users;
    public static String testUsers_ID5;
    public static int idNumber;
    public static String dataResults;
    public static String user99;

    static {
        ISettingsFile environment = new JsonSettingsFile("Test_ConfigData.json");
        url = environment.getValue("/url").toString();
        urlPost = environment.getValue("/urlPost").toString();
        testData_Users = environment.getValue("/testData_Users").toString();
        testUsers_ID5 = environment.getValue("/jsonPathTest5").toString();
        idNumber = (int) environment.getValue("/idNumber");
        dataResults = environment.getValue("/dataResults").toString();
        user99 = environment.getValue("/testData_User99").toString();
    }
}
