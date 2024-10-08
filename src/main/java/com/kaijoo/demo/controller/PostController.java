package com.kaijoo.demo.controller;

import com.kaijoo.demo.dto.GetMultipleItemsResponse;
import com.kaijoo.demo.dto.GetSingleItemsResponse;
import com.kaijoo.demo.dto.ItemCreatedOrUpdatedResponse;
import com.kaijoo.demo.dto.ItemDeletedResponse;
import com.kaijoo.demo.model.Post;
import com.kaijoo.demo.model.User;
import com.kaijoo.demo.model.UserInfoDetails;
import com.kaijoo.demo.repository.PaginatedPostRepository;
import com.kaijoo.demo.repository.PostRepository;
import com.kaijoo.demo.service.JwtService;
import com.kaijoo.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/posts") // This means URL's start with /posts (after Application path)
public class PostController {

    // Add post repository here
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PaginatedPostRepository paginatedPostRepository;

    // Add jwt service here
    @Autowired
    private JwtService jwtService;

    // Add user service here
    @Autowired
    private UserService userService;

    // The routes will be added here
    // Create a post
    @PostMapping(path="")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> createPost(
            HttpServletRequest request,
            @RequestBody Post post
    ) {
        try {
            // Retrieve the JWT from cookies
            String token = jwtService.getTokenFromCookies(request.getCookies());
            String email = jwtService.extractEmail(token);

            // build a json array with the information using the UserInfoDetails class object
            UserInfoDetails userInfoDetails = (UserInfoDetails) userService.loadUserByUsername(email);

            // validate the media item user
            boolean userIsValid = jwtService.validateToken(
                    token,
                    userInfoDetails
            );

            // If the user is not valid, return an error
            if (!userIsValid) {
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "User is not valid",
                        "/posts",
                        null
                );

                return ResponseEntity.status(401).body(response);
            }

            // Set the owner of the social link
            // cast the UserInfoDetails object to a User object
            User owner = new User();

            owner.setId(userInfoDetails.getId());

            post.setOwner(owner);

            // Save the post
            postRepository.save(post);

            // Return the response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                "Post created",
                null,
                "/posts",
                "/posts/by-id/" + post.getId()
            );

            return ResponseEntity.ok().body(response);



        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ItemCreatedOrUpdatedResponse(
                null,
                "Error creating post: " + e.getMessage(),
                "/posts",
                null
            ));
        }
    }

    // Update a post
    @PutMapping(path="/by-id/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> updatePost(
            @PathVariable int id,
            HttpServletRequest request,
            @RequestBody Post post
            ) {
        try {
            // Retrieve the JWT from cookies
            String token = jwtService.getTokenFromCookies(request.getCookies());
            String email = jwtService.extractEmail(token);


            // build a json array with the information using the UserInfoDetails class object
            UserInfoDetails userInfoDetails = (UserInfoDetails) userService.loadUserByUsername(email);

            // validate the media item user
            boolean userIsValid = jwtService.validateToken(
                    token,
                    userInfoDetails
            );

            // If the user is not valid, return an error
            if (!userIsValid) {
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "User is not valid",
                        "/posts",
                        null
                );

                return ResponseEntity.status(401).body(response);
            }

            // update the post
            Post postToUpdate = postRepository.findById(id).isPresent() ?
                    postRepository.findById(id).get() : null;

            // check if the post exists
            if (postToUpdate == null) {
                return ResponseEntity.badRequest().body(new ItemCreatedOrUpdatedResponse(
                    null,
                    "Post does not exist",
                    "/posts",
                    null
                ));
            }

            // Check if the user is the owner of the post
            if (postToUpdate.getOwner().getId() != userInfoDetails.getId()) {
                return ResponseEntity.badRequest().body(new ItemCreatedOrUpdatedResponse(
                    null,
                    "User is not the owner of the post",
                    "/posts",
                    null
                ));
            }

            // Update the post
            postToUpdate.setTitle(post.getTitle());
            postToUpdate.setDescription(post.getDescription());
            postToUpdate.setAddress(post.getAddress());
            postToUpdate.setPhone(post.getPhone());
            postToUpdate.setEmail(post.getEmail());

            // Get media items and social links from the post body and save them
            if (post.getMediaItems() != null) {
                postToUpdate.setMediaItems(post.getMediaItems());
            }

            if (post.getSocialLinks() != null) {
                postToUpdate.setSocialLinks(post.getSocialLinks());
            }

            // Get category and subcategory from the post body and save them
            if (post.getCategory() != null) {
                postToUpdate.setCategory(post.getCategory());
            }

            if (post.getSubCategory() != null) {
                postToUpdate.setSubCategory(post.getSubCategory());
            }

            // Get tags from the post body and save them
            if (post.getTags() != null) {
                postToUpdate.setTags(post.getTags());
            }

            // Save the post
            postRepository.save(postToUpdate);

            // Return the response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                "Post updated",
                null,
                "/posts",
                    "/posts/by-id/" + id
            );

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ItemCreatedOrUpdatedResponse(
                null,
                "Error updating post: " + e.getMessage(),
                "/posts",
                null
            ));
        }
    }

    // Delete a post
    @DeleteMapping(path="/by-id/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<ItemDeletedResponse> deletePost(
            @PathVariable int id,
            HttpServletRequest request
    ) {
        try {
            // Retrieve the JWT from cookies
            String token = jwtService.getTokenFromCookies(request.getCookies());
            String email = jwtService.extractEmail(token);

            // build a json array with the information using the UserInfoDetails class object
            UserInfoDetails userInfoDetails = (UserInfoDetails) userService.loadUserByUsername(email);

            // validate the media item user
            boolean userIsValid = jwtService.validateToken(
                    token,
                    userInfoDetails
            );

            // If the user is not valid, return an error
            if (!userIsValid) {
                ItemDeletedResponse response = new ItemDeletedResponse(
                        null,
                        "User is not valid",
                        "/posts"
                );

                return ResponseEntity.status(401).body(response);
            }

            // Get the post to delete
            Post postToDelete = postRepository.findById(id).isPresent() ?
                    postRepository.findById(id).get() : null;

            // Check if the post exists
            if (postToDelete == null) {
                return ResponseEntity.badRequest().body(new ItemDeletedResponse(
                    null,
                    "Post does not exist",
                    "/posts"
                ));
            }

            // if the user is not the owner of the post, return an error
            if (postToDelete.getOwner().getId() != userInfoDetails.getId()) {
                return ResponseEntity.badRequest().body(new ItemDeletedResponse(
                    null,
                    "User is not the owner of the post",
                    "/posts"
                ));
            }

            // Delete the post
            postRepository.delete(postToDelete);

            // Return the response
            ItemDeletedResponse response = new ItemDeletedResponse(
                "Post deleted",
                null,
                "/posts"
            );

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ItemDeletedResponse(
                null,
                "Error deleting post: " + e.getMessage(),
                "/posts"
            ));
        }
    }

    // Get a post by id
    @GetMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<GetSingleItemsResponse> getPostById(int id) {
        // Get the Post element
        Post post = postRepository.findById(id).isPresent() ?
                postRepository.findById(id).get() : null;

        // If post does not exist, return an error
        if (post == null) {
            return ResponseEntity.badRequest().body(new GetSingleItemsResponse(
                "Post does not exist",
                null,
                "/posts",
                null
            ));
        }

        // create a response object
        GetSingleItemsResponse response = new GetSingleItemsResponse(
            "Post found",
            "/posts/by-id/" + id,
            "/posts",
            post
        );

        // Return the response
        return ResponseEntity.ok().body(response);
    }

    // Get all posts, here we will include optional pagination, and sorting,
    // so include that in url params
    @GetMapping(path="")
    public @ResponseBody ResponseEntity<GetMultipleItemsResponse> getAllPosts(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "25") Integer size,
            @RequestParam(required = false, defaultValue = "id,desc") String[] sort,
            @RequestParam(required = false) String keyword
    ) {
        // Parse the sorting parameters from the request
        String sortField = sort[0];
        Sort.Direction sortDirection = Sort.Direction.fromString(sort[1]);
        Sort sortBy = Sort.by(sortDirection, sortField);

        // Create Pageable object with page, size, and sorting
        Pageable pageable = PageRequest.of(page, size, sortBy);
        // Get all posts from the database, if page and size are not null, use pagination

        // If keyword is provided, use custom query to filter posts by keyword
        List<Post> posts;
        if (keyword != null && !keyword.isEmpty()) {
            posts = paginatedPostRepository.findByKeyword(keyword, pageable);
        } else {
            posts = paginatedPostRepository.findAll(pageable).getContent(); // Using List for response
        }

        // create a response object
        GetMultipleItemsResponse response = new GetMultipleItemsResponse(
            null,
            "/posts",
            posts
        );

        // Return the response
        return ResponseEntity.ok().body(response);
    }

}
