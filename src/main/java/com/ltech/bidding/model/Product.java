package com.ltech.bidding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ltech.bidding.model.enumeration.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    @Column(length = 2000)
    private String description;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private String location;

    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name = "contact_method_set_id")
    private ContactMethodSet contactMethodSet;

    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name = "comment_set_id")
    private CommentSet commentSet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_price_id")
    private Price currentPrice;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Set<Price> prices = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name = "image_content_id")
    private ContentKey images;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_and_types",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "product_type_id"))
    private Set<ProductType> productTypes = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name = "additional_data_id")
    private ContentKey additionalData;


}
