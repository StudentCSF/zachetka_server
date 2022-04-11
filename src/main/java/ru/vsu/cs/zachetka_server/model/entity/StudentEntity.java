package ru.vsu.cs.zachetka_server.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "student", schema = "public")
public class StudentEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID uid;

    @Column
    private String fio;

    @Column
    private Byte course;

    @Column
    private Byte gruppa;
}