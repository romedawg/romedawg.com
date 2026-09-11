package com.curiousape.blog.service;

import com.curiousape.blog.dto.CreatePostRequest;
import com.curiousape.blog.model.BlogPost;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final List<BlogPost> posts = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("data/posts.json");
            if (resource.exists()) {
                try (InputStream inputStream = resource.getInputStream()) {
                    List<BlogPost> loaded = objectMapper.readValue(inputStream, new TypeReference<List<BlogPost>>() {});
                    if (loaded != null) {
                        posts.addAll(loaded);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load initial posts.json: " + e.getMessage());
        }
    }

    public List<BlogPost> getPosts(String category, String tag, String search) {
        return posts.stream()
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
                .sorted((a, b) -> {
                    String dateA = a.getDate() != null ? a.getDate() : "";
                    String dateB = b.getDate() != null ? b.getDate() : "";
                    return dateB.compareTo(dateA);
                })
                .collect(Collectors.toList());
    }

    public Optional<BlogPost> getPostById(String idOrSlug) {
        if (idOrSlug == null) return Optional.empty();
        return posts.stream()
                .filter(p -> idOrSlug.equals(p.getId()) || idOrSlug.equalsIgnoreCase(p.getSlug()))
                .findFirst();
    }

    public synchronized BlogPost createPost(CreatePostRequest request) {
        int maxId = posts.stream()
                .mapToInt(p -> {
                    try {
                        return Integer.parseInt(p.getId());
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);

        String id = String.valueOf(maxId + 1);
        String rawTitle = request.getTitle() != null ? request.getTitle().trim() : "Untitled";
        String slug = rawTitle.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "") + "-" + id;
        String dateStr = LocalDate.now().toString();

        List<String> tags = request.getTags();
        if (tags == null || tags.isEmpty()) {
            tags = List.of("General");
        }

        String content = request.getContent() != null ? request.getContent() : "";
        String summary = request.getSummary();
        if (summary == null || summary.isBlank()) {
            summary = content.length() > 140 ? content.substring(0, 137) + "..." : content;
        }

        BlogPost post = new BlogPost(
                id,
                rawTitle,
                slug,
                request.getAuthor() != null && !request.getAuthor().isBlank() ? request.getAuthor() : "Barnaby the Monkey",
                request.getAuthorRole() != null && !request.getAuthorRole().isBlank() ? request.getAuthorRole() : "Tree Climber & Dev",
                dateStr,
                calculateReadTime(content),
                request.getCategory() != null && !request.getCategory().isBlank() ? request.getCategory() : "General",
                tags,
                summary,
                content,
                0,
                false
        );

        posts.add(0, post);
        return post;
    }

    public synchronized Optional<Integer> likePost(String idOrSlug) {
        Optional<BlogPost> optionalPost = getPostById(idOrSlug);
        if (optionalPost.isPresent()) {
            BlogPost post = optionalPost.get();
            post.setLikes(post.getLikes() + 1);
            return Optional.of(post.getLikes());
        }
        return Optional.empty();
    }

    private String calculateReadTime(String content) {
        if (content == null || content.isBlank()) {
            return "1 min read";
        }
        int wordCount = content.trim().split("\\s+").length;
        int minutes = Math.max(1, (int) Math.ceil(wordCount / 180.0));
        return minutes + " min read";
    }
}
