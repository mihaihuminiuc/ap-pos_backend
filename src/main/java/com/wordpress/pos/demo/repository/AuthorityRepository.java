package com.wordpress.pos.demo.repository;

import com.wordpress.pos.demo.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    public List<Authority> findAll();
}
