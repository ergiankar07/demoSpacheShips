package com.example.spaceships.spaceships.controller;


import com.example.spaceships.spaceships.entities.SpaceShip;
import com.example.spaceships.spaceships.service.SpaceShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/spaceships")
public class SpaceShipController {

    @Autowired
    private SpaceShipService spaceShipService;

    @GetMapping
    public Page<SpaceShip> getAllSpaceships(Pageable pageable) {
        return spaceShipService.getAllSpaceships(pageable);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "spaceship", key = "#id")
    public ResponseEntity<SpaceShip> getSpaceShipById(@PathVariable Long id) {
        return spaceShipService.getSpaceShipById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<SpaceShip> searchSpaceshipsByName(@RequestParam String name) {
        return spaceShipService.searchSpaceshipsByName(name);
    }

    @PostMapping
    @CacheEvict(value = "spaceship", allEntries = true)
    public SpaceShip createSpaceShip(@RequestBody SpaceShip spaceShip) {
        return spaceShipService.createSpaceShip(spaceShip);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "spaceship", key = "#id")
    public ResponseEntity<SpaceShip> updateSpaceShip(@PathVariable Long id, @RequestBody SpaceShip spaceShip) {
        return spaceShipService.updateSpaceShip(id, spaceShip)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "spaceship", key = "#id")
    public ResponseEntity<Void> deleteSpaceShip(@PathVariable Long id) {
        if (spaceShipService.deleteSpaceShip(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}