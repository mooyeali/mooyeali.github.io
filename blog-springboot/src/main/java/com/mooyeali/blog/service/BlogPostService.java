package com.mooyeali.blog.service;

import com.mooyeali.blog.model.BlogPost;
import com.mooyeali.blog.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlogPostService {
    
    @Autowired
    private BlogPostRepository blogPostRepository;
    
    public Page<BlogPost> getAllPublishedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blogPostRepository.findByIsPublishedTrueOrderByCreatedAtDesc(pageable);
    }
    
    public Optional<BlogPost> getPostById(Long id) {
        return blogPostRepository.findById(id);
    }
    
    public BlogPost getPostByIdAndIncrementView(Long id) {
        Optional<BlogPost> postOpt = blogPostRepository.findById(id);
        if (postOpt.isPresent()) {
            blogPostRepository.incrementViewCount(id);
            return postOpt.get();
        }
        return null;
    }
    
    public Page<BlogPost> searchPosts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blogPostRepository.findByIsPublishedTrueAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
                keyword, pageable);
    }
    
    public List<BlogPost> getRecentPosts() {
        return blogPostRepository.findTop5ByIsPublishedTrueOrderByCreatedAtDesc();
    }
    
    public List<BlogPost> getPopularPosts() {
        return blogPostRepository.findTop5ByIsPublishedTrueOrderByViewCountDesc();
    }
    
    public BlogPost savePost(BlogPost post) {
        return blogPostRepository.save(post);
    }
    
    public void deletePost(Long id) {
        blogPostRepository.deleteById(id);
    }
    
    public List<BlogPost> getAllPosts() {
        return blogPostRepository.findAll();
    }
}