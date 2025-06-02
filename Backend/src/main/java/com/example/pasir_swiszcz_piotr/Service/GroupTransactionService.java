package com.example.pasir_swiszcz_piotr.Service;

import com.example.pasir_swiszcz_piotr.DTO.GroupTransactionDTO;
import com.example.pasir_swiszcz_piotr.model.Debt;
import com.example.pasir_swiszcz_piotr.model.Group;
import com.example.pasir_swiszcz_piotr.model.Membership;
import com.example.pasir_swiszcz_piotr.model.User;
import com.example.pasir_swiszcz_piotr.repository.DebtRepository;
import com.example.pasir_swiszcz_piotr.repository.GroupRepository;
import com.example.pasir_swiszcz_piotr.repository.MembershipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Service
public class GroupTransactionService {
    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final DebtRepository debtRepository;

    private GroupTransactionService(GroupRepository groupRepository, MembershipRepository membershipRepository, DebtRepository debtRepository) {
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.debtRepository = debtRepository;
    }

    public void addGroupTransaction(GroupTransactionDTO dto, User currentUser){
        Group group = groupRepository.findById(dto.getGroupId()).orElseThrow(()->new EntityNotFoundException("nie znaleziono grupy"));

        List<Membership> members = membershipRepository.findByGroupId(group.getId());
        List<Long> selectedUserIds = dto.getSelectedUserIds();

        if(selectedUserIds==null || selectedUserIds.isEmpty()){
            throw new IllegalArgumentException("Nie wybrano uzyt");
        }

        if(members.isEmpty()){
            throw new IllegalArgumentException("brak czlonkow");
        }
        double amountPerUser = dto.getAmount()/members.size();

        for(Membership member : members){
            User debtor = member.getUser();
            if(debtor.getId()!=(currentUser.getId()) && selectedUserIds.contains(debtor.getId())){
                Debt debt = new Debt();
                debt.setDebtor(debtor);
                debt.setAmount(amountPerUser);
                debt.setCreditor(currentUser);
                debt.setGroup(group);
                debt.setTitle(dto.getTitle());
                debt.setMarkedAsPaid(false);
                debt.setConfirmedByCredit(false);
                debtRepository.save(debt);
            }
        }
    }
}
