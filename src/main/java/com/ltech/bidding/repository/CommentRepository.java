package com.ltech.bidding.repository;

import com.ltech.bidding.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select c from Comment c join CommentSet cs on cs.id = c.commentSet where cs.id = :commentSetId")
    Page<Comment> findByCommentSetId(Long commentSetId, Pageable pageable);
}
