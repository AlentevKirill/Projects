package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TalksPage extends Page{
    @Override
    protected void action(HttpServletRequest request, Map<String, Object> view) {
        User user = getUser();
        if (user == null) {
            setMessage("It is impossible to correspond without being an authenticated user");
            throw new RedirectException("/index");
        }
        view.put("talks", userService.findAllMessages(user.getId()));
        view.put("users", userService.findAll());
    }

    private void talk(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        String recipientUserLogin = request.getParameter("recipientUserLogin");
        String text = request.getParameter("text");

        userService.validateTalk(recipientUserLogin, text);

        Talk talk = new Talk();
        talk.setSourceUserId(getUser().getId());
        talk.setTargetUserId(userService.findByLoginOrEmail(recipientUserLogin, "login").getId());
        talk.setText(text);

        userService.saveTalk(talk);

        throw new RedirectException("/talks");
    }
}
