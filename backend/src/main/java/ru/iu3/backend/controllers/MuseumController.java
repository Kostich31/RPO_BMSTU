package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ru.iu3.backend.models.Country;
import ru.iu3.backend.models.Museum;
import ru.iu3.backend.models.Painting;
import ru.iu3.backend.repositories.MuseumRepository;
import ru.iu3.backend.tools.DataValidationException;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1")
public class MuseumController {
    // Репозиторий нашего музея
    @Autowired
    MuseumRepository museumRepository;

    @GetMapping("/museums")
//    public List getAllMuseums() {
//        return museumRepository.findAll();
//    }
    public Page<Museum> getAllMuseums(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return museumRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/museums/{id}")
    public ResponseEntity<Museum> getPainting(@PathVariable(value = "id") Long museumId)
            throws DataValidationException {
        Museum museum = museumRepository.findById(museumId)
                .orElseThrow(()->new DataValidationException("Музей с таким индексом не найдена"));
        return ResponseEntity.ok(museum);
    }


    @GetMapping("/museums/{id}/paintings")
    public ResponseEntity<List<Painting>> getPaintingMuseums(@PathVariable(value = "id") Long museumID) {
        Optional<Museum> cc = museumRepository.findById(museumID);
        if (cc.isPresent()) {
            return ResponseEntity.ok(cc.get().paintings);
        }

        return ResponseEntity.ok(new ArrayList<Painting>());
    }


    @PostMapping("/museums")
    public ResponseEntity<Object> createMuseum(@RequestBody Museum museum) throws Exception {
        try {
            // Попытка сохранить что-либо в базу данных
            Museum newMusem = museumRepository.save(museum);
            return new ResponseEntity<Object>(newMusem, HttpStatus.OK);
        } catch (Exception exception) {
            // Указываем тип ошибки
            String error;
            if (exception.getMessage().contains("ConstraintViolationException")) {
                error = "museumAlreadyExists";
            } else {
                error = exception.getMessage();
            }

            Map<String, String> map = new HashMap<>();
            map.put("error", error + "\n");

            return ResponseEntity.ok(map);
        }
    }


    @PutMapping("/museums/{id}")
    public ResponseEntity<Museum> updateMuseum(@PathVariable(value = "id") Long museumID,
                                                @RequestBody Museum museumDetails) {
        Museum museum = null;
        Optional<Museum> cc = museumRepository.findById(museumID);

        if (cc.isPresent()) {
            museum = cc.get();

            // Осуществляем обновление данных
            museum.name = museumDetails.name;
            museum.location = museumDetails.location;

            museumRepository.save(museum);
            return ResponseEntity.ok(museum);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Museum not found");
        }
    }


//    @DeleteMapping("/museums/{id}")
//    public ResponseEntity<Object> deleteMuseum(@PathVariable(value = "id") Long museumID) {
//        Optional<Museum> museum = museumRepository.findById(museumID);
//        Map<String, Boolean> resp = new HashMap<>();
//
//        // Возвратит true, если объект существует (не пустой)
//        if (museum.isPresent()) {
//            museumRepository.delete(museum.get());
//            resp.put("deleted", Boolean.TRUE);
//        } else {
//            resp.put("deleted", Boolean.FALSE);
//        }
//
//        return ResponseEntity.ok(resp);
//    }

    @PostMapping("/deletemuseums")
    public ResponseEntity<List<Museum>> deleteMuseums(@Valid @RequestBody List<Museum> museums) {
        museumRepository.deleteAll(museums);
        return new ResponseEntity(HttpStatus.OK);
    }
}