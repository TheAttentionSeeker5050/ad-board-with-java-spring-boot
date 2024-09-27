package com.kaijoo.demo.model;

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
@Table(name = "post")
public class Post {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int id;

    private String title;
    private String description;
    private String address;
    private String phone;
    private String email;

    // Many-to-many relation with tags, a post can have many tags, and a tag can belong to many
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"))
    @JsonIgnoreProperties({"posts"})
    private List<Tag> tags;

    // One-to-many relation with SubCategory, a post can have one subcategory, but a subcategory can have many posts
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    @JsonIgnoreProperties({"category", "posts"})
    private SubCategory subCategory;

    // One-to-many relation with Category, a post can have one category, but a category can have many posts
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"subCategories", "posts"})
    private Category category;

    // One-to-many relation with User, a post can have one user, but a user can have many posts
    @ManyToOne
    @JoinColumn(name = "owner")
    @JsonIgnoreProperties({"mediaItems", "posts", "socialLinks", "conversations"})
    private User owner;


    // One-to-many relation with media items, a post can have many media items
    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties({"owner", "post"})
    private List<MediaItem> mediaItems;

    // One-to-many relation with social links, a post can have many social links
    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties({"owner", "post"})
    private List<SocialLink> socialLinks;

    // One-to-many relation with conversation, a post can have many conversations, but a conversation can have one post
    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties({"post"})
    private List<Conversation> conversations;

    // Constructor
    public Post(String title, String description, String address, String phone, String email) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

}
