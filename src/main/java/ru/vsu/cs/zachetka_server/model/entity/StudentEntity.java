package ru.vsu.cs.zachetka_server.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "student", schema = "public")
public class StudentEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID uid;

    @Column(name = "user_uid")
    private UUID userUid;

    @Column(name = "fio")
    private String fio;

    @Column(name = "course")
    private Byte course;

    @Column(name = "group_number")
    private Float group;
}