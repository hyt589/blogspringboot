package com.example.blog.dao;


import com.example.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsById(long id);

    Post findById(long id);

}
