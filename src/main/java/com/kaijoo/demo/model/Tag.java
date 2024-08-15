package com.kaijoo.demo.model;

import jakarta.persistence.*;
import jdk.jfr.Recording;
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
@Table(name = "tag")
public class Tag {

    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;


    @ManyToMany(mappedBy = "tags")
    private List<Post> posts;

    public Tag(String name) {
        this.name = name;
    }

}
