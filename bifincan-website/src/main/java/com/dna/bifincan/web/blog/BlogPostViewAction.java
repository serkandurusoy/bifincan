package com.dna.bifincan.web.blog;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;

import com.dna.bifincan.model.blog.BlogComment;
import com.dna.bifincan.model.blog.BlogCommentRating;
import com.dna.bifincan.model.blog.BlogPost;
import com.dna.bifincan.model.products.ProductComment;
import com.dna.bifincan.model.products.ProductCommentRating;
import com.dna.bifincan.model.type.VoteType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.BlogService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.StringUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;

@Named("blogPostViewAction")
@Scope(ScopeType.VIEW)
public class BlogPostViewAction implements Serializable {
	private static final long serialVersionUID = -2716826638091934090L;

	private static final Logger log = LoggerFactory.getLogger(BlogPostViewAction.class);

    @Inject
    private BlogService blogService;

//    @Inject
//    @Named("currentUser")        
//    User currentUser;
    String slug;

    BlogPost currentPost;

    BlogPost prePost = null;

    BlogPost nextPost = null;

    BlogComment newComment = new BlogComment();
    
    private boolean author;

    private Long votableCommentCount;

    @Inject
    private UserService userService;
    
    public BlogPost getNextPost() {
        return nextPost;
    }

    public void setNextPost(BlogPost nextPost) {
        this.nextPost = nextPost;
    }

    public BlogPost getPrePost() {
        return prePost;
    }

    public void setPrePost(BlogPost prePost) {
        this.prePost = prePost;
    }

    public BlogPost getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(BlogPost currentPost) {
        this.currentPost = currentPost;
    }

    public BlogComment getNewComment() {
        return newComment;
    }

    public void setNewComment(BlogComment newComment) {
        this.newComment = newComment;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void initPost() {
        if (log.isDebugEnabled()) {
            log.debug("initializing...");
        }
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (this.slug == null || this.slug.trim().length() == 0) {
                FacesUtils.sendHttpStatusCode(HttpStatus.NOT_FOUND, "slug is invalid.");
                return;
            }
            loadPost();

            if (this.currentPost == null) {
                FacesUtils.sendHttpStatusCode(HttpStatus.NOT_FOUND, "Blog post is not found.");
                return;
            }


            this.prePost = blogService.findPreBlogPost(this.currentPost);
            this.nextPost = blogService.findNextBlogPost(this.currentPost);
        }

        this.newComment = new BlogComment();
    }

    public void loadPost() {
    	User user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY);
    	
        if (log.isDebugEnabled()) {
            log.debug("loading post...@" + slug);
        }
        this.currentPost = blogService.findBySlug(slug);
        
        //System.out.println(">>> comments for " + this.currentPost.getId() + " is " + + blogService.countCommentsByPostAndUser(this.currentPost, user) + " , user = " + user.getId());
        setAuthor(blogService.countCommentsByPostAndUser(this.currentPost, user) > 0 ? true : false); 
        setVotableCommentCount(blogService.countVotableCommentsByPost(this.currentPost)); 
    }
//    
//    private boolean dataValid=true;
//
//    public boolean isDataValid() {
//        return dataValid;
//    }

    public void saveComment() {
        if (log.isDebugEnabled()) {
            log.debug("saving comment...@");
            log.debug("content of comment...@" + this.newComment.getContent());
        }
       // dataValid=true;
        
        User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY);

        log.debug("current user @"+currentUser.getUsername());
        
        if (this.currentPost.getLastComment()!=null && this.currentPost.getLastComment().getUser().getId().longValue() ==currentUser.getId().longValue()){
                 FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "error.commentedConsecutive"));
         //        dataValid=false;
                 return;
        }
        
        //        AntiSamy as = new AntiSamy();
        //        CleanResults cr = null;
        //        try {
        //            Policy policy = Policy.getInstance(BlogPostViewAction.class.getResource("/antisamy-tinymce.xml"));
        //            cr = as.scan(this.newComment.getContent(), policy);
        //        } catch (ScanException ex) {
        //            log.error(ex.getMessage());
        //        } catch (PolicyException ex) {
        //            log.error(ex.getMessage());
        //        }
        
        String _content = StringUtils.escapeHTML(this.newComment.getContent());
        
        _content = StringUtils.escapeHTML(this.newComment.getContent());
        
        _content = StringUtils.trimRepeatedText(_content);
        
        _content = _content.trim();

        if (_content.length() < 20) {
            FacesUtils.addErrorMessage("Bu çok kısa olmadı mı?");
       //     dataValid=false;
            return;
        }
        this.newComment.setContent(_content);

        if (log.isDebugEnabled()) {
            log.debug("content after scanned...@" + this.newComment.getContent());
        }

        this.newComment.setCommentedAt(new Date());

        if (log.isDebugEnabled()) {
            log.debug("current user @" + currentUser.getUsername());
        }

        this.newComment.setUser(currentUser);
       // this.currentPost.addComment(this.newComment);

        // a workaround
       /*
         * this.currentPost =
         */ //blogService.saveBlogPost(this.currentPost, null, null);

        Object[] values = blogService.updateBlogPostAfterAddingUserComment(currentUser, this.currentPost.getId(), this.newComment);
        this.currentPost = (BlogPost) values[0];
        Integer pointsToBeAdded = (Integer) values[1];

        currentUser.setPointsBalance(pointsToBeAdded);  // update the user's points in session

        //   
        FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "info.commentWasPosted"));

        this.newComment = new BlogComment();

        // loadPost();
    }

    public void vote(Long commentId, boolean doLike) {
    	Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
    	User currentUser = userService.findUserByUsername(principal.getName());

    	BlogCommentRating rating = new BlogCommentRating();

    	rating.setType(doLike ? VoteType.LIKE : VoteType.DISLIKE);
    	rating.setUser(currentUser);
    	rating.setRatingTime(new Date());
    	
    	blogService.saveBlogCommentRating(commentId, rating);

    	loadPost();
    }
    
    public static String getCleanHTML(String htmlString) {
        return StringUtils.getCleanHTML(htmlString);
    }

    public boolean isAuthor() {
            return author;
    }

    public void setAuthor(boolean author) {
            this.author = author;
    }
    
    public Long getVotableCommentCount() {
        return this.votableCommentCount;
    }

    public void setVotableCommentCount(Long votableCommentCount) {
        this.votableCommentCount = votableCommentCount;
    }
    
}
