package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    public ResponseEntity<Rol> createRol(Rol rol) {
        Optional<Rol> existingRol = rolRepository.findByNombreRol(rol.getNombreRol());
        if (existingRol.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        Rol createdRol = rolRepository.save(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRol);
    }

    @Override
    public ResponseEntity<Rol> getRolById(Long id) {
        Optional<Rol> rol = rolRepository.findById(id);
        return rol.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Rol> updateRol(Long id, Rol rol) {
        Optional<Rol> existingRol = rolRepository.findById(id);
        if (existingRol.isPresent()) {
            rol.setIdRol(id);
            Rol updatedRol = rolRepository.save(rol);
            return ResponseEntity.ok(updatedRol);
        }
        return ResponseEntity.notFound().build();
    }
}
