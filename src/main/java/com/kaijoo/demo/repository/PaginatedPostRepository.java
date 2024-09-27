package com.kaijoo.demo.repository;


import com.kaijoo.demo.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaginatedPostRepository extends PagingAndSortingRepository<Post, Integer> {
    // Add custom methods here

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.subCategory sc " +
            "LEFT JOIN p.owner o " +
            "WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(o.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(sc.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
    List<Post> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // find by tag id, and only show title, description, address, phone, email
    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE t.id = :tagId")
    List<Post> findByTagId(@Param("tagId") int tagId, Pageable pageable);

    // Find by category id, and only show title, description, address, phone, email
    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.category c " +
            "WHERE c.id = :categoryId")
    List<Post> findByCategoryId(@Param("categoryId") int categoryId, Pageable pageable);

    // Find by sub category id, and only show title, description, address, phone, email
    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.subCategory sc " +
            "WHERE sc.id = :subCategoryId")
    List<Post> findBySubCategoryId(@Param("subCategoryId") int subCategoryId, Pageable pageable);

    // Find by owner, and only show title, description, address, phone, email
    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.owner o " +
            "WHERE o.id = :ownerId")
    List<Post> findByOwnerId(@Param("ownerId") int ownerId, Pageable pageable);

}
