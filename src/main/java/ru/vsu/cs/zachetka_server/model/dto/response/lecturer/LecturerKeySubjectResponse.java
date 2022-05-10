package ru.vsu.cs.zachetka_server.model.dto.response.lecturer;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

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

    @Override
    @JsonValue
    public String toString() {
        return this.slUid + "%" + this.name + "%" +  this.semester;
    }
}
