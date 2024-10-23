package io.github.filipeFerreira7.quarkussocial.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User extends DefaultEntity {

    @Column(name="name")
    private String name;
    @Column(name ="age")
    private Integer age;


}
