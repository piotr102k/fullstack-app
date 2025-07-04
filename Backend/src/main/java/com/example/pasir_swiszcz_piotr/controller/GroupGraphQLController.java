package com.example.pasir_swiszcz_piotr.controller;

import com.example.pasir_swiszcz_piotr.DTO.GroupDTO;
import com.example.pasir_swiszcz_piotr.Service.GroupService;
import com.example.pasir_swiszcz_piotr.model.Group;
import com.example.pasir_swiszcz_piotr.repository.GroupRepository;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GroupGraphQLController {
    private final GroupService groupService;

    public GroupGraphQLController(GroupService groupService) {
        this.groupService = groupService;
    }

    @QueryMapping
    public List<Group> groups(){
        return groupService.getAllGroups();
    }

    @MutationMapping
    public Group createGroup(@Valid @Argument GroupDTO groupDTO){
        return groupService.createGroup(groupDTO);
    }

    @MutationMapping
    public Boolean deleteGroup(@Argument Long id){
        groupService.deleteGroup(id);
        return true;
    }
}
