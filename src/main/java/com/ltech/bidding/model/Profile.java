package com.ltech.bidding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltech.bidding.model.enumeration.ContactMethodType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "profile")
@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_method_set_id")
    private ContactMethodSet contactMethodSet;
    @Column(name="preferred_contact_method")
    private String preferredContactMethod;
    @Column
    private String city;
    @Column
    private String country;
    @Column
    private String address;
    @Column
    private String region;
}
