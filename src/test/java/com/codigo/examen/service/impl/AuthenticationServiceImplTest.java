package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.request.SignInRequest;
import com.codigo.examen.request.SignUpRequest;
import com.codigo.examen.response.AuthenticationResponse;
import com.codigo.examen.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceImplTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void signUpUserSuccess() {
        // Configurar datos de prueba
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("test_user");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setTelefono("123456789");
        signUpRequest.setPassword("password");

        Usuario usuario = new Usuario();
        usuario.setUsername(signUpRequest.getUsername());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setTelefono(signUpRequest.getTelefono());
        Rol rolUsuario = new Rol();
        rolUsuario.setNombreRol("ROLE_USER");
        usuario.getRoles().add(rolUsuario);

        when(rolRepository.findByNombreRol("ROLE_USER")).thenReturn(Optional.of(rolUsuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario createdUsuario = authenticationService.signUpUser(signUpRequest);

        assertNotNull(createdUsuario);
        assertEquals(signUpRequest.getUsername(), createdUsuario.getUsername());
        assertEquals(signUpRequest.getEmail(), createdUsuario.getEmail());
        assertEquals(signUpRequest.getTelefono(), createdUsuario.getTelefono());
        assertTrue(createdUsuario.getRoles().contains(rolUsuario));
    }

    @Test
    public void testSignUpUser_RoleNotFound() {
        when(rolRepository.findByNombreRol("ROLE_USER")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setUsername("username");
            signUpRequest.setEmail("email@example.com");
            signUpRequest.setTelefono("123456789");
            signUpRequest.setPassword("password");
            authenticationService.signUpUser(signUpRequest);
        });
    }

    @Test
    void testSignUpAdmin() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("test_user");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setTelefono("123456789");
        signUpRequest.setPassword("password");

        Usuario usuario = new Usuario();
        usuario.setUsername(signUpRequest.getUsername());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setTelefono(signUpRequest.getTelefono());
        Rol rolUsuario = new Rol();
        rolUsuario.setNombreRol("ROLE_ADMIN");
        usuario.getRoles().add(rolUsuario);

        when(rolRepository.findByNombreRol("ROLE_ADMIN")).thenReturn(Optional.of(rolUsuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario createdUsuario = authenticationService.signUpAdmin(signUpRequest);

        assertNotNull(createdUsuario);
        assertEquals(signUpRequest.getUsername(), createdUsuario.getUsername());
        assertEquals(signUpRequest.getEmail(), createdUsuario.getEmail());
        assertEquals(signUpRequest.getTelefono(), createdUsuario.getTelefono());
        assertTrue(createdUsuario.getRoles().contains(rolUsuario));
    }

    @Test
    public void testSignUpAdmin_RoleNotFound() {
        when(rolRepository.findByNombreRol("ROLE_ADMIN")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setUsername("username");
            signUpRequest.setEmail("email@example.com");
            signUpRequest.setTelefono("123456789");
            signUpRequest.setPassword("password");
            authenticationService.signUpAdmin(signUpRequest);
        });
    }

    @Test
    void testSignin() {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("username");
        signInRequest.setPassword("password");
        Usuario usuario = new Usuario();
        usuario.setUsername(signInRequest.getUsername());
        when(usuarioRepository.findByUsername(signInRequest.getUsername())).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("generated_token");
        AuthenticationResponse response = authenticationService.signin(signInRequest);
        assertNotNull(response);
        assertEquals("generated_token", response.getToken());
    }

}