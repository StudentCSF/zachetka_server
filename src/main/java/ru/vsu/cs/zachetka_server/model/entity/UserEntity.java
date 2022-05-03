package ru.vsu.cs.zachetka_server.model.entity;

import java.util.UUID;

import lombok.*;
import ru.vsu.cs.zachetka_server.model.enumerate.UserRole;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "user_", schema = "public")
public class UserEntity {
    @Id
    @Column(name = "uid")
    private UUID uid;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
