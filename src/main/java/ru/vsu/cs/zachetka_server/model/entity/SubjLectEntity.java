package ru.vsu.cs.zachetka_server.model.entity;

import java.util.UUID;

import lombok.*;
import ru.vsu.cs.zachetka_server.model.enumerate.EvalType;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "subj_lect", schema = "public")
public class SubjLectEntity {

    @Id
    @Column(name = "uid")
    private UUID uid;

    @Column(name = "subj_uid")
    private UUID subjUid;

    @Column(name = "lect_uid")
    private UUID lectUid;

    @Column(name = "period")
    private String period;

    @Column(name = "eval_type")
    @Enumerated(EnumType.STRING)
    private EvalType evalType;

}
