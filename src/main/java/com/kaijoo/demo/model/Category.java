package com.kaijoo.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int id;
    private String name;

    // One-to-many relation with SubCategory, a category can have many sub categories
    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties({"category", "posts"})
    private List<SubCategory> subCategories;

    // One-to-many relation with Post, a category can have many posts
    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties({"mediaItems", "socialLinks", "owner", "tags", "subCategory", "category", "conversations"})
    private List<Post> posts;

    // Constructor
    public Category(String name) {
        this.name = name;
    }


}

