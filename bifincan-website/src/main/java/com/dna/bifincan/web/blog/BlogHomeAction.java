package com.dna.bifincan.web.blog;

import com.dna.bifincan.model.blog.BlogPost;
import com.dna.bifincan.service.BlogService;
import java.io.Serializable;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import com.dna.bifincan.util.spring.ScopeType;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("blogHomeAction")
@Scope(ScopeType.VIEW)
public class BlogHomeAction implements Serializable {
	private static final long serialVersionUID = 6504582705599447647L;

	private static final Logger log = LoggerFactory.getLogger(BlogHomeAction.class);

    @Inject
    BlogService blogService;

    BlogPost mainPost;

    List<BlogPost> nextSix;

    List<BlogPost> nextMore;

    public BlogPost getMainPost() {
        return mainPost;
    }

    public void setMainPost(BlogPost mainPost) {
        this.mainPost = mainPost;
    }

    public List<BlogPost> getNextSix() {
        return nextSix;
    }

    public void setNextSix(List<BlogPost> nextSix) {
        this.nextSix = nextSix;
    }

    public List<BlogPost> getNextMore() {
        return nextMore;
    }

    public void setNextMore(List<BlogPost> nextMore) {
        this.nextMore = nextMore;
    }

    public void loadPosts() {
        List<BlogPost> _posts = blogService.findAllPosts();

        int postsSize = _posts.size();

        if (!_posts.isEmpty()) {
            mainPost = _posts.get(0);
        }

        if (postsSize > 1) {
            int max = postsSize >= 7 ? 7 : postsSize;
            nextSix = _posts.subList(1, max);
        }

        if (postsSize > 7) {
            nextMore = _posts.subList(7, postsSize);
        }
    }
}
