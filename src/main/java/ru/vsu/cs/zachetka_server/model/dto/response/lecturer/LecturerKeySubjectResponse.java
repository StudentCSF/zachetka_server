package ru.vsu.cs.zachetka_server.model.dto.response.lecturer;

import lombok.*;
import ru.vsu.cs.zachetka_server.model.enumerate.EvalType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturerKeySubjectResponse {

    private String name;

    private Byte semester;

    private UUID slUid;

    private EvalType evalType;
}
