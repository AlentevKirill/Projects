package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Notice;
import ru.itmo.wp.form.UserCredentials;
import ru.itmo.wp.service.NoticeService;
import ru.itmo.wp.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class NoticePage extends Page{

    private final NoticeService noticeService;

    public NoticePage(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/notice")
    public String noticeGet(Model model) {
        model.addAttribute("noticeForm", new Notice());
        return "NoticePage";
    }

    @PostMapping("/notice")
    public String noticePost(@Valid @ModelAttribute("noticeForm") Notice noticeForm,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "NoticePage";
        }

        noticeService.save(noticeForm);

        return "redirect:/notice";
    }
}
