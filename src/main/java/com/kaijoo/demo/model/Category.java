package com.kaijoo.demo.model;

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
    private String linkID;

    // One-to-many relation with SubCategory, a category can have many sub categories
    @OneToMany(mappedBy = "sub_category")
    private List<SubCategory> subCategories;

    // One-to-many relation with Post, a category can have many posts
    @OneToMany(mappedBy = "category")
    private List<Post> posts;

    // Constructor
    public Category(String name, String linkID) {
        this.name = name;
        this.linkID = linkID;
    }


}

