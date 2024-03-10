package com.codigo.examen.controller;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ms-examen/v1/administrador")
@RequiredArgsConstructor
public class AdminController {
    private final RolService rolService;

    @PostMapping("/roles")
    public ResponseEntity<Rol> createRol(@RequestBody Rol rol) {
        return rolService.createRol(rol);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Rol> getRolById(@PathVariable Long id) {
        return rolService.getRolById(id);
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<Rol> updateRol(@PathVariable Long id, @RequestBody Rol rol) {
        return rolService.updateRol(id, rol);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> deleteRol(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
