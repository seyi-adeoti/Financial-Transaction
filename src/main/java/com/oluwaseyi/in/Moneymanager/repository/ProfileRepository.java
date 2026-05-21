package com.oluwaseyi.in.Moneymanager.repository;

import com.oluwaseyi.in.Moneymanager.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
