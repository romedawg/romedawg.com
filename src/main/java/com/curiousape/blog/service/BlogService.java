package com.curiousape.blog.service;

import com.curiousape.blog.dto.CreatePostRequest;
import com.curiousape.blog.model.BlogPost;
import com.curiousape.blog.model.BlogPostEntity;
import com.curiousape.blog.repository.BlogPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogPostRepository repository;

    public BlogService(BlogPostRepository repository) {
        this.repository = repository;
    }

    public List<BlogPost> getPosts(String category, String tag, String search) {
        return repository.findAllByOrderByDateDesc().stream()
                .filter(p -> {
                    if (category != null && !category.isBlank() && !category.equalsIgnoreCase("all")) {
                        if (p.getCategory() == null || !p.getCategory().equalsIgnoreCase(category)) {
                            return false;
                        }
                    }
                    if (tag != null && !tag.isBlank()) {
                        if (p.getTags() == null || p.getTags().stream().noneMatch(t -> t.equalsIgnoreCase(tag))) {
                            return false;
                        }
                    }
                    if (search != null && !search.isBlank()) {
                        String q = search.toLowerCase();
                        boolean matchTitle = p.getTitle() != null && p.getTitle().toLowerCase().contains(q);
                        boolean matchSummary = p.getSummary() != null && p.getSummary().toLowerCase().contains(q);
                        boolean matchContent = p.getContent() != null && p.getContent().toLowerCase().contains(q);
                        boolean matchTags = p.getTags() != null && p.getTags().stream().anyMatch(t -> t.toLowerCase().contains(q));
                        return matchTitle || matchSummary || matchContent || matchTags;
                    }
                    return true;
                })
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<BlogPost> getPostById(String idOrSlug) {
        if (idOrSlug == null) return Optional.empty();
        try {
            long id = Long.parseLong(idOrSlug);
            Optional<BlogPostEntity> byId = repository.findById(id);
            if (byId.isPresent()) return byId.map(this::toDto);
        } catch (NumberFormatException ignored) {}
        return repository.findBySlug(idOrSlug).map(this::toDto);
    }

    @Transactional
    public BlogPost createPost(CreatePostRequest request) {
        String rawTitle = request.getTitle() != null ? request.getTitle().trim() : "Untitled";
        String content = request.getContent() != null ? request.getContent() : "";
        String summary = request.getSummary();
        if (summary == null || summary.isBlank()) {
            summary = content.length() > 140 ? content.substring(0, 137) + "..." : content;
        }

        List<String> tags = request.getTags() != null && !request.getTags().isEmpty()
                ? new java.util.ArrayList<>(request.getTags())
                : new java.util.ArrayList<>(java.util.List.of("General"));

        String baseSlug = rawTitle.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");

        boolean isFeat = request.isFeatured() != null && request.isFeatured();
        if (isFeat) {
            repository.findAll().forEach(p -> {
                if (p.isFeatured()) {
                    p.setFeatured(false);
                    repository.save(p);
                }
            });
        }

        BlogPostEntity entity = new BlogPostEntity();
        entity.setTitle(rawTitle);
        entity.setSlug(baseSlug.isBlank() ? "post-" + System.currentTimeMillis() : baseSlug + "-" + System.currentTimeMillis());
        entity.setAuthor(request.getAuthor() != null && !request.getAuthor().isBlank() ? request.getAuthor() : "Roman");
        entity.setAuthorRole(request.getAuthorRole() != null && !request.getAuthorRole().isBlank() ? request.getAuthorRole() : "Contributor");
        entity.setDate(LocalDate.now());
        entity.setReadTime(calculateReadTime(content));
        entity.setCategory(request.getCategory() != null && !request.getCategory().isBlank() ? request.getCategory() : "General");
        entity.setTags(tags);
        entity.setSummary(summary);
        entity.setContent(content);
        entity.setLikes(0);
        entity.setFeatured(isFeat);

        BlogPostEntity saved = repository.save(entity);
        saved.setSlug(baseSlug.isBlank() ? "post-" + saved.getId() : baseSlug + "-" + saved.getId());
        return toDto(saved);
    }

    @Transactional
    public Optional<BlogPost> updatePost(String idStr, CreatePostRequest request) {
        try {
            long id = Long.parseLong(idStr);
            return repository.findById(id).map(entity -> {
                String rawTitle = (request.getTitle() != null && !request.getTitle().isBlank())
                        ? request.getTitle().trim() : entity.getTitle();
                String content = request.getContent() != null ? request.getContent() : entity.getContent();

                String summary = request.getSummary();
                if (summary == null || summary.isBlank()) {
                    summary = content.length() > 140 ? content.substring(0, 137) + "..." : content;
                }
                List<String> tags = (request.getTags() != null && !request.getTags().isEmpty())
                        ? new java.util.ArrayList<>(request.getTags()) : entity.getTags();

                entity.setTitle(rawTitle);
                if (request.getAuthor() != null && !request.getAuthor().isBlank())
                    entity.setAuthor(request.getAuthor());
                if (request.getAuthorRole() != null && !request.getAuthorRole().isBlank())
                    entity.setAuthorRole(request.getAuthorRole());
                if (request.getCategory() != null && !request.getCategory().isBlank())
                    entity.setCategory(request.getCategory());
                if (request.getFeatured() != null) {
                    boolean isFeat = request.getFeatured();
                    if (isFeat) {
                        repository.findAll().forEach(p -> {
                            if (p.isFeatured() && p.getId() != entity.getId()) {
                                p.setFeatured(false);
                                repository.save(p);
                            }
                        });
                    }
                    entity.setFeatured(isFeat);
                }
                entity.setTags(tags);
                entity.setSummary(summary);
                entity.setContent(content);
                entity.setReadTime(calculateReadTime(content));
                String baseSlug = rawTitle.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
                entity.setSlug(baseSlug.isBlank() ? "post-" + entity.getId() : baseSlug + "-" + entity.getId());
                return toDto(entity);
            });
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Integer> likePost(String idOrSlug) {
        Optional<BlogPost> dto = getPostById(idOrSlug);
        if (dto.isEmpty()) return Optional.empty();

        long entityId = Long.parseLong(dto.get().getId());
        return repository.findById(entityId).map(entity -> {
            entity.setLikes(entity.getLikes() + 1);
            return repository.save(entity).getLikes();
        });
    }

    @Transactional
    public boolean deletePost(String idOrSlug) {
        if (idOrSlug == null || idOrSlug.isBlank()) return false;
        try {
            long id = Long.parseLong(idOrSlug);
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return true;
            }
        } catch (NumberFormatException ignored) {}
        Optional<BlogPostEntity> bySlug = repository.findBySlug(idOrSlug);
        if (bySlug.isPresent()) {
            repository.delete(bySlug.get());
            return true;
        }
        return false;
    }

    private BlogPost toDto(BlogPostEntity e) {
        return new BlogPost(
                String.valueOf(e.getId()),
                e.getTitle(),
                e.getSlug(),
                e.getAuthor(),
                e.getAuthorRole(),
                e.getDate() != null ? e.getDate().toString() : null,
                e.getReadTime(),
                e.getCategory(),
                e.getTags(),
                e.getSummary(),
                e.getContent(),
                e.getLikes(),
                e.isFeatured()
        );
    }

    private String calculateReadTime(String content) {
        if (content == null || content.isBlank()) return "1 min read";
        int words = content.trim().split("\\s+").length;
        return Math.max(1, (int) Math.ceil(words / 180.0)) + " min read";
    }
}
