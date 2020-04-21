package com.dna.bifincan.model.products;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.address.Country;
import com.dna.bifincan.model.blog.BlogComment;
import com.dna.bifincan.model.quiz.QuizQuestion;
import com.dna.bifincan.model.type.ProductClassifier;

@Entity
@Table(name = "product", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_name", columnNames = "name"),
    @UniqueConstraint(name = "UQ_barcode", columnNames = "barcode"),
    @UniqueConstraint(name = "UQ_slug", columnNames = "slug")})
@NamedQueries({
    @NamedQuery(name = "Product.findAllByCriteria", query = "select distinct p from Product p "
    + "where p in (:allResults)")
})
public class Product extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -5979916163080461472L;

    @Column(name = "name", nullable = false, length = 255)
    @NotNull
    @NotEmpty
    private String name;

    @Column(name = "short_description", nullable = false, length = 255)
    @NotNull
    @NotEmpty
    private String shortDescription;

    @Column(name = "keywords", nullable = false, length = 255)
    @NotNull
    @NotEmpty
    private String keywords;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategoryLevelThree category;

    @Column(name = "barcode", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    private String barcode;

    @Column(name = "image_small")
    private Long imageSmall;

    @Column(name = "image_large")
    private Long imageLarge;

    @Column(name = "image_detail")
    private Long imageDetail;

    @Column(name = "slug", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    private String slug;

    @Column(nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean active = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "entry_date", nullable = false)
    private Date entryDate;

    @Column(name = "price_money", nullable = false)
    private BigDecimal priceMoney;

    @Column(name = "price_points", nullable = false)
    private int pricePoints;

    @Column(name = "points_gained_per_question", nullable = false)
    private int pointsGainedPerQuestion;

    @ManyToOne
    @JoinColumn(name = "price_source_id", nullable = false)
    private ProductPriceSource priceSource;

    @Column(name = "loss", nullable = false)
    private int loss;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "order_quantity", nullable = false)
    private int orderQuantity;

    @Column(name = "welcome_product", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean welcomeProduct;

    @Column(name = "surprise_product", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean surpriseProduct;

    @Column(name = "orderable_product", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean orderableProduct;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "classifier", nullable = false)
    private ProductClassifier classifier;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "product", orphanRemoval = true)
    @OrderBy("commentedAt asc")
    private List<ProductComment> comments = new ArrayList<ProductComment>();

    @OneToOne(mappedBy = "product")
    private QuizQuestion quizQuestion;

    @OneToOne(optional = true)
    @JoinColumn(name = "last_comment_id")
    private ProductComment lastComment;

    public Product() {
    }

    public Product(String name, String shortDescription, String barcode) {
        super();
        this.name = name;
        this.shortDescription = shortDescription;
        this.barcode = barcode;
    }

    public void addComment(ProductComment _comment) {
        if (!this.comments.contains(_comment)) {
            _comment.setProduct(this);
            this.comments.add(_comment);
            this.setLastComment(_comment);
        }
    }

    public void removeComment(ProductComment _comment) {
        if (this.comments.contains(_comment)) {
            _comment.setProduct(null);
            this.comments.remove(_comment);
            if (this.getLastComment() != null && this.getLastComment().getId().longValue() == _comment.getId().longValue()) {
                this.setLastComment(null);
            }
        }
    }

    public ProductComment getLastComment() {
        return lastComment;
    }

    public void setLastComment(ProductComment lastComment) {
        this.lastComment = lastComment;
    }

    public List<ProductComment> getComments() {
        return comments;
    }

    public void setComments(List<ProductComment> comments) {
        this.comments = comments;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public ProductCategoryLevelThree getCategory() {
        return category;
    }

    public void setCategory(ProductCategoryLevelThree category) {
        this.category = category;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public Long getImageDetail() {
        return imageDetail;
    }

    public void setImageDetail(Long imageDetail) {
        this.imageDetail = imageDetail;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public BigDecimal getPriceMoney() {
        return priceMoney;
    }

    public void setPriceMoney(BigDecimal priceMoney) {
        this.priceMoney = priceMoney;
    }

    public int getPricePoints() {
        return pricePoints;
    }

    public void setPricePoints(int pricePoints) {
        this.pricePoints = pricePoints;
    }

    public int getPointsGainedPerQuestion() {
        return pointsGainedPerQuestion;
    }

    public void setPointsGainedPerQuestion(int pointsGainedPerQuestion) {
        this.pointsGainedPerQuestion = pointsGainedPerQuestion;
    }

    public ProductPriceSource getPriceSource() {
        return priceSource;
    }

    public void setPriceSource(ProductPriceSource priceSource) {
        this.priceSource = priceSource;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    @Transient
    public int getRemainingQuantity() {
        return this.stockQuantity - (this.orderQuantity + this.loss);
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public boolean isWelcomeProduct() {
        return welcomeProduct;
    }

    public void setWelcomeProduct(boolean welcomeProduct) {
        this.welcomeProduct = welcomeProduct;
    }

    public boolean isSurpriseProduct() {
        return surpriseProduct;
    }

    public void setSurpriseProduct(boolean surpriseProduct) {
        this.surpriseProduct = surpriseProduct;
    }

    public boolean isOrderableProduct() {
        return orderableProduct;
    }

    public void setOrderableProduct(boolean orderableProduct) {
        this.orderableProduct = orderableProduct;
    }

    public ProductClassifier getClassifier() {
        return classifier;
    }

    public void setClassifier(ProductClassifier classifier) {
        this.classifier = classifier;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public QuizQuestion getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(QuizQuestion quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", shortDescription="
                + shortDescription + ", description=" + description
                + ", country=" + country + ", brand=" + brand + ", barcode="
                + barcode + ", slug=" + slug + "]";
    }
}