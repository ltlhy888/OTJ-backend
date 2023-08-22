package com.ltech.bidding.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name="comment_set")
public class CommentSet {
    @Id
    @GeneratedValue
    private Long id;
}
