package ru.iu3.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "museums")
@Access(AccessType.FIELD)
public class Museum {
    // ID музея - первичный ключ
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    // Поле название (имя)
    @Column(name = "name", nullable = false)
    public String name;

    // Поле "локация"
    @Column(name = "location")
    public String location;

    // Обратная связь: от одного к многим. В данном случае устанавливаем связь между двумя таблицами
    @JsonIgnore
    @OneToMany(mappedBy = "museumid")
    public List<Painting> paintings = new ArrayList<>();

    // Указываем связь "многие-ко-многим". Идём через промежуточную таблицу usermuseums
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "usersmuseums", joinColumns = @JoinColumn(name = "museumid"), inverseJoinColumns = @JoinColumn(name = "userid"))
    public Set<User> users = new HashSet<>();

    // Создаём конструктор
    public Museum() {
    }

    public Museum(Long id) {
        this.id = id;
    }
}