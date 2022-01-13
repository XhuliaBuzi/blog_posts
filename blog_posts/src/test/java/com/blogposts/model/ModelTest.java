package com.blogposts.model;

import com.blogposts.repository.PostsRepository;
import com.blogposts.service.PostsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AuthorTest {
    private Author author;

 @Test
    void AuthotTest(){
     author=new Author();
//     assertNotNull(author);
//     author=new Author(1,"Xhulia Buzi");
//     assertNotNull(author);
     author.setId(1);
     author.setName("Xhulia Buzi");
     assertNotNull(author.getId());
     assertNotNull(author.getName());
 }
}
