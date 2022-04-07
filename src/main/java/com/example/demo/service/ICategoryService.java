package com.example.demo.service;

import com.example.demo.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ICategoryService {
    boolean existsByNameCategory(String nameCategory);
    Page<Category> findAll(Pageable pageable);
    Page<Category> findByNameCategoryQuery(@Param("nameCategory") String nameCategory, Pageable pageable);
    Page<Category> findAllByNameCategoryContaining(String nameCategory, Pageable pageable);
    List<Category> findAll();
    Optional<Category> findById(Long id);
    void deleteById(Long id);
    Category save(Category category);
}
