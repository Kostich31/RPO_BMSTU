package ru.iu3.backend.controllers;
// Импортируем необходимые модули
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.repositories.CountryRepository;

import java.util.*;

/**
 * Класс-контроллер таблицы "стран"
 * @author kostya
 */
@RestController
@RequestMapping("api/v1")
public class CountryController {
    // Получаем ссылку на репозиторий
    @Autowired
    CountryRepository countryRepository;

    /**
     * Метод, который возвращает просто список стран
     * @return - Список стран, которые есть в ьазе данных
     */
    @GetMapping("/countries")
    public List getAllCountries() {
        return countryRepository.findAll();
    }

    /**
     * Метод, который извлекает информацию из таблицы country, вместе с информацией по конкретному artists
     * @param countryID - ID страны
     * @return - Возвращает artists
     */
    @GetMapping("/countries/{id}/artists")
    public ResponseEntity<List<Artist>> getCountryArtists(@PathVariable(value = "id") Long countryID) {
        Optional<Country> cc = countryRepository.findById(countryID);
        if (cc.isPresent()) {
            return ResponseEntity.ok(cc.get().artists);
        }

        return ResponseEntity.ok(new ArrayList<Artist>());
    }

     /**
     * Метод, который добавляет country в таблиц
     * RequestBody - это наш экземпляр (через curl передаётся в виде JSON)
     * @param country - наш экземпляр класса country
     * @return - статус (ОК/НЕ ОК)
     */
    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@RequestBody Country country)
        throws Exception {
        try {
            // Попытка сохранить что-либо в базу данных
            Country newCountry = countryRepository.save(country);
            return new ResponseEntity<Object>(newCountry, HttpStatus.OK);
        } catch (Exception exception) {
            // Указываем тип ошибки
            String error;
            if (exception.getMessage().contains("ConstraintViolationException")) {
                error = "countAlreadyExists";
            } else {
                error = exception.getMessage();
            }
            Map<String, String> map = new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
        }
    }
    /**
     * Метод, который обновляет данные в таблице
     * @param countryID - указываем id по которому будем обновлять данные
     * @param countryDetails - сводки по Country
     * @return - ОК/НЕ ОК
     */
    @PutMapping("/countries/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable(value = "id") Long countryID,
                                                 @RequestBody Country countryDetails) {
        Country country = null;
        Optional<Country> cc = countryRepository.findById(countryID);
        if (cc.isPresent()) {
            country = cc.get();
            country.name = countryDetails.name;
            countryRepository.save(country);
            return ResponseEntity.ok(country);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "country not found");
        }
    }
    /**
     * Метод, который удаляет информацию из базы данных
     * @param countryId - по какому ID-шнику удаляем информацию
     * @return - возвращает true, если удалено успешно, false - в противном случае
     */
    @DeleteMapping("/countries/{id}")
    public ResponseEntity<Object> deleteCountry(@PathVariable(value = "id") Long countryId) {
        Optional<Country> country = countryRepository.findById(countryId);
        Map<String, Boolean> resp = new HashMap<>();
        // Возвратит true, если объект существует (не пустой)
        if (country.isPresent()) {
            countryRepository.delete(country.get());
            resp.put("deleted", Boolean.TRUE);
        } else {
            resp.put("deleted", Boolean.FALSE);
        }
        return ResponseEntity.ok(resp);
    }
}
