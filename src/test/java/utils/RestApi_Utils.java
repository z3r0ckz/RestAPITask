package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RestApi_Utils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> getList(String json, Class<T> dtoClass) {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("JSON content is null or empty");
        }
        try {
            JavaType type = objectMapper.getTypeFactory().constructType(dtoClass);
            if (json.startsWith("[")) {
                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, dtoClass);
                return objectMapper.readValue(json, listType);
            } else {
                return Collections.singletonList(objectMapper.readValue(json, type));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> getListFromFile(String filePath, Class<T> dtoClass) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path is null or empty");
        }
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    throw new IllegalArgumentException("File path points to a directory, expected a file");
                } else {
                    JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, dtoClass);
                    return objectMapper.readValue(file, listType);
                }
            } else {
                throw new FileNotFoundException("File not found at: " + filePath);
            }
        } catch (IllegalArgumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    //------------------ Simple HTTP Get
    public static HttpResponse<JsonNode> get(String route) {
        return Unirest.get(route)
                .header("accept", "application/json")
                .asJson();
    }
    //-------------- Simple post
    public static HttpResponse<JsonNode> post(String route, JSONObject requestBody)  {
        Unirest.post(route)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .asJson();
        return null;
    }
}
