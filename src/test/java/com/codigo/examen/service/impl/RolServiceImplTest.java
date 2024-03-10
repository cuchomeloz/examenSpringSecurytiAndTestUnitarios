package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolServiceImplTest {
    @Mock
    private  RolRepository rolRepository;
    @InjectMocks
    private RolServiceImpl rolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createRolSuccess() {
        Rol rol = new Rol();
        rol.setNombreRol("ROLE_TEST");

        when(rolRepository.findByNombreRol(rol.getNombreRol())).thenReturn(Optional.empty());
        when(rolRepository.save(rol)).thenReturn(rol);

        ResponseEntity<Rol> response = rolService.createRol(rol);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(rol, response.getBody());
    }

    @Test
    void createRol_RolAlreadyExists() {
        Rol rol = new Rol();
        rol.setNombreRol("ROLE_TEST");
        when(rolRepository.findByNombreRol(rol.getNombreRol())).thenReturn(Optional.of(rol));
        ResponseEntity<Rol> response = rolService.createRol(rol);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getRolByIdSuccess() {
        Long rolId = 1L;
        Rol rol = new Rol();
        rol.setIdRol(rolId);

        when(rolRepository.findById(rolId)).thenReturn(Optional.of(rol));

        ResponseEntity<Rol> response = rolService.getRolById(rolId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(rol, response.getBody());
    }

    @Test
    void getRolById_RolNotFound() {
        Long rolId = 1L;

        when(rolRepository.findById(rolId)).thenReturn(Optional.empty());

        ResponseEntity<Rol> response = rolService.getRolById(rolId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateRolSuccess() {
        Long rolId = 1L;
        Rol existingRol = new Rol();
        existingRol.setIdRol(rolId);

        Rol updatedRol = new Rol();
        updatedRol.setIdRol(rolId);
        updatedRol.setNombreRol("UPDATED_ROLE");

        when(rolRepository.findById(rolId)).thenReturn(Optional.of(existingRol));
        when(rolRepository.save(updatedRol)).thenReturn(updatedRol);

        ResponseEntity<Rol> response = rolService.updateRol(rolId, updatedRol);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(updatedRol, response.getBody());
    }


    @Test
    void updateRol_RolNotFound() {
        Long rolId = 1L;
        Rol updatedRol = new Rol();
        updatedRol.setIdRol(rolId);
        when(rolRepository.findById(rolId)).thenReturn(Optional.empty());

        ResponseEntity<Rol> response = rolService.updateRol(rolId, updatedRol);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}