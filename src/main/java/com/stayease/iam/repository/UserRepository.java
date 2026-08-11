package com.stayease.iam.repository;

import com.stayease.iam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The data-access layer for User.
 *
 * By extending JpaRepository<User, Long> (entity type = User, id type = Long),
 * Spring Data JPA AUTO-GENERATES the implementation at startup. You instantly
 * get: save(), findById(), findAll(), deleteById(), count(), existsById()...
 * — no SQL to write.
 *
 * The two methods below are "derived queries": Spring reads the METHOD NAME and
 * writes the SQL for you. `existsByEmail` -> SELECT ... WHERE email = ?
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}