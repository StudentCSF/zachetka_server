package ru.vsu.cs.zachetka_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.vsu.cs.zachetka_server.model.dto.request.AddUserRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.AuthUserRequest;
import ru.vsu.cs.zachetka_server.model.entity.UserEntity;
import ru.vsu.cs.zachetka_server.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
public class UserController {

    private final UserService userService;

    private final String host = "http://localhost:8080";

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

    @PostMapping("/user/auth")
    public RedirectView authorize(@RequestBody AuthUserRequest authUserRequest) {
        UserEntity user = this.userService.authUser(authUserRequest);
        String role = user.getRole().name().toLowerCase(Locale.ROOT);
        return new RedirectView(String.format("/%s", role));
    }
}
