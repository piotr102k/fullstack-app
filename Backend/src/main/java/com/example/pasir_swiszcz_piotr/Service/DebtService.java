package com.example.pasir_swiszcz_piotr.Service;

import com.example.pasir_swiszcz_piotr.DTO.DebtDTO;
import com.example.pasir_swiszcz_piotr.model.*;
import com.example.pasir_swiszcz_piotr.repository.DebtRepository;
import com.example.pasir_swiszcz_piotr.repository.GroupRepository;
import com.example.pasir_swiszcz_piotr.repository.TransactionRepository;
import com.example.pasir_swiszcz_piotr.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class DebtService {
    private final DebtRepository debtRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DebtService(DebtRepository debtRepository, GroupRepository groupRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.debtRepository = debtRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Debt> getGroupDebts(Long groupId) {
        return debtRepository.findByGroupId(groupId);
    }

    public Debt createDebt(DebtDTO debtDTO) {
        Group group = groupRepository.findById(debtDTO.getGroupId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy: "+debtDTO.getGroupId()));
        User debtor = userRepository.findById(debtDTO.getDebtorId()).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono dluznika: "+debtDTO.getDebtorId()));
        User creditor = userRepository.findById(debtDTO.getCreditorId()).orElseThrow(()->new EntityNotFoundException("Nie znaleziono wierzyciele:" + debtDTO.getCreditorId()));
        Debt debt = new Debt();
        debt.setGroup(group);
        debt.setCreditor(creditor);
        debt.setDebtor(debtor);
        debt.setAmount(debtDTO.getAmount());
        debt.setTitle(debtDTO.getTitle());
        debt.setMarkedAsPaid(false);
        debt.setConfirmedByCredit(false);

        return debtRepository.save(debt);
    }
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("NIE znaleziono uzytkoniwka: "+email));
    }

    public void deleteDebt(@Argument Long debtId) {
        Debt debt = debtRepository.findById(debtId).orElseThrow(()->new EntityNotFoundException("Nie znaleziono długu: "+debtId));

        if(!(debt.getCreditor().getId()==getCurrentUser().getId())){
            throw new SecurityException("Tylko wierny moze usunac ten dlug");
        }

        debtRepository.deleteById(debtId);
    }

    public boolean markAsPaid(Long debtId,User user) {
        Debt debt = debtRepository.findById(debtId).orElseThrow(()->new EntityNotFoundException("Nie znaleziono dlugu"));
        if(!(debt.getDebtor().getId()==getCurrentUser().getId())){
            throw new SecurityException("Nie jestes dluznikiem");
        }
        debt.setMarkedAsPaid(true);
        debtRepository.save(debt);
        return true;

    }

    public boolean confirmPayment(Long debtId,User user) {
        Debt debt = debtRepository.findById(debtId).orElseThrow(()->new EntityNotFoundException("Nie znaleziono dlugu"));
        if(!(debt.getCreditor().getId()==getCurrentUser().getId())){
            throw new SecurityException("Nie jestes dluznikiem");
        }

        if(!debt.isMarkedAsPaid()){
            throw new IllegalArgumentException("dluznik nie oplacil jeszcze");
        }

        //debt.setConfirmedByCredit(true);
        debtRepository.deleteById(debtId);
        Transaction incomeTx=new Transaction(
                debt.getAmount(),
                TransactionType.INCOME,
                "Splata dlugo",
                "Splata dlugo od" + debt.getDebtor().getEmail(),
                debt.getCreditor(),
                LocalDateTime.now()
        );
        transactionRepository.save(incomeTx);

        Transaction expenseTx=new Transaction(
                debt.getAmount(),
                TransactionType.EXPENSE,
                "Splata dlugo",
                "Splacono dlugg dla"+ debt.getCreditor().getEmail(),
                debt.getDebtor(),
                LocalDateTime.now()
        );
        transactionRepository.save(expenseTx);
        return true;

    }
}
