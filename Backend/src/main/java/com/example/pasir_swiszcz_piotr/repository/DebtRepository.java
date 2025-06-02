package com.example.pasir_swiszcz_piotr.repository;

import com.example.pasir_swiszcz_piotr.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtRepository  extends JpaRepository<Debt, Long> {
    List<Debt> findByGroupId(Long groupid);
}
