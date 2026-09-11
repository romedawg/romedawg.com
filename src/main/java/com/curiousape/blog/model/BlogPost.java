package com.curiousape.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogPost {
    private String id;
    private String title;
    private String slug;
    private String author;
    private String authorRole;
    private String date;
    private String readTime;
    private String category;
    private List<String> tags = new ArrayList<>();
    private String summary;
    private String content;
    private int likes;
    private boolean featured;

    public BlogPost() {}

    public BlogPost(String id, String title, String slug, String author, String authorRole,
                    String date, String readTime, String category, List<String> tags,
                    String summary, String content, int likes, boolean featured) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.author = author;
        this.authorRole = authorRole;
        this.date = date;
        this.readTime = readTime;
        this.category = category;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.summary = summary;
        this.content = content;
        this.likes = likes;
        this.featured = featured;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getAuthorRole() { return authorRole; }
    public void setAuthorRole(String authorRole) { this.authorRole = authorRole; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getReadTime() { return readTime; }
    public void setReadTime(String readTime) { this.readTime = readTime; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags != null ? tags : new ArrayList<>(); }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
}
