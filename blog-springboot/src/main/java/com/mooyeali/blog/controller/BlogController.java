package com.mooyeali.blog.controller;

import com.mooyeali.blog.model.BlogPost;
import com.mooyeali.blog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BlogController {
    
    @Autowired
    private BlogPostService blogPostService;
    
    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size) {
        Page<BlogPost> posts = blogPostService.getAllPublishedPosts(page, size);
        List<BlogPost> recentPosts = blogPostService.getRecentPosts();
        List<BlogPost> popularPosts = blogPostService.getPopularPosts();
        
        model.addAttribute("posts", posts);
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("popularPosts", popularPosts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("totalElements", posts.getTotalElements());
        
        return "index";
    }
    
    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        BlogPost post = blogPostService.getPostByIdAndIncrementView(id);
        if (post == null) {
            return "redirect:/";
        }
        
        List<BlogPost> recentPosts = blogPostService.getRecentPosts();
        
        model.addAttribute("post", post);
        model.addAttribute("recentPosts", recentPosts);
        
        return "post";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam String keyword,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size,
                        Model model) {
        Page<BlogPost> posts = blogPostService.searchPosts(keyword, page, size);
        List<BlogPost> recentPosts = blogPostService.getRecentPosts();
        
        model.addAttribute("posts", posts);
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("totalElements", posts.getTotalElements());
        
        return "search";
    }
    
    @GetMapping("/admin")
    public String admin(Model model) {
        List<BlogPost> allPosts = blogPostService.getAllPosts();
        model.addAttribute("posts", allPosts);
        return "admin";
    }
    
    @GetMapping("/admin/new")
    public String newPost(Model model) {
        model.addAttribute("post", new BlogPost());
        return "admin-edit";
    }
    
    @GetMapping("/admin/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        BlogPost post = blogPostService.getPostById(id).orElse(null);
        if (post == null) {
            return "redirect:/admin";
        }
        model.addAttribute("post", post);
        return "admin-edit";
    }
    
    @PostMapping("/admin/save")
    public String savePost(@ModelAttribute BlogPost post) {
        blogPostService.savePost(post);
        return "redirect:/admin";
    }
    
    @PostMapping("/admin/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        blogPostService.deletePost(id);
        return "redirect:/admin";
    }
}