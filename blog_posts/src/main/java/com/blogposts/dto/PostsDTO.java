package com.blogposts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsDTO {
    private String author;
    private Integer authorId;
    private Integer id;
    private Integer likes;
    private Float popularity;
    private Integer reads;
    private List<String> tags;
}