package com.example.pasir_swiszcz_piotr.Service;

import com.example.pasir_swiszcz_piotr.DTO.BalanceDTO;
import com.example.pasir_swiszcz_piotr.DTO.TransactionDTO;
import com.example.pasir_swiszcz_piotr.model.Transaction;
import com.example.pasir_swiszcz_piotr.model.TransactionType;
import com.example.pasir_swiszcz_piotr.model.User;
import com.example.pasir_swiszcz_piotr.repository.TransactionRepository;
import com.example.pasir_swiszcz_piotr.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getAllTransactions() {

        User user= getCurrentUser();
        return transactionRepository.findAllByUser(user);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow((()->new EntityNotFoundException("Nie znaleziono " + id)));
    }

    public Transaction updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow((()->new EntityNotFoundException("Nie znaleziono " + id)));

        if(!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new SecurityException("Brak dostępu do edycji");
        }

        transaction.setAmount(transactionDTO.getAmount());
        transaction.setNotes(transactionDTO.getNotes());
        transaction.setTags(transactionDTO.getTags());
        transaction.setType(transactionDTO.getType());
        return transactionRepository.save(transaction);
    }

    public Transaction createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction(transactionDTO.getAmount(),transactionDTO.getType(),transactionDTO.getTags(),transactionDTO.getNotes(),getCurrentUser(), LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("nie znaleziono zalogowanego uzytkownika"));
    }

    public BalanceDTO getUserBalance(User user){
        List<Transaction> userTransactions = transactionRepository.findAllByUser((user));

        double income = userTransactions.stream().filter(t->t.getType()==TransactionType.INCOME).mapToDouble(Transaction::getAmount).sum();
        double expense = userTransactions.stream().filter(t->t.getType()==TransactionType.EXPENSE).mapToDouble(Transaction::getAmount).sum();
        return new BalanceDTO(income,expense,income-expense);
    }
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
