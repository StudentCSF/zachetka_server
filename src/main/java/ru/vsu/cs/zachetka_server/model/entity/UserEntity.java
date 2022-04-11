package ru.vsu.cs.zachetka_server.model.entity;

import java.util.UUID;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user", schema = "public")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private UUID uid;

    @Column
    private String login;

    @Column
    private String password;

    @Column
    private Byte role;
}
