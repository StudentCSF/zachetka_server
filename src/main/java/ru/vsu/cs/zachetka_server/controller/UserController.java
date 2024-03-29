package ru.vsu.cs.zachetka_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.zachetka_server.model.dto.request.AddUserRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.AuthUserRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.RedirectAuthResponse;
import ru.vsu.cs.zachetka_server.model.entity.UserEntity;
import ru.vsu.cs.zachetka_server.service.UserService;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<UserEntity> getAllUsers() {
        return null;
    }

    @PostMapping("/user/register")
    public UUID registerUser(@RequestBody AddUserRequest addUserRequest) {
        return this.userService.addUser(addUserRequest);
    }

    @PostMapping("/user/auth")
    public RedirectAuthResponse authorizeUser(@RequestBody AuthUserRequest authUserRequest) {
        UserEntity user = this.userService.authUser(authUserRequest);
        String role = user.getRole().name().toLowerCase(Locale.ROOT);
        return RedirectAuthResponse.builder()
                .role(role)
                .userUid(user.getUid())
                .build();
    }
}
