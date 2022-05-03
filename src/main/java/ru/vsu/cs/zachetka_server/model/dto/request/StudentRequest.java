package ru.vsu.cs.zachetka_server.model.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StudentRequest implements IValidated {

    @NotBlank
    private String fio;

    private Byte course;

    private Float group;
}
