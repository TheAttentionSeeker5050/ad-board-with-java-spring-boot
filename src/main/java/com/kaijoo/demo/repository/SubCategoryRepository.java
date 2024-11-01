package com.kaijoo.demo.repository;

import com.kaijoo.demo.model.SubCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends CrudRepository<SubCategory, Integer> {
    SubCategory findByName(String name);
    List<SubCategory> findByCategory_Id(int id);
}
