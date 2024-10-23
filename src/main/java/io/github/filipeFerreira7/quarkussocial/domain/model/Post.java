package io.github.filipeFerreira7.quarkussocial.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="posts")
@Data
public class Post extends DefaultEntity{
    @Column(name = "post_text")
    private String text;
    @Column(name ="dateTime")
    private LocalDateTime dateTime;

    @ManyToOne // muitas postagens para um usuário
    @JoinColumn(name="user_id")
    private User user;

    @PrePersist
    public void prePersist(){
        setDateTime(LocalDateTime.now());
    }

}
