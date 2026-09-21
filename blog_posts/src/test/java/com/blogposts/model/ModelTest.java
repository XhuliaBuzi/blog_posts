package com.blogposts.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ModelTest {
    private Author author;
    private Tags tags;
    private Posts posts;

    @BeforeEach
    void setUp() {
        author = new Author(1, "Xhulia");
        tags = new Tags(1, "tech");
        List<Tags> list = new ArrayList<>();
        list.add(tags);
        posts = new Posts(1, 1, (float) 0.1, 1, author, list);
    }

    @Test
    void AuthotTest() {
        assertNotNull(author.getId());
        assertNotNull(author.getName());
    }

    @Test
    void TagsTest() {
        assertNotNull(tags.getId());
        assertNotNull(tags.getName());
    }

    @Test
    void PostsTest() {
        assertNotNull(posts.getId());
        assertNotNull(posts.getReads());
        assertNotNull(posts.getPopularity());
        assertNotNull(posts.getAuthor());
        assertNotNull(posts.getTags());
        assertNotNull(posts.getLikes());

    }
}
