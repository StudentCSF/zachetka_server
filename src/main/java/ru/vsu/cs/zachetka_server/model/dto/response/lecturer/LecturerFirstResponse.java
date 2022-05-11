package ru.vsu.cs.zachetka_server.model.dto.response.lecturer;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class LecturerFirstResponse {

    private UUID lectUid;

    private String fio;

    private Set<String> periods;
}
