package io.github.filipeFerreira7.quarkussocial.rest;

import io.github.filipeFerreira7.quarkussocial.domain.model.Follower;
import io.github.filipeFerreira7.quarkussocial.domain.model.User;
import io.github.filipeFerreira7.quarkussocial.domain.repository.FollowerRepository;
import io.github.filipeFerreira7.quarkussocial.domain.repository.UserRepository;
import io.github.filipeFerreira7.quarkussocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Status;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {
    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;



    Long userId;
    Long followerId;


    @BeforeEach
    @Transactional
     void setUp(){
        //usuario
        var user = new User();
        user.setAge(19);
        user.setName("Filipe");
        userRepository.persist(user);
        userId = user.getId();

        // seguidor
        var follower = new User();
        follower.setAge(79);
        follower.setName("Juvenal");
        userRepository.persist(follower);
        followerId = follower.getId();

        //cria o follower
        var followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);
        followerRepository.persist(followerEntity);
    }
    @Test
    @DisplayName("should return 409 when followerId is equal to User id")
    public void sameUserAsFollowerTest(){
        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId",userId)
                .when()
                .put()
                .then()
                .statusCode(409)
                .body(Matchers.is("You cannot follow yourself!"));

    }

    @Test
    @DisplayName("should return 404 on follow a User id doesn't exists")
    public void userNotFoundWhenTryingToFollowTest(){
        var body = new FollowerRequest();
        body.setFollowerId(userId);

        var inexistent = 988;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId",inexistent)
                .when()
                .put()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("should follow a user")
    public void followUserTest(){

        var body = new FollowerRequest();
        body.setFollowerId(followerId);


        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId",userId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }

    @Test
    @DisplayName("should return 404 on list user followera and User id doesn't exists")
    public void userNotFoundWhenListingFollowersTest(){
        var inexistent = 988;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId",inexistent)
                .when()
                .get()
                .then()
                .statusCode(404);

    }


    @Test
    @DisplayName("should list a user's followers")
    public void listFollowersTest(){
        var response =
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId",userId)
                .when()
                .get()
                .then()
                .extract().response();
        var followersCount = response.jsonPath().get("followersCount");
        var followersContent = response.jsonPath().getList("content");

        assertEquals(Response.Status.OK.getStatusCode(),response.statusCode());
        assertEquals(1,followersCount);
        assertEquals(1,followersContent.size());

    }

    @Test
    @DisplayName("should return 404 on unfollow user and User id doesn't exists")
    public void userNotFoundWhenUnfollowingAUserTest(){
        var inexistent = 988;
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId",inexistent)
                .queryParam("followerId",followerId)
                .when()
                .delete()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("should Unfollow an user")
    public void unfollowUserTest(){

        given()
                .pathParam("userId",userId)
                .queryParam("followerId",followerId)
                .when()
                .delete()
                .then()
                .statusCode(204);

    }

}