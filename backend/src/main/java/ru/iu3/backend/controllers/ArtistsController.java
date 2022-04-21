package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.repositories.ArtistRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class ArtistsController {
    @Autowired
    ArtistRepository artistsRepository;

    @GetMapping("/artists")
    public List getAllCountries() {
        return artistsRepository.findAll();
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody Artist artists) throws Exception {
        try {
            Artist newArtists = artistsRepository.save(artists);
            return new ResponseEntity<Object>(newArtists, HttpStatus.OK);
        } catch (Exception exception) {
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

    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> updateCountry(@PathVariable(value = "id") Long artistsID,
                                                 @RequestBody Artist artistDetails) {
        Artist artist = null;
        Optional<Artist> cc = artistsRepository.findById(artistsID);

        if (cc.isPresent()) {
            artist = cc.get();

            artist.name = artistDetails.name;
            artist.age = artistDetails.age;
            artist.countryid = artistDetails.countryid;

            artistsRepository.save(artist);
            return ResponseEntity.ok(artist);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
        }
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Object> deleteCountry(@PathVariable(value = "id") Long artistID) {
        Optional<Artist> artists = artistsRepository.findById(artistID);
        Map<String, Boolean> resp = new HashMap<>();

        if (artists.isPresent()) {
            artistsRepository.delete(artists.get());
            resp.put("deleted", Boolean.TRUE);
        } else {
            resp.put("deleted", Boolean.FALSE);
        }

        return ResponseEntity.ok(resp);
    }
}
