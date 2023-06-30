package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage {

    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");;
        if (user == null) {
            request.getSession().setAttribute("message",
                    "It is impossible to correspond without being an authenticated user");
            throw new RedirectException("/index");
        }
    }

    private void article(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        String title = request.getParameter("title");
        String text = request.getParameter("text");

        articleService.validateArticle(title, text);

        Article article = new Article();
        article.setUserId(((User) request.getSession().getAttribute("user")).getId());
        article.setTitle(title);
        article.setText(text);

        articleService.saveArticle(article);

        throw new RedirectException("/article");
    }
}
