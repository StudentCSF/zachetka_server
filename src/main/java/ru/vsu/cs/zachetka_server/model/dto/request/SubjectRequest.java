package ru.vsu.cs.zachetka_server.model.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class SubjectRequest implements IValidated {

    @NotBlank
    private String name;

    @NotNull
    private Byte semester;
}
