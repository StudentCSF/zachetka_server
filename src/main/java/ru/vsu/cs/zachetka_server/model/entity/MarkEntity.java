package ru.vsu.cs.zachetka_server.model.entity;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;
import ru.vsu.cs.zachetka_server.model.enumerate.Mark;

import javax.persistence.*;

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
    @Enumerated(EnumType.STRING)
    private Mark mark;

    @Column(name = "date")
    private LocalDate date;
}
