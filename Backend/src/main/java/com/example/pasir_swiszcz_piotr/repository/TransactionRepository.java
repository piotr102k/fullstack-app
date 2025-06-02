package com.example.pasir_swiszcz_piotr.repository;

import com.example.pasir_swiszcz_piotr.model.Transaction;
import com.example.pasir_swiszcz_piotr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);
}
