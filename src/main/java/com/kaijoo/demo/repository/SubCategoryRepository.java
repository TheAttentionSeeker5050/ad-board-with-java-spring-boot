package com.kaijoo.demo.repository;

import com.kaijoo.demo.model.SubCategory;
import org.springframework.data.repository.CrudRepository;

public interface SubCategoryRepository extends CrudRepository<SubCategory, Integer> {
    SubCategory findByName(String name);
}
