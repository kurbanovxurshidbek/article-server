package com.article.article.model.dto;

import com.article.article.model.entity.Article;
import com.article.article.model.entity.Comment;
import com.article.article.model.entity.UserAccount;

public record CommentDto(
        Long id,
        Long articleId,
        UserAccountDto userAccountDto,
        Long parentCommentId,
        String content
) {

    public static CommentDto of(Long articleId, UserAccountDto userAccountDto, String content) {
        return CommentDto.of(null, articleId, userAccountDto, null, content);
    }

    public static CommentDto of(Long articleId, UserAccountDto userAccountDto, Long parentCommentId, String content) {
        return CommentDto.of(null, articleId, userAccountDto, parentCommentId, content);
    }

    public static CommentDto of(Long id, Long articleId, UserAccountDto userAccountDto, Long parentCommentId, String content) {
        return new CommentDto(id, articleId, userAccountDto, parentCommentId, content);
    }

    public static CommentDto from(Comment entity) {

        return new CommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getParentComment() != null ? entity.getParentComment().getId() : null,
                entity.getContent()
        );
    }

    public Comment toEntity(Article article, UserAccount userAccount) {
        return Comment.of(
                article,
                userAccount,
                content
        );
    }
}
