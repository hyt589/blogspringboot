package com.example.blog.controller;

import com.example.blog.dao.PostRepository;
import com.example.blog.dao.UserRepository;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/")
    public String index(@ModelAttribute("message") String message, Model model) {
        int i = 1;
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("users", userRepository);
        return "index";
    }


}
