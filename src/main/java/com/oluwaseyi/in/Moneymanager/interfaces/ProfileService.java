package com.oluwaseyi.in.Moneymanager.interfaces;

import com.oluwaseyi.in.Moneymanager.entity.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileService {

    Profile create(Profile profile);

    List<Profile> findAll();

    Optional<Profile> findById(Long id);
}
