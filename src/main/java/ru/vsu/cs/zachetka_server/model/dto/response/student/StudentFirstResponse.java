package ru.vsu.cs.zachetka_server.model.dto.response.student;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class StudentFirstResponse {

    private String fio;

    private UUID studUid;

    private List<Byte> semesters;
}
