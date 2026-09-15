package com.curiousape.blog;

import com.curiousape.blog.model.BlogPost;
import com.curiousape.blog.model.BlogPostEntity;
import com.curiousape.blog.repository.BlogPostRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder {

    private final BlogPostRepository repository;
    private final ObjectMapper objectMapper;

    public DataSeeder(BlogPostRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void seed() {
        if (repository.count() > 0) {
            return;
        }
        try {
            ClassPathResource resource = new ClassPathResource("data/posts.json");
            if (!resource.exists()) return;

            try (InputStream in = resource.getInputStream()) {
                List<BlogPost> posts = objectMapper.readValue(in, new TypeReference<>() {});
                for (BlogPost p : posts) {
                    BlogPostEntity entity = new BlogPostEntity();
                    entity.setTitle(p.getTitle());
                    entity.setSlug(p.getSlug());
                    entity.setAuthor(p.getAuthor());
                    entity.setAuthorRole(p.getAuthorRole());
                    entity.setDate(p.getDate() != null ? LocalDate.parse(p.getDate()) : LocalDate.now());
                    entity.setReadTime(p.getReadTime());
                    entity.setCategory(p.getCategory());
                    entity.setTags(p.getTags());
                    entity.setSummary(p.getSummary());
                    entity.setContent(p.getContent());
                    entity.setLikes(p.getLikes());
                    entity.setFeatured(p.isFeatured());
                    repository.save(entity);
                }
            }
        } catch (Exception e) {
            System.err.println("DataSeeder: failed to seed posts — " + e.getMessage());
        }
    }
}
