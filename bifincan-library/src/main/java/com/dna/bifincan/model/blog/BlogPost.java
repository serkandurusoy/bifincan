/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.model.blog;

import com.dna.bifincan.model.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author hantsy
 */
@Entity
@Table(name = "blog_post", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_title", columnNames = "title"),
    @UniqueConstraint(name = "UQ_slug", columnNames = "slug")})
public class BlogPost extends BaseEntity implements Serializable {
    
    @NotNull
    @NotEmpty
    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @NotNull
    @NotEmpty
    @Column(name = "slug", nullable = false, length = 250)
    private String slug;

    @Column(name = "keywords", nullable = false, length = 255)
    @NotNull
    @NotEmpty
    private String keywords;

    @NotNull
    @NotEmpty
    @Lob
    @Column(name = "teaser", nullable = false, columnDefinition = "text")
    private String teaser;

    @NotNull
    @NotEmpty
    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "image_small")
    private Long imageSmall;

    @Column(name = "image_large")
    private Long imageLarge;
    
    // TO-DO: The setter should be the time of post, i.e. new Date;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "posted_at", nullable = false)
    private Date postedAt = new Date();

    // bi-directional association for comments on this blog post
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value="commentedAt asc")
    private List<BlogComment> comments = new ArrayList<BlogComment>();

    @OneToOne(optional = true)
    @JoinColumn(name = "last_comment_id")
    private BlogComment lastComment;

    public BlogPost() {
    }

    public BlogPost(String title, String slug, String teaser, String keywords, String content) {
        this.title = title;
        this.slug = slug;
        this.teaser = teaser;
        this.teaser = keywords;
        this.content = content;
        this.postedAt = new Date();
    }

    public void addComment(BlogComment _comment) {
        if (!comments.contains(_comment)) {
            _comment.setPost(this);
            comments.add(_comment);
            this.setLastComment(_comment);
        }
    }

    public void removeComment(BlogComment _comment) {
        if (comments.contains(_comment)) {
            _comment.setPost(null);
            comments.remove(_comment);
            if (this.getLastComment() != null && this.getLastComment().getId().longValue() == _comment.getId().longValue()) {
                this.setLastComment(null);
            }
        }
    }

    public BlogComment getLastComment() {
        return lastComment;
    }

    public void setLastComment(BlogComment lastComment) {
        this.lastComment = lastComment;
    }

    public List<BlogComment> getComments() {
        return comments;
    }

    public void setComments(List<BlogComment> comments) {
        this.comments = comments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public Long getImageSmall() {
		return imageSmall;
	}

	public void setImageSmall(Long imageSmall) {
		this.imageSmall = imageSmall;
	}

	public Long getImageLarge() {
		return imageLarge;
	}

	public void setImageLarge(Long imageLarge) {
		this.imageLarge = imageLarge;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BlogPost other = (BlogPost) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.slug, other.slug)) {
            return false;
        }
        if (!Objects.equals(this.teaser, other.teaser)) {
            return false;
        }
        if (!Objects.equals(this.keywords, other.keywords)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.postedAt, other.postedAt)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.title);
        hash = 17 * hash + Objects.hashCode(this.slug);
        hash = 17 * hash + Objects.hashCode(this.teaser);
        hash = 17 * hash + Objects.hashCode(this.keywords);
        hash = 17 * hash + Objects.hashCode(this.content);
        hash = 17 * hash + Objects.hashCode(this.postedAt);
        return hash;
    }
}