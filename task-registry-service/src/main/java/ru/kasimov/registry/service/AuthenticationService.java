package ru.kasimov.registry.service;

import ru.kasimov.registry.domain.dto.JwtAuthResponse;
import ru.kasimov.registry.domain.dto.SignInRequest;
import ru.kasimov.registry.domain.dto.SignUpRequest;

public interface AuthenticationService {

    JwtAuthResponse signUp(SignUpRequest request);

    JwtAuthResponse signIn(SignInRequest request);
}
