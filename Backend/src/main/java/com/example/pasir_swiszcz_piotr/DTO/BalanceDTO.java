package com.example.pasir_swiszcz_piotr.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class BalanceDTO {
    double totalIncome;
    double totalExpense;
    double balance;
}
