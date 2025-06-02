package com.example.pasir_swiszcz_piotr.repository;


import com.example.pasir_swiszcz_piotr.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository  extends JpaRepository<Membership, Long> {
    List<Membership> findByGroupId(Long groupid);
}

