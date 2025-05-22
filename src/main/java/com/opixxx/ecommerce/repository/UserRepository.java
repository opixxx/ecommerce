package com.opixxx.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.opixxx.ecommerce.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}