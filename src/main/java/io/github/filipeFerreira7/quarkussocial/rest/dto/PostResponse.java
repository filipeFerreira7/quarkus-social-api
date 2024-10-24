package io.github.filipeFerreira7.quarkussocial.rest.dto;

import io.github.filipeFerreira7.quarkussocial.domain.model.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private String text;
    private LocalDateTime dateTime;
    

    public static PostResponse fromEntity(Post post){
        PostResponse response = new PostResponse();

        response.setText(post.getText());
        response.setDateTime(post.getDateTime());

        return response;
    }

}
