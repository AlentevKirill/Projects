package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.service.NoticeService;
import ru.itmo.wp.service.UserService;

@Controller
public class UserPage extends Page{

    private final UserService userService;

    public UserPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public String userById(@PathVariable("id") String userId, Model model) {
        try {
            long id = Long.parseLong(userId);
            User user = userService.findById(id);
            if (user != null) {
                model.addAttribute("userr", userService.findById(id));
            }
        } catch (NumberFormatException ignored) {}
        return "UserPage";
    }

}
