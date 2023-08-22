package com.ltech.bidding.controller;

import com.ltech.bidding.model.Comment;
import com.ltech.bidding.model.CommentSet;
import com.ltech.bidding.model.UserPrincipal;
import com.ltech.bidding.repository.CommentRepository;
import com.ltech.bidding.repository.CommentSetRepository;
import com.ltech.bidding.service.dto.comment.CommentRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/comment")
@RestController
public class CommentController {
    private final CommentRepository commentRepository;
    private final CommentSetRepository commentSetRepository;

    public CommentController(CommentSetRepository commentSetRepository,
                             CommentRepository commentRepository) {
        this.commentSetRepository = commentSetRepository;
        this.commentRepository = commentRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Comment comment = new Comment();
        comment.setMsg(commentRequestDto.getMsg());
        comment.setRate(commentRequestDto.getRate());
        CommentSet commentSet = commentSetRepository.findById(commentRequestDto.getCommentSetId()).orElseThrow();
        comment.setCommentSet(commentSet);
        comment.setUser(userPrincipal.user());
        comment.setEnabled(true);
        commentRepository.save(comment);

        return ResponseEntity.ok().body(comment);
    }

    @GetMapping("/{commentSetId}")
    public ResponseEntity<?> getComments(@PathVariable Long commentSetId, Pageable pageable) {
        return ResponseEntity.ok().body(commentRepository.findByCommentSetId(commentSetId,pageable));
    }
}
