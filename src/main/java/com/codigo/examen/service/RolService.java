package com.codigo.examen.service;

import com.codigo.examen.entity.Rol;
import org.springframework.http.ResponseEntity;

public interface RolService {
    ResponseEntity<Rol> createRol(Rol rol);
    ResponseEntity<Rol> getRolById(Long id);
    ResponseEntity<Rol> updateRol(Long id, Rol rol);
}
