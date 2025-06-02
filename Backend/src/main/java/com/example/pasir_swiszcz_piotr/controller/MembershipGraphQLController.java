package com.example.pasir_swiszcz_piotr.controller;

import com.example.pasir_swiszcz_piotr.DTO.GroupResponseDTO;
import com.example.pasir_swiszcz_piotr.DTO.MembershipDTO;
import com.example.pasir_swiszcz_piotr.DTO.MembershipResonseDTO;
import com.example.pasir_swiszcz_piotr.Service.GroupTransactionService;
import com.example.pasir_swiszcz_piotr.Service.MembershipService;
import com.example.pasir_swiszcz_piotr.model.Group;
import com.example.pasir_swiszcz_piotr.model.Membership;
import com.example.pasir_swiszcz_piotr.model.User;
import com.example.pasir_swiszcz_piotr.repository.GroupRepository;
import com.example.pasir_swiszcz_piotr.repository.MembershipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MembershipGraphQLController {
    private final MembershipService membershipService;
    private final MembershipRepository membershipRepository;
    private final GroupRepository groupRepository;

    public MembershipGraphQLController(MembershipService membershipService, MembershipRepository membershipRepository, GroupRepository groupRepository) {
        this.membershipService = membershipService;
        this.membershipRepository = membershipRepository;
        this.groupRepository = groupRepository;
    }

    @QueryMapping
    public List<MembershipResonseDTO> groupMembers(@Argument Long groupId){
        Group group = groupRepository.findById(groupId).orElseThrow(()->new EntityNotFoundException("Brak grupy o ID "+groupId));
        return membershipService.getMemberships(groupId).stream().map(membership -> new MembershipResonseDTO(
                membership.getId(),
                membership.getUser().getId(),
                membership.getGroup().getId(),
                membership.getUser().getEmail()
        )).toList();
    }

    @MutationMapping
    public MembershipResonseDTO addMember(@Argument MembershipDTO membershipDTO){
        Membership membership = membershipService.addMember(membershipDTO);
        return new MembershipResonseDTO(
                membership.getId(),
                membership.getUser().getId(),
                membership.getGroup().getId(),
                membership.getUser().getEmail()
        );
    }

    @QueryMapping
    public List<GroupResponseDTO> myGroups(){
        User user = membershipService.getCurrentUser();
        return groupRepository.findByMemberships_User(user).stream().map(group->new GroupResponseDTO(
                group.getId(),
                group.getName(),
                group.getOwner().getId()
        )).toList();
    }
}
