package com.ltech.bidding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltech.bidding.model.enumeration.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "content_key")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ContentKey extends BaseEntity{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name="content_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType ContentType;
    @Column
    private String description;
}
