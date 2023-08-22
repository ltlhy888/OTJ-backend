package com.ltech.bidding.service;

import com.ltech.bidding.model.Content;

public interface ContentService {
    String get(String key);
    Content create(String content, String contentKeyId, Integer Priority);

    String update();
}
