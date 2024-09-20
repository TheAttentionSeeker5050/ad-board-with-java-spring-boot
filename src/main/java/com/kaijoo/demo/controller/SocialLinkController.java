package com.kaijoo.demo.controller;

import com.kaijoo.demo.dto.GetMultipleItemsResponse;
import com.kaijoo.demo.dto.GetSingleItemsResponse;
import com.kaijoo.demo.dto.ItemCreatedOrUpdatedResponse;
import com.kaijoo.demo.model.SocialLink;
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
                    "Error creating social link",
                    "/social-links",
                    null
            ));
        }
    }

    // Update a social link
    @PutMapping(path="/by-id/{id}")
    public String updateSocialLink() {
        return "Social Link updated";
    }

    // Delete a social link
    @DeleteMapping(path="/by-id/{id}")
    public String deleteSocialLink() {
        return "Social Link deleted";
    }

    // Get all social links
    @GetMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<GetSingleItemsResponse> getSocialLinkById(int id) {
        SocialLink socialLink = socialLinkRepository.findById(id).isEmpty() ?
                null : socialLinkRepository.findById(id).get();


        if (socialLink == null) {
            return ResponseEntity.badRequest().body(new GetSingleItemsResponse(
                    "Social link not found",
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
        Iterable<SocialLink> socialLinks = socialLinkRepository.findAll();

        // create a response object
        GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                (String) null,
                "/social-links",
                (List) socialLinks
        );

        // return the response object
        return ResponseEntity.ok(response);

    }
}
