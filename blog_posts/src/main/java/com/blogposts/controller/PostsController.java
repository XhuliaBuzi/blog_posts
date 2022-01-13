package com.blogposts.model;

import com.blogposts.model.Posts;
import com.blogposts.service.PostsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class PostsController {
    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping(path = "/ping")
    public ResponseEntity getPing() {
        Map<String, Boolean> status = new HashMap<>();
        status.put("success", true);
        return new ResponseEntity(status, HttpStatus.OK);
    }

    @GetMapping
    public List<Posts> getPosts(@RequestParam(required = false, value = "sort", defaultValue = "author") String sort) {
        return postsService.getPosts();
    }


}
