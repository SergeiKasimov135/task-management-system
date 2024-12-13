package ru.kasimov.registry.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kasimov.registry.domain.dto.JwtAuthResponse;
import ru.kasimov.registry.domain.dto.SignInRequest;
import ru.kasimov.registry.domain.dto.SignUpRequest;
import ru.kasimov.registry.domain.model.Role;
import ru.kasimov.registry.domain.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DefaultAuthenticationServiceTest {

    @InjectMocks
    private DefaultAuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_ShouldReturnJwtAuthResponse() {
        SignUpRequest request = new SignUpRequest("test@example.com",
                "testuser", "password", Role.ROLE_USER);
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword("encodedPassword");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userService.saveUser(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtAuthResponse response = authenticationService.signUp(request);

        assertEquals("jwtToken", response.getToken());
        verify(userService).saveUser(any(User.class));
    }

    @Test
    void signIn_ShouldReturnJwtAuthResponse() {
        SignInRequest request = new SignInRequest("test@example.com", "password");
        User user = new User();
        user.setUsername("testuser");

        when(userService.getByEmail(request.getEmail())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        JwtAuthResponse response = authenticationService.signIn(request);

        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager).authenticate(any());
    }
}
