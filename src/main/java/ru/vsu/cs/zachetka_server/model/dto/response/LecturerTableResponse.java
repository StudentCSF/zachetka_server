package ru.vsu.cs.zachetka_server.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class LecturerTableResponse {

    private Map<Float, List<LecturerInfoResponse>> table;
}
