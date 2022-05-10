package ru.vsu.cs.zachetka_server.model.dto.response.lecturer;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MainLecturerInfoResponse {

    private String fio;

    Map<String, LecturerPeriodDataResponse> info;
}
