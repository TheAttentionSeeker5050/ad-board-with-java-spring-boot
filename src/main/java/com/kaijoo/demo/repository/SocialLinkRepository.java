package com.kaijoo.demo.repository;

import com.kaijoo.demo.model.SocialLink;
import org.springframework.data.repository.CrudRepository;

public interface SocialLinkRepository extends CrudRepository<SocialLink, Integer> {
    // Add custom methods here
}
