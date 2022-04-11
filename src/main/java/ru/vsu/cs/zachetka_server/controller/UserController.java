package ru.vsu.cs.zachetka_server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.zachetka_server.model.entity.UserEntity;
import ru.vsu.cs.zachetka_server.repository.UserRepository;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(this.userRepository.findAll());
    }

   // @PutMapping("user/{uid}")
    //public ResponseEntity<UserEntity> addUser(@RequestBody )
}
