package br.com.clickbus.challenge.controller;


import br.com.clickbus.challenge.dto.PlaceDTO;
import br.com.clickbus.challenge.entity.Place;
import br.com.clickbus.challenge.exception.PlaceNotFoundException;
import br.com.clickbus.challenge.service.PlaceService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api("places")
@RestController
@RequestMapping("places")
public class PlaceController {

    private final PlaceService service;

    public PlaceController(PlaceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid PlaceDTO dto) {
        return new ResponseEntity(service.save(dto.buildPlace()).convertToDTO(), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        return service.findById(id)
                      .map(place -> ResponseEntity.ok(place.convertToDTO()))
                      .orElseThrow(() -> new PlaceNotFoundException(HttpStatus.NOT_FOUND.toString()));
    }

    @GetMapping(name = "findByName", path = "/")
    public ResponseEntity<List<PlaceDTO>> findByName(@RequestParam(value = "name") String name) {
        List<Place> places = service.findByName(name);

        if (places.isEmpty()) {
            throw new PlaceNotFoundException("There are no places associated with the given name.");
        }

        return ResponseEntity.ok(service.findByName(name)
                .stream()
                .map(Place::convertToDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping
    public ResponseEntity findAll() {
        Iterable<PlaceDTO> places = PlaceDTO.convertToList(service.findAll());
        return ResponseEntity.ok(places);
    }

    @PutMapping("/{id}")
    public ResponseEntity alter(@PathVariable Long id, @RequestBody @Valid PlaceDTO placeDTO) {
        Place place = service.findById(id).orElseThrow(null);
        return new ResponseEntity(service.alter(place, placeDTO).convertToDTO(), HttpStatus.OK);
    }
}
