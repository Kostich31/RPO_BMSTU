package ru.iu3.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-модель, описывающая артистов
 * 
 * @author kostya
 */
@Entity
@Table(name = "artists")
@Access(AccessType.FIELD)
public class Artist {
    // Поле ID (not null, auto increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long id;
    // Поле с именем
    @Column(name = "name", nullable = false)
    public String name;

    // Поле, которое показывает отношение многие-к-одному
    @ManyToOne
    @JoinColumn(name = "countryid")
    public Country countryid;

    // Устанавливаем обратную связь: один ко многим от таблицы артистов в музее
    @JsonIgnore
    @OneToMany(mappedBy = "artistid")
    public List<Painting> paintings = new ArrayList<Painting>();

    // Поле "Возраст"
    @Column(name = "age")
    public String age;

    // Конструктор без параметров
    public Artist() {
    }

    /**
     * Конструктор с параметром id
     * 
     * @param id
     */
    public Artist(Long id) {
        this.id = id;
    }
}
