package com.ltech.bidding.service.impl;

import com.ltech.bidding.exception.NotFoundException;
import com.ltech.bidding.model.Content;
import com.ltech.bidding.model.ContentKey;
import com.ltech.bidding.repository.ContentKeyRepository;
import com.ltech.bidding.repository.ContentRepository;
import com.ltech.bidding.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ContentServiceImpl implements ContentService {
    private final ContentRepository contentRepository;

    // Get content by content key
    @Override
    public String get(String key) {
        List<Content> contentList = contentRepository.findByContentKeyIdOrderByPriorityAsc(key);

        if(contentList != null && contentList.isEmpty()) {
            throw new RuntimeException("Not Found");
        }

        Content content = contentList.get(0);

        for(Content c :contentList) {
            content = content.getPriority() > c.getPriority() ? c :content;
        }

        return content.getContent();
    }

    @Override
    public Content create(String content, String contentKeyId, Integer priority) {
        Content contentObject = new Content();
        contentObject.setPriority(priority);
        contentObject.setContentKeyId(contentKeyId);
        contentObject.setContent(content);

        return contentRepository.save(contentObject);
    }

    @Override
    public String update() {
        return null;
    }
}
