package com.blogposts.service;

import com.blogposts.dto.PostsDTO;
import com.blogposts.dto.Response;
import com.blogposts.model.Author;
import com.blogposts.model.Posts;
import com.blogposts.model.Tags;
import com.blogposts.repository.PostsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostsServiceTest {

    @Mock
    private PostsRepository postsRepository;
    @Mock
    private RestTemplate restTemplate;

    private PostsService postsService;

    @BeforeEach
    void setUp() {
        postsService = new PostsService(postsRepository, restTemplate);
        lenient().when(restTemplate.getForEntity(anyString(), eq(Response.class)))
                .thenReturn(ResponseEntity.ok(new Response(new ArrayList<>())));
    }

    @Test
    void getPostsReturnsOnlyPostsMatchingRequestedTags() {
        Author author = new Author(1, "Xhulia");
        Posts techPost = new Posts(1, 10, 0.5f, 100, author, List.of(new Tags(1, "tech")));
        Posts otherPost = new Posts(2, 5, 0.2f, 50, author, List.of(new Tags(2, "design")));
        when(postsRepository.findAll()).thenReturn(List.of(techPost, otherPost));

        Response response = postsService.getPosts("tech", "id", "asc");

        assertNotNull(response);
        assertEquals(1, response.getPosts().size());
        assertEquals(1, response.getPosts().get(0).getId());
    }

    @Test
    void getPostsSortsDescendingByRequestedField() {
        Author author = new Author(1, "Xhulia");
        Tags tag = new Tags(1, "tech");
        Posts lowLikes = new Posts(1, 1, 0.1f, 10, author, List.of(tag));
        Posts highLikes = new Posts(2, 9, 0.1f, 10, author, List.of(tag));
        when(postsRepository.findAll()).thenReturn(List.of(lowLikes, highLikes));

        Response response = postsService.getPosts("tech", "likes", "desc");

        List<Integer> ids = response.getPosts().stream().map(PostsDTO::getId).toList();
        assertEquals(List.of(2, 1), ids);
    }

    @Test
    void getPostsRejectsBlankTags() {
        assertThrows(IllegalArgumentException.class, () -> postsService.getPosts("", "id", "asc"));
    }

    @Test
    void getPostsRejectsInvalidSortBy() {
        assertThrows(IllegalArgumentException.class, () -> postsService.getPosts("tech", "invalid", "asc"));
    }

    @Test
    void getPostsRejectsInvalidDirection() {
        assertThrows(IllegalArgumentException.class, () -> postsService.getPosts("tech", "id", "sideways"));
    }
}
