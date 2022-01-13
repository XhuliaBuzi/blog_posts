package com.blogposts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Posts {
    @Id
    private Integer id;
    private int likes;
    private float popularity;
    private int reads;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "posts_tags",
            joinColumns = {@JoinColumn(name = "posts_id")},
            inverseJoinColumns = {@JoinColumn(name = "tags_id")}
    )
    private List<Tags> tags;
}
