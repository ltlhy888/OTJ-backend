package com.ltech.bidding.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Integer rate;
    @Column(nullable = false, length = 200)
    private String msg;
    @Column(nullable = false)
    private Boolean enabled;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_set_id", nullable = false)
    private CommentSet commentSet;
}
