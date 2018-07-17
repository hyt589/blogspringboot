package com.example.blog.controller;

import com.example.blog.dao.UserRepository;
import com.example.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("/user")
public class UserController {

    private static final int USERNAME_MAX_LENGTH = 32;

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/create")
    public String createUser(@ModelAttribute("message") String message) {
        return "createUser";
    }

    @PostMapping("/create")
    public String signUp(@RequestParam("Username") String username,
                         @RequestParam("Password") String password,
                         @RequestParam("Confirm Password") String confirmPassword,
                         @RequestParam("Email Address") String emailAddress,
                         final RedirectAttributes redirectAttributes) {
        if (username.length() > USERNAME_MAX_LENGTH ||
                username.length() == 0 ||
                password.length() == 0 ||
                confirmPassword.length() == 0 ||
                emailAddress.length() == 0 ||
                !password.equals(confirmPassword) ||
                userRepository.existsByEmailAddress(emailAddress)) {

            redirectAttributes.addFlashAttribute("message", "Invalid information");
            return "redirect:/user/create";
        }

        User user = new User(username, password, emailAddress);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "Account created");
        return "redirect:/login";
    }

    @GetMapping("/create")
    public String signUpReEnter(@ModelAttribute("message") String message){
        return "createUser";
    }

    @RequestMapping("/login")
    public String login(@RequestParam("email address") String emailAddress,
                        @RequestParam("password") String password,
                        HttpSession session,
                        final RedirectAttributes redirectAttributes) {
        if (!userRepository.existsByEmailAddress(emailAddress)) {
            redirectAttributes.addFlashAttribute("message", "Email not registered");
            return "redirect:/login";
        }
        User user = userRepository.findByEmailAddress(emailAddress);
        if (user.auth(password)) {
            session.setAttribute("CURRENT_USER", userRepository.findByEmailAddress(emailAddress));
            redirectAttributes.addFlashAttribute("message", "Session logged in");
            return "redirect:/";
        }else {
            redirectAttributes.addFlashAttribute("message", "Invalid password");
            return "redirect:/login";
        }
    }

    @GetMapping("/sign-in-page")
    public String signIn() {
        return "login";
    }


}
