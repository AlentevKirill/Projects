package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MyArticlesPage {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");;
        if (user == null) {
            request.getSession().setAttribute("message",
                    "It is impossible to correspond without being an authenticated user");
            throw new RedirectException("/index");
        }
        view.put("articles", articleService.findAllUsersArticle(user.getId()));
    }

    private void changePermission(HttpServletRequest request, Map<String, Object> view) {
        boolean hidden = articleService.find(Long.parseLong(request.getParameter("articleId"))).getHidden(); //ОШИБКА
        articleService.changePermission(Long.parseLong(request.getParameter("articleId")), !hidden);
    }

    /*private void findAllUsersArticle(HttpServletRequest request, Map<String, Object> view) {
        view.put("articles", articleService.findAllUsersArticle(request.getSession().getAttribute()));
    }*/
}
