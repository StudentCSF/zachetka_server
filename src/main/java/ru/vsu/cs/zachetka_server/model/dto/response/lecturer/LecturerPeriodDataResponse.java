package ru.vsu.cs.zachetka_server.model.dto.response.lecturer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import ru.vsu.cs.zachetka_server.component.serializer.LecturerKeySubjectSerializer;

import java.util.Map;

@Data
@Builder
public class LecturerPeriodDataResponse {

    @JsonSerialize(keyUsing = LecturerKeySubjectSerializer.class)
    private Map<LecturerKeySubjectResponse, LecturerTableResponse> subjects;
}
