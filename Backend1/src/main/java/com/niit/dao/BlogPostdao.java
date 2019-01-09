package com.niit.dao;

import java.util.List;

import com.niit.model.BlogPost;

public interface BlogPostdao {
void addBlogPost(BlogPost blogPost);
List<BlogPost> getBlogsWaitingForApproval();
List<BlogPost> getBlogsApproved();
BlogPost getBlog(int blogId);
void approveBlogPost(int blogPostId);
void rejectBlogPost(int blogPostId);
void updateBlogPost(BlogPost blogPost);
}