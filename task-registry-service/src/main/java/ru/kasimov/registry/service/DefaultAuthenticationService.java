package ru.kasimov.registry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kasimov.registry.domain.dto.JwtAuthResponse;
import ru.kasimov.registry.domain.dto.SignInRequest;
import ru.kasimov.registry.domain.dto.SignUpRequest;
import ru.kasimov.registry.domain.model.User;

@Service
@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        this.userService.saveUser(user);

        var jwt = this.jwtService.generateToken(user);
        return new JwtAuthResponse(jwt);
    }

    @Override
    public JwtAuthResponse signIn(SignInRequest request) {
        var user = this.userService.getByEmail(request.getEmail());

        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                request.getPassword()
        ));


        var jwt = this.jwtService.generateToken(user);
        return new JwtAuthResponse(jwt);
    }

}
