package com.example.blog.controller;

import com.example.blog.dao.PostRepository;
import com.example.blog.dao.UserRepository;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/create")
    public String createPost() {
        return "createPost";
    }

    @RequestMapping("/create/submit")
    public String submission(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             HttpSession session) {
        User user = (User)session.getAttribute("CURRENT_USER");
        Post post = new Post(user.getEmailAddress(), title, content);
        postRepository.save(post);
        return "redirect:/";
    }

    @RequestMapping("/listing/postId")
    public String postFromIndex(@RequestParam long postId,
                                Model model) {
        Post post = postRepository.findById(postId);
        User author = userRepository.findByEmailAddress(post.getAuthor());
        if (postRepository.existsById(postId)) {
            model.addAttribute("post", post);
            model.addAttribute("author", author);
            return "post";
        } else {
            model.addAttribute("message", "Post does not exist");
            return "error";
        }

    }
}
