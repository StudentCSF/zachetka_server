package ru.vsu.cs.zachetka_server.model.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class AddMarkRawsRequest implements IValidated {

    @NotNull
    private Float group;

    @NotNull
    private UUID slUid;
}
