package com.example.pasir_swiszcz_piotr.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipDTO {
@NotNull(message="Email uzytkownika nie moze byc puste")
    private String userEmail;

@NotNull(message = "ID grupy nie moze byc puste")
    private Long groupId;
}
