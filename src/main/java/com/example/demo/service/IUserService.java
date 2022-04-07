package com.example.demo.service;

import com.example.demo.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByUserName(String name);// tìm kiếm user có tồn tại trong database hay ko
    Boolean existsByUserName(String username); // kiểm tra username đã tồn tại trong database hay chưa trước khi tạo mới
    Boolean existsByEmail(String email); // kiểm tra email đã tồn tại trong database hay chưa trước khi tạo mới
    User save(User user);
}
