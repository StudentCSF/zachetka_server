package ru.vsu.cs.zachetka_server.model.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class AddStudentRequest implements IValidated {

    @NotBlank
    private String fio;

    @NotBlank
    private Byte course;

    @NotBlank
    private Float group;

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
