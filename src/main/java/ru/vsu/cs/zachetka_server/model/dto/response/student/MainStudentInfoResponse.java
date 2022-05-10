package ru.vsu.cs.zachetka_server.model.dto.response.student;

import lombok.Builder;
import lombok.Data;
import ru.vsu.cs.zachetka_server.model.dto.response.student.StudentInfoResponse;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class MainStudentInfoResponse {

    private String fio;

    private Map<Byte, List<StudentInfoResponse>> info;
}
