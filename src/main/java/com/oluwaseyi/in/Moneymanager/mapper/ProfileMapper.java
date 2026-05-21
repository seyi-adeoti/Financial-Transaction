package com.oluwaseyi.in.Moneymanager.mapper;

import com.oluwaseyi.in.Moneymanager.dto.ProfileRequest;
import com.oluwaseyi.in.Moneymanager.dto.ProfileResponse;
import com.oluwaseyi.in.Moneymanager.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public Profile toEntity(ProfileRequest request) {
        return new Profile(request.getName(), request.getEmail());
    }

    public ProfileResponse toResponse(Profile entity) {
        return new ProfileResponse(entity.getId(), entity.getName(), entity.getEmail());
    }
}
