package com.ltech.bidding.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltech.bidding.model.enumeration.TransactionStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="transaction", indexes = {@Index(name="idx_third_party", columnList = "third_party_id,third_party_type")})
public class Transaction extends BaseEntity {
    @Id
    private String id;
    @Column(length = 50)
    private String currency;
    @Column(length = 50)
    private String status;
    public void setStatus(String status) {
        for(TransactionStatus ts : TransactionStatus.values()) {
            if (ts.name().equals(status)) {
                this.status = status;
                break;
            }
        }
    }
    @Column(name="user_key")
    private String userKey;
    @Column(name="third_party_id")
    private String thirdPartyId;
    @Column(name="third_party_type", length = 50)
    private String thirdPartyType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "price_id", nullable = false)
    private Price price;
}
