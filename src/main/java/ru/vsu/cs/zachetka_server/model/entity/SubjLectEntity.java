package ru.vsu.cs.zachetka_server.model.entity;

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
@Table(name = "subj_lect", schema = "public")
public class SubjLectEntity {
    @Id
    @Column(name = "uid")
    private UUID uid;

    @Column(name = "subj_uid")
    private UUID subjUid;

    @Column(name = "lect_uid")
    private UUID lectUid;


}
