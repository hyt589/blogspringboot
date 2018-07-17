package com.example.blog.controller;

import com.example.blog.dao.PostRepository;
import com.example.blog.dao.UserRepository;
import com.example.blog.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {

//    @Autowired
//    PostRepository postRepository;
//
//    @Autowired
//    UserRepository userRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public IndexController(final PostRepository postRepository,
                           final UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping("/")
    public String index(@ModelAttribute("message") String message, Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        int maxPage = posts.getTotalPages();
        model.addAttribute("posts", posts);
        model.addAttribute("users", userRepository);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("maxPage", maxPage);
        return "index";
    }


}
