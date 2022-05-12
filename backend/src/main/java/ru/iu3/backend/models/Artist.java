package ru.iu3.backend.models;
import javax.persistence.*;
@Entity
@Table(name = "artists")
@Access(AccessType.FIELD)
public class Artist {
    public Artist() { }
    public Artist(Long id) {
        this.id = id;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;
    @Column(name = "name", nullable = false, unique = true)
    public String name;

    @ManyToOne()
    @JoinColumn(name = "countryid")
    public Country countryid;

    @Column(name = "age")
    public String age;
}