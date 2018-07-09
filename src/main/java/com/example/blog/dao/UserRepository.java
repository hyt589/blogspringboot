package com.example.blog.dao;

import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAddress(String emailAddress);

    User findByEmailAddress(String emailAddress);


}
