package com.ltech.bidding.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BaseContent extends BaseEntity {
    @Column(name = "content_key_id")
    private String contentKeyId;
    @Column
    private Integer priority;
}
