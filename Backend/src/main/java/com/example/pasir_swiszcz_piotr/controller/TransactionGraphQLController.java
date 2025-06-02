package com.example.pasir_swiszcz_piotr.controller;

import com.example.pasir_swiszcz_piotr.DTO.BalanceDTO;
import com.example.pasir_swiszcz_piotr.DTO.TransactionDTO;
import com.example.pasir_swiszcz_piotr.Service.TransactionService;
import com.example.pasir_swiszcz_piotr.model.Transaction;
import com.example.pasir_swiszcz_piotr.model.User;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TransactionGraphQLController {
    private final TransactionService transactionService;

    public TransactionGraphQLController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.getAllTransactions();
    }

    @MutationMapping
    public Transaction addTransaction(@Valid @Argument TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    @MutationMapping
    public Transaction updateTransaction(@Argument Long id,@Valid @Argument TransactionDTO transactionDTO) {
        return transactionService.updateTransaction(id, transactionDTO);
    }

    @QueryMapping
    public BalanceDTO userBalance(){
        User user = transactionService.getCurrentUser();
        return transactionService.getUserBalance(user);
    }

}
