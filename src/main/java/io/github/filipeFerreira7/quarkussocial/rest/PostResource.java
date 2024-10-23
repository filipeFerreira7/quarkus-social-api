package io.github.filipeFerreira7.quarkussocial.rest;
import io.github.filipeFerreira7.quarkussocial.domain.model.Post;
import io.github.filipeFerreira7.quarkussocial.domain.model.User;
import io.github.filipeFerreira7.quarkussocial.domain.repository.FollowerRepository;
import io.github.filipeFerreira7.quarkussocial.domain.repository.PostRepository;
import io.github.filipeFerreira7.quarkussocial.domain.repository.UserRepository;
import io.github.filipeFerreira7.quarkussocial.rest.dto.CreatePostRequest;
import io.github.filipeFerreira7.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON) //consome jSON
@Produces(MediaType.APPLICATION_JSON) // return jSON
public class PostResource {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final PostRepository repository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository repository, FollowerRepository followerRepository
    ){
        this.repository = repository;
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request){
        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);

        repository.persist(post);


        return Response.status(Response.Status.CREATED).build();
    }
    @GET
    public Response listPosts(@PathParam("userId") Long userId,
                              @HeaderParam("followerId") Long followerId){

        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(followerId == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Your forgot the header followerId")
                    .build();
        }

        User follower = userRepository.findById(followerId);
        if(follower == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("inexistent follower followerId")
                    .build();
        }

        boolean follows = followerRepository.follows(follower, user);

//        if(!follows){
//            return Response.status(Response.Status.FORBIDDEN)
//                    .entity("You cannot see this post because you don't follow the user")
//                    .build();
//        }
        PanacheQuery<Post> query = repository.find("user",
                Sort.by("dateTime", Sort.Direction.Descending),user);

        List<Post> list = query.list();

         var postResponseList =

                 list.stream()
//                .map(post -> PostResponse.fromEntity(post))
                 .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(postResponseList).build();

    }

}
