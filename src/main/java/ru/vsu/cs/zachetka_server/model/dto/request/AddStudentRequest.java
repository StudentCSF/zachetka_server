package ru.vsu.cs.zachetka_server.model.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddStudentRequest implements IValidated {

    @NotBlank
    private String fio;

    @NotNull
    private Integer initYear;

    @NotNull
    private Byte initSem;

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
