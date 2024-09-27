package com.kaijoo.demo.controller;


import com.kaijoo.demo.dto.GetMultipleItemsResponse;
import com.kaijoo.demo.dto.GetSingleItemsResponse;
import com.kaijoo.demo.dto.ItemCreatedOrUpdatedResponse;
import com.kaijoo.demo.dto.ItemDeletedResponse;
import com.kaijoo.demo.model.Category;
import com.kaijoo.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/categories") // This means URL's start with /subcategories (after Application path)
public class CategoryController {
    // Add category repository here
    @Autowired
    private CategoryRepository categoryRepository;

    // Some rules, categories have to be unique, and can't be deleted if there are subcategories

    // The routes will be added here
    // Create a category
    @PostMapping(path="")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> createCategory(
            @RequestBody Category category) {
        try {
            // Check if the category already exists
            Category existingCategory = categoryRepository.findByName(category.getName());

            if (existingCategory != null) {
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "Category already exists",
                        "/categories",
                        null);

                return ResponseEntity.badRequest().body(response);
            }

            // Save the category
            categoryRepository.save(category);

            // Prepare the response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Category created",
                    null,
                    "/categories",
                    "/categories/" + category.getId());

            return ResponseEntity.ok(response);


        } catch (Exception e) {

            // Prepare the response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    null,
                    "Error creating category: " + e.getMessage(),
                    "/categories",
                    null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Edit a category
    @PutMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<ItemCreatedOrUpdatedResponse> editCategory(
            @PathVariable("id") Integer id,
            @RequestBody Category category) {
        try {
            // Check if the category exists
            Category categoryToUpdate = categoryRepository.findById(id).isEmpty()
                    ? null : categoryRepository.findById(id).get();

            // If the category doesn't exist, return an error
            if (categoryToUpdate == null) {
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "Category not found",
                        "/categories",
                        null);

                return ResponseEntity.status(404).body(response);
            }

            // Check if a category with the same name already exists other than the one being updated
            Category existingCategory = categoryRepository.findByName(category.getName());

            if (existingCategory != null && existingCategory.getId() != id) {
                ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                        null,
                        "Another category with the same name already exists",
                        "/categories",
                        null
                );

                return ResponseEntity.badRequest().body(response);
            }

            if (category.getName() != null) categoryToUpdate.setName(category.getName());

            // Update the category
            categoryRepository.save(categoryToUpdate);

            // Prepare the response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    "Category updated",
                    null,
                    "/categories",
                    "/categories/" + categoryToUpdate.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Prepare the response
            ItemCreatedOrUpdatedResponse response = new ItemCreatedOrUpdatedResponse(
                    null,
                    "Error updating category: " + e.getMessage(),
                    "/categories",
                    null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Delete a category
    // If the category has subcategories, don't allow deletion
    @DeleteMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<ItemDeletedResponse> deleteCategory(
            @PathVariable("id") Integer id) {
        try {
            // Check if the category exists
            Category categoryToDelete = categoryRepository.findById(id).isEmpty()
                    ? null : categoryRepository.findById(id).get();

            // If the category doesn't exist, return an error
            if (categoryToDelete == null) {
                ItemDeletedResponse response = new ItemDeletedResponse(
                        null,
                        "Category not found",
                        "/categories"
                );

                return ResponseEntity.status(404).body(response);
            }

            // Check if category has subcategories, if so, don't allow deletion
            if (!categoryToDelete.getSubCategories().isEmpty()) {
                ItemDeletedResponse response = new ItemDeletedResponse(
                        null,
                        "Please remove subcategories before deleting",
                        "/categories"
                );

                return ResponseEntity.badRequest().body(response);
            }

            // Delete the category
            categoryRepository.delete(categoryToDelete);

            // Prepare the response
            ItemDeletedResponse response = new ItemDeletedResponse(
                    "Category deleted",
                    null,
                    "/categories"
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Prepare the response
            ItemDeletedResponse response = new ItemDeletedResponse(
                    null,
                    "Error deleting category: " + e.getMessage(),
                    "/categories"
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Get all categories
    @GetMapping(path="")
    public @ResponseBody ResponseEntity<GetMultipleItemsResponse> getAllCategories() {
        try {
            // Get all categories, but prevent infinite recursion
            List<Category> categories = categoryRepository.findAll().iterator().hasNext()
                    ? (List<Category>) categoryRepository.findAll() : null;

            // Prepare the response
            GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                    null,
                    "/categories",
                    categories);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Prepare the response
            GetMultipleItemsResponse response = new GetMultipleItemsResponse(
                    "Error getting categories: " + e.getMessage(),
                    "/categories",
                    null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Get a category by id
    @GetMapping(path="/by-id/{id}")
    public @ResponseBody ResponseEntity<GetSingleItemsResponse> getCategoryById(
            @PathVariable("id") Integer id) {
        try {
            // Get the category
            Category category = categoryRepository.findById(id).isEmpty()
                    ? null : categoryRepository.findById(id).get();

            // If the category doesn't exist, return an error
            if (category == null) {
                GetSingleItemsResponse response = new GetSingleItemsResponse(
                        "Category with id " + id + " not found",
                        null,
                        "/categories",
                        null);

                return ResponseEntity.status(404).body(response);
            }

            // Prepare the response
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    null,
                    "/categories/by-id/" + category.getId(),
            "/categories",
                    category);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Prepare the response
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    "Error getting category: " + e.getMessage(),
                    null,
                    "/categories",
                    null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Get a category by name
    @GetMapping(path="/by-name/{name}")
    public @ResponseBody ResponseEntity<GetSingleItemsResponse> getCategoryByName(
            @PathVariable("name") String name) {
        try {
            // Get the category
            Category category = categoryRepository.findByName(name);

            // If the category doesn't exist, return an error
            if (category == null) {
                GetSingleItemsResponse response = new GetSingleItemsResponse(
                        "Category with name " + name + " not found",
                        null,
                        "/categories",
                        null);

                return ResponseEntity.status(404).body(response);
            }

            // Prepare the response
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    null,
                    "/categories/by-id/" + category.getId(),
                    "/categories",
                    category);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Prepare the response
            GetSingleItemsResponse response = new GetSingleItemsResponse(
                    "Error getting category: " + e.getMessage(),
                    null,
                    "/categories",
                    null);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
