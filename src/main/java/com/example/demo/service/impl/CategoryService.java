package com.example.demo.service.impl;

import com.example.demo.Repository.ICategoryRepository;
import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.security.userPrinciple.UserDetailService;
import com.example.demo.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    ICategoryRepository categoryRepository;
    @Autowired
    UserDetailService userDetailService;

    @Override
    public boolean existsByNameCategory(String nameCategory) {
        return categoryRepository.existsByNameCategory(nameCategory);
    }
    @Override
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    //Cach 2: Tu trien khai @Query
    @Override
    public Page<Category> findByNameCategoryQuery(String nameCategory, Pageable pageable) {
        return categoryRepository.findByNameCategoryQuery(nameCategory,pageable);
    }
    //Cach 1 : Dung ham co san cua tang Repository
    @Override
    public Page<Category> findAllByNameCategoryContaining(String nameCategory, Pageable pageable) {
        return categoryRepository.findAllByNameCategoryContaining(nameCategory, pageable);
    }

    @Override
    public Optional<Category> findById(Long id) {
        User user= userDetailService.getCurrentUser();

        return categoryRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category save(Category category) {
        User user= userDetailService.getCurrentUser();
        category.setUser(user);
        return categoryRepository.save(category);
    }
}
