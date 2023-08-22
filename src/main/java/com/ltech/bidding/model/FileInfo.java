package com.ltech.bidding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltech.bidding.model.enumeration.FileProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="file_info")
public class FileInfo extends BaseContent{
    @Id
    private String id;
    @Column
    private String name;
    @Column(nullable = false)
    private String url;
    @Column(name="file_type", nullable = false)
    private String fileType;
    @Column
    private Long size;
    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private FileProvider provider;
}
