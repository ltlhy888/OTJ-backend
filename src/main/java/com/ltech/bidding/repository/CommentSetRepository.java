package com.ltech.bidding.repository;

import com.ltech.bidding.model.CommentSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentSetRepository extends JpaRepository<CommentSet, Long> {
}
