package com.example.spaceships.spaceships;
import com.example.spaceships.spaceships.controller.SpaceShipController;
import com.example.spaceships.spaceships.entities.SpaceShip;
import com.example.spaceships.spaceships.service.SpaceShipService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpaceShipController.class)
public class SpaceShipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpaceShipService spaceShipService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllSpaceships() throws Exception {
        Page<SpaceShip> page = new PageImpl<>(Collections.singletonList(new SpaceShip(1L, "Enterprise", "Exploracion")));
        when(spaceShipService.getAllSpaceships(PageRequest.of(0, 10))).thenReturn(page);

        mockMvc.perform(get("/spaceships")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Enterprise"));
    }

    @Test
    void testGetSpaceShipById() throws Exception {
        SpaceShip ship = new SpaceShip(1L, "Halcon Milenario", "Carga");
        when(spaceShipService.getSpaceShipById(1L)).thenReturn(Optional.of(ship));

        mockMvc.perform(get("/spaceships/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Halcon Milenario"));
    }

    @Test
    void testSearchSpaceshipsByName() throws Exception {
        List<SpaceShip> ships = Arrays.asList(new SpaceShip(1L, "X-Wing", "Caza"));
        when(spaceShipService.searchSpaceshipsByName("X")).thenReturn(ships);

        mockMvc.perform(get("/spaceships/search")
                        .param("name", "X"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("X-Wing"));
    }

    @Test
    void testCreateSpaceShip() throws Exception {
        SpaceShip ship = new SpaceShip(null, "TIE Fighter", "Caza");
        SpaceShip savedShip = new SpaceShip(1L, "TIE Fighter", "Caza");
        when(spaceShipService.createSpaceShip(any(SpaceShip.class))).thenReturn(savedShip);

        mockMvc.perform(post("/spaceships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ship)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("TIE Fighter"));
    }

    @Test
    void testUpdateSpaceShip() throws Exception {
        SpaceShip ship = new SpaceShip(1L, "Updated Ship", "Exploracion");
        when(spaceShipService.updateSpaceShip(eq(1L), any(SpaceShip.class))).thenReturn(Optional.of(ship));

        mockMvc.perform(put("/spaceships/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ship)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Updated Ship"));
    }

    @Test
    void testDeleteSpaceShip() throws Exception {
        when(spaceShipService.deleteSpaceShip(1L)).thenReturn(true);

        mockMvc.perform(delete("/spaceships/1"))
                .andExpect(status().isNoContent());
    }
}