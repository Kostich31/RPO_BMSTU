package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.models.Museum;
import ru.iu3.backend.models.Painting;
import ru.iu3.backend.repositories.MuseumRepository;
import ru.iu3.backend.repositories.PaintingRepository;
import ru.iu3.backend.tools.DataValidationException;

import javax.validation.Valid;
import java.util.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1")
public class PaintingController {
    @Autowired
    PaintingRepository paintingRepository;



    @GetMapping("/paintings")
    public Page<Painting> getAllPaintings(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return paintingRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/paintings/{id}")
    public ResponseEntity<Painting> getPainting(@PathVariable(value = "id") Long paintingId)
            throws DataValidationException {
        Painting painting = paintingRepository.findById(paintingId)
                .orElseThrow(()->new DataValidationException("Картина с таким индексом не найдена"));
        return ResponseEntity.ok(painting);
    }

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

    @PostMapping("/deletepaintings")
    public ResponseEntity<List<Painting>> deletePaintings(@Valid @RequestBody List<Painting> paintings) {
        paintingRepository.deleteAll(paintings);
        return new ResponseEntity(HttpStatus.OK);
    }
}
