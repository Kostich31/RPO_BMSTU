package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Museum;
import ru.iu3.backend.models.Painting;
import ru.iu3.backend.models.User;
import ru.iu3.backend.repositories.MuseumRepository;
import ru.iu3.backend.repositories.UserRepository;
import ru.iu3.backend.tools.DataValidationException;
import ru.iu3.backend.tools.Utils;


import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1")
public class UserController {
    // По аналогии здесь используется два репозитория
    @Autowired
    UserRepository userRepository;

    @Autowired
    MuseumRepository museumRepository;


    @GetMapping("/users")
    public Page<User> getAllUsers(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return userRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "login")));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") Long userId)
            throws DataValidationException
    {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new DataValidationException("Пользователь с таким индексом не найдена"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User user) throws Exception {
        try {
            User nc = userRepository.save(user);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        } catch (Exception exception) {
            // Указываем тип ошибки
            String error;
            if (exception.getMessage().contains("ConstraintViolationException")) {
                error = "artistAlreadyExists";
            } else {
                error = exception.getMessage();
            }

            Map<String, String> map = new HashMap<>();
            map.put("error", error + "\n");

            return ResponseEntity.ok(map);
        }
    }


    @PostMapping("/users/{id}/addmuseums")
    public ResponseEntity<Object> addMuseums(@PathVariable(value = "id") Long userID,
                                             @Validated @RequestBody Set<Museum> museums) {
        // Извлекаем пользователя по конкретному ID-шнику
        Optional<User> uu = userRepository.findById(userID);
        int cnt = 0;

        if (uu.isPresent()) {
            User u = uu.get();

            // Если музеев несколько (а такое может быть вполне, то тогда добавляем их поочерёдно)
            for(Museum m: museums) {
                // Если есть музей, то мы, конечно, добавим его. Защита от дурака
                Optional<Museum> mm = museumRepository.findById(m.id);
                if (mm.isPresent()) {
                    u.addMuseum(mm.get());
                    ++cnt;
                }
            }

            // Сохраняем
            userRepository.save(u);
        }

        // Формируем
        Map<String, String> response = new HashMap<>();
        response.put("added", String.valueOf(cnt));

        return ResponseEntity.ok(response);
    }


    @PostMapping("/users/{id}/removemuseums")
    public ResponseEntity<Object> removeMuseums(@PathVariable(value = "id") Long userId,
                                                @Validated @RequestBody Set<Museum> museums) {
        Optional<User> uu = userRepository.findById(userId);
        int cnt = 0;

        if (uu.isPresent()) {
            User u = uu.get();
            for (Museum m: museums) {
                u.removeMuseum(m);
                ++cnt;
            }

            userRepository.save(u);
        }

        // Формируем ответ
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity updateUser(@PathVariable(value = "id") Long userId,
                                     @Valid @RequestBody User userDetails)
            throws DataValidationException
    {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new DataValidationException(" Пользователь с таким индексом не найден"));
            user.email = userDetails.email;
            String np = userDetails.np;
            if (np != null && !np.isEmpty()) {
                byte[] b = new byte[32];
                new Random().nextBytes(b);
                String salt = new String(Hex.encode(b));
                System.out.print(salt);
                user.password = Utils.ComputeHash(np, salt);
                user.salt = salt;
            }
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("users.email_UNIQUE"))
                throw new DataValidationException("Пользователь с такой почтой уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUsers(@PathVariable(value = "id") Long userId) {
        Optional<User> users = userRepository.findById(userId);
        Map<String, Boolean> resp = new HashMap<>();

        // Возвратит true, если объект существует (не пустой)
        if (users.isPresent()) {
            userRepository.delete(users.get());
            resp.put("deleted", Boolean.TRUE);
        } else {
            resp.put("deleted", Boolean.FALSE);
        }

        return ResponseEntity.ok(resp);
    }
}