package io.github.filipeFerreira7.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateUserRequest {
    @NotBlank(message = "the field 'name' must be filled")
    private String name;
    @NotNull(message = "the field 'age' must be filled")
    private Integer age;
}
