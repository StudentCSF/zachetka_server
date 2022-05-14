package ru.vsu.cs.zachetka_server.model.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.vsu.cs.zachetka_server.model.enumerate.EvalType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddSubjLectRequest implements IValidated {

    @NotBlank
    private String lectFio;

    @NotBlank
    private String subjName;

    @NotNull
    private Byte semester;

    @NotNull
    private EvalType evalType;

    @NotBlank
    private String period;

}
