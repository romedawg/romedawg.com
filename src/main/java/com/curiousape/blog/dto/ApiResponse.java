package com.curiousape.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private Integer count;
    private T posts;
    private T post;
    private Integer likes;
    private String error;

    public ApiResponse() {}

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setPost(data);
        return response;
    }

    public static <T> ApiResponse<T> okList(T data, int count) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setPosts(data);
        response.setCount(count);
        return response;
    }

    public static <T> ApiResponse<T> okLikes(int likes) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setLikes(likes);
        return response;
    }

    public static <T> ApiResponse<T> fail(String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setError(error);
        return response;
    }

    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public T getPosts() { return posts; }
    public void setPosts(T posts) { this.posts = posts; }

    public T getPost() { return post; }
    public void setPost(T post) { this.post = post; }

    public Integer getLikes() { return likes; }
    public void setLikes(Integer likes) { this.likes = likes; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
