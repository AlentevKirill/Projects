package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Role;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.security.AnyRole;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.PostService;
import ru.itmo.wp.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;

    public PostPage(PostService postService) {
        this.postService = postService;
    }

    @Guest
    @GetMapping("/post/{id}")
    public String postById(@PathVariable("id") String postId, Model model, HttpSession session) {
        long id;
        try {
            id = Long.parseLong(postId);
        } catch (NumberFormatException ignored) {
            putMessage(session, "Post is not found");
            return "redirect:/";
        }
        Post post = postService.findById(id);
        if (post != null) {
            model.addAttribute("postById", post);
            model.addAttribute("comment", new Comment());
        }

        return "PostPage";
    }

    @AnyRole({Role.Name.WRITER, Role.Name.ADMIN})
    @PostMapping("/post/{id}")
    public String writeComment(@PathVariable("id") String postId,
                               @Valid @ModelAttribute("comment") Comment comment,
                               BindingResult bindingResult,
                               Model model,
                               HttpSession httpSession) {

        Post post;
        try {
            long id = Long.parseLong(postId);
            post = postService.findById(id);
            if (bindingResult.hasErrors()) {
                model.addAttribute("postById", post);
                return "PostPage";
            }
            postService.writeComment(getUser(httpSession), post, comment);
            putMessage(httpSession, "You published new post");
            return "redirect:/post/" + post.getId();
        } catch (NumberFormatException ignored) {
            putMessage(httpSession, "Post is not found");
            return "redirect:/";
        }
    }
}
