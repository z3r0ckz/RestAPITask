package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import tests.MyLogger;
import java.util.Collections;
import java.util.List;

public class RestApiUtils {
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
    public static <T> T getObject(String responseBody, Class<T> clazz) {
        try {
            return objectMapper.readValue(responseBody, clazz);
        } catch (Exception e) {
            MyLogger.error("The Stack Trace error is: "+e);
            return null;
        }
    }
    //------------------ Simple HTTP Get
    public static HttpResponse<JsonNode> get(String route) {
        return Unirest.get(route)
                .header("accept", "application/json")
                .asJson();
    }
    //-------------- Simple HTTP post
    public static HttpResponse<JsonNode> post(String route, JSONObject requestBody) {

        return Unirest.post(route)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .asJson();
    }

}
