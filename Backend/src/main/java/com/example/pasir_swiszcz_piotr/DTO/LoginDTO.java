package com.example.pasir_swiszcz_piotr.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
public class LoginDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
