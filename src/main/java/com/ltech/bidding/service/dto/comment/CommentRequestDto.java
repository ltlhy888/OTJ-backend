package com.ltech.bidding.service.dto.comment;

import lombok.Data;

@Data
public class CommentRequestDto {
    private String msg;
    private Integer rate;
    private Long commentSetId;
}
