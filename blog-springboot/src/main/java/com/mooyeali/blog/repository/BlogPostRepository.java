package com.mooyeali.blog.repository;

import com.mooyeali.blog.model.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    
    // Find all published posts ordered by creation date descending
    Page<BlogPost> findByIsPublishedTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // Find posts by title containing keyword (case insensitive)
    Page<BlogPost> findByIsPublishedTrueAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
            String keyword, Pageable pageable);
    
    // Find recent posts (limit)
    List<BlogPost> findTop5ByIsPublishedTrueOrderByCreatedAtDesc();
    
    // Increment view count
    @Modifying
    @Query("UPDATE BlogPost b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    // Find most viewed posts
    List<BlogPost> findTop5ByIsPublishedTrueOrderByViewCountDesc();
}