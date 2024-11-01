package com.kaijoo.demo.controller;

import com.kaijoo.demo.dto.GetMultipleItemsResponse;
import com.kaijoo.demo.dto.GetSingleItemsResponse;
import com.kaijoo.demo.dto.ItemCreatedOrUpdatedResponse;
import com.kaijoo.demo.dto.ItemDeletedResponse;
import com.kaijoo.demo.model.Post;
import com.kaijoo.demo.model.Tag;
import com.kaijoo.demo.repository.PaginatedPostRepository;
import com.kaijoo.demo.repository.PostRepository;
import com.kaijoo.demo.repository.TagRepository;
import com.kaijoo.demo.service.JwtService;
import com.kaijoo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller // This means that this class is a Controller
@RequestMapping(path="/tags")
public class TagController {

    // Initiate the tag repository
    @Autowired
    private TagRepository tagRepository;

    // initiate the post repository
    @Autowired
    private PaginatedPostRepository postRepository;


    // Add a new tag, get the entity properties from the request body, received formatted as a json object,
    // receive a post request to /tags, no /add or anything else, just the base path
    @PostMapping(path="")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> addNewTag (@RequestBody Tag tag) {
        try{

            // First check if a tag already exists with the same name
            Tag existingTag = tagRepository.findByName(tag.getName());

            if (existingTag != null) {
                // return a response with the error message and status code 400 if the tag already exists
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "A tag with the same name already exists",
                        "/tags",
                        null);

                return ResponseEntity.badRequest().body(response);
            }


            // Save the tag to the database
            tagRepository.save(tag);

            // Make the response content object
            ItemCreatedOrUpdatedResponse response =
                    new ItemCreatedOrUpdatedResponse(
                            "Item created successfully",
                            null,
                            "/tags",
                            "/tags/by-id/" + tag.getId()
                    );

            // return the response using ResponseEntity
            return ResponseEntity.created(
                    new URI("/tags/by-id/" + tag.getId())
            ).body(response);


        } catch (Exception e) {
            // return a response with the error message and status code 400 if there is an error
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    null,
                    "Error creating tag: " + e.getMessage(),
                    "/tags",
                    null
            );

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Autocomplete a tag, receive a get request to /tags/autocomplete-list
    @GetMapping(path="/autocomplete-list")
    public @ResponseBody ResponseEntity<GetMultipleItemsResponse> autocompleteTag() {
        try {
            // Get all tags from the database, if it could not find any, return an empty list
            Optional<List<Tag>> tags = Optional.of((List<Tag>) tagRepository.findAll());

            // ditch the posts in the tags
            tags.ifPresent(tagList -> {
                for (Tag tag : tagList) {
                    tag.setPosts(null);
                }
            });

            // Make the response content object
            GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                    null,
                    "/tags",
                    tags.orElse(null)
            );

            // return the response using ResponseEntity
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // return a response with the error message and status code 400 if there is an error
            GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                    "Error getting tags: " + e.getMessage(),
                    "/tags",
                    null
            );

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Update a tag, get the entity properties from the request body, received formatted as a json object
    // receive a put request to /tags/{id}
    @PutMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> updateTag (@PathVariable int id, @RequestBody Tag tag) {
        try{

            // First get the tag by id to see if it exists
            Tag tagToUpdate = tagRepository.findById(id).isEmpty() ? null : tagRepository.findById(id).get();

            if (tagToUpdate == null) {
                // return a response with the error message and status code 404 if the tag is not found
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "Tag with ID " + id + " not found",
                        "/tags",
                        null);

                return ResponseEntity.status(404).body(response);
            }

            // Check if a tag with the same name already exists
            Tag existingTag = tagRepository.findByName(tag.getName());

            if (existingTag != null && existingTag.getId() != id) {
                // return a response with the error message and status code 400 if the tag already exists
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "A tag with the same name already exists",
                        "/tags",
                        null
                );

                return ResponseEntity.badRequest().body(response);
            }

            // Set the id of the tag to the id in the path
            tag.setId(id);

            // Save the tag to the database
            tagRepository.save(tag);

            // Make the response content object
            ItemCreatedOrUpdatedResponse response =
                    new ItemCreatedOrUpdatedResponse(
                            "Item updated successfully",
                            null,
                            "/tags",
                            "/tags/by-id/" + tag.getId()
                    );

            // return the response using ResponseEntity
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // return a response with the error message and status code 400 if there is an error
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    null,
                    "Error updating tag: " + e.getMessage(),
                    "/tags",
                    null
            );

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Delete a tag, receive a delete request to /tags/{id}
    // Use deleted item response dto
    @DeleteMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<ItemDeletedResponse> deleteTag (@PathVariable int id) {
        try{
            // Get the tag by id
            Tag tag = tagRepository.findById(id).isEmpty() ? null : tagRepository.findById(id).get();

            // if the tag is not found, return a response with the error message and status code 404
            if (tag == null) {
                ItemDeletedResponse response = new ItemDeletedResponse(
                        "Tag with ID " + id + " not found",
                        null,
                        "/tags");

                return ResponseEntity.status(404).body(response);
            }

            // save the tag name and id into a string
            String tagIdentifier = "tag " + tag.getName() + " with ID " + tag.getId();

            // Delete the tag from the database
            tagRepository.deleteById(id);

            // Make the response content object
            ItemDeletedResponse response = new ItemDeletedResponse(
                    "Item deleted successfully",
                    null,
                    "/tags");

            // return the response using ResponseEntity
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Make the response object
            ItemDeletedResponse response = new ItemDeletedResponse(
                    null,
                    "Error deleting tag: " + e.getMessage(),
                    "/tags");

            // return a response with the error message and status code 400 if there is an error
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get all tags, receive a get request to /tags
    @GetMapping(path="")
    public @ResponseBody ResponseEntity<GetMultipleItemsResponse> getAllTags() {
        try {
            // Get all tags from the database, if it could not find any, return an empty list

            // cast to list of tags, because
            // the dto does not specify the type of the data field
            List<Tag> tags = tagRepository.findAll().iterator().hasNext() ?
                    (List<Tag>) tagRepository.findAll() : null;


            if (tags != null) {
                // For each tag, manually load and set the posts
                for (Tag tag : tags) {
                    // Fetch only the limited posts (e.g., the last 10) for each tag using pagination
                    Pageable pageable = PageRequest.of(0, 10);
                    List<Post> limitedPosts = postRepository.findByTagId(tag.getId(), pageable);

                    // Set the limited posts to the tag object manually
                    tag.setPosts(limitedPosts);
                }
            }

            // Make the response content object
            GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                    null,
                    "/tags",
                    tags
            );

            // return the response using ResponseEntity
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // return a response with the error message and status code 400 if there is an error
            GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                    "Error getting tags: " + e.getMessage(),
                    "/tags",
                    null
            );

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get a tag by id, receive a get request to /tags/{id}
    @GetMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<GetSingleItemsResponse> getTagById(@PathVariable int id) {
        try {
            // Get the tag by id
            Tag tag = tagRepository.findById(id).isEmpty() ? null : tagRepository.findById(id).get();

            // if the tag is not found, return a response with the error message and status code 404
            if (tag == null) {
                GetSingleItemsResponse response = new GetSingleItemsResponse(
                        "Tag with ID " + id + " not found",
                        null,
                        "/tags",null
                );

                return ResponseEntity.status(404).body(response);
            }

            // Make the response content object
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    null,
                    "/tags/" + id,
                    "/tags",
                    tag);

            // return the response using ResponseEntity
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // return a response with the error message and status code 400 if there is an error
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    "Error getting tag: " + e.getMessage(),
                     null,
                    "/tags",
                    null);

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get a tag by name, receive a get request to /tags/by-name/{name}
    @GetMapping(path="/by-name/{name}")
    public @ResponseBody ResponseEntity<GetSingleItemsResponse> getTagByName(@PathVariable String name) {
        try {
            // Get the tag by name
            Tag tag = tagRepository.findByName(name) == null ? null : tagRepository.findByName(name);

            if (tag == null) {
                // return a response with the error message and status code 404 if the tag is not found
                GetSingleItemsResponse response = new GetSingleItemsResponse(
                        "Tag with name " + name + " not found",
                        null,
                        "/tags",
                        null
                );

                return ResponseEntity.status(404).body(response);
            }

            // Make the response content object
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    null,
                    "/tags/" + tag.getId(),
                    "/tags",
                    tag);

            // return the response using ResponseEntity
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // return a response with the error message and status code 400 if there is an error
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    "Error getting tag: " + e.getMessage(),
                    null,
                    "/tags",
                    null
            );

            return ResponseEntity.badRequest().body(response);
        }
    }

}
