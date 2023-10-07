package com.myblog1.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
//In spring security roles are not working with Data
@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 60)
    private String name;
}
