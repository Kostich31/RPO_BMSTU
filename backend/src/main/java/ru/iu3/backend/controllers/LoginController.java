package ru.iu3.backend.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.iu3.backend.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import ru.iu3.backend.repositories.UserRepository;
import ru.iu3.backend.tools.Utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/auth")
public class LoginController {
    // Используем несколько репозиториев
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> credentials) {
        // В качестве JSON записываем логин, пароль. Логин - admin, пароль - qwerty
        String login = credentials.get("login");
        String pwd = credentials.get("password");

        if (!pwd.isEmpty() && !login.isEmpty()) {
            // Если логин и пароль не пусты, то ищем в БД пользователя с данным логином (логин уникальный)
            Optional<User> uu = userRepository.findByLogin(login);
            if (uu.isPresent()) {
                // Если нашли пользователя, то извлекаем информацию о нём
                User u2 = uu.get();

                // Извлекаем соль и пароль
                String hash1 = u2.password;
                String salt = u2.salt;
                // Высчитываем пароль (захешированный в одну сторону)
                String hash2 = Utils.ComputeHash(pwd, salt);

                // Если захешированный пароль из базы совпадает с просчитанным паролем, то формируем рандомный токен
                if (hash1.toLowerCase().equals(hash2.toLowerCase())) {
                    String token = UUID.randomUUID().toString();
                    u2.token = token;

                    // Добавляем активность пользователя - текущее время
                    u2.activity = LocalDateTime.now();

                    // Сохраняем информацию (save просто записывает, flush применяет все изменения)
                    User u3 = userRepository.saveAndFlush(u2);
                    return new ResponseEntity<Object>(u3, HttpStatus.OK);
                }
            }
        }

        // А если пользователь не нашёлся, то выбрасываем ошибку - пользователь не найден
        return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("/logout")
    public ResponseEntity logout(@RequestHeader(value = "Authorization", required = false) String token) {
        // Вот здесь была ошибка в методичке. Она исправлена
        if (token != null && !token.isEmpty()) {
            // Очищаем токен от всякого мусора
            token = StringUtils.removeStart(token, "Bearer").trim();

            // Находим пользователя по токену, а не по логину, чтобы в JSON ничего не передавать
            Optional<User> uu = userRepository.findByToken(token);
            if (uu.isPresent()) {
                // Пользователь найден -> можно разлогинить его на уровне базы данных
                User u = uu.get();

                u.token = null;
                userRepository.save(u);

                // Возвращаем статус - ОК
                return new ResponseEntity(HttpStatus.OK);
            }
        }

        // По токену пользователь не был найден -> ошибка 401
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}