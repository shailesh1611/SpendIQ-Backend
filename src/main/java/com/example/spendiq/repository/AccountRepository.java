package com.example.spendiq.repository;

import com.example.spendiq.entity.Account;
import com.example.spendiq.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.isDefault = false WHERE a.isDefault = true AND a.user.userId = :userId")
    void checkAndUnsetDefaultAccount(@Param("userId") UUID userId);

    Optional<List<Account>> findAllByUserUserId(UUID userId);
    boolean existsByAccountNameAndUser(String accountName, User user);
}
