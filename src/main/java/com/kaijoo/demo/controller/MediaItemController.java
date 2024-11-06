package com.kaijoo.demo.controller;


import com.kaijoo.demo.dto.GetMultipleItemsResponse;
import com.kaijoo.demo.dto.GetSingleItemsResponse;
import com.kaijoo.demo.dto.ItemCreatedOrUpdatedResponse;
import com.kaijoo.demo.dto.ItemDeletedResponse;
import com.kaijoo.demo.model.MediaItem;
import com.kaijoo.demo.model.Post;
import com.kaijoo.demo.model.User;
import com.kaijoo.demo.model.UserInfoDetails;
import com.kaijoo.demo.repository.MediaItemRepository;
import com.kaijoo.demo.repository.PostRepository;
import com.kaijoo.demo.repository.UserRepository;
import com.kaijoo.demo.service.JwtService;
import com.kaijoo.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/media-items") // This means URL's start with /media-items (after Application path)
public class MediaItemController {

    // Add media item repository here
    @Autowired
    private MediaItemRepository mediaItemRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;


    // The routes will be added here
    // Create a media item
    @PostMapping(path="")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> createMediaItem(
            HttpServletRequest request,
            @RequestBody MediaItem mediaItem
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
                        "/media-items",
                        null
                );

                return ResponseEntity.status(401).body(response);
            }


            // Set the owner of the media item
            // cast the UserInfoDetails object to a User object
            User owner = userRepository.findById(userInfoDetails.getId()).isPresent() ?
                    userRepository.findById(userInfoDetails.getId()).get() : null;

            // if user is not found, return an error
            if (owner == null) {
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "User owner not found",
                        "/media-items",
                        null
                );

                return ResponseEntity.badRequest().body(response);
            }

            // add owner to media item
            mediaItem.setOwner(owner);

            // Explicitly set the post
            // Use the post id to get the post from the database
            if (mediaItem.getPost() != null) {
                // Make sure that the owner of the post is the same as the user
                Post post = postRepository.findById(mediaItem.getPost().getId()).isPresent() ?
                        postRepository.findById(mediaItem.getPost().getId()).get() : null;

                // If the post does not exist, make the post null
                if (post == null) {
                    mediaItem.setPost(null);
                } else {
                    // If the post exists, make sure that the owner of the post is the same as the user
                    if (post.getOwner().getId() != userInfoDetails.getId()) {
                        mediaItem.setPost(null);
                    } else {
                        mediaItem.setPost(post);
                    }
                }
            } else {
                mediaItem.setPost(null);
            }

           // Save the media item
            mediaItemRepository.save(mediaItem);

            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Media item created",
                    null,
                    "/media-items",
                    "/media-items/by-id/" + mediaItem.getId()
            );

            return ResponseEntity.created(
                    new URI("/media-items/by-id/" + mediaItem.getId())
            ).body(response);

        } catch (Exception e) {
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    null,
                    "Error creating media item: " + e.getMessage(),
                    "/media-items",
                    null
            );

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Update a media item
    @PutMapping(path="/by-id/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> updateMediaItem(
            @PathVariable int id,
            HttpServletRequest request,
            @RequestBody MediaItem mediaItem
    ) {
        try{
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
                        "/media-items",
                        null
                );

                return ResponseEntity.status(401).body(response);
            }

            // Update the media item
            MediaItem mediaItemToUpdate = mediaItemRepository.findById(id).isPresent() ?
                    mediaItemRepository.findById(id).get() : null;

            // check if media item exists
            if (mediaItemToUpdate == null) {
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "Media item not found",
                        "/media-items",
                        null
                );

                return ResponseEntity.badRequest().body(response);
            }

            // check if onwer is the same as the user
            if (mediaItemToUpdate.getOwner().getId() != userInfoDetails.getId()) {
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "User is not the owner of the media item",
                        "/media-items",
                        null
                );

                return ResponseEntity.status(401).body(response);
            }

            // save all the new information except the id and the owner
            mediaItemToUpdate.setItemType(mediaItem.getItemType());
            mediaItemToUpdate.setLink(mediaItem.getLink());
            mediaItemToUpdate.setIconLink(mediaItem.getIconLink());
            mediaItemToUpdate.setTitle(mediaItem.getTitle());
            mediaItemToUpdate.setAlt(mediaItem.getAlt());

            // Post is not updated, it is set when the media item is created

            // save the updated media item
            mediaItemRepository.save(mediaItemToUpdate);

            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Media item updated",
                    null,
                    "/media-items",
                    "/media-items/by-id/" + mediaItemToUpdate.getId()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    null,
                    "Error updating media item: " + e.getMessage(),
                    "/media-items",
                    null
            );

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Delete a media item
    @DeleteMapping(path="/by-id/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<ItemDeletedResponse> deleteMediaItem(
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
                        "/media-items"
                );

                return ResponseEntity.status(401).body(response);
            }

            // Delete the media item
            MediaItem mediaItemToDelete = mediaItemRepository.findById(id).isPresent() ?
                    mediaItemRepository.findById(id).get() : null;

            if (mediaItemToDelete == null) {
                ItemDeletedResponse response = new ItemDeletedResponse(
                        null,
                        "Media item not found",
                        "/media-items"
                );

                return ResponseEntity.badRequest().body(response);
            }

            // check if onwer is the same as the user
            if (mediaItemToDelete.getOwner().getId() != userInfoDetails.getId()) {
                ItemDeletedResponse response = new ItemDeletedResponse(
                        null,
                        "User is not the owner of the media item",
                        "/media-items"
                );

                return ResponseEntity.status(401).body(response);
            }

            mediaItemRepository.delete(mediaItemToDelete);

            ItemDeletedResponse response = new ItemDeletedResponse(
                    "Media item deleted",
                    null,
                    "/media-items"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ItemDeletedResponse response = new ItemDeletedResponse(
                    null,
                    "Error deleting media item: " + e.getMessage(),
                    "/media-items"
            );

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get all media items
    @GetMapping(path="")
    public @ResponseBody ResponseEntity<GetMultipleItemsResponse> getAllMediaItems() {

        // get all media items from the database
        List<MediaItem> mediaItems = mediaItemRepository.findAll().iterator().hasNext() ?
                (List<MediaItem>) mediaItemRepository.findAll() : null;

        // create a response object
        GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                null,
                "/media-items",
                mediaItems
        );

        // return the response object
        return ResponseEntity.ok(response);
    }

    // Get a media item by id
    @GetMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<GetSingleItemsResponse> getMediaItemById(int id) {
        MediaItem mediaItem = mediaItemRepository.findById(id).isEmpty() ?
                null : mediaItemRepository.findById(id).get();

        if (mediaItem == null) {
            return ResponseEntity.badRequest().body(new GetSingleItemsResponse(
                    "Media item not found",
                    null,
                    "/media-items",
                    null
            ));
        }

        // create a response object
        GetSingleItemsResponse response = new GetSingleItemsResponse(
                (String) null,
                "/media-items/by-id/" + id,
                "/media-items",
                mediaItem
        );

        // return the response object
        return ResponseEntity.ok(response);
    }

}
