package ru.vsu.cs.zachetka_server.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RedirectAuthResponse {

    private String role;

    private UUID userUid;
}
