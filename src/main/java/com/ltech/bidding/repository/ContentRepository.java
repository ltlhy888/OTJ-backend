package com.ltech.bidding.repository;

import com.ltech.bidding.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, String>, BaseContentRepository<Content> {

}
