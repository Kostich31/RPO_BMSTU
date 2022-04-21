package ru.iu3.backend.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Painting;
import ru.iu3.backend.repositories.MuseumRepository;
import ru.iu3.backend.repositories.PaintingRepository;

import java.util.*;

/**
 * Класс - контроллер картин
 * @author kostya
 */
@RestController
@RequestMapping("api/v1")
public class PaintingController {
    // По аналогии у нас будет два репозитория
    @Autowired
    PaintingRepository paintingRepository;

    @Autowired
    MuseumRepository museumRepository;

    /**
     * Метод, который возвращает список всех картин, которые есть в базе данных
     * @return - список картин
     */
    @GetMapping("/paintings")
    public List getAllPaintings() {
        return paintingRepository.findAll();
    }

    /**
     * Метод, который добавляет картины в базу данных
     * @param painting - картины
     * @return - заголовок. Ок/не ок
     */
    @PostMapping("/paintings")
    public ResponseEntity<Object> createPainting(@RequestBody Painting painting) {
        try {
            Painting newPainting = paintingRepository.save(painting);
            return new ResponseEntity<Object>(newPainting, HttpStatus.OK);
        } catch (Exception exception) {
            // Указываем тип ошибки
            String error;
            if (exception.getMessage().contains("ConstraintViolationException")) {
                error = "paintingAlreadyExists";
            } else {
                error = exception.getMessage();
            }
            Map<String, String> map = new HashMap<>();
            map.put("error", error + "\n");
            return ResponseEntity.ok(map);
        }
    }

    /**
     * Метод, обновляющий данные по картинам
     * @param id - ID картины
     * @param paintingDetails - сведения по картинам
     * @return - ОК/не ОК
     */
    @PutMapping("/paintings/{id}")
    public ResponseEntity<Painting> updatePainting(@PathVariable(value = "id") Long id,
                                                   @RequestBody Painting paintingDetails) {
        Painting painting = null;
        Optional<Painting> cc = paintingRepository.findById(id);
        if (cc.isPresent()) {
            painting = cc.get();

            // Сведения о картинах
            painting.name = paintingDetails.name;
            painting.museumid = paintingDetails.museumid;
            painting.artistid = paintingDetails.artistid;
            painting.year = paintingDetails.year;
            paintingRepository.save(painting);
            return ResponseEntity.ok(painting);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "painting not found");
        }
    }

    /**
     * Метод, который осуществляет удаление картины
     * @param paintingID - ID картины
     * @return - статус: удален/не удален
     */
    @DeleteMapping("/paintings/{id}")
    public ResponseEntity<Object> deletePainting(@PathVariable(value = "id") Long paintingID) {
        Optional<Painting> cc = paintingRepository.findById(paintingID);
        Map<String, Boolean> resp = new HashMap<>();
        if (cc.isPresent()) {
            paintingRepository.delete(cc.get());
            resp.put("deleted", Boolean.TRUE);
        } else {
            resp.put("deleted", Boolean.FALSE);
        }
        return ResponseEntity.ok(resp);
    }
}
