package com.blogposts.service;

import com.blogposts.dto.PostsDTO;
import com.blogposts.dto.Response;
import com.blogposts.model.Author;
import com.blogposts.model.Posts;
import com.blogposts.model.Tags;
import com.blogposts.repository.PostsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostsService {

    private static final Logger log = LoggerFactory.getLogger(PostsService.class);
    private static final String HATCHWAYS_POSTS_URL = "https://api.hatchways.io/assessment/blog/posts";
    private static final Set<String> VALID_DIRECTIONS = Set.of("asc", "desc");
    private static final Map<String, Comparator<PostsDTO>> SORT_COMPARATORS = Map.of(
            "id", Comparator.comparing(PostsDTO::getId),
            "reads", Comparator.comparing(PostsDTO::getReads),
            "likes", Comparator.comparing(PostsDTO::getLikes),
            "popularity", Comparator.comparing(PostsDTO::getPopularity)
    );

    private final PostsRepository postsRepository;
    private final RestTemplate restTemplate;

    public PostsService(PostsRepository postsRepository, RestTemplate restTemplate) {
        this.postsRepository = postsRepository;
        this.restTemplate = restTemplate;
    }

    @Cacheable("Posts")
    public Response getPosts(String tags, String sortBy, String direction) {
        if (tags == null || tags.isBlank()) {
            throw new IllegalArgumentException("Tags value shouldn't be empty.");
        }
        String normalizedSortBy = sortBy == null ? "id" : sortBy.toLowerCase(Locale.ROOT);
        Comparator<PostsDTO> comparator = SORT_COMPARATORS.get(normalizedSortBy);
        if (comparator == null) {
            throw new IllegalArgumentException(
                    "sortBy value should be one of: " + SORT_COMPARATORS.keySet());
        }
        String normalizedDirection = direction == null ? "asc" : direction.toLowerCase(Locale.ROOT);
        if (!VALID_DIRECTIONS.contains(normalizedDirection)) {
            throw new IllegalArgumentException("direction value should be one of: " + VALID_DIRECTIONS);
        }

        String[] splitTags = tags.split(",");
        fetchAndStorePosts(splitTags);

        Set<String> requestedTags = new HashSet<>(Arrays.asList(splitTags));
        List<PostsDTO> postsDTOs = postsRepository.findAll().stream()
                .filter(posts -> posts.getTags().stream().map(Tags::getName).anyMatch(requestedTags::contains))
                .map(this::postsToDto)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        postsDTOs.sort("desc".equals(normalizedDirection) ? comparator.reversed() : comparator);

        return new Response(postsDTOs);
    }

    private void fetchAndStorePosts(String[] tags) {
        for (String tag : tags) {
            Response response = restTemplate.getForEntity(HATCHWAYS_POSTS_URL + "?tag=" + tag, Response.class).getBody();
            if (response == null || response.getPosts() == null) {
                log.warn("No posts returned from Hatchways API for tag '{}'", tag);
                continue;
            }
            for (PostsDTO dto : response.getPosts()) {
                postsRepository.save(dtoToPosts(dto));
            }
        }
    }

    private Posts dtoToPosts(PostsDTO dto) {
        List<Tags> tags = dto.getTags().stream()
                .map(name -> new Tags(null, name))
                .collect(Collectors.toList());
        Author author = new Author(dto.getAuthorId(), dto.getAuthor());
        return new Posts(dto.getId(), dto.getLikes(), dto.getPopularity(), dto.getReads(), author, tags);
    }

    private PostsDTO postsToDto(Posts posts) {
        List<String> tagsString = posts.getTags().stream()
                .map(Tags::getName)
                .collect(Collectors.toList());
        return new PostsDTO(
                posts.getAuthor().getName(),
                posts.getAuthor().getId(),
                posts.getId(),
                posts.getLikes(),
                posts.getPopularity(),
                posts.getReads(),
                tagsString);
    }
}
