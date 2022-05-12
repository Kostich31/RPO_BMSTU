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
import ru.iu3.backend.repositories.ArtistRepository;
import ru.iu3.backend.repositories.CountryRepository;
import ru.iu3.backend.tools.DataValidationException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    CountryRepository countryRepository;
    @GetMapping("/artists")
    public Page getAllArtists(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return artistRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity getArtist(@PathVariable(value = "id") Long artistId)
            throws DataValidationException
    {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(()-> new DataValidationException("Художник с таким индексом не найден"));
        return ResponseEntity.ok(artist);
    }


    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody Artist artist)
            throws Exception {
        try {
            Optional<Country>
                    cc = countryRepository.findById(artist.countryid.id);
            if (cc.isPresent()) {
                artist.countryid = cc.get();
            }
            Artist nc = artistRepository.save(artist);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            System.out.println(ex.getMessage());
            if (ex.getMessage().contains("artists.name_UNIQUE"))
                error = "artistalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map =  new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object> (map, HttpStatus.OK);
        }
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long artistID,
                                                 @RequestBody Artist artistDetails) {
        Artist artist = null;
        Optional<Artist> cc = artistRepository.findById(artistID);

        if (cc.isPresent()) {
            artist = cc.get();
            artist.name = artistDetails.name;

            artistRepository.save(artist);
            return ResponseEntity.ok(artist);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
        }
    }


//    @DeleteMapping("/artists/{id}")
//    public ResponseEntity<Object> deleteArtist(@PathVariable(value = "id") Long artistId) {
//        Optional<Artist> country = artistRepository.findById(artistId);
//        Map<String, Boolean> resp = new HashMap<>();
//
//        // Возвратит true, если объект существует (не пустой)
//        if (country.isPresent()) {
//            artistRepository.delete(country.get());
//            resp.put("deleted", Boolean.TRUE);
//        } else {
//            resp.put("deleted", Boolean.FALSE);
//        }
//
//        return ResponseEntity.ok(resp);
//    }

    @PostMapping("/deleteartists")
    public ResponseEntity<List<Artist>> deleteArtists(@Valid @RequestBody List<Artist> artists) {
        artistRepository.deleteAll(artists);
        return new ResponseEntity(HttpStatus.OK);
    }


}