package com.blogposts.service;

import com.blogposts.dto.PostsDTO;
import com.blogposts.dto.Response;
import com.blogposts.model.Author;
import com.blogposts.model.Posts;
import com.blogposts.model.Tags;
import com.blogposts.repository.PostsRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final Set<Posts> postsHashSet = new HashSet<>();
    private final Response responseApi = new Response();
    private List<PostsDTO> postsDTOS;

    public PostsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    @Cacheable("Posts")
    public Response getPosts(String tags, String sortBy, String direction) throws Exception {
        if (tags == null || tags.isEmpty()) {
            throw new Exception("Tags value shouldn't be empty. ");
        }
        List<String> stringsSortBy = Arrays.asList("id", "reads", "likes", "popularity");
        if (sortBy != null && !stringsSortBy.contains(sortBy.toLowerCase(Locale.ROOT))) {
            throw new Exception("Sortby value should not be with different value. You should put sortBy one of:“id”, “reads”, “likes”, “popularity” ");
        }
        List<String> stringsDirection = Arrays.asList("asc", "desc");
        if (sortBy != null && !stringsDirection.contains(direction.toLowerCase(Locale.ROOT))) {
            throw new Exception("Direction value should not be with different value. You should put Direction one of: “asc”, “desc”");
        }
        postsDTOS = new ArrayList<>();
        String[] splitTags = tags.split(",");
        readAPI(splitTags);
        searchPosts(splitTags);
        for (Posts posts : postsHashSet) {
            PostsDTO postsDTO = postsToDto(posts);
            if (!postsDTOS.contains(postsDTO)) {
                postsDTOS.add(postsToDto(posts));
            }
        }
        if (direction.toLowerCase(Locale.ROOT).equals("asc")) {
            sortListAsc(sortBy);
        } else if (direction.toLowerCase(Locale.ROOT).equals("desc")) {
            sortListDesc(sortBy);
        }
        responseApi.setPosts(postsDTOS);
        return responseApi;
    }

    private void sortListAsc(String sortBy) {
        if (sortBy.toLowerCase(Locale.ROOT).equals("id"))
            postsDTOS.sort(Comparator.comparing(PostsDTO::getId));
        else if (sortBy.toLowerCase(Locale.ROOT).equals("reads"))
            postsDTOS.sort(Comparator.comparing(PostsDTO::getReads));
        else if (sortBy.toLowerCase(Locale.ROOT).equals("likes"))
            postsDTOS.sort(Comparator.comparing(PostsDTO::getLikes));
        else if (sortBy.toLowerCase(Locale.ROOT).equals("popularity"))
            postsDTOS.sort(Comparator.comparing(PostsDTO::getPopularity));
    }

    private void sortListDesc(String sortBy) {
        if (sortBy.toLowerCase(Locale.ROOT).equals("id"))
            postsDTOS.sort(Comparator.comparing(PostsDTO::getId).reversed());
        else if (sortBy.toLowerCase(Locale.ROOT).equals("reads"))
            postsDTOS.sort(Comparator.comparing(PostsDTO::getReads).reversed());
        else if (sortBy.toLowerCase(Locale.ROOT).equals("likes"))
            postsDTOS.sort(Comparator.comparing(PostsDTO::getLikes).reversed());
        else if (sortBy.toLowerCase(Locale.ROOT).equals("popularity"))
            postsDTOS.sort(Comparator.comparing(PostsDTO::getPopularity).reversed());
    }

    private void searchPosts(String[] tags) {
        for (String tag : tags) {
            for (int i = 0; i < postsRepository.findAll().size(); i++) {
                Posts posts = postsRepository.findAll().get(i);
                for (int j = 0; j < posts.getTags().size(); j++) {
                    if (posts.getTags().get(j).getName().equals(tag)) {
                        postsHashSet.add(posts);
                    }
                }
            }
        }
    }

    public void readAPI(String[] splitTags) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.hatchways.io/assessment/blog/posts";
        for (String tag : splitTags) {
            Response response = restTemplate.getForEntity(url + "?tag=" + tag, Response.class).getBody();
            for (PostsDTO
                    dto : response.getPosts()) {
                postsRepository.save(dtoToPosts(dto));
            }
        }
    }

    private Posts dtoToPosts(PostsDTO dto) {
        Posts posts = new Posts();
        posts.setId(dto.getId());
        posts.setLikes(dto.getLikes());
        posts.setPopularity(dto.getPopularity());
        posts.setReads(dto.getReads());
        List<Tags> tags = new ArrayList<>();
        for (String tag : dto.getTags()) {
            Tags tags1 = new Tags();
            tags1.setName(tag);
            tags.add(tags1);
        }
        posts.setTags(tags);
        Author author = new Author();
        author.setId(dto.getAuthorId());
        author.setName(dto.getAuthor());
        posts.setAuthor(author);
        return posts;
    }

    private PostsDTO postsToDto(Posts posts) {
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setId(posts.getId());
        postsDTO.setAuthorId(posts.getAuthor().getId());
        postsDTO.setAuthor(posts.getAuthor().getName());
        postsDTO.setLikes(posts.getLikes());
        postsDTO.setPopularity(posts.getPopularity());
        postsDTO.setReads(posts.getReads());
        List<String> tagsString = new ArrayList<>();
        for (Tags tag : posts.getTags()) {
            String string = tag.getName();
            tagsString.add(string);
        }
        postsDTO.setTags(tagsString);
        return postsDTO;
    }
}
