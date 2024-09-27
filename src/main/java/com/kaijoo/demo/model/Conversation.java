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
@Table(name = "conversation")
public class Conversation {

    @Id
    @Column(name = "sub_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String conversationID;


    // Many-to-many relation with User, a conversation can have many (2) users and a user can have many conversations
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "conversation_user",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties({"mediaItems", "posts", "socialLinks", "conversations"})
    private List<User> users;

    // One-to-many relation with Post, a post can have many conversations, but a conversation can have one post
    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"mediaItems", "socialLinks", "owner", "tags", "subCategory", "category", "conversations"})
    private Post post;

    public Conversation(String conversationID)
    {
        this.conversationID = conversationID;
    }

}
