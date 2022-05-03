package ru.vsu.cs.zachetka_server.model.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.vsu.cs.zachetka_server.model.enumerate.UserRole;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddUserRequest implements IValidated {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotNull
    private UserRole role;
}
