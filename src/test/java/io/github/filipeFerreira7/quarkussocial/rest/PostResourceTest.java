package io.github.filipeFerreira7.quarkussocial.rest;

import io.github.filipeFerreira7.quarkussocial.domain.model.Follower;
import io.github.filipeFerreira7.quarkussocial.domain.model.Post;
import io.github.filipeFerreira7.quarkussocial.domain.model.User;
import io.github.filipeFerreira7.quarkussocial.domain.repository.FollowerRepository;
import io.github.filipeFerreira7.quarkussocial.domain.repository.PostRepository;
import io.github.filipeFerreira7.quarkussocial.domain.repository.UserRepository;
import io.github.filipeFerreira7.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {
    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;
    @Inject
    PostRepository postRepository;

    Long userId;

    Long userNotFollowerId;

    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUP(){
        //usuario padrao testes
        var user = new User();
        user.setAge(19);
        user.setName("Filipe");
        userRepository.persist(user);
        userId = user.getId();

        //postagem criada
        Post post = new Post();
        post.setText("Yo my homies, wassup? ");
        post.setUser(user);
        postRepository.persist(post);


        //usuario que nao segue
        var userNotFollower= new User();
        userNotFollower.setAge(22);
        userNotFollower.setName("Xilas");
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        //usuario que segue
        var userFollower= new User();
        userFollower.setAge(32);
        userFollower.setName("Jocivan");
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);
    }

    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Beautiful girl");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
            .pathParam("userId",userId)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("should return 404 when trying to make a post for an inexistent user")
    public void PostForAnInexistentUserTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Beautiful girl");

        var inexistent = 999;
        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId",inexistent)
                .when()
                .post()
                .then()
                .statusCode(404);
    }
    @Test
    @DisplayName("should return 404 when user doesn't exists")
    public void listPostUserNotFoundTest(){
        var inexistent = 999;
        given()
                .pathParam("userId",inexistent)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("should return 400 when follower doesn't exists")
    public void listPostFollowerNotFoundTest(){

        var inexistent = 999;
        given()
                .pathParam("userId",userId)
                .header("followerId",inexistent)
                .when()
                    .get()
                .then()
                .statusCode(400).body(Matchers.is("Inexistent followerId"));
    }

    @Test
    @DisplayName("should return 403 when follower isn't follower")
    public void listPostNotAFollowerTest(){
        given()
                .pathParam("userId",userId)
                .header("followerId",userNotFollowerId)
                .when()
                .get()
                .then()
                .statusCode(403).body(Matchers.is("You cannot see this post because you don't follow the user"));
    }

    @Test
    @DisplayName("should return posts")
    public void listPostsTest(){
        given()
                .pathParam("userId",userId)
                .header("followerId",userFollowerId)
                .when()
                .get()
                .then()
                .statusCode(200).body("size()", Matchers.is(1));
    }
}