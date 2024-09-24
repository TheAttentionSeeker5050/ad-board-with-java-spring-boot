package com.kaijoo.demo.controller;

import com.kaijoo.demo.dto.GetMultipleItemsResponse;
import com.kaijoo.demo.dto.GetSingleItemsResponse;
import com.kaijoo.demo.dto.ItemCreatedOrUpdatedResponse;
import com.kaijoo.demo.dto.ItemDeletedResponse;
import com.kaijoo.demo.model.MediaItem;
import com.kaijoo.demo.model.SocialLink;
import com.kaijoo.demo.model.User;
import com.kaijoo.demo.model.UserInfoDetails;
import com.kaijoo.demo.repository.SocialLinkRepository;
import com.kaijoo.demo.service.JwtService;
import com.kaijoo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller // This means that this class is a Controller
@RequestMapping(path="/social-links") // This means URL's start with /social-links (after Application path)
public class SocialLinkController {

    // Add social link repository here
    @Autowired
    private SocialLinkRepository socialLinkRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService service;

    // The routes will be added here
    // Create a social link
    @PostMapping(path="")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> createSocialLink(
            @RequestHeader("Authorization") String token,
            @RequestBody SocialLink socialLink
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


            // Set the owner of the social link
            // cast the UserInfoDetails object to a User object
            User owner = new User();

            owner.setId(userInfoDetails.getId());


            // set the owner of the social link
            socialLink.setOwner(owner);

            // save the social link
            socialLinkRepository.save(socialLink);

            // return a success response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Social link created",
                    null,
                    "/social-links",
                    "/social-links/by-id/" + socialLink.getId()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ItemCreatedOrUpdatedResponse(
                    null,
                    "Error creating social link: " + e.getMessage(),
                    "/social-links",
                    null
            ));
        }
    }

    // Update a social link
    @PutMapping(path="/by-id/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> updateSocialLink(
            @PathVariable int id,
            @RequestHeader("Authorization") String token,
            @RequestBody SocialLink socialLink
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

            // update the social link
            SocialLink socialLinkToUpdate = socialLinkRepository.findById(id).isPresent() ?
                    socialLinkRepository.findById(id).get() : null;

            // check if the social link exists
            if (socialLinkToUpdate == null) {
                return ResponseEntity.badRequest().body(new ItemCreatedOrUpdatedResponse(
                        null,
                        "Social link with id " + id + " not found",
                        "/social-links",
                        null
                ));
            }

            // check if the user is the owner of the social link
            if (socialLinkToUpdate.getOwner().getId() != userInfoDetails.getId()) {
                return ResponseEntity.badRequest().body(new ItemCreatedOrUpdatedResponse(
                        null,
                        "User is not the owner of the social link",
                        "/social-links",
                        null
                ));
            }

            // set the new values on all the fields except the id and owner
            socialLinkToUpdate.setLink(socialLink.getLink());
            socialLinkToUpdate.setIconLink(socialLink.getIconLink());
            socialLinkToUpdate.setText(socialLink.getText());
            socialLinkToUpdate.setAlt(socialLink.getAlt());

            socialLinkRepository.save(socialLinkToUpdate);

            // return a success response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Social link updated",
                    null,
                    "/social-links",
                    "/social-links/by-id/" + socialLinkToUpdate.getId()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ItemCreatedOrUpdatedResponse(
                    null,
                    "Error updating social link: " + e.getMessage(),
                    "/social-links",
                    null
            ));
        }
    }

    // Delete a social link
    @DeleteMapping(path="/by-id/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<ItemDeletedResponse> deleteSocialLink(
            @PathVariable int id,
            @RequestHeader("Authorization") String token
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
                ItemDeletedResponse response = new ItemDeletedResponse(
                        null,
                        "User is not valid",
                        "/media-items"
                );

                return ResponseEntity.status(401).body(response);
            }

            // delete the social link
            SocialLink socialLinkToDelete = socialLinkRepository.findById(id).isPresent() ?
                    socialLinkRepository.findById(id).get() : null;

            // check if the social link exists
            if (socialLinkToDelete == null) {
                return ResponseEntity.badRequest().body(new ItemDeletedResponse(
                        null,
                        "Social link with id " + id + " not found",
                        "/social-links"
                ));
            }

            // check if the user is the owner of the social link
            if (socialLinkToDelete.getOwner().getId() != userInfoDetails.getId()) {
                return ResponseEntity.badRequest().body(new ItemDeletedResponse(
                        null,
                        "User is not the owner of the social link",
                        "/social-links"
                ));
            }

            socialLinkRepository.delete(socialLinkToDelete);

            // return a success response
            ItemDeletedResponse response = new ItemDeletedResponse(
                    "Social link deleted",
                    null,
                    "/social-links"
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ItemDeletedResponse(
                    null,
                    "Error deleting social link: " + e.getMessage(),
                    "/social-links"
            ));
        }
    }

    // Get all social links
    @GetMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<GetSingleItemsResponse> getSocialLinkById(int id) {
        SocialLink socialLink = socialLinkRepository.findById(id).isEmpty() ?
                null : socialLinkRepository.findById(id).get();


        if (socialLink == null) {
            return ResponseEntity.badRequest().body(new GetSingleItemsResponse(
                    "Social link with id " + id + " not found",
                    "/social-links/by-id/" + id,
                    "/social-links",
                    null
            ));
        }

        // create a response object
        GetSingleItemsResponse response = new GetSingleItemsResponse(
                (String) null,
                "/social-links/by-id/" + id,
                "/social-links",
                socialLink
        );

        // return the response object
        return ResponseEntity.ok(response);
    }

    // Get a social link by id
    @GetMapping(path="")
    public @ResponseBody ResponseEntity<GetMultipleItemsResponse> getAllSocialLinks() {

        // get all social links from the database
        List<SocialLink> socialLinks = socialLinkRepository.findAll().iterator().hasNext() ?
                (List<SocialLink>) socialLinkRepository.findAll() : null;

        // create a response object
        GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                (String) null,
                "/social-links",
                socialLinks
        );

        // return the response object
        return ResponseEntity.ok(response);

    }
}
