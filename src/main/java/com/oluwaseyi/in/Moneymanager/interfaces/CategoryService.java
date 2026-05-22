package com.oluwaseyi.in.Moneymanager.interfaces;

import com.oluwaseyi.in.Moneymanager.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category create(Category category);
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
}
