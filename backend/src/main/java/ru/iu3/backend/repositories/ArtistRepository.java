package ru.iu3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iu3.backend.models.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
