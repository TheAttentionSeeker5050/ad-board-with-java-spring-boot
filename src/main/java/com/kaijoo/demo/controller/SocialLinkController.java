package com.kaijoo.demo.controller;

//import com.kaijoo.demo.repository.SocialLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller // This means that this class is a Controller
@RequestMapping(path="/social-links") // This means URL's start with /social-links (after Application path)
public class SocialLinkController {

    // Add social link repository here
    @Autowired
//    private SocialLinkRepository socialLinkRepository;

    // The routes will be added here
    // Create a social link
    @PostMapping(path="")
    public String createSocialLink() {
        return "Social Link created";
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
    @GetMapping(path="")
    public String getAllSocialLinks() {
        return "Get all social links";
    }

    // Get a social link by id
    @GetMapping(path="/by-id/{id}")
    public String getSocialLinkById() {
        return "get social link by id";
    }

}
