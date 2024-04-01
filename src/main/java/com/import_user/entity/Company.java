package com.import_user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "companys")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;
}
