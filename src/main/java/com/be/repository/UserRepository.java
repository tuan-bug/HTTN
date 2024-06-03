package com.be.repository;

import com.be.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    List<Users> findAllByStatus(boolean status);

    Users findByUsername(String username);

    boolean existsByUsername(String username);

    Users getUsersById(String id);


    long countByStatus(boolean b);
}
