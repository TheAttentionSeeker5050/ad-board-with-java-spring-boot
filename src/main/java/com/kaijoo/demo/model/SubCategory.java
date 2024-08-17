package com.kaijoo.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sub_category")
public class SubCategory
{
    @Id
    @Column(name = "sub_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String linkID;

    // One-to-many with Categories, a SubCategory can only belong to one Category,
    // but a Category can have many SubCategories
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("subCategories")
    private Category category;

    // One-to-many with Posts, a SubCategory can have many Posts, but a Post can only belong to one SubCategory
    @OneToMany(mappedBy = "subCategory")
    private List<Post> posts;

    public SubCategory(String name, String linkID)
    {
        this.name = name;
        this.linkID = linkID;
    }

}
