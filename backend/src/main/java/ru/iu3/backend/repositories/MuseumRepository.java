package ru.iu3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iu3.backend.models.Museum;

public interface MuseumRepository extends JpaRepository<Museum, Long> {
}