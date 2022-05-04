package ru.vsu.cs.zachetka_server.model.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.vsu.cs.zachetka_server.model.enumerate.Mark;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class StudentRawResponse {

    UUID studUid;

    String studFio;

    Mark mark;

    LocalDate examDate;
}
