package com.curiousape.blog.repository;

import com.curiousape.blog.model.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {

    Optional<BlogPostEntity> findBySlug(String slug);

    List<BlogPostEntity> findAllByOrderByDateDesc();
}
