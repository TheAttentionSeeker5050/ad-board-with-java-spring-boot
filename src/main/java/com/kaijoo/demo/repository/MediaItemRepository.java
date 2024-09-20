package com.kaijoo.demo.repository;

import com.kaijoo.demo.model.MediaItem;
import org.springframework.data.repository.CrudRepository;

public interface MediaItemRepository extends CrudRepository<MediaItem, Integer> {
    // Add custom methods here
}
