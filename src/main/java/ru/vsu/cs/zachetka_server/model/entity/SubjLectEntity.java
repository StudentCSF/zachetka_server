package ru.vsu.cs.zachetka_server.model.entity;

import java.util.UUID;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "subj_lect", schema = "public")
public class SubjLectEntity {
    @Id
    @Column
    private UUID uid;

    @Column
    private UUID subjUid;

    @Column
    private UUID lectUid;


}
