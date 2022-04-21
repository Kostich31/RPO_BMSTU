package ru.iu3.backend.models;

import javax.persistence.*;

@Entity
@Table(name = "paintings")
@Access(AccessType.FIELD)

public class Painting {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "artistid")
    public Long artistid;

    @Column(name = "museumid")
    public Long museumid;

    @Column(name = "year")
    public Long year;
}
