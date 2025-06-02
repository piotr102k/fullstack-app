package com.example.pasir_swiszcz_piotr.DTO;

import com.example.pasir_swiszcz_piotr.model.TransactionType;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class TransactionDTO {


    @NotNull(message="Kwota nie moze byc pusta")
    @Min(value=1,message = "Kwota musi byc wieksza niż 0")
    private Double amount;


    private TransactionType type;

    @Size(max=50,message="Tagi nie moga byc dluzsze od 50")
    private String tags;

    @Size(max=255,message="Notatka nie moze byc dluzsza od 255")
    private String notes;
}
