package ru.vsu.cs.zachetka_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.vsu.cs.zachetka_server.model.dto.request.AddUserRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.AuthUserRequest;
import ru.vsu.cs.zachetka_server.model.entity.UserEntity;
import ru.vsu.cs.zachetka_server.service.UserService;

import java.util.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return null;
    }

    @PostMapping("/user/register")
    public void registerUser(@RequestBody AddUserRequest addUserRequest) {
        this.userService.addUser(addUserRequest);
    }

    @PostMapping("/user/au")
    public RedirectView authorize(@RequestBody AuthUserRequest authUserRequest) {
        UserEntity user = this.userService.authUser(authUserRequest);
        String role = user.getRole().name().toLowerCase(Locale.ROOT);
        return new RedirectView(String.format("/%s/%s", role, user.getUid()));
    }

    @PostMapping("/user/auth")
    public String authorizeUser(@RequestBody AuthUserRequest authUserRequest) {
        UserEntity user = this.userService.authUser(authUserRequest);
        String role = user.getRole().name().toLowerCase(Locale.ROOT);
        return String.format("/%s/%s", role, user.getUid());
    }
}
