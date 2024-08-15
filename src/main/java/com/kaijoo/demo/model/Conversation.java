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
@Table(name = "conversation")
public class Conversation {

    @Id
    @Column(name = "sub_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String conversationID;


    // one-to-many with Users, a conversation can have many users
    @OneToMany(mappedBy = "conversation")
    private List<User> users;

    // one-to-one with Post, a conversation can have one post
    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Conversation(String conversationID)
    {
        this.conversationID = conversationID;
    }

}
