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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags;

    // One-to-many relation with SubCategory, a post can have one subcategory, but a subcategory can have many posts
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    // One-to-many relation with Category, a post can have one category, but a category can have many posts
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // One-to-many relation with User, a post can have one user, but a user can have many posts
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;


    // One-to-many relation with media items, a post can have many media items
    @OneToMany(mappedBy = "media_item")
    private List<MediaItem> mediaItems;

    // One-to-many relation with social links, a post can have many social links
    @OneToMany(mappedBy = "social_link")
    private List<SocialLink> socialLinks;

    // One-to-one relation with conversation, a post can have one conversation
    @OneToOne(mappedBy = "conversation")
    private Conversation conversation;

    // Constructor
    public Post(String title, String description, String address, String phone, String email) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

}
