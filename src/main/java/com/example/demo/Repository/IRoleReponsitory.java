package com.example.demo.Repository;

import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleReponsitory extends JpaRepository<Role,Long> {

    Optional<Role> findByName(RoleName name);
}
