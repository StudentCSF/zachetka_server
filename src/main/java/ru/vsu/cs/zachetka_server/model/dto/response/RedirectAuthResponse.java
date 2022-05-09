package ru.vsu.cs.zachetka_server.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RedirectAuthResponse {

    private String redirect;
}
