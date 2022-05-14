package ru.vsu.cs.zachetka_server.model.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.vsu.cs.zachetka_server.model.enumerate.EvalType;

import java.util.UUID;

@Data
@Builder
public class SubjLectResponse {

    private UUID slUid;

    private String lectFio;

    private String subjName;

    private EvalType evalType;
}
