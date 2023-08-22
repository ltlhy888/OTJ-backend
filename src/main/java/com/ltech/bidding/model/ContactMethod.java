package com.ltech.bidding.model;


import com.ltech.bidding.model.enumeration.ContactMethodType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="contact_method")
public class ContactMethod extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private ContactMethodType type;
    @Column(nullable = false)
    private String contact;
    @Column(nullable = false)
    private Boolean enabled;

}
