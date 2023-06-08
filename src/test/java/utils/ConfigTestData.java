package utils;

import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;

public class ConfigTestData {
    public static String url;
    public static int idNumber;

    static {
        ISettingsFile environment = new JsonSettingsFile("Test_ConfigData.json");
        idNumber = (int) environment.getValue("/idNumber");
    }

    static {
        ISettingsFile configEnvironment = new JsonSettingsFile("config.json");
        url = configEnvironment.getValue("/url").toString();
    }
}
