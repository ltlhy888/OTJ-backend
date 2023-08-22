package com.ltech.bidding.service.dto.contact;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SendEmailDto implements Serializable {
    @NotNull
    private String to;
    @NotNull
    private String subject;
    @NotNull
    private String content;
}
