package com.kaijoo.demo.controller;


import com.kaijoo.demo.dto.GetMultipleItemsResponse;
import com.kaijoo.demo.dto.GetSingleItemsResponse;
import com.kaijoo.demo.dto.ItemCreatedOrUpdatedResponse;
import com.kaijoo.demo.model.MediaItem;
import com.kaijoo.demo.model.User;
import com.kaijoo.demo.model.UserInfoDetails;
import com.kaijoo.demo.repository.MediaItemRepository;
import com.kaijoo.demo.service.JwtService;
import com.kaijoo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/media-items") // This means URL's start with /media-items (after Application path)
public class MediaItemController {

    // Add media item repository here
    @Autowired
    private MediaItemRepository mediaItemRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService service;


    // The routes will be added here
    // Create a media item
    @PostMapping(path="")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> createMediaItem(
            @RequestHeader("Authorization") String token,
            @RequestBody MediaItem mediaItem
    ) {
        try {
            // Extract email from token
            // take bearer out of token
            String email = jwtService.extractEmail(token.substring(7));

            // build a json array with the information using the UserInfoDetails class object
            UserInfoDetails userInfoDetails = (UserInfoDetails) service.loadUserByUsername(email);

            // validate the media item user
            boolean userIsValid = jwtService.validateToken(
                    token.substring(7),
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

           // Save the media item
            mediaItemRepository.save(mediaItem);

            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Media item created",
                    null,
                    "/media-items",
                    "/media-items/by-id/" + mediaItem.getId()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    null,
                    "Error creating media item",
                    "/media-items",
                    null
            );

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Update a media item
    @PutMapping(path="/by-id/{id}")
    public String updateMediaItem() {
        return "Media item updated";
    }

    // Delete a media item
    @DeleteMapping(path="/by-id/{id}")
    public String deleteMediaItem() {
        return "Media item deleted";
    }

    // Get all media items
    @GetMapping(path="")
    public @ResponseBody ResponseEntity<GetMultipleItemsResponse> getAllMediaItems() {

        // get all media items from the database
        Iterable<MediaItem> mediaItems = mediaItemRepository.findAll();

        // create a response object
        GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                (String) null,
                "/media-items",
                (List) mediaItems
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
                    "/media-items/by-id/" + id,
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
