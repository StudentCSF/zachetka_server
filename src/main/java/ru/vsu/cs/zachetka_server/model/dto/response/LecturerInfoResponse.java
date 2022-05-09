package ru.vsu.cs.zachetka_server.model.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.vsu.cs.zachetka_server.model.enumerate.Mark;

import java.util.UUID;

@Data
@Builder
public class LecturerInfoResponse {

    UUID studUid;

    String studFio;

    Mark mark;

    String examDate;
}
