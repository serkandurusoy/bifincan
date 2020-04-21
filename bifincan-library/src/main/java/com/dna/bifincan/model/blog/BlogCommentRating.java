package com.dna.bifincan.model.blog;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.type.VoteType;
import com.dna.bifincan.model.user.User;

@Entity
@Table(name = "blog_comment_rating")
public class BlogCommentRating extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1284488474419098614L;

	@ManyToOne
    @JoinColumn(name = "blog_comment", nullable = false)	
	private BlogComment blogComment;
	
    @Column(name = "type", nullable = false, length = 32)
    @Enumerated(EnumType.ORDINAL)
	private VoteType type;
    
    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
	private User user;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rating_time", nullable = false)
    @NotNull
	private Date ratingTime;
    
    public BlogCommentRating() { }

	public BlogComment getBlogComment() {
		return blogComment;
	}

	public VoteType getType() {
		return type;
	}

	public void setType(VoteType type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getRatingTime() {
		return ratingTime;
	}

	public void setRatingTime(Date ratingTime) {
		this.ratingTime = ratingTime;
	}

	public void setBlogComment(BlogComment blogComment) {
		this.blogComment = blogComment;
	}

}
