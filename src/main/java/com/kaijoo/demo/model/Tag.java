package com.kaijoo.demo.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jdk.jfr.Recording;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.web.PageableDefault;

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


    // Limit the amount of post to just 10
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"mediaItems", "socialLinks", "owner", "tags", "subCategory", "category", "conversations"})
    private List<Post> posts;

    public Tag(String name) {
        this.name = name;
    }

}
