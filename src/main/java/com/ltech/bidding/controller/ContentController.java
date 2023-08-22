package com.ltech.bidding.controller;

import com.ltech.bidding.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @GetMapping("/top/{contentKey}")
    public String getContent(String contentKey) {
        return contentService.get(contentKey);
    }
}
