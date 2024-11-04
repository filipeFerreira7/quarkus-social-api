package io.github.filipeFerreira7.quarkussocial.rest;

import io.github.filipeFerreira7.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class UserResourceTest {
    @Test
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
}