package ru.vsu.cs.zachetka_server.model.entity;

import java.util.UUID;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "lecturer", schema = "public")
public class LecturerEntity {
    @Id
    @Column
    private UUID uid;

    @Column
    private String fio;
}
