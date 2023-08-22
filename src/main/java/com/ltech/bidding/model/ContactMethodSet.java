package com.ltech.bidding.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="contact_method_set")
public class ContactMethodSet {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "contact_method_set_id", nullable = false)
    private Set<ContactMethod> contactMethods = new HashSet<>();
}
