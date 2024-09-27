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
@Table(name = "user")
public class User {

    // Getters and Setters
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private String roles = ROLE_USER;

    // const role types
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    // One-to-many relation with Post, a user can have many posts, but a post can have one user
    @OneToMany(mappedBy = "owner")
    @JsonIgnoreProperties({"mediaItems", "socialLinks", "owner", "tags", "subCategory", "category", "conversations"})
    private List<Post> posts;

    // One-to-many relation with MediaItem, a user can have many media items, but a media item can have one user
    @OneToMany(mappedBy = "owner")
    @JsonIgnoreProperties({"owner", "post"})
    private List<MediaItem> mediaItems;

    // One-to-many relation with SocialLink, a user can have many social links, but a social link can have one user
    // avoid circular reference by avoiding owner inside SocialLink
    @OneToMany(mappedBy = "owner")
    @JsonIgnoreProperties({"owner", "post"})
    private List<SocialLink> socialLinks;


    // Many-to-many relation with Conversation, a user can have many conversations and a conversation can have many (2) users
    @OneToMany(mappedBy = "users")
    private List<Conversation> conversations;

    // Constructors
    public User(String name, String email, String password, String roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = ROLE_USER;
    }


}