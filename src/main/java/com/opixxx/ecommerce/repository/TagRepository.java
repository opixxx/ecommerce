package com.opixxx.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opixxx.ecommerce.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}