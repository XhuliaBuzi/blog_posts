package com.blogposts.controller;

import com.blogposts.dto.Response;
import com.blogposts.service.PostsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class PostsController {
    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping(path = "/ping")
    public ResponseEntity<Map<String, Boolean>> getPing() {
        Map<String, Boolean> status = new HashMap<>();
        status.put("success", true);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping(path = "/posts")
    public ResponseEntity getPosts(@RequestParam(value = "tags") String tags,
                                   @RequestParam(required = false, value = "sortBy", defaultValue = "id") String sortBy,
                                   @RequestParam(required = false, value = "direction", defaultValue = "asc") String direction) {
        long start = System.currentTimeMillis();
        Response response = null;
        try {
            response = postsService.getPosts(tags, sortBy, direction);
        } catch (Exception e) {
            Map<String, String> status = new HashMap<>();
            status.put("Error", e.getMessage());
            return new ResponseEntity(status, HttpStatus.BAD_REQUEST);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return new ResponseEntity(response, HttpStatus.OK);
    }


}
