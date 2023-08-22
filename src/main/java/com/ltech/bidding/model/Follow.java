package com.ltech.bidding.model;

import com.ltech.bidding.model.enumeration.FollowType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="follow")
public class Follow extends BaseContent{
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "which_id")
    private String whichId;
    @Column(name="which_type")
    @Enumerated(EnumType.STRING)
    private FollowType whichType;
}
