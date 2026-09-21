package com.blogposts.controller;

import com.blogposts.dto.Response;
import com.blogposts.service.PostsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class PostsController {

    private static final Logger log = LoggerFactory.getLogger(PostsController.class);

    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping(path = "/ping")
    public ResponseEntity<Map<String, Boolean>> getPing() {
        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping(path = "/posts")
    public ResponseEntity<Response> getPosts(@RequestParam(value = "tags") String tags,
                                              @RequestParam(required = false, value = "sortBy", defaultValue = "id") String sortBy,
                                              @RequestParam(required = false, value = "direction", defaultValue = "asc") String direction) {
        long start = System.currentTimeMillis();
        Response response = postsService.getPosts(tags, sortBy, direction);
        log.debug("getPosts resolved in {} ms", System.currentTimeMillis() - start);
        return ResponseEntity.ok(response);
    }

}
