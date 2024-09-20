package com.kaijoo.demo.controller;

import com.kaijoo.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/posts") // This means URL's start with /posts (after Application path)
public class PostController {

    // Add post repository here
    @Autowired
    private PostRepository postRepository;

    // The routes will be added here
    // Create a post
    @PostMapping(path="")
    public String createPost() {
        return "Post created";
    }

    // Update a post
    @PutMapping(path="/by-id/{id}")
    public String updatePost() {
        return "Post updated";
    }

    // Delete a post
    @DeleteMapping(path="/by-id/{id}")
    public String deletePost() {
        return "Post deleted";
    }

    // Get all posts
    @GetMapping(path="")
    public String getAllPosts() {
        return "All posts";
    }

    // Get a post by id
    @GetMapping(path="/by-id/{id}")
    public String getPostById() {
        return "Post by id";
    }

}
