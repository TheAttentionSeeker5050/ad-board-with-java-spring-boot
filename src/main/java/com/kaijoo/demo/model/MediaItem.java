package com.kaijoo.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media_item")
public class MediaItem {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_item_id")
    private int id;
    private String itemType;
    private String link;
    private String iconLink;
    private String title;
    private String alt;

    // One-to-one relation with User, a media item can belong to only one user
    @ManyToOne
    @JoinColumn(name = "owner_id")
    // ignore multiple references to the same object
    @JsonIgnoreProperties({"mediaItems", "posts", "socialLinks", "conversations"})
    private User owner;

    // One-to-many relation with Post, a media item can belong to many posts,
    // but a post can have many media items
    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"mediaItems", "socialLinks", "owner", "tags", "subCategory", "category", "conversations"})
    private Post post;

    // Constructor
    public MediaItem(
            String itemType, String link, String iconLink, String title, String alt
    ) {
        this.itemType = itemType;
        this.link = link;
        this.iconLink = iconLink;
        this.title = title;
        this.alt = alt;
    }

}
