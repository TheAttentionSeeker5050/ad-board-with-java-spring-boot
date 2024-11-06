package com.kaijoo.demo.repository;

import com.kaijoo.demo.model.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    Tag findByName(String name);
    // Find all by name that contain the argument name in its field name
    List<Tag> findAllByNameContaining(String name);
}
