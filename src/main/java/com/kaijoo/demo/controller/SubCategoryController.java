package com.kaijoo.demo.controller;

import com.kaijoo.demo.dto.GetMultipleItemsResponse;
import com.kaijoo.demo.dto.GetSingleItemsResponse;
import com.kaijoo.demo.dto.ItemCreatedOrUpdatedResponse;
import com.kaijoo.demo.dto.ItemDeletedResponse;
import com.kaijoo.demo.model.SubCategory;
import com.kaijoo.demo.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/sub-categories") // This means URL's start with /subcategories (after Application path)
public class SubCategoryController {
    // Add sub-category repository here
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    // The routes will be added here

    // Some rules, two sub-categories can have the same name, but they must have different parent categories
    // However, this won't be validated in this implementation

    // Add a new sub-category
    @PostMapping(path="")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> addSubCategory(
            @RequestBody SubCategory subCategory) {

        try {
            // Save the sub-category
            subCategoryRepository.save(subCategory);

            // Return the response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Sub-category created successfully",
                    null, "/sub-categories",
                    "/sub-categories/by-id/" + subCategory.getId());

            return ResponseEntity.created(
                    new URI("/sub-categories/by-id/" + subCategory.getId())
            ).body(response);


        } catch (Exception e) {
            // Return the response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Error creating sub-category",
                    e.getMessage(), "/sub-categories", null);

            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Update a sub-category
    @PutMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> updateSubCategory(
            @PathVariable int id, @RequestBody SubCategory subCategory) {

        try {
            // Find the sub-category
            SubCategory subCategoryToUpdate = subCategoryRepository.findById(id).isEmpty()
                    ? null : subCategoryRepository.findById(id).get();

            // Check if the sub-category exists, if not return a 404 response
            if (subCategoryToUpdate == null) {
                // Return the response
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        "Sub-category not found",
                        "Sub-category with id " + id + " not found",
                        "/sub-categories", null);

                return ResponseEntity.status(404).body(response);
            }

            // Update the sub-category, if any of the fields are null, keep the old value
            if (subCategory.getName() != null)  subCategoryToUpdate.setName(subCategory.getName());
            if (subCategory.getLinkID() != null)  subCategoryToUpdate.setLinkID(subCategory.getLinkID());
            if (subCategory.getCategory() != null)  subCategoryToUpdate.setCategory(subCategory.getCategory());

            // Save the sub-category
            subCategoryRepository.save(subCategoryToUpdate);

            // Return the response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Sub-category updated successfully",
                    null, "/sub-categories",
                    "/sub-categories/by-id/" + subCategoryToUpdate.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Return the response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Error updating sub-category",
                    e.getMessage(), "/sub-categories", null);

            return ResponseEntity.internalServerError().body(response);

        }
    }

    // Delete a sub-category
    @DeleteMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<ItemDeletedResponse> deleteSubCategory(@PathVariable int id) {

        try {
            // Find the sub-category
            SubCategory subCategory = subCategoryRepository.findById(id).isEmpty()
                    ? null : subCategoryRepository.findById(id).get();

            // Check if the sub-category exists, if not return a 404 response
            if (subCategory == null) {
                // Return the response
                ItemDeletedResponse response = new ItemDeletedResponse(
                        null,
                        "Sub-category with id " + id + " not found",
                        "/sub-categories");

                return ResponseEntity.status(404).body(response);
            }

            // Delete the sub-category
            subCategoryRepository.delete(subCategory);

            // Return the response
            ItemDeletedResponse response = new ItemDeletedResponse(
                    "Sub-category deleted successfully",
                    null,
                    "/sub-categories");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Return the response
            ItemDeletedResponse response = new ItemDeletedResponse(
                    null,
                    "Error deleting sub-category" + e.getMessage(),
                    "/sub-categories");

            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Get a sub-category by id
    @GetMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<GetSingleItemsResponse> getSubCategoryById(@PathVariable int id) {

        try {
            // Find the sub-category
            SubCategory subCategory = subCategoryRepository.findById(id).isEmpty()
                    ? null : subCategoryRepository.findById(id).get();

            // Check if the sub-category exists, if not return a 404 response
            if (subCategory == null) {
                // Return the response
                GetSingleItemsResponse response = new GetSingleItemsResponse(
                        "Sub-category not found",
                         null,
                        "/sub-categories",
                        null);
                return ResponseEntity.status(404).body(response);
            }

            // Return the response
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    null,
                    "/sub-categories/by-id/" + subCategory.getId(),
                    "/sub-categories",
                    subCategory);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Return the response
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    "Error getting sub-category: " + e.getMessage(),
                     null,
                    "/sub-categories",
                    null);

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get subcategory by name
    @GetMapping(path="/by-name/{name}")
    public @ResponseBody ResponseEntity<GetSingleItemsResponse> getSubCategoryByName(
            @PathVariable String name) {

        try {
            // Find the sub-category
            SubCategory subCategory = subCategoryRepository.findByName(name) == null
                    ? null : subCategoryRepository.findByName(name);

            // Check if the sub-category exists, if not return a 404 response
            if (subCategory == null) {
                // Return the response
                GetSingleItemsResponse response = new GetSingleItemsResponse(
                        "Sub-category not found",
                         null,
                        "/sub-categories",
                        null);
                return ResponseEntity.status(404).body(response);
            }

            // Return the response
            GetSingleItemsResponse response = new GetSingleItemsResponse(null,
                     "/sub-categories/by-id/" + subCategory.getId(),
                    "/sub-categories",
                    subCategory);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Return the response
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    "Error getting sub-category: " + e.getMessage(),
                     null,
                    "/sub-categories",
                    null);

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get all subcategories
    @GetMapping(path="")
    public @ResponseBody ResponseEntity<GetMultipleItemsResponse> getAllSubCategories() {

        try {
            // Find all sub-categories
            // cast to list of SubCategories, because
            // the dto does not specify the type of the data field
            List<SubCategory> subCategories = subCategoryRepository.findAll().iterator().hasNext()
                    ? (List<SubCategory>) subCategoryRepository.findAll() : null;

            // Return the response
            GetMultipleItemsResponse response = new GetMultipleItemsResponse(null,
                    "/sub-categories", subCategories);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Return the response
            GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                    "Error getting sub-categories: " + e.getMessage(),
                    "/sub-categories", null);

            return ResponseEntity.badRequest().body(response);
        }
    }


}
