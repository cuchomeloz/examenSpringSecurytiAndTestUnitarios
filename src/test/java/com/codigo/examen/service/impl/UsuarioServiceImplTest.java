package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceImplTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createUsuarioSuccess(){
        Usuario usuario = new Usuario();
        usuario.setUsername("testUser");

        when(usuarioRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> response = usuarioService.createUsuario(usuario);

        assertEquals(ResponseEntity.ok(usuario), response);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void createUsuario_ExistingUser() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testUser");

        when(usuarioRepository.findByUsername("testUser")).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = usuarioService.createUsuario(usuario);

        assertEquals(ResponseEntity.badRequest().body(null), response);
        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    void usuarioByIdSuccess() {
        Long idUsuarioExistente = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setIdUsuario(idUsuarioExistente);

        when(usuarioRepository.findById(idUsuarioExistente)).thenReturn(Optional.of(usuarioExistente));

        ResponseEntity<Usuario> response = usuarioService.getUsuarioById(idUsuarioExistente);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioExistente, response.getBody());
    }

    @Test
    void usuarioByIdNotFound() {
        Long idUsuarioNoExistente = 999L;
        when(usuarioRepository.findById(idUsuarioNoExistente)).thenReturn(Optional.empty());
        ResponseEntity<Usuario> response = usuarioService.getUsuarioById(idUsuarioNoExistente);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void updateUsuarioSuccess() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("testUser");

        Usuario existingUsuario = new Usuario();
        existingUsuario.setIdUsuario(1L);
        existingUsuario.setUsername("existingUser");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existingUsuario));
        when(usuarioRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> response = usuarioService.updateUsuario(1L, usuario);

        assertEquals(ResponseEntity.ok(usuario), response);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void updateUsuario_NotFound() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("testUser");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioService.updateUsuario(1L, usuario);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
        public void deleteUsuarioSucces() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("testUser");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = usuarioService.deleteUsuario(1L);

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    public void deleteUsuario_NotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioService.deleteUsuario(1L);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(usuarioRepository, never()).delete(any());
    }



}