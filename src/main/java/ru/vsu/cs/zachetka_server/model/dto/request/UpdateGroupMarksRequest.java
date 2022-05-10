package ru.vsu.cs.zachetka_server.model.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.vsu.cs.zachetka_server.model.enumerate.Mark;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class UpdateGroupMarksRequest implements IValidated {

    @NotNull
    private UUID studUid;

    private Mark mark;

    private String date;
}
