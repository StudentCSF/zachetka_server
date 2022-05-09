package ru.vsu.cs.zachetka_server.component.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import ru.vsu.cs.zachetka_server.model.dto.response.LecturerKeySubjectResponse;

import java.io.IOException;

@JsonComponent
public class LecturerKeySubjectSerializer extends JsonSerializer<LecturerKeySubjectResponse> {

    private final ObjectMapper objectMapper;

    @Autowired
    public LecturerKeySubjectSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void serialize(LecturerKeySubjectResponse lecturerKeySubjectResponse,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider)
            throws IOException {
        this.objectMapper.writeValueAsString(lecturerKeySubjectResponse);
        jsonGenerator.writeFieldName(lecturerKeySubjectResponse.toString());
    }
}
