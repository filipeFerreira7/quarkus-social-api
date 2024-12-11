package io.github.filipeFerreira7.quarkussocial.rest;

import io.github.filipeFerreira7.quarkussocial.rest.dto.CreateUserRequest;
import io.github.filipeFerreira7.quarkussocial.rest.dto.ResponseError;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
@TestMethodOrder(MethodOrderer.class)
class UserResourceTest {


    @Test
    @Order(1)
    @DisplayName("should create an user sucessfully")
    public void createUserTest(){
        var user = new CreateUserRequest();
        user.setName("Filipao");
        user.setAge(30);

      var response =
              given()
                .contentType(ContentType.JSON)
                .body(user).when()
                .post("/users").then().extract().response();

      assertEquals(201,response.statusCode());
      assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @Order(2)
    @DisplayName("should return error when json is not valid")
    public void createUserValidationError(){
        var user = new CreateUserRequest();
        user.setAge(null);
        user.setName(null);

        var response = given().contentType(ContentType.JSON).body(user).when().post("/users").then()
                .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());

        assertEquals("Validation Error", response.jsonPath().getString("message"));
        List<Map<String,String>> errors = response.jsonPath().getList("errors");

        assertNotNull(errors.get(0).get("message"));
        assertEquals("the field 'name' must be filled",errors.get(0).get("message"));
        assertEquals("the field 'age' must be filled",errors.get(1).get("message"));
    }

    @Test
    @Order(3)
    @DisplayName("Should list all users")
    void listAllUserTest(){

        given().contentType(ContentType.JSON).when().get("/users").then().statusCode(200)
                .body("size()", Matchers.is(12));
    }

}