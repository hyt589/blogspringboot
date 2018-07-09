package com.example.blog.controller;

import com.example.blog.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginPage(@RequestParam("next") Optional<String> next,
                            @ModelAttribute("message") String message) {
        return "login";
    }

    @PostMapping
    public String login(@RequestParam("next") Optional<String> next, User user, HttpSession session,
                        final RedirectAttributes redirectAttributes) {
        session.setAttribute("CURRENT_USER", user);
        redirectAttributes.addFlashAttribute("message", "Session logged in");
        return "redirect:".concat(next.orElse("/"));
    }
}
