package com.blogposts.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PostsDtoTest {
    private PostsDTO postsDTO;

    @BeforeEach
    void setUp() {
        postsDTO = new PostsDTO("", 1, 1, 1, (float) 1, 1, new ArrayList<>());
    }

    @Test
    void PostsDTOTest() {
        assertNotNull(postsDTO.getId());
        assertNotNull(postsDTO.getAuthor());
        assertNotNull(postsDTO.getAuthorId());
        assertNotNull(postsDTO.getLikes());
        assertNotNull(postsDTO.getPopularity());
        assertNotNull(postsDTO.getReads());
        assertNotNull(postsDTO.getTags());

    }
}
