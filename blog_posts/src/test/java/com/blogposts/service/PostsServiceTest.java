package com.blogposts.service;

import com.blogposts.model.Tags;
import com.blogposts.repository.PostsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PostsServiceTest {
    @Mock
    PostsRepository postsRepository;
    private PostsService postsService;

    @BeforeEach
    void setUp() {
        postsService = new PostsService(postsRepository);
    }

    @Test
    void getPostsTest()throws Exception {
        assertNotNull(postsService.getPosts("tech", "id", "desc"));
        assertNotNull(postsService.getPosts("culture,design", "reads", "desc"));
        assertNotNull(postsService.getPosts("science,startups", "likes", "desc"));
        assertNotNull(postsService.getPosts("history,politics", "popularity", "desc"));
        assertNotNull(postsService.getPosts("health,tech", "id", "asc"));
        assertNotNull(postsService.getPosts("history,science", "reads", "asc"));
        assertNotNull(postsService.getPosts("design,tech", "likes", "asc"));
        assertNotNull(postsService.getPosts("history", "popularity", "asc"));
    }
}
