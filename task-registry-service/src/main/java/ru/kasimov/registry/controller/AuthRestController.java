package ru.kasimov.registry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kasimov.registry.domain.dto.JwtAuthResponse;
import ru.kasimov.registry.domain.dto.SignInRequest;
import ru.kasimov.registry.domain.dto.SignUpRequest;
import ru.kasimov.registry.service.AuthenticationService;

@RestController
@RequestMapping("v1/registry-api/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthRestController {

    private final AuthenticationService authService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("sign-up")
    public JwtAuthResponse signUp(@RequestBody @Validated SignUpRequest request) {
        return this.authService.signUp(request);
    }

    @Operation(summary = "Аутентификация пользователя")
    @PostMapping("sign-in")
    public JwtAuthResponse signIn(@RequestBody @Validated SignInRequest request) {
        return this.authService.signIn(request);
    }

}
