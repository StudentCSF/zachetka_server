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
@Table(name = "lecturer", schema = "public")
public class LecturerEntity {

    @Id
    @Column
    private UUID uid;

    @Column(name = "user_uid")
    private UUID userUid;

    @Column
    private String fio;
}
