package ru.iu3.backend.models;

import javax.persistence.*;

@Entity
@Table(name = "artists")
@Access(AccessType.FIELD)

public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "countryid")
    public Long countryid;

    @Column(name = "age")
    public String age;

    public Artist() {}

    public Artist(Long id) {
        this.id = id;
    }
}
