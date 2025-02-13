package com.example.spendiq.repository;

import com.example.spendiq.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUserName(String userName);
}
