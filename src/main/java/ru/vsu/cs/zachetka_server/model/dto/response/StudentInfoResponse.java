package ru.vsu.cs.zachetka_server.model.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.vsu.cs.zachetka_server.model.enumerate.Mark;

import java.time.LocalDate;

@Data
@Builder
public class StudentInfoResponse {

    private String subjName;

    private String lectFio;

    private Mark mark;

    private LocalDate date;
}
