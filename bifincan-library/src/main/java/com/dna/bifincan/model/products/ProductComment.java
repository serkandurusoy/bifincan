/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.model.products;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.user.User;

/**
 *
 * @author hantsy
 */
@Entity
@Table(name = "product_comment")
public class ProductComment extends BaseEntity implements Serializable {

    @NotNull
    @NotEmpty
    @Lob
    @NotBlank
    @Length(min = 20, max = 2048)
    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;
    // TO-DO: The setter should be the time of comment, i.e. new Date;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "commented_at", nullable = false)
    private Date commentedAt=new Date();

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "num_of_likes", nullable = false)
    private int numberOfLikes;
    
    @Column(name = "num_of_dislikes", nullable = false)    
    private int numberOfDisLikes;    
    
    @Column(name = "fi_like", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean fiLike = false;

    @Column(name = "fi_dislike", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean fiDisLike = false;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "productComment", orphanRemoval = true)
    private List<ProductCommentRating> ratings;   
    
    public ProductComment() {
    }

    public ProductComment(String content, User user, Product product) {
        this.content = content;
        this.user = user;
        this.product = product;
        this.commentedAt = new Date();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(Date postedAt) {
        this.commentedAt = postedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    public int getNumberOfLikes() {
		return numberOfLikes;
	}

	public void setNumberOfLikes(int numberOfLikes) {
		this.numberOfLikes = numberOfLikes;
	}

	public int getNumberOfDisLikes() {
		return numberOfDisLikes;
	}

	public void setNumberOfDisLikes(int numberOfDisLikes) {
		this.numberOfDisLikes = numberOfDisLikes;
	}
	
	public List<ProductCommentRating> getRatings() {
		return ratings;
	}

	public void setRatings(List<ProductCommentRating> ratings) {
		this.ratings = ratings;
	}

    public boolean isFiLike() {
        return fiLike;
    }

    public void setFiLike(boolean fiLike) {
        this.fiLike = fiLike;
    }

    public boolean isFiDisLike() {
        return fiDisLike;
    }

    public void setFiDisLike(boolean fiDisLike) {
        this.fiDisLike = fiDisLike;
    }
	@Transient
	public boolean getVotingEligibility(User voter) {
		if((this.getUser().getId().intValue() != voter.getId().intValue())) {
			if(ratings.size() > 0) {
				for(ProductCommentRating rating : ratings) {
					if(rating.getUser().getId().intValue() == voter.getId().intValue()) {
						return false;
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProductComment other = (ProductComment) obj;
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.commentedAt, other.commentedAt)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.content);
        hash = 59 * hash + Objects.hashCode(this.commentedAt);
        hash = 59 * hash + Objects.hashCode(this.user);
        return hash;
    }

    
    
}