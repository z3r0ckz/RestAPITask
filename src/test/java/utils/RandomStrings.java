package utils;

public class RandomStrings {

     public static String generateBody() {
         String[] bodyNames = { "BodyRandom", "Randoom", "demoBody", "sample" };
         int bodyIndex = (int) (Math.random() * bodyNames.length);
         return bodyNames[bodyIndex];
     }

    public static String generateTitle() {
        // Generar un nombre de usuario aleatorio
        String[] titleNames = { "user123", "testuser", "demo_user", "titles2023" };
        int index = (int) (Math.random() * titleNames.length);
        return titleNames[index];
    }

}

