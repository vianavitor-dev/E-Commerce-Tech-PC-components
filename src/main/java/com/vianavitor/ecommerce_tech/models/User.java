package com.vianavitor.ecommerce_tech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(80)", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String email;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String password; // hashed password
}



