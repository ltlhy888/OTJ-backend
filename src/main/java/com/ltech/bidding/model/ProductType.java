package com.ltech.bidding.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name="product_type")
public class ProductType {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private Boolean available;
    @Column(nullable = false)
    private Boolean display;
    private String description;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_type_children",
            joinColumns = @JoinColumn(name = "product_type_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
    private Set<ProductType> children = new HashSet<>();
}
