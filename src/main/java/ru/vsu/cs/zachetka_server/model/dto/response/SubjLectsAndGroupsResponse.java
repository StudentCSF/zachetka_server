package ru.vsu.cs.zachetka_server.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class SubjLectsAndGroupsResponse {

    private List<SubjLectResponse> subjLects;

    private Set<Float> groups;
}
