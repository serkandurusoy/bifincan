/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.model.blog.BlogComment;
import com.dna.bifincan.model.blog.BlogCommentRating;
import com.dna.bifincan.model.blog.BlogPost;
import com.dna.bifincan.model.products.Image;
import com.dna.bifincan.model.type.PointTransactionType;
import com.dna.bifincan.model.type.VoteType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.repository.blog.BlogCommentRatingRepository;
import com.dna.bifincan.repository.blog.BlogCommentRepository;
import com.dna.bifincan.repository.blog.BlogPostRepository;
import com.dna.bifincan.repository.products.ImageRepository;

@Service
@Named("blogService")
public class BlogService {

    @Inject
    private BlogPostRepository postRepository;
    @Inject
    private BlogCommentRepository commentRepository;
    @Inject
    private BlogCommentRatingRepository blogCommentRatingRepository;
    @Inject
    private ImageRepository imageRepository;
    @Inject
    private PointTransactionAccountingService transactionService;

    @Transactional
    public boolean saveBlogPost(BlogPost post, Image imageLarge, Image imageSmall) {
        if (post.getId() == null || post.getId() == 0) {
            if (imageLarge != null && imageLarge.getContents() != null && imageLarge.getContents().length > 0) {
                imageRepository.save(imageLarge);
                post.setImageLarge(imageLarge.getId());
            }

            if (imageSmall != null && imageSmall.getContents() != null && imageSmall.getContents().length > 0) {
                imageRepository.save(imageSmall);
                post.setImageSmall(imageSmall.getId());
            }

        } else {
            /*
             * TODO: this is just a workaround in order to preserve the current
             * logo from empty logo form submissions; However a flag should be
             * added to indicate that we really want to reset the logo.. Usign
             * the DTO pattern may be better here..
             */
            if (imageLarge != null && imageLarge.getContents().length > 0) {
                if (post.getImageLarge() != null) {
                    Image curImageLarge = imageRepository.findOne(post.getImageLarge());
                    if (curImageLarge != null) {
                        imageRepository.delete(curImageLarge);
                    }
                }
                imageRepository.save(imageLarge);
                post.setImageLarge(imageLarge.getId());
            }

            if (imageSmall != null && imageSmall.getContents().length > 0) {
                if (post.getImageSmall() != null) {
                    Image curImageSmall = imageRepository.findOne(post.getImageSmall());
                    if (curImageSmall != null) {
                        imageRepository.delete(curImageSmall);
                    }
                }
                imageRepository.save(imageSmall);
                post.setImageSmall(imageSmall.getId());
            }
        }

        postRepository.save(post); // no checks at the moment; just save it


        return true;

    }

    @Transactional
    public Object[] updateBlogPostAfterAddingUserComment(User user, Long blogPostId, BlogComment blogComment) {
        Object[] vals = new Object[2];
        
        BlogPost oPost = postRepository.findOne(blogPostId);
        if(oPost != null) {
        	oPost.addComment(blogComment);
        	vals[0] = postRepository.save(oPost); 
        	vals[1] = transactionService.addConfigurationPoints(user, PointTransactionType.BLOG_COMMENT_POINTS);
        }

        return vals;
    }

    @Transactional
    public boolean saveBlogComment(BlogComment comment) {
        commentRepository.save(comment);  // no checks at the moment; just save it
        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Long countByCommentAndVoter(BlogComment blogComment, User user) {
    	return blogCommentRatingRepository.countByCommentAndVoter(blogComment.getId(), user); 
    }
       
    @Transactional
    public Integer saveBlogCommentRating(Long commentId, BlogCommentRating rating) {
    	BlogComment comment = commentRepository.findOne(commentId);

    	int numOfLikes = comment.getNumberOfLikes();
    	int numOfDisLikes = comment.getNumberOfDisLikes();
    	
    	if(rating.getUser().getId().intValue() != comment.getUser().getId().intValue()) {

    		if(countByCommentAndVoter(comment, rating.getUser()) == 0) {
    			rating.setBlogComment(comment);
	    		blogCommentRatingRepository.save(rating);
	    		
	    		comment.getRatings().add(rating); 

		    	if(rating.getType() == VoteType.LIKE) {
		    		comment.setNumberOfLikes(++numOfLikes);
		    		commentRepository.save(comment);
		    		
		    		return transactionService.addPoints(comment.getUser(), PointTransactionType.VOTING_POINTS , 1);
		    	} else if(rating.getType() == VoteType.DISLIKE) {
		    		comment.setNumberOfDisLikes(++numOfDisLikes);
		    		commentRepository.save(comment);
		    	} else {
		    		throw new RuntimeException("Not supported");
		    	}
		    	
    		}	
    	}	
    	
		return null;
    }
    
    public Page<BlogPost> findPosts(int first, int pageSize) {
        Sort defaultSort = new Sort(new Sort.Order(Sort.Direction.DESC, "postedAt"));
        Pageable cond = new PageRequest(first, pageSize, defaultSort);
        return postRepository.findAll(cond);
    }

    public BlogPost findPost(Long valueOf) {
        return postRepository.findOne(valueOf);
    }

    public boolean deleteBlogPost(BlogPost post) {
        if (post.getImageLarge() != null) {
            Image imgLarge = imageRepository.findOne(post.getImageLarge());
            if (imgLarge != null) {
                imageRepository.delete(imgLarge);
            }
        }

        if (post.getImageSmall() != null) {
            Image imgSmall = imageRepository.findOne(post.getImageSmall());
            if (imgSmall != null) {
                imageRepository.delete(imgSmall);
            }
        }

        // get a fresh copy of the entity
        BlogPost tempPost = this.postRepository.findOne(post.getId());
        this.postRepository.delete(tempPost);  // no checks at the moment; just delete it

        return true;
    }

    public Page<BlogComment> findComments(int first, int pageSize) {
        Sort defaultSort = new Sort(new Sort.Order(Sort.Direction.DESC, "commentedAt"));
        Pageable cond = new PageRequest(first, pageSize, defaultSort);
        return commentRepository.findAll(cond);
    }

    public BlogPost findBySlug(String slug) {
        List<BlogPost> posts = postRepository.findBySlug(slug);
        if (!posts.isEmpty()) {
            return posts.get(0);
        }
        return null;
    }

    public BlogComment findComment(Long valueOf) {
        return commentRepository.findOne(valueOf);
    }

    public List<BlogPost> findAllPosts() {
        Sort defaultSort = new Sort(new Sort.Order(Sort.Direction.DESC, "postedAt"));
        return postRepository.findAll(defaultSort);
    }

    public BlogPost findPreBlogPost(BlogPost currentPost) {
        return postRepository.findPrePost(currentPost.getPostedAt());
    }

    public BlogPost findNextBlogPost(BlogPost currentPost) {
        return postRepository.findNextPost(currentPost.getPostedAt());
    }

    public List<BlogPost> findMostPopPosts() {
        return postRepository.findMostPopPosts();
    }

    public List<BlogPost> findRecentPosts() {
        return postRepository.findRecentPosts(5);
    }

    public List<BlogPost> findLastPosts(int maxReuslt) {
        return postRepository.findRecentPosts(25);
    }

    public List<BlogComment> findRecentComments() {
        return commentRepository.findRecentComments();
    }

    public Image findBlogPostImage(Long imageId) {
        return imageRepository.findOne(imageId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Long findBlogPostIdBySlug(String slug) {
        // Find the blog post id
        return postRepository.findBlogPostIdBySlug(slug);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BlogPost findBlogPostById(Long blogPostId) {
        return postRepository.findOne(blogPostId);
    }
    
    
    public long countCommentByUser(User _user){
        return commentRepository.countByUser(_user);
    }
    
    public long noOfBlogComments(){
        return commentRepository.count();
    }
    
    public long countCommentsByPostAndUser(BlogPost post, User user){
        return commentRepository.countCommentsByPostAndUser(post, user);
    }    
    
    public long countVotableCommentsByPost(BlogPost post){
        return commentRepository.countVotableCommentsByPost(post);
    }    
    
    public long countCommentsLikesByPostAndUser(BlogPost post, User user){
        return commentRepository.countCommentsByPostAndUserAndVoteType(post, user, VoteType.LIKE);
    } 
    
    public long countCommentsDislikesByPostAndUser(BlogPost post, User user){
        return commentRepository.countCommentsByPostAndUserAndVoteType(post, user, VoteType.DISLIKE);
    } 

    public long countLikesReceivedForUserAtPost(BlogPost post, User user){
        if (countCommentsByPostAndUser(post, user) > 0) {
            return commentRepository.countLikesReceivedForUserAtPost(post, user);
        }
        return 0;
    } 

    public long countDisLikesReceivedForUserAtPost(BlogPost post, User user){
        if (countCommentsByPostAndUser(post, user) > 0) {
            return commentRepository.countDisLikesReceivedForUserAtPost(post, user);
        }
        return 0;
    } 

}
