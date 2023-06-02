package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JsonUtils {
    public static String readJsonFile(String filePath) {
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(filePath));
            return new String(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validateIFIsJSONFormat(String inputJSON) {
        try {
            JsonParser.parseString(inputJSON);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void saveJSON(String json) {
        try (FileWriter fileWriter = new FileWriter("src/test/resources/dataResults.json")) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkSortedIDJSON(String JSONString) {
        JSONArray dataArray = new JSONArray(JSONString);
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject dato = dataArray.getJSONObject(i);
            int currentId = dato.getInt("id");
            ids.add(currentId);
        }
        List<Integer> sortedIds = new ArrayList<>(ids);
        sortedIds.sort(Comparator.naturalOrder());
        return ids.equals(sortedIds);
    }

    public static <T> String toJsonString(T dto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
