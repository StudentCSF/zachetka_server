package ru.vsu.cs.zachetka_server.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "student_group")
public class StudentGroupEntity {

    @Id
    private UUID uid;

    @Column(name = "stud_uid")
    private UUID studUid;

    @Column(name = "semester")
    private Byte semester;

    @Column(name = "group_number")
    private Float group;
}
