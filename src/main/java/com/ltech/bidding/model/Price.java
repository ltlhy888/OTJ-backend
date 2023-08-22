package com.ltech.bidding.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltech.bidding.model.enumeration.PriceStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="price")
public class Price extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long price;
    private String description;
    @Column(length = 50)
    private String currency;
    private int priority;
    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private PriceStatus status;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(name="price_type", nullable = false)
    private String priceType;
}
