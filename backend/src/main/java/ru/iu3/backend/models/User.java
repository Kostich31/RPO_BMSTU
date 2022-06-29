package ru.iu3.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Access(AccessType.FIELD)
public class User {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "login", unique = true, nullable = false)
    public String login;

    @JsonIgnore
    @Column(name = "password")
    public String password;

    @Transient
    public String np;

    @Column(name = "email")
    public String email;

    @JsonIgnore
    @Column(name = "salt")
    public String salt;

//    @JsonIgnore
    @Column(name = "token")
    public String token;

    @Column(name = "activity")
    public LocalDateTime activity;


    @ManyToMany(mappedBy = "users")
    public Set<Museum> museums = new HashSet<>();

    public User() {}


    public User(Long id) {
        this.id = id;
    }


    public void addMuseum(Museum m) {
        this.museums.add(m);
        m.users.add(this);
    }


    public void removeMuseum(Museum m) {
        this.museums.remove(m);
        m.users.remove(this);
    }
}