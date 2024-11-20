package com.example.spaceships.spaceships.repository;

import com.example.spaceships.spaceships.entities.SpaceShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceShipRepository extends JpaRepository<SpaceShip, Long> {
    List<SpaceShip> findByNameContainingIgnoreCase(String name);
}