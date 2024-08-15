package com.kaijoo.demo.controller;

import com.kaijoo.demo.model.Tag;
import com.kaijoo.demo.repository.TagRepository;
import com.kaijoo.demo.service.JwtService;
import com.kaijoo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/tags")
public class TagController {

    // Initiate the tag repository
    @Autowired
    private TagRepository tagRepository;


    // Add a new tag, get the entity properties from the request body, received formatted as a json object,
    // receive a post request to /tags, no /add or anything else, just the base path
    @PostMapping(path="")
    public @ResponseBody String addNewTag (@RequestBody Tag tag) {
        try{
            tagRepository.save(tag);
            return "Saved";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update a tag, get the entity properties from the request body, received formatted as a json object
    // receive a put request to /tags/{id}
    @PutMapping(path="/by-id/{id}")
    public @ResponseBody String updateTag (@PathVariable int id, @RequestParam String name) {
        Tag tag = tagRepository.findById(id).get();
        tag.setName(name);
        tagRepository.save(tag);
        return "Updated";
    }

    // Delete a tag, receive a delete request to /tags/{id}
    @DeleteMapping(path="/by-id/{id}")
    public @ResponseBody String deleteTag (@PathVariable int id) {
        tagRepository.deleteById(id);
        return "Deleted";
    }

    // Get all tags, receive a get request to /tags
    @GetMapping(path="")
    public @ResponseBody Iterable<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    // Get a tag by id, receive a get request to /tags/{id}
    @GetMapping(path="/by-id/{id}")
    public @ResponseBody Tag getTagById(@PathVariable int id) {
        return tagRepository.findById(id).get();
    }

    // Get a tag by name, receive a get request to /tags/by-name/{name}
    @GetMapping(path="/by-name/{name}")
    public @ResponseBody Tag getTagByName(@PathVariable String name) {
        return tagRepository.findByName(name);
    }

}
