package com.ltech.bidding.repository;

import com.ltech.bidding.model.Content;

import java.util.List;

public interface BaseContentRepository<T> {
    List<T> findByContentKeyIdOrderByPriorityAsc(String id);
}
