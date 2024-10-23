package io.github.filipeFerreira7.quarkussocial.domain.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="followers")
public class Follower extends DefaultEntity {

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="follower_id")
    private User follower;
}
