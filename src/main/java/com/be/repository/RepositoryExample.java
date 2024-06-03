package com.be.repository;

import com.be.entity.Examples;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryExample extends JpaRepository<Examples, Integer> {
}
