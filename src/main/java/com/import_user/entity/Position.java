package com.import_user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "positions")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;
}
