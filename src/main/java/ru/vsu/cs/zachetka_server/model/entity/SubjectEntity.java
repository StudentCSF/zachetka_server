package ru.vsu.cs.zachetka_server.model.entity;

import java.util.UUID;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "subject", schema = "public")
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private UUID uid;

    @Column
    private String name;

    @Column
    private String semester;
}
