package com.example.demo.Repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface  IUserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String name);// tìm kiếm user có tồn tại trong database hay ko
    Boolean existsByUsername(String username); // kiểm tra username đã tồn tại trong database hay chưa trước khi tạo mới
    Boolean existsByEmail(String email); // kiểm tra email đã tồn tại trong database hay chưa trước khi tạo mới
}
