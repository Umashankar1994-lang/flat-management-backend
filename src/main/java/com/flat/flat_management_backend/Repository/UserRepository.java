package com.flat.flat_management_backend.repository;

import com.flat.flat_management_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFlatNo(String flatNo);
}
