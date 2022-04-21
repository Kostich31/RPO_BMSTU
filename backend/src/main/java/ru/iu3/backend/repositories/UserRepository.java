package ru.iu3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iu3.backend.models.Users;

import java.util.Optional;

/**
 * Интерфейс, который содержит в себе диапазон встроенных функций для обработки данных
 * @author kostya
 */
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByToken(String valueOf);
    Optional<Users> findByLogin(String login);
}
