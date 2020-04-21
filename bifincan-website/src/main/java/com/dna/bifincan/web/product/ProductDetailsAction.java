package com.dna.bifincan.web.product;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.order.ShoppingCart;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.products.ProductComment;
import com.dna.bifincan.model.products.ProductCommentRating;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.type.VoteType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.OrderService;
import com.dna.bifincan.service.ProductService;
import com.dna.bifincan.service.RatingService;
import com.dna.bifincan.service.UserService;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.StringUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;

@Named("productDetailsAction")
@Scope(ScopeType.VIEW)
public class ProductDetailsAction implements Serializable {

    private static final long serialVersionUID = -3573527175273720762L;

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ProductDetailsAction.class);

    private String slug;

    private Long productId, votableCommentCount;

    private Product product;

    private boolean available, removable, author;

    private float averageRating;

    private ProductComment newComment = new ProductComment();

    @Inject
    private ProductService productService;

    @Inject
    private UserService userService;

    @Inject
    protected ConfigurationService configurationService;

    @Inject
    protected OrderService orderService;

    @Inject
    protected RatingService ratingService;

    public ProductDetailsAction() {
    }

    @PostConstruct
    public void postCon() {
     //   log.debug("post construct...");
    }

    public void initProduct() {
     //   log.debug("initProduct...");
     //   log.debug("product id@..." + productId);
    //    log.debug("product slug...@" + slug);
        if (!FacesContext.getCurrentInstance().isPostback()) {
            loadProductInfo();
        }
        this.newComment = new ProductComment();
    }

    public void loadProductInfo() {
      // log.debug(" loadProductInfo...");
        User user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY);

        if (productId == null && slug != null && !slug.isEmpty()) {
            productId = productService.findProductIdBySlug(slug);
        }

        if (productId == null) {
            FacesUtils.sendHttpStatusCode(HttpStatus.NOT_FOUND,
                    "ERROR: neither slug or id parameter is defined.");
            return;
        }

       // try {

            this.product = productService.findByIdUnrestricted(productId);

            // check the availability of product against several criteria (user, product etc)
            // pessimistic logic; we cannot cache any of this information.. it should be checked every time
            if (user != null) {
                List<Long> availableProductIds = productService.findAvailableProductIdsByCriteria(user);

                // pre-set the flag of availability
                setAvailable(this.product != null && availableProductIds != null && availableProductIds.contains(productId));

                boolean canUserOrder = orderService.canUserPlaceNewOrder(user.getUsername()); // check against other criteria
                if (!canUserOrder) {
                    setAvailable(false);
                }

                ShoppingCart userShoppingCart = user != null ? user.getShoppingCart() : null;

                int maxNoOfOrderableProducts = 0;
                Configuration config = configurationService.find(ConfigurationType.MAX_NO_OF_ORDERABLE_PRODUCTS.getKey());
                if (config != null) {
                    maxNoOfOrderableProducts = Integer.parseInt(config.getValue());
                }

    			Configuration bounusOrderableConfig = configurationService.find(ConfigurationType.NO_OF_BONUS_ORDERABLE_PRODUCTS.getKey());
                int numberOfBonusOrderableProducts = Integer.parseInt(bounusOrderableConfig.getValue());
                
                if(user.getActivityLevel() == WebConstants.HIGH_USER_ACTIVITY)  maxNoOfOrderableProducts += numberOfBonusOrderableProducts;
                
				if(user.getActivityLevel() == WebConstants.LOW_USER_ACTIVITY) { // low lever activity
					setAvailable(false);
				} else if (userShoppingCart != null && userShoppingCart.getProducts().contains(product.getSlug())) {
                    setRemovable(true);
                    setAvailable(false);
                } else if (userShoppingCart != null && userShoppingCart.getProducts().size() > 0
                        && userShoppingCart.getProducts().size() >= maxNoOfOrderableProducts) {
                    setRemovable(false);
                    setAvailable(false);
                }

				setAuthor(productService.countCommentsbyProductAndUser(product, user) > 0 ? true : false); 
				setVotableCommentCount(productService.countVotableCommentsByProduct(product)); 
            } else {
                setRemovable(false);
                setAvailable(false);
                setAuthor(false);
                setVotableCommentCount(0L);
            }

            float avgValue = roundToHalf(ratingService.findAverageRatingByProduct(product.getId()));

            this.averageRating = avgValue;

        //} catch (InvalidUserAccessException ex) {
            /*
             * FacesUtils.addErrorMessage(FacesUtils.getBundleKey(We
             * String.valueOf(Constants.MINIbConstants.MESSAGE_BUNDLE_KEY,
             * WebConstants.ERROR_INVALID_AGE_KEY, new String[] {MUM_AGE_FOR_CLASSIFIED_PRODUCTS)
             * }));
             */
           /* log.debug(ex.getMessage());
            try {            	
               // FacesUtils.getExternalContext().dispatch("/errorrestricted.xhtml");
                return;
            } catch (Exception e) {
            	return;
            }*/ 
       // }

        if (getProduct() == null) {
            FacesUtils.sendHttpStatusCode(HttpStatus.NOT_FOUND, "ERROR: there is no product registered for id="
                    + productId + " and slug=" + slug);
            return;
        }
    }
    
//    private boolean dataValid = true;
//
//    public boolean isDataValid() {
//        return dataValid;
//    }

    public void saveComment() {
     //   if (log.isDebugEnabled()) {
     //       log.debug("saving comment...@");
     //       log.debug("content of comment...@" + this.newComment.getContent());
    //    }
        User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(WebConstants.CURRENT_USER_SESSION_KEY);
     //   log.debug("current user @" + currentUser.getUsername());

        if (this.product.getLastComment()!=null && this.product.getLastComment().getUser().getId().longValue() == currentUser.getId().longValue()) {
            FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "error.commentedConsecutive"));
     //       dataValid = false;
            return;
        }

        String _content = StringUtils.escapeHTML(this.newComment.getContent());
        
        _content = StringUtils.escapeHTML(this.newComment.getContent());
        
        _content = StringUtils.trimRepeatedText(_content);
        
        _content = _content.trim();

        if (_content.length() < 20) {
            FacesUtils.addErrorMessage("Bu çok kısa olmadı mı?");
     //       dataValid=false;
            return;
        }
        
        this.newComment.setContent(_content);

    //    if (log.isDebugEnabled()) {
    //       log.debug("content after scanned...@" + this.newComment.getContent());
    //    }

        this.newComment.setCommentedAt(new Date());

      //  if (log.isDebugEnabled()) {
     //       log.debug("current user @" + currentUser.getUsername());
     //   }

        this.newComment.setUser(currentUser);
     //   this.product.addComment(this.newComment);


        Object[] values = productService.updateProductAfterAddingUserComment(currentUser, this.product.getId(), this.newComment); 

        this.product = (Product) values[0];
        Integer pointsToBeAdded = (Integer) values[1];

        currentUser.setPointsBalance(pointsToBeAdded);  // update the user's points in session

        //   
        FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "info.commentWasPosted"));

        this.newComment = new ProductComment();
    }

    public void vote(Long commentId, boolean doLike) {
    	Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
    	User currentUser = userService.findUserByUsername(principal.getName());

    	ProductCommentRating rating = new ProductCommentRating();

    	rating.setType(doLike ? VoteType.LIKE : VoteType.DISLIKE);
    	rating.setUser(currentUser);
    	rating.setRatingTime(new Date());
    	
    	productService.saveProductCommentRating(rating, commentId);
		
    	loadProductInfo();
    }
    
    //--- helper methods ---//
    public String getCleanHTML(String htmlString) { 
        return StringUtils.getCleanHTML(htmlString);
    }

    public float roundToHalf(float rval) {
        return ((float) (Math.round(rval * 2))) / 2;
    }

	
    //--- setters & getters ---//
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ProductComment getNewComment() {
        return newComment;
    }

    public void setNewComment(ProductComment newComment) {
        this.newComment = newComment;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
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
