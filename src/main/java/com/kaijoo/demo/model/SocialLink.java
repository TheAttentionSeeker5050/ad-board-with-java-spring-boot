package com.kaijoo.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "social_link")
public class SocialLink {

    // Getters and Setters
    @Id
    @Column(name = "social_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String link;
    private String iconLink;
    private String text;
    private String alt;

    // One-to-one relation with User, a media item can belong to only one user
    @OneToOne
    @JoinColumn(name = "owner_id")
    private UserInfoDetails owner;

    // One-to-many relation with Post, a social link can belong to many posts
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // Constructor
    public SocialLink(
            String link, String iconLink, String text, String alt
    ) {
        this.link = link;
        this.iconLink = iconLink;
        this.text = text;
        this.alt = alt;

    }

}
