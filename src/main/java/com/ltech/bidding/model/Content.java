package com.ltech.bidding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltech.bidding.model.enumeration.ContentType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="content")
public class Content extends BaseContent{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonIgnore
    private String id;

    @Column(nullable = false)
    @Lob
    private String content;
}
