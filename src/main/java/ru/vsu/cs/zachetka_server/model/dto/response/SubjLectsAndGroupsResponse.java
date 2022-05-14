package ru.vsu.cs.zachetka_server.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SubjLectsAndGroupsResponse {

    private List<SubjLectResponse> subjLects;

    private List<Float> groups;
}
