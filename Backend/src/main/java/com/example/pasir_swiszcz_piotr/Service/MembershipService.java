package com.example.pasir_swiszcz_piotr.Service;

import com.example.pasir_swiszcz_piotr.DTO.MembershipDTO;
import com.example.pasir_swiszcz_piotr.model.Group;
import com.example.pasir_swiszcz_piotr.model.Membership;
import com.example.pasir_swiszcz_piotr.model.User;
import com.example.pasir_swiszcz_piotr.repository.GroupRepository;
import com.example.pasir_swiszcz_piotr.repository.MembershipRepository;
import com.example.pasir_swiszcz_piotr.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipService {
    private MembershipRepository membershipRepository;
    private GroupRepository groupRepository;
    private UserRepository userRepository;

    public MembershipService(MembershipRepository membershipRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public List<Membership> getMemberships(Long groupId) {
        return membershipRepository.findByGroupId(groupId);
    }

    public Membership addMember(MembershipDTO membershipDTO) {
        User user = userRepository.findByEmail(membershipDTO.getUserEmail()).orElseThrow(()->new EntityNotFoundException("NIe znalezion uzytkownika o mailu"+membershipDTO.getUserEmail()));
        Group group = groupRepository.findById(membershipDTO.getGroupId()).orElseThrow(()-> new EntityNotFoundException("Nie znaleziono grupy: "+membershipDTO.getGroupId()));

        boolean alreadyMember = membershipRepository.findByGroupId(group.getId()).stream().anyMatch(m -> m.getUser().getId()==(user.getId()));

        if(alreadyMember){
            throw new IllegalArgumentException("Uzytkownik jest czlonkiem grupy");
        }
            Membership membership = new Membership();
            membership.setUser(user);
            membership.setGroup(group);
            return membershipRepository.save(membership);

    }

    public void removeMember(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId).orElseThrow(()->new EntityNotFoundException("Czlonkowstwo nie istnieje"));
        User currentUser= getCurrentUser();
        User groupOwner = membership.getGroup().getOwner();

        if(!(currentUser.getId()==(groupOwner.getId()))){
            throw new SecurityException("Tylko wlasciciel moze usuwac czlonkow");
        }

    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("NIE znaleziono uzytkoniwka: "+email));
    }
}
