package com.getyourticket.cf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User Entity store information about users.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "USERS")
public class User {
    @Id
    @Column(name="id", length = 45)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="firstname", length = 45)
    private String firstname;

    @Column(name="lastname", length = 45)
    private String lastname;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="username")
    private String username;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;
//    private Set<Ticket> tickets = new HashSet<>();
    @OneToOne(mappedBy = "user")
    private Cart cart;
}
