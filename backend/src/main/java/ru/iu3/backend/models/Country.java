package ru.iu3.backend.models;

import javax.persistence.*;

@Entity
@Table(name = "countries")
@Access(AccessType.FIELD)

public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public Long id;

    @Column(name = "name", nullable = false, unique = true)
    public String name;

    public Country() {}

    public Country(Long id) {
        this.id = id;
    }
}
