package com.kaijoo.demo.repository;

import com.kaijoo.demo.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    // Add custom methods here
    Category findByName(String name);
}
