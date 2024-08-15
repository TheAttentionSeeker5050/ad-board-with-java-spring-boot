package com.kaijoo.demo.repository;

import com.kaijoo.demo.model.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    Tag findByName(String name);
}
