package ru.vsu.cs.zachetka_server.model.entity;

import java.util.UUID;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "subject", schema = "public")
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private UUID uid;

    @Column(name = "name")
    private String name;

    @Column
    private String semester;
}
