package ru.vsu.cs.zachetka_server.model.entity;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "mark", schema = "public")
public class MarkEntity {
    @Id
    @Column(name = "uid")
    private UUID uid;

    @Column(name = "stud_uid")
    private UUID studUid;

    @Column(name = "subj_lect_uid")
    private UUID slUid;

    @Column(name = "mark")
    private Byte mark;

    @Column(name = "date")
    private LocalDate date;
}
