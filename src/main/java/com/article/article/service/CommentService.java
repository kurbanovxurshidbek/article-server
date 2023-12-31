package com.article.article.service;

import com.article.article.exception.ResourceNotFoundException;
import com.article.article.model.common.Header;
import com.article.article.model.dto.CommentDto;
import com.article.article.model.dto.UserAccountDto;
import com.article.article.model.entity.Article;
import com.article.article.model.entity.Comment;
import com.article.article.model.entity.UserAccount;
import com.article.article.model.request.CommentRequest;
import com.article.article.repository.ArticleRepository;
import com.article.article.repository.CommentRepository;
import com.article.article.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final UserAccountRepository userAccountRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public CommentDto saveComment(Header<CommentRequest> dto) {
        CommentRequest commentRequest = dto.getData();

        UserAccount userAccount = userAccountRepository.findByUsername(commentRequest.username()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Article article = articleRepository.findById(commentRequest.articleId()).orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        CommentDto commentDto = commentRequest.toDto(UserAccountDto.from(userAccount));

        Comment comment = commentDto.toEntity(article, userAccount);

        // Provide parent comment id is it is available
        if (commentRequest.parentCommentId() != null) {
            Optional<Comment> optionalParentComment = commentRepository.findById(commentRequest.parentCommentId());
            optionalParentComment.ifPresent(comment::setParentComment);
        }

        Comment savedComment = commentRepository.save(comment);
        return CommentDto.from(savedComment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Page<CommentDto> getComments(Long articleId, Pageable paging) {
        return commentRepository.findByArticle_Id(articleId, paging).map(CommentDto::from);
    }
}
