package com.dna.bifincan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.common.util.Constants;
import com.dna.bifincan.exception.InvalidUserAccessException;
import com.dna.bifincan.model.dto.HomeProductDTO;
import com.dna.bifincan.model.products.Image;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.products.ProductComment;
import com.dna.bifincan.model.products.ProductCommentRating;
import com.dna.bifincan.model.products.ProductOrderCriteria;
import com.dna.bifincan.model.products.ProductPriceSource;
import com.dna.bifincan.model.survey.SurveyAnswer;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.survey.SurveyProduct;
import com.dna.bifincan.model.survey.SurveyQuestion;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.type.OrderDetailsType;
import com.dna.bifincan.model.type.PointTransactionType;
import com.dna.bifincan.model.type.ProductClassifier;
import com.dna.bifincan.model.type.VoteType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.repository.order.OrderDetailsRepository;
import com.dna.bifincan.repository.order.OrderRepository;
import com.dna.bifincan.repository.products.ImageRepository;
import com.dna.bifincan.repository.products.ProductCommentRatingRepository;
import com.dna.bifincan.repository.products.ProductCommentRepository;
import com.dna.bifincan.repository.products.ProductOrderCriteriaRepository;
import com.dna.bifincan.repository.products.ProductPriceSourceRepository;
import com.dna.bifincan.repository.products.ProductRepository;
import com.dna.bifincan.repository.survey.SurveyAnswerRepository;
import com.dna.bifincan.repository.survey.SurveyProductRepository;
import com.dna.bifincan.repository.user.UserRepository;

@Service
@Named("productService")
public class ProductService {

    @SuppressWarnings("unused")
    private final static Logger log = LoggerFactory.getLogger(ProductService.class);

    @Inject
    private ProductRepository productRepository;
    @Inject
    private ProductCommentRepository productCommentRepository;    
    @Inject
    private ProductOrderCriteriaRepository productOrderCriteriaRepository;  
    @Inject
    private ProductCommentRatingRepository productCommentRatingRepository;      
    @Inject
    private CriteriaService criteriaService;
    @Inject
    private UserRepository userRepository;
    @Inject
    private OrderDetailsRepository orderDetailsRepository;
    @Inject
    private SurveyProductRepository surveyProductRepository;
    @Inject
    private ProductPriceSourceRepository productPriceSourceRepository;
    @Inject
    private ImageRepository imageRepository;
    @Inject
    private OrderService orderService;    
    @Inject
    private OrderRepository orderRepository; 
    @Inject
    private SurveyAnswerRepository surveyAnswerRepository;  
    @Inject
    private PointTransactionAccountingService transactionService;

    public ProductService() { }

    protected ProductService(ProductRepository productRepository,
            ProductOrderCriteriaRepository productOrderCriteriaRepository) {
        super();
        this.productRepository = productRepository;
        this.productOrderCriteriaRepository = productOrderCriteriaRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Product> findProductsByCriteria(User user) {
        // 1. Get the product ids validated upon criteria for the given user 
        Set<Long> allResults = fetchProductIdsValidatedUponUserCriteria(user);

        // 2. Find the distinct products associated with the List#0 - if no list then no products
        List<Product> filteredProducts = null;
        if (allResults != null && !allResults.isEmpty()) {
            filteredProducts = productRepository.findAllByCriteria(allResults);
        }

        // 3. return the list of products
        return filteredProducts;
    }

    
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Product findRandomAvailableProduct(User user) {
    	Product product = null;
    	
    	List<Product> availableProducts = findAvailableProductsByCriteria(user);
    	if(availableProducts != null && !availableProducts.isEmpty()) {
    		Collections.shuffle(availableProducts, new java.util.Random());
    		product = availableProducts.get(0);
    	}    	
    	return product;
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Product> findAvailableProducts(User user) {
        List<Product> availableProducts = findAvailableProductsByCriteria(user);

        return availableProducts;
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Long> findOtherProductIds(User user) {
        List<Long> otherProductIds = findOtherProductIdsByCriteria(user);

        return otherProductIds;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Product> findOtherProducts(User user) {
        List<Long> otherProductIds = findOtherProductIdsByCriteria(user);

        List<Product> products = null;
        if (otherProductIds != null && !otherProductIds.isEmpty()) {
        	products = findOrderedProductsByIds(otherProductIds);
        }

        return products;
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Long> findOutOfStockProductIds(User user) {
        // 1. Find the product ids 
        List<Long> outOfStockproductIds = productRepository.findOutOfStockProductIds(user.getAge(),
                Constants.MINIMUM_AGE_FOR_CLASSIFIED_PRODUCTS);
        
        return outOfStockproductIds;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Long> findAllActiveProductIds() {
        // Find the product ids 
        List<Long> allProductIds = productRepository.findAllActiveProductIds();

        return allProductIds;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<String> findAllProductSlugs() {
        List<String> allProductSlugs = productRepository.findAllActiveProductSlugs();

        return allProductSlugs;
    }

    //@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Long findProductIdBySlug(String slug) {
        // Find the product id
        return productRepository.findProductIdBySlug(slug);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Product> findProductsBySlugs(Set<String> slugs) {
        // Find the product id
        return productRepository.findProductsBySlugs(slugs);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Set<Long> fetchProductIdsValidatedUponUserCriteria(User user) {
        Set<Long> allResults = new HashSet<Long>();

        List<ProductOrderCriteria> criteria = productOrderCriteriaRepository.findAllOrderedByProduct();

        long currentProductId = 0, oldProductId = 0; // init variables
        boolean curProdCriteriaStatisfied = false, saveProduct = false; // init the flags

        if (criteria != null && criteria.size() > 0) {
            for (ProductOrderCriteria criterion : criteria) {
                if (currentProductId != criterion.getProduct().getId()) {
                	//log.debug(">> c1. currentProductId = " + currentProductId + ", criterion.getProduct().getId() = " + criterion.getProduct().getId());
                    oldProductId = currentProductId;
                    if (oldProductId > 0 && curProdCriteriaStatisfied == true) {
                        saveProduct = true;
                        //log.debug(">> c2a. oldProductId = " + oldProductId + ", curProdCriteriaStatisfied = " + curProdCriteriaStatisfied + ", saveProduct = " + saveProduct);
                    } else if(oldProductId > 0 && curProdCriteriaStatisfied == false) {
                    	saveProduct = false;
                    	//log.debug(">> c2b. oldProductId = " + oldProductId + ", curProdCriteriaStatisfied = " + curProdCriteriaStatisfied + ", saveProduct = " + saveProduct);
                    }
                    currentProductId = criterion.getProduct().getId();
                    curProdCriteriaStatisfied = true;  // optimistic set
                } else {
                    saveProduct = false;
                    //log.debug(">> c3. saveProduct = " + saveProduct);
                }
               // log.debug(">> criterion.getProduct().getId() = " + criterion.getProduct().getId() + ", currentProductId = " + 
                //		currentProductId + ", curProdCriteriaStatisfied = " + curProdCriteriaStatisfied + ", saveProduct= " + saveProduct);
                
                
                if (!criteriaService.evaluateCriterion(criterion, user)) { 
                	// log.debug(">> 1. evaluation failed...");
                    curProdCriteriaStatisfied = false;
                }

                if (criterion.getSurveyOption() != null) { // for survey's option definition 
                    // points to the candidate survey
                	// log.debug(">> 2. check option...");
                    Long countOptsMatches = userRepository.findOptionMatches(user.getId(),
                            criterion.getSurveyOption().getId());

                    if (countOptsMatches == 0) {
                    	//log.debug(">>> 3. REJECTING PRODUCT (opt eq) : " + criterion.getProduct().getId());
                        curProdCriteriaStatisfied = false;
                    }
                }

                if (saveProduct == true) {
                	//log.debug(">>> 4. Add PRODUCT : " + oldProductId);
                    allResults.add(oldProductId);
                }

            }
        }

        if (curProdCriteriaStatisfied == true) {
        	//log.debug(">>> 5. Add PRODUCT (out of loop_: " + oldProductId);
            allResults.add(currentProductId);
        }
       // log.debug(">>> 6a. allResults = " + allResults);
        List<Long> noCriteriaResult = productRepository.findAllActiveUnrestricted();
      //  log.debug(">>> 6b. noCriteriaResult = " + noCriteriaResult);
        allResults.addAll(noCriteriaResult);
       // log.debug(">>> 6c. allResults = " + allResults);
        return allResults;
    }
    
  
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Product> findAvailableProductsByCriteria(User user) {
        // 1. Get the product ids validated upon criteria for the given user 
        Set<Long> checkedIds = fetchProductIdsValidatedUponUserCriteria(user);

        List<Product> products = null;
        if (checkedIds != null && !checkedIds.isEmpty()) {
        	//log.debug(">>> avail:  found the products : " + checkedIds);
        	// 2. Find the product ids with proper values for classified, active, remaining quantity based on the initial list
        	
        	List<Product> filteredProducts = productRepository.findAvailableOrderableProductsByCriteria(checkedIds, 
        				user.getAge(), Constants.MINIMUM_AGE_FOR_CLASSIFIED_PRODUCTS);
        	//log.debug(">>> avail: found the filteredProducts : " + filteredProducts);
        	
            if(filteredProducts != null && !filteredProducts.isEmpty()) {
            	// check each product whether is has been ordered less than 'daysRequireBeforeProdIsOrderable' days before
            	for(Product product : filteredProducts) {
            		Long orderId = orderDetailsRepository.findLastUserOrderByOrderDetailsProduct(user.getId(), product.getId());
            		if(orderId != null) {
	            		com.dna.bifincan.model.order.Order order = orderRepository.findOne(orderId);
	            		if(order.getReceivedTime() != null && orderService.calculateRemainingDaysForAction(order, 
	            				ConfigurationType.DAYS_REQUIRED_BEFORE_PRODUCT_IS_ORDERABLE) == 0) {
	            			if(products == null) products = new ArrayList<Product>();
	            			products.add(product);
	            		} 
            		} else {
            			if(products == null) products = new ArrayList<Product>();
            			products.add(product);
            		}
            	}	
            	
            }
            
        }

        return products;
    }    

    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Long> findAvailableProductIdsByCriteria(User user) {
        // 1. Get the product ids validated upon criteria for the given user 
        Set<Long> checkedIds = fetchProductIdsValidatedUponUserCriteria(user);

        List<Long> productIds = null;
        if (checkedIds != null && !checkedIds.isEmpty()) {
        	//log.debug(">>> A1. avail:  found the products : " + checkedIds);
        	// 2. Find the product ids with proper values for classified, active, remaining quantity based on the initial list
        	
        	List<Long> filteredProductIds = productRepository.findAvailableOrderableProductIdsByCriteria(checkedIds, 
        				user.getAge(), Constants.MINIMUM_AGE_FOR_CLASSIFIED_PRODUCTS);
        	//log.debug(">>> A1. avail: found the filteredProductIds : " + filteredProductIds);
        	
            if(filteredProductIds != null && !filteredProductIds.isEmpty()) {
            	// check each product whether is has been ordered less than 'daysRequireBeforeProdIsOrderable' days before
            	for(Long productId : filteredProductIds) {
            		Long orderId = orderDetailsRepository.findLastUserOrderByOrderDetailsProduct(user.getId(), productId);
            		if(orderId != null) {
	            		com.dna.bifincan.model.order.Order order = orderRepository.findOne(orderId);
	            		if(order.getReceivedTime() != null && orderService.calculateRemainingDaysForAction(order, 
	            				ConfigurationType.DAYS_REQUIRED_BEFORE_PRODUCT_IS_ORDERABLE) == 0) {
	            			if(productIds == null) productIds = new ArrayList<Long>();
	            			productIds.add(productId);
	            		}
            		} else {
            			if(productIds == null) productIds = new ArrayList<Long>();
            			productIds.add(productId);
            		}
            	}	
            	//log.debug(">>> A1. avail: found the productIds : " + productIds);
            }
            
        }

        return productIds;
    }    

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Long> findOtherProductIdsByCriteria(User user) {  	
    	// 1. Find all the active with remaining stock products (excluding the out-of-stock products)
    	List<Long> productIds = productRepository.findAllActiveWithStockProducts(); 
    	//log.debug(">>> oth: allActiveStockedIds = " + productIds);
    	
    	// 2. find all the available product ids
    	List<Long> allAvailableIds = findAvailableProductIdsByCriteria(user);
    	//log.debug(">>> oth: allAvailableIds = " + allAvailableIds);
    	
    	if(productIds != null && allAvailableIds != null) {
    		// 3. find the non-common products
    		productIds.removeAll(allAvailableIds);
    	}     	
    	//log.debug(">>> oth: productIds = " + productIds);
    	

        return productIds;
    }

    /**
     * This method first finds the products that have been defined as 'welcome'
     * ones. From this list it either selects them all or a sublist of them
     * depending on the maximum allowed number of products (based on type eg.
     * 'welcome') being defined in the 'configuration' table. Finally shuffles
     * the list in order to return a list of random product ids. Note that the
     * products are selected if they are active, have a remaining quantity > 0
     * and are validate upon user's order criteria.
     *
     * @param user the current user for whom the products will be validated upon
     * @param maxNoOfProducts maximum allowed number of products
     * @param type the type of order item
     * @param excludedList the list of products ids which should be excluded
     * @return the list of random products ids
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Long> getRandomLatestActiveProductIds(User user, int maxNoOfProducts,
            OrderDetailsType type, List<Long> excludedList) {
        List<Long> randomResults = null;

        // fetch the validated product ids upon the given user
        Set<Long> allResults = fetchProductIdsValidatedUponUserCriteria(user);

        // fetch latest active products ids from the list of the validated products
        List<Long> productIds = null;

        switch (type) {
            case WELCOME_PRODUCT:
                //productIds = productRepository.getActiveWelcomeProductIds(allResults, excludedList); 
                productIds = productRepository.getActiveWelcomeProductIds(allResults);
                break;
            case SURPRISE_PRODUCT:
                //productIds = productRepository.getActiveSurpriseProductIds(allResults, excludedList); 
                productIds = productRepository.getActiveSurpriseProductIds(allResults);
                break;
        }

        // construct a random list of the product ids
        if (productIds != null) {
            if (productIds.size() <= maxNoOfProducts) {
                randomResults = productIds;
            } else {
                Collections.shuffle(productIds, new Random((new Date()).getTime()));
                randomResults = productIds.subList(0, maxNoOfProducts);
            }
        }

        return randomResults;
    }

    //@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Product findById(Long productId, User user) throws InvalidUserAccessException {
        Product product = null;

        boolean canAccess = false;
        if (user != null) {
            List<Long> productIds = productRepository.findLegalProductIdsByListAndUserAge(Arrays.asList(productId),
                    user.getAge(), Constants.MINIMUM_AGE_FOR_CLASSIFIED_PRODUCTS);

            if (productIds != null && !productIds.isEmpty()) {
                product = productRepository.findOne(productId);
                canAccess = true;
            }
        } else {
            product = productRepository.findOne(productId);
            if (product == null) {
                return null;
            }
            if (product.getClassifier() == ProductClassifier.NORMAL) {
                canAccess = true;
            }
        }

        if (!canAccess) {
            throw new InvalidUserAccessException("Error: You are not allowed to access this product.");
        }

        return product;
    }

    public Product findByIdUnrestricted(Long productId) {
    	return productRepository.findOne(productId);
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Product findById(Long productId) {
        return productRepository.findOne(productId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProductOrderCriteria findProductOrderCriterionById(Long criterioId) {
        return productOrderCriteriaRepository.findOne(criterioId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.getProductsByIds(productIds);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Product> findOrderedProductsByIds(List<Long> productIds) {
        return productRepository.getOrderedProductsByIds(productIds);
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Page<Product> findProducts(int first, int pageSize) {
        Pageable cond = new PageRequest(first, pageSize);
        return productRepository.findAll(cond);
    }

    public List<Product> findActiveProducts(int maxResults) {
        return productRepository.findLatestActiveProducts(maxResults);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Page<ProductOrderCriteria> findProductOrderCriteria(int first, int pageSize) {
        Pageable cond = new PageRequest(first, pageSize);
        return productOrderCriteriaRepository.findAll(cond);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ProductPriceSource> findProductPriceSources() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.productPriceSourceRepository.findAll(sort);
    }

    /**
     * Saves a product, but first checks for duplicate name.
     *
     * @param product
     * @return true for success or false for failure
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean saveProduct(Product product, Image imageDetail, Image imageLarge, Image imageSmall) {
        Long total = productRepository.countByUniqueFields(product.getName(), product.getBarcode(),
                product.getSlug(), product.getId() != null ? product.getId() : 0);
        if (total == 0) {
            if (product.getId() == null || product.getId() == 0) {
                if (imageDetail != null && imageDetail.getContents() != null && imageDetail.getContents().length > 0) {
                    imageRepository.save(imageDetail);
                    product.setImageDetail(imageDetail.getId());
                }

                if (imageLarge != null && imageLarge.getContents() != null && imageLarge.getContents().length > 0) {
                    imageRepository.save(imageLarge);
                    product.setImageLarge(imageLarge.getId());
                }

                if (imageSmall != null && imageSmall.getContents() != null && imageSmall.getContents().length > 0) {
                    imageRepository.save(imageSmall);
                    product.setImageSmall(imageSmall.getId());
                }

                product.setEntryDate(new Date());
            } else {
                /*
                 * TODO: this is just a workaround in order to preserve the
                 * current logo from empty logo form submissions; However a flag
                 * should be added to indicate that we really want to reset the
                 * logo.. Usign the DTO pattern may be better here..
                 */
                if (imageDetail != null && imageDetail.getContents().length > 0) {
                    if (product.getImageDetail() != null) {
                        Image curImageDetail = imageRepository.findOne(product.getImageDetail());
                        if (curImageDetail != null) {
                            imageRepository.delete(curImageDetail);
                        }
                    }
                    imageRepository.save(imageDetail);
                    product.setImageDetail(imageDetail.getId());
                }

                if (imageLarge != null && imageLarge.getContents().length > 0) {
                    if (product.getImageLarge() != null) {
                        Image curImageLarge = imageRepository.findOne(product.getImageLarge());
                        if (curImageLarge != null) {
                            imageRepository.delete(curImageLarge);
                        }
                    }
                    imageRepository.save(imageLarge);
                    product.setImageLarge(imageLarge.getId());
                }

                if (imageSmall != null && imageSmall.getContents().length > 0) {
                    if (product.getImageSmall() != null) {
                        Image curImageSmall = imageRepository.findOne(product.getImageSmall());
                        if (curImageSmall != null) {
                            imageRepository.delete(curImageSmall);
                        }
                    }
                    imageRepository.save(imageSmall);
                    product.setImageSmall(imageSmall.getId());
                }
            }

            //product.setClassifier(ProductClassifier.NORMAL);

            this.productRepository.save(product);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes a product, but first checks for existing dependencies.
     *
     * @param local product
     * @return true for success or false for failure (ONLY due to dependencies)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteProduct(Product product) {
        Long total1 = orderDetailsRepository.countByProduct(product.getId()); // optimistic check (there are more dependencies)
        if (total1 == 0) {

            if (product.getImageDetail() != null) {
                Image imgDetail = imageRepository.findOne(product.getImageDetail());
                if (imgDetail != null) {
                    imageRepository.delete(imgDetail);
                }
            }

            if (product.getImageLarge() != null) {
                Image imgLarge = imageRepository.findOne(product.getImageLarge());
                if (imgLarge != null) {
                    imageRepository.delete(imgLarge);
                }
            }

            if (product.getImageSmall() != null) {
                Image imgSmall = imageRepository.findOne(product.getImageSmall());
                if (imgSmall != null) {
                    imageRepository.delete(imgSmall);
                }
            }

            // get a fresh copy of the entity
            Product tempProduct = this.productRepository.findOne(product.getId());
            this.productRepository.delete(tempProduct);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Saves a product price source, but first checks for duplicate name.
     *
     * @param source
     * @return true for success or false for failure
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean saveProductPriceSource(ProductPriceSource source) {
        Long total = productPriceSourceRepository.countByName(source.getName(),
                source.getId() != null ? source.getId() : 0);
        if (total == 0) {
            this.productPriceSourceRepository.save(source);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Saves a product order criterion.
     *
     * @param criterion
     * @return true for success or false for failure
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean saveProductOrderCriteria(ProductOrderCriteria criterion) {
        this.productOrderCriteriaRepository.save(criterion);

        return true;
    }

    /**
     * Deletes a product price source, but first checks for existing
     * dependencies.
     *
     * @param local source
     * @return true for success or false for failure (ONLY due to dependencies)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteProductPriceSource(ProductPriceSource source) {
        Long total1 = productRepository.countByProductPriceSource(source.getId());
        if (total1 == 0) {
            // get a fresh copy of the entity
            ProductPriceSource tempSource = this.productPriceSourceRepository.findOne(source.getId());
            this.productPriceSourceRepository.delete(tempSource);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes a product order criterion.
     *
     * @param criterion
     * @return true for success or false for failure (ONLY due to dependencies)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteProductOrderCriteria(ProductOrderCriteria criterion) {
        ProductOrderCriteria tempCriterion = this.productOrderCriteriaRepository.findOne(criterion.getId());
        this.productOrderCriteriaRepository.delete(tempCriterion);

        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Product> findByName(String name) {
        return productRepository.findByName("%" + name + "%");
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Long countAvailableWelcomeProducts() {
        return productRepository.countAvailableWelcomeProducts();
    }

    public List<Product> findAllActiveProducts() {
        return productRepository.findAllActiveProducts();
    }

    public List<Product> findAllActiveProductsAllTypes() {
        return productRepository.findAllActiveProductsAllTypes();
    }

    public List<Product> findAllActiveProductsAllTypesInStock() {
        return productRepository.findAllActiveProductsAllTypesInStock();
    }

    public List<Product> findAllActiveProductsAllTypesOutOfStock() {
        return productRepository.findAllActiveProductsAllTypesOutOfStock();
    }

    @Transactional()
    public Object[] updateProductAfterAddingUserComment(User currentUser, Long productId, ProductComment comment) {
        Object[] vals = new Object[2];

        Product oProduct =  productRepository.findOne(productId);
        if(oProduct != null) { 
        	oProduct.addComment(comment);
        	vals[0] = productRepository.save(oProduct); 
        	vals[1] = transactionService.addConfigurationPoints(currentUser, PointTransactionType.PRODUCT_COMMENT_POINTS);
        }
        
        return vals;
    }

    public List<ProductComment> findRecentComments() {
        return productCommentRepository.findRecentComments();
    }
    
    
    public long countCommentByUser(User _user){
        return productCommentRepository.countByUser(_user);
    }

    @Transactional
    public boolean saveProductComment(ProductComment currentComment) {
    	productCommentRepository.save(currentComment);
        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Long countByCommentAndVoter(ProductComment productComment, User user) {
    	return productCommentRatingRepository.countByCommentAndVoter(productComment.getId(), user);
    }
    
    @Transactional
    public Integer saveProductCommentRating(ProductCommentRating rating, Long commentId) {
    	ProductComment comment = productCommentRepository.findOne(commentId);

    	int numOfLikes = comment.getNumberOfLikes();
    	int numOfDisLikes = comment.getNumberOfDisLikes();
    	
    	if(rating.getUser().getId().intValue() != comment.getUser().getId().intValue()) {
    		if(countByCommentAndVoter(comment, rating.getUser()) == 0) {
    			rating.setProductComment(comment);
    			
	    		productCommentRatingRepository.save(rating);  
	    		comment.getRatings().add(rating); 

		    	if(rating.getType() == VoteType.LIKE) {
		    		comment.setNumberOfLikes(++numOfLikes);
		    		productCommentRepository.save(comment);

		    		return transactionService.addPoints(comment.getUser(), PointTransactionType.VOTING_POINTS , 1);
		    	} else if(rating.getType() == VoteType.DISLIKE) {
		    		comment.setNumberOfDisLikes(++numOfDisLikes);  
		    		productCommentRepository.save(comment);
		    	} else {
		    		throw new RuntimeException("Not supported");
		    	}
		    	
    		}	
    	}	
    	
		return null;
    }
    
    public Page<ProductComment> findComments(int first, int pageSize) {
        Sort defaultSort = new Sort(new Sort.Order(Sort.Direction.DESC, "commentedAt"));
        Pageable cond = new PageRequest(first, pageSize, defaultSort);
        return productCommentRepository.findAll(cond);
    }

    public ProductComment findComment(Long valueOf) {
        return productCommentRepository.findOne(valueOf);
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Product> findOutOfStockProducts(User user) {
    	List<Product> outOfStockproducts = productRepository.findOutOfStockProducts(user.getAge(),
                Constants.MINIMUM_AGE_FOR_CLASSIFIED_PRODUCTS);
        return outOfStockproducts;
    }   
    
    public Product fetchRandomProductPromoter(String username) /*throws NoProductPromoterException*/ {
    	Product product = null;
    	
    	User user = userRepository.findByUsername(username);

        // 1. Get the product ids validated upon criteria for the given user 
        Set<Long> checkedIds = fetchProductIdsValidatedUponUserCriteria(user);

        Long randomProductId = null;
        if (checkedIds != null && !checkedIds.isEmpty()) {
        	List<Long> filteredProductIds = productRepository.findPromoterProductIdsByCriteria(checkedIds, user.getPointsBalance()); 
        	
            if(filteredProductIds != null && !filteredProductIds.isEmpty()) {
            	Collections.shuffle(filteredProductIds, new Random());
            	
            	// check each product whether is has been ordered less than 'daysRequireBeforeProdIsOrderable' days before
            	for(Long productId : filteredProductIds) {
            		Long orderId = orderDetailsRepository.findLastUserOrderByOrderDetailsProduct(user.getId(), productId);
            		if(orderId != null) {
	            		com.dna.bifincan.model.order.Order order = orderRepository.findOne(orderId);
	            		if(order.getReceivedTime() != null && orderService.calculateRemainingDaysForAction(order, 
	            				ConfigurationType.DAYS_REQUIRED_BEFORE_PRODUCT_IS_ORDERABLE) == 0) {
	            			randomProductId = productId; 
	            		}
            		} else {
            			randomProductId = productId; 
            		}
            		if(randomProductId != null) break;
            	}	
            	
            }
        }
        
        if(randomProductId != null) {
        	 product = productRepository.findOne(randomProductId);
        } 
        
    	return product;
    }
    
    public HomeProductDTO fetchRandomHomeProduct() {
    	HomeProductDTO productDTO = null;
    	
    	List<Long> productIds = productRepository.findAllActiveNormalProductIds();
    	//log.debug(">> productIds = " + productIds);
    	if(productIds != null && !productIds.isEmpty()) {
    		//List<Long> filteredProductIds = orderDetailsRepository.findMinOrderedAndSurveyCompletedByList(productIds);
    		List<Object[]> filteredProductIds = orderDetailsRepository.findMinOrderedAndSurveyCompletedByList(productIds);
    		//log.debug(">> 1a.filteredProductIds = " + (filteredProductIds!=null?filteredProductIds.size():0));   		
    		if(filteredProductIds != null && !filteredProductIds.isEmpty()) { 
    			Collections.shuffle(filteredProductIds, new Random());
        		//for(Object[] vals : filteredProductIds) {
        		//	System.out.println("id = " + vals[0] + ", count = " + vals[1]);
        		//} 
    			Product randomProduct = null;
    			SurveyOption surveyOption = null;

    			for(Object[] row : filteredProductIds) {
    				randomProduct = productRepository.findOne((Long)row[0]);
    			
    				List<Long> surveyIds = surveyProductRepository.findProductSurveyIdsByProduct(randomProduct);
    				//log.debug(">> surveyIds = " + surveyIds + " for prod = " + (Long)row[0]);
    				if(surveyIds != null && !surveyIds.isEmpty()) {
    					Collections.shuffle(surveyIds, new Random());
    					
    					for(Long surveyId : surveyIds) {
	    					SurveyProduct tProduct = surveyProductRepository.findOne(surveyId);
	    					//log.debug(">> checking survey id..."  + surveyId);
	    					List<SurveyQuestion> tQuestions = tProduct.getSurveyQuestions();
	    					if(tQuestions != null && !tQuestions.isEmpty()) {
	    						//log.debug(">> questions not empty..."  + tQuestions.size());
	    						Collections.shuffle(tQuestions, new Random());
	    						for(SurveyQuestion tQuestion : tQuestions) {
	    							SurveyAnswer lastAnswer = surveyAnswerRepository.fetchLastAnswerByQuestion(tQuestion);
	    							
	    							if(lastAnswer != null) {
	    								//log.debug(">> answer found ..." + lastAnswer.getId());
	    								List<SurveyOption> tOptions = lastAnswer.getSurveyOptions();// surveyOptionRepository.fetchAllAnsweredOptionsByQuestion(lastAnswer.getSurveyQuestion().getId());
	    								if(tOptions != null && !tOptions.isEmpty()) {
	    									//log.debug(">> options found ..." + tOptions.size());
	    									Collections.shuffle(tOptions, new Random());
	    									surveyOption = tOptions.get(0);
	    									//log.debug(">> returning option ..." + surveyOption.getId());
	    									break;
	    								}
	    							}
	    						}
	    						
	    						if(surveyOption != null) break; // from survey's loop
	    					}	
    					}
    					
    					if(surveyOption != null) break; // from product's loop
    					
    				} 
    			}	
    			
				if(randomProduct != null && surveyOption != null) {
					productDTO = new HomeProductDTO();
					
    				productDTO.setId(randomProduct.getId());
    				productDTO.setBrandName(randomProduct.getBrand().getName());
    				productDTO.setOptionText(surveyOption.getTitle());
    				productDTO.setProductName(randomProduct.getName());
    				productDTO.setProductShortDescription(randomProduct.getShortDescription());
    				productDTO.setProductSlug(randomProduct.getSlug());
    				productDTO.setQuestionText(surveyOption.getQuestion().getTitle());
    				
    				int percentage = 0;
    			//	log.debug(">> randomProduct.getId() = " + randomProduct.getId());
    				List<Long> userIds = orderDetailsRepository.fetchOrderUserIdsBySurveyedProductId(randomProduct.getId());
    				if(userIds != null && !userIds.isEmpty()) {
    				//	log.debug(">> userIds = " + userIds);
    					//log.debug(">> surveyOption.getId() = " + surveyOption.getId());
    					//log.debug(">> surveyOption.getQuestion().getId() = " + surveyOption.getQuestion().getId());
    					List<SurveyAnswer> answers = surveyAnswerRepository.fetchLatestByQuestionIdAndUserList(surveyOption.getQuestion().getId(), 
    							userIds);
    					//log.debug(">> answers.size() = " + (answers != null ? answers.size() : 0));
    					if(answers != null && !answers.isEmpty()) {
    						int optionAnswerCount = 0;
    						int distinctOptions = 0;//answers.size();
    						//log.debug(">> distinctUsers = " + distinctUsers);
    						for(SurveyAnswer answer : answers) {
    							if(answer.getSurveyQuestion().isSelectMultiple()) {
    								distinctOptions += answer.getSurveyOptions().size();
    							} else {
    								distinctOptions += 1;
    							}
    							
    							if(answer.getSurveyOptions() != null && answer.getSurveyOptions().contains(surveyOption)) {
    								optionAnswerCount++;
    							}
    						}
    						//log.debug("1. optionAnswerCount = " + optionAnswerCount + 
    						//		", distinctUsers = " + distinctUsers);
 
    						//log.debug(">> optionAnswerCount / distinctUsers = " + (float)optionAnswerCount / (float)distinctUsers);
    						percentage = Math.round( ((float)optionAnswerCount / (float)distinctOptions) * 100);
    						
    					}
    				}
    				
    				productDTO.setPercentage(percentage);
				}
    		}	
    	}
    	
    	return productDTO;
    }
    
    public BigDecimal totalProductMoneyValue(){
        BigDecimal result=this.productRepository.sumMoneyValueOfAllProducts();
        if(result==null) {
            result=BigDecimal.ZERO;
        }
        return result;
    }
    
    
    public long noOfProductComments(){
        return this.productCommentRepository.count();
    }  
    
    public long countCommentsbyProductAndUser(Product product, User user) {
    	return productCommentRepository.countCommentsbyProductAndUser(product, user);
    }
    
    public long countVotableCommentsByProduct(Product product) {
    	return productCommentRepository.countVotableCommentsByProduct(product);
    }
    
    public long countCommentLikessbyProductAndUser(Product product, User user) {
    	return productCommentRepository.countCommentsbyProductAndUserAndVoteType(product, user, VoteType.LIKE);
    }
    
    public long countCommentsDislikesbyProductAndUser(Product product, User user) {
    	return productCommentRepository.countCommentsbyProductAndUserAndVoteType(product, user, VoteType.DISLIKE);
    }

    public long countLikesReceivedForUserAtProduct(Product product, User user) {
        if (countCommentsbyProductAndUser(product, user) > 0) {
            return productCommentRepository.countLikesReceivedForUserAtProduct(product, user);
        }
    	return 0;
    }

    public long countDisLikesReceivedForUserAtProduct(Product product, User user) {
        if (countCommentsbyProductAndUser(product, user) > 0) {
            return productCommentRepository.countDisLikesReceivedForUserAtProduct(product, user);
        }
    	return 0;
    }

}
