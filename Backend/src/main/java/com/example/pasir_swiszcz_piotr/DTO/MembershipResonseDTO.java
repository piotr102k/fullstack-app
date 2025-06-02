package com.example.pasir_swiszcz_piotr.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipResonseDTO {
    private Long id;
    private Long userId;
    private Long groupId;
    private String userEmail;
}
