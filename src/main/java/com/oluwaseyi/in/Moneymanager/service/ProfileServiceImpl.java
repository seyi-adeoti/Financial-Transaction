package com.oluwaseyi.in.Moneymanager.service;

import com.oluwaseyi.in.Moneymanager.entity.Profile;
import com.oluwaseyi.in.Moneymanager.interfaces.ProfileService;
import com.oluwaseyi.in.Moneymanager.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile create(Profile profile) {
        logger.info("Creating profile for name: {}", profile.getName());
        return profileRepository.save(profile);
    }

    @Override
    public List<Profile> findAll() {
        logger.info("Retrieving all profiles");
        return profileRepository.findAll();
    }

    @Override
    public Optional<Profile> findById(Long id) {
        logger.info("Retrieving profile with id: {}", id);
        return profileRepository.findById(id);
    }
}
