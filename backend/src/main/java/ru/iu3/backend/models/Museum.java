package ru.iu3.backend.models;

import javax.persistence.*;

@Entity
@Table(name = "museums")
@Access(AccessType.FIELD)

public class Museum {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "location")
    public String location;

    public Museum() {
    }

    public Museum(Long id) {
        this.id = id;
    }
}
