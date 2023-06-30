package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;

import java.util.List;

public class ArticleService {
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public void validateArticle(String title, String text) throws ValidationException {
        if (Strings.isNullOrEmpty(title.trim())) {
            throw new ValidationException("Title is required");
        }
        if (title.length() > 254) {
            throw new ValidationException("Title can't be longer than 254 letters");
        }

        if (Strings.isNullOrEmpty(text.trim())) {
            throw new ValidationException("Text is required");
        }
        if (text.trim().length() > 254) {
            throw new ValidationException("Text can't be longer than 254 characters");
        }
    }

    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    public List<Article> findAllArticle() {
        return articleRepository.findAllArticle();
    }

    public List<Article> findAllUsersArticle(long userId) {
        return articleRepository.findAllUsersArticle(userId);
    }

    public void changePermission(long articleId, boolean hidden) {
        articleRepository.changePermission(articleId, hidden);
    }

    public Article find(long id) {
        return articleRepository.find(id);
    }
}
