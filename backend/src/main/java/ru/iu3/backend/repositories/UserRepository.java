package ru.iu3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iu3.backend.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}