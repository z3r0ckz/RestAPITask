package tests;

import DTO.*;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.*;

import java.util.*;

public class TestApiRest {
    @Test
    public static void testApiRest() {
        //------Step 1 Send GET Request to get all posts (/posts).
        HttpResponse<JsonNode> response = RestApiUtils.get(ConfigTestData.url + "/posts");
        JsonNode responseBody = response.getBody();
        int statusCode = response.getStatus();
        List<PostsDTO> users = RestApiUtils.getList(responseBody.toString(), PostsDTO.class);
        //---------Status code 200
        Assert.assertEquals(statusCode,HttpStatus.OK.getCode(),"The Status Code is incorrect");
        //validate the list is JSON
        String contentType = response.getHeaders().getFirst("Content-Type");
        boolean isJSON = contentType != null && contentType.toLowerCase().contains("application/json");
        Assert.assertTrue(isJSON, "The list in the response body is not JSON");
        // Step 3: Assertion to check if the list is sorted in ascending order by id
        users.sort(Comparator.comparingInt(PostsDTO::getId));
        List<PostsDTO> sortedList = new ArrayList<>(users);
        Assert.assertEquals(users, sortedList, "The list of posts is not sorted in ascending order by id");
        //Step 2 Send GET request to get post with id=99 (/posts/99).
        //Status code 200
        response = RestApiUtils.get(ConfigTestData.url + "/posts/99");
        statusCode = response.getStatus();
        Assert.assertEquals(statusCode,HttpStatus.OK.getCode(),"The Status code is incorrect");
        //Post information is correct
        // Validate the user information
        PostsDTO user = RestApiUtils.getObject(response.getBody().toString(), PostsDTO.class);
        if (user != null) {
            Assert.assertEquals(user.getId(), 99, "The user ID doesn't match");
            Assert.assertEquals(user.getUserId(), 10, "The UserID doesn't match");
            Assert.assertNotNull(user.getTitle(), "The title is empty");
            Assert.assertNotNull(user.getBody(), "The Body is empty");
        }
        //Step 3 Send GET request to get post with id=150 (/posts/150)
        response = RestApiUtils.get(ConfigTestData.url + "/posts/150");
        responseBody = response.getBody();
        statusCode = response.getStatus();
        //Code status 404
        Assert.assertEquals(statusCode,HttpStatus.NOT_FOUND.getCode(),"The Status code is incorrect");
        //Body is Empty
        Assert.assertTrue(responseBody.toString().trim().isEmpty() || responseBody.toString().equals("{}"),"The body is not Empty");

        //Step 4 Send POST request to create post with userId=1 and random body and random title (/posts).
        int userID = 1;
        String title = RandomStrings.generateTitle();
        String body = RandomStrings.generateBody();
        JSONObject postParams = new JSONObject();
        postParams.put("userId", userID);
        postParams.put("title", title);
        postParams.put("body", body);

        // Send POST request
        HttpResponse<JsonNode> responsePost = RestApiUtils.post(ConfigTestData.url + "/posts", postParams);
        int statusPostCode = responsePost.getStatus();
        Assert.assertEquals(statusPostCode, HttpStatus.POST_INFORMATION_IS_CORRECT.getCode(), "The code is incorrect");
        // Get the response body
        responseBody = responsePost.getBody();
        // Validate the response
        if (responseBody != null) {
            JSONArray jsonArray = responseBody.getArray();
            if (jsonArray.length() > 0) {
                JSONObject jsonResponse = jsonArray.getJSONObject(0);
                String responseTitle = jsonResponse.getString("title");
                String responseBodyKey = jsonResponse.getString("body");
                int responseUserId = jsonResponse.getInt("userId");
                int responseId = jsonResponse.getInt("id");
                // Validate data
                Assert.assertEquals(responseId,101, "The ID doesn't match");
                Assert.assertEquals(responseUserId, userID, "The user ID doesn't match");
                Assert.assertEquals(responseTitle,title, "The title doesn't match");
                Assert.assertEquals(responseBodyKey,body, "The body doesn't match");
            } else {
                MyLogger.error("No posts found in the response.");
            }
        } else {
            MyLogger.error("Response body is null.");
        }
        //---------Step 5 Send GET request to get users (/users).
        response = RestApiUtils.get(ConfigTestData.url + "/users");
        responseBody = response.getBody();
        statusCode = response.getStatus();
        //--------------Status code 200
        Assert.assertEquals(statusCode,HttpStatus.OK.getCode(),"The Status Code is incorrect");
        //validate the list is JSON
        contentType = response.getHeaders().getFirst("Content-Type");
        isJSON = contentType != null && contentType.toLowerCase().contains("application/json");
        Assert.assertTrue(isJSON, "The list in the response body is not JSON");
        //User (id=5) data equals
        UserDTO userDTO = RestApiUtils.getObject(responseBody.toString(), UserDTO.class);

        if (userDTO != null) {
            Assert.assertEquals(userDTO.getName(), "Chelsey Dietrich", "The name is incorrect");
            Assert.assertEquals(userDTO.getUsername(), "Kamren", "The username is incorrect");
            Assert.assertEquals(userDTO.getEmail(), "Lucio_Hettinger@annie.ca", "The email is incorrect");

            AddressDTO address = userDTO.getAddress();
            Assert.assertEquals(address.getStreet(), "Skiles Walks", "The street is incorrect");
            Assert.assertEquals(address.getSuite(), "Suite 351", "The suite is incorrect");
            Assert.assertEquals(address.getCity(), "Roscoeview", "The city is incorrect");
            Assert.assertEquals(address.getZipcode(), "33263", "The zipcode is incorrect");

            GeoDTO geo = address.getGeo();
            Assert.assertEquals(geo.getLat(), "-31.8129", "The latitude is incorrect");
            Assert.assertEquals(geo.getLng(), "62.5342", "The longitude is incorrect");

            Assert.assertEquals(userDTO.getPhone(), "(254)954-1289", "The phone is incorrect");
            Assert.assertEquals(userDTO.getWebsite(), "demarco.info", "The website is incorrect");

            CompanyDTO company = userDTO.getCompany();
            Assert.assertEquals(company.getName(), "Keebler LLC", "The company name is incorrect");
            Assert.assertEquals(company.getCatchPhrase(), "User-centric fault-tolerant solution", "The catchPhrase is incorrect");
            Assert.assertEquals(company.getBs(), "revolutionize end-to-end systems", "The bs is incorrect");
        }
        //Step 6 Send GET request to get user with id=5 (/users/5).
        response = RestApiUtils.get(ConfigTestData.url + "/users/5");
        statusCode = response.getStatus();
        //---------Status code 200
        Assert.assertEquals(statusCode, HttpStatus.OK.getCode(), "The Status code is incorrect");
        UserDTO expectedUserDTO = RestApiUtils.getObject(response.getBody().toString(), UserDTO.class);
        // User data matches with user data in the previous step.
        Assert.assertEquals(userDTO, expectedUserDTO, "User data does not match the expected data");
    }

}
