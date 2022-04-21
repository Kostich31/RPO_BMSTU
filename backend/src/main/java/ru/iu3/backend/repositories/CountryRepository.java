package ru.iu3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iu3.backend.models.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
