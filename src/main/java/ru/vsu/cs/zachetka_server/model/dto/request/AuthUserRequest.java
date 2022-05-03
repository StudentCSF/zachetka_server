package ru.vsu.cs.zachetka_server.model.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class AuthUserRequest implements IValidated {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
