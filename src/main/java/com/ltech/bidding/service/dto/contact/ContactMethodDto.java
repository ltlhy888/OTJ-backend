package com.ltech.bidding.service.dto.contact;

import com.ltech.bidding.model.enumeration.ContactMethodType;
import lombok.Data;

import javax.persistence.Column;

@Data
public class ContactMethodDto {

    private String type;

    private String contact;
}
