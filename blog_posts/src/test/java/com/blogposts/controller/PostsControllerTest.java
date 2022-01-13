package com.blogposts.controller;

import com.blogposts.dto.Response;
import com.blogposts.service.PostsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
public class PostsControllerTest {
    @Mock
    PostsService postsService;
    private PostsController postsController;

    @BeforeEach
    void setUp() {
        postsController = new PostsController(postsService);
    }

    @Test
    void getPingTest() {
        Assertions.assertNotNull(postsController.getPing());
    }

    @Test
    void getPostsTest() throws Exception {
        Mockito.when(postsService.getPosts("tech","reads","desc")).thenReturn(new Response());

        assertNotNull(postsController.getPosts("tech","reads","desc"));
    }
}
