package com.curiousape.blog.controller;

import com.curiousape.blog.dto.ApiResponse;
import com.curiousape.blog.dto.CreatePostRequest;
import com.curiousape.blog.model.BlogPost;
import com.curiousape.blog.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class BlogApiController {

    private final BlogService blogService;

    public BlogApiController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BlogPost>>> getAllPosts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String search) {
        List<BlogPost> list = blogService.getPosts(category, tag, search);
        return ResponseEntity.ok(ApiResponse.okList(list, list.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogPost>> getPostById(@PathVariable String id) {
        Optional<BlogPost> post = blogService.getPostById(id);
        return post.map(blogPost -> ResponseEntity.ok(ApiResponse.ok(blogPost)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail("Post not found")));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BlogPost>> createPost(@RequestBody CreatePostRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank() ||
            request.getContent() == null || request.getContent().isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("Title and content are required"));
        }
        BlogPost created = blogService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(created));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> likePost(@PathVariable String id) {
        Optional<Integer> newLikes = blogService.likePost(id);
        return newLikes.map(likes -> ResponseEntity.ok(ApiResponse.<Void>okLikes(likes)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail("Post not found")));
    }
}
