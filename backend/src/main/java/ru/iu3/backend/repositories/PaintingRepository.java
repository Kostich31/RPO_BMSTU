package ru.iu3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iu3.backend.models.Painting;

public interface PaintingRepository extends JpaRepository<Painting, Long> {
}