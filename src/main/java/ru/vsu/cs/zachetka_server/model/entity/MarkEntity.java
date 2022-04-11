package ru.vsu.cs.zachetka_server.model.entity;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "mark", schema = "public")
public class MarkEntity {
    @Id
    @Column
    private UUID uid;

    @Column
    private UUID studUid;

    @Column
    private UUID slUid;

    @Column
    private Byte mark;

    @Column
    private LocalDate date;
}
