package ru.iu3.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "artistid")
    public Artist artistid;

    @ManyToOne
    @JoinColumn(name = "museumid")
    public Museum museumid;

    @Column(name = "year")
    public Long year;
}