package ru.vsu.cs.zachetka_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vsu.cs.zachetka_server.component.BaseRequestValidationComponent;
import ru.vsu.cs.zachetka_server.exception.IncorrectPasswordException;
import ru.vsu.cs.zachetka_server.exception.RequestNotValidException;
import ru.vsu.cs.zachetka_server.exception.UserAlreadyExistsException;
import ru.vsu.cs.zachetka_server.exception.UserNotFoundException;
import ru.vsu.cs.zachetka_server.model.dto.request.AddUserRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.AuthUserRequest;
import ru.vsu.cs.zachetka_server.model.entity.UserEntity;
import ru.vsu.cs.zachetka_server.repository.UserRepository;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BaseRequestValidationComponent validationComponent;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, BaseRequestValidationComponent validationComponent, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.validationComponent = validationComponent;
        this.encoder = bCryptPasswordEncoder;
    }

    public UUID addUser(AddUserRequest addUserRequest) {
        if (!this.validationComponent.isValid(addUserRequest)) {
            throw new RequestNotValidException();
        }
        if (this.userRepository.findByLogin(addUserRequest.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        UUID userUid = UUID.randomUUID();

        this.userRepository.save(
                UserEntity.builder()
                        .login(addUserRequest.getLogin())
                        .password(this.encoder.encode(addUserRequest.getPassword()))
                        .role(addUserRequest.getRole())
                        .uid(userUid)
                        .build()
        );

        return userUid;
    }

    public UserEntity authUser(AuthUserRequest authUserRequest) {
        if (!this.validationComponent.isValid(authUserRequest)) {
            throw new RequestNotValidException();
        }

        UserEntity user = this.userRepository.findByLogin(authUserRequest.getLogin())
                .orElseThrow(UserNotFoundException::new);

        if (!encoder.matches(authUserRequest.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        return user;
    }
}
