package ru.kasimov.registry.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kasimov.registry.domain.model.Role;

@Data
@AllArgsConstructor
@Schema(description = "Запрос на аутентификацию")
public class SignInRequest {
    @Schema(description = "Адрес электронной почты пользователя", example = "user1@gmail.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Пароль пользователя", example = "my_password123")
    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

}
