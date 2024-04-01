package com.import_user.entity;
import jakarta.persistence.*;

import java.sql.Date;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "birthday")
    private Date birthday;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "position")
    private Position position;

    @Column(name = "salary")
    private Integer salary;
}
