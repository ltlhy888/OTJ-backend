package com.ltech.bidding.service.dto.general;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonSerialize
public class ErrorDto {
    private String message;
    private String reason;
}
