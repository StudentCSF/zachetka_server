package ru.vsu.cs.zachetka_server.model.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.vsu.cs.zachetka_server.model.enumerate.Mark;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class MarkRequest implements IValidated {

    @NotNull
    private UUID slUid;

    @NotNull
    private UUID studUid;

    private Mark mark;

    private LocalDate date;
}
