package com.example.spaceships.spaceships.service;

import com.example.spaceships.spaceships.entities.SpaceShip;
import com.example.spaceships.spaceships.repository.SpaceShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceShipService {

    @Autowired
    private SpaceShipRepository spaceShipRepository;

    public Page<SpaceShip> getAllSpaceships(Pageable pageable) {
        return spaceShipRepository.findAll(pageable);
    }

    public Optional<SpaceShip> getSpaceShipById(Long id) {
        return spaceShipRepository.findById(id);
    }

    public List<SpaceShip> searchSpaceshipsByName(String name) {
        return spaceShipRepository.findByNameContainingIgnoreCase(name);
    }

    public SpaceShip createSpaceShip(SpaceShip spaceShip) {
        return spaceShipRepository.save(spaceShip);
    }

    public Optional<SpaceShip> updateSpaceShip(Long id, SpaceShip spaceShip) {
        return spaceShipRepository.findById(id).map(existing -> {
            existing.setNombre(spaceShip.getNombre());
            existing.setTipo(spaceShip.getTipo());
            return spaceShipRepository.save(existing);
        });
    }

    public boolean deleteSpaceShip(Long id) {
        if (spaceShipRepository.existsById(id)) {
            spaceShipRepository.deleteById(id);
            return true;
        }
        return false;
    }
}