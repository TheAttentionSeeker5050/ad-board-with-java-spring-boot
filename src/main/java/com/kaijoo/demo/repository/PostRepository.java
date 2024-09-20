package com.kaijoo.demo.repository;

import com.kaijoo.demo.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer> {
    // Add custom methods here
}
