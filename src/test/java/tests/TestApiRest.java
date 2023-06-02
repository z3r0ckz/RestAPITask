package tests;

import DTO.PostsDTO;
import DTO.UserDataDTO;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.*;

import java.util.List;

public class TestApiRest {
    @Test
    public static void testApiRest() {
        //------Step 1 Send GET Request to get all posts (/posts).
        HttpResponse<JsonNode> response = RestApi_Utils.get(ConfigTestData.url + "/posts");
        JsonNode responseBody = response.getBody();
        int statusCode = response.getStatus();
        List<PostsDTO> users = RestApi_Utils.getList(responseBody.toString(), PostsDTO.class);
        //---------Status code 200
        Assert.assertEquals(statusCode,HttpStatus.OK.getCode(),"The Status Code is incorrect");
        //validate the list is JSON
        Assert.assertTrue(JsonUtils.validateIFIsJSONFormat(responseBody.toString()),"The Data is not in JSON format");
        //Post are sorted by ascending
        Assert.assertTrue(JsonUtils.checkSortedIDJSON(responseBody.toString()),"The list is not in ascendant order");
        //Step 2 Send GET request to get post with id=99 (/posts/99).
        //Status code 200
        response = RestApi_Utils.get(ConfigTestData.url + "/posts/99");
        statusCode = response.getStatus();
        Assert.assertEquals(statusCode,HttpStatus.OK.getCode(),"The Status code is incorrect");
        //Post information is correct
        List<PostsDTO> expectedUsers = RestApi_Utils.getListFromFile(ConfigTestData.testData_Users, PostsDTO.class);
        PostsDTO expectedUser = null;
        int expectedUserID = 0;
        String expectedBody = null;
        String expectedTitle = null;
        for (PostsDTO user : expectedUsers) {
            if (user.getId() == 99) {
                expectedUser = user;
                expectedUserID = user.getUserId();
                expectedTitle = user.getTitle();
                expectedBody = user.getBody();
                break;
            }
        }
        Assert.assertEquals(users.get(99-1), expectedUser, "The users don't match");
        Assert.assertEquals(expectedUserID,10,"The UserID don't match");
        Assert.assertNotNull(expectedTitle,"The title is empty");
        Assert.assertNotNull(expectedBody,"The Body is empty");
        //Step 3 Send GET request to get post with id=150 (/posts/150)
        response = RestApi_Utils.get(ConfigTestData.url + "/posts/150");
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
        postParams.put("userId",userID);
        postParams.put("title", title);
        postParams.put("body",body );
        //------------------------------Response Code 201
        HttpResponse<JsonNode> responsePost = RestApi_Utils.post(ConfigTestData.url+"/posts",postParams);
        assert responsePost != null;
        int statusPostCode = responsePost.getStatus();
        Assert.assertEquals(statusPostCode,HttpStatus.POST_INFORMATION_IS_CORRECT.getCode(),"The code is incorrect");
        response = RestApi_Utils.get(ConfigTestData.url+"/posts");
        responseBody = response.getBody();
        //-----------Post information is correct-------------
        JSONObject jsonResponse = responseBody.getObject();
        if (jsonResponse != null) {
            String responseTitle = jsonResponse.getString("title");
            String responseBodyKey = jsonResponse.getString("body");
            int responseUserId = jsonResponse.getInt("userId");
            int responseId = jsonResponse.getInt("id");
            // Validate data
            Assert.assertEquals(1, responseId, "The ID don't match");
            Assert.assertEquals(userID, responseUserId, "The user ID don't match");
            Assert.assertEquals(title, responseTitle, "The title don't match");
            Assert.assertEquals(body, responseBodyKey, "The body don't match");
        }
        //---------Step 5 Send GET request to get users (/users).
        response = RestApi_Utils.get(ConfigTestData.url + "/users");
        responseBody = response.getBody();
        statusCode = response.getStatus();
        //--------------Status code 200
        Assert.assertEquals(statusCode,HttpStatus.OK.getCode(),"The Status Code is incorrect");
        //validate the list is JSON
        Assert.assertTrue(JsonUtils.validateIFIsJSONFormat(responseBody.toString()),"The Data is not in JSON format");
        JsonUtils.saveJSON(responseBody.toPrettyString());
        //User id=5 Data equals
        List<UserDataDTO> usersDTO = RestApi_Utils.getList(responseBody.toString(), UserDataDTO.class);
        List<UserDataDTO> expectedUserDTO = RestApi_Utils.getListFromFile(ConfigTestData.dataResults, UserDataDTO.class);

        UserDataDTO expectedUser_DTO = null;
        for (UserDataDTO userDTO : usersDTO) {
            if (userDTO.getId() == 5) {
                expectedUser_DTO = userDTO;

                break;
            }
        }
        String jsonString = JsonUtils.toJsonString(expectedUser_DTO);
        Assert.assertEquals(jsonString,JsonUtils.readJsonFile(ConfigTestData.testUsers_ID5),"The Data don't match with the ID=5");
        //Step 6 Send GET request to get user with id=5 (/users/5).
        //Status code 200
        response = RestApi_Utils.get(ConfigTestData.url + "/users/5");
        responseBody = response.getBody();
        statusCode = response.getStatus();
        Assert.assertEquals(statusCode,HttpStatus.OK.getCode(),"The Status code is incorrect");
        //User data matches with user data in the previous step.
        assert expectedUser_DTO != null;
        Assert.assertEquals("UserDataDTO"+responseBody,expectedUser_DTO.toString(),"The users don't match");

    }

}
