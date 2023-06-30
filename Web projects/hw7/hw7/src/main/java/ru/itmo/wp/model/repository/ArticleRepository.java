package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;

import java.util.List;

public interface ArticleRepository {
    void save(Article article);
    Article find(long id);
    List<Article> findAllArticle();
    List<Article> findAllUsersArticle(long userId);

    void changePermission(long articleId, boolean hidden);
}
