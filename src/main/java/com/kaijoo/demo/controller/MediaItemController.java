package com.kaijoo.demo.controller;

//import com.kaijoo.demo.repository.MediaItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/media-items") // This means URL's start with /media-items (after Application path)
public class MediaItemController {

//    // Add media item repository here
//    @Autowired
//    private MediaItemRepository mediaItemRepository;

    // The routes will be added here
    // Create a media item
    @PostMapping(path="")
    public String createMediaItem() {
        return "Media item created";
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
    public String getAllMediaItems() {
        return "Getting all media items";
    }

    // Get a media item by id
    @GetMapping(path="/by-id/{id}")
    public String getMediaItemById() {
        return "Getting media item by id";
    }

}
