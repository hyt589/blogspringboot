package com.example.blog.controller;

import com.example.blog.dao.PostRepository;
import com.example.blog.dao.UserRepository;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final
    PostRepository postRepository;

    private final
    UserRepository userRepository;

    @Autowired
    public SearchController(UserRepository userRepository,
                            PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    @SuppressWarnings("SameReturnValue")
    @RequestMapping("/do")
    public String search(@RequestParam("pattern") String pattern,
                         Model model) {
        List<String> regexList = new ArrayList<>();
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == ' ') {
                regexList.add(regex.toString());
                regex = new StringBuilder();
                continue;
            }
            regex.append(pattern.charAt(i));
        }
        regexList.add(regex.toString());
        List<Pattern> patterns = new ArrayList<>();
        for (String str : regexList
                ) {
            patterns.add(Pattern.compile(str, Pattern.CASE_INSENSITIVE));
        }
        List<Post> posts = postRepository.findAll();
        List<Post> postsToShow = new ArrayList<>();
        List<User> users = userRepository.findAll();
        List<User> usersToShow = new ArrayList<>();
        for (Pattern p : patterns
                ) {
            for (Post post : posts
                    ) {
                Matcher matcherTitle = p.matcher(post.getTitle());
                Matcher matcherContent = p.matcher(post.getContent());
                Matcher matcherAuthor = p.matcher(userRepository.findByEmailAddress(post.getAuthor()).getUsername());
                if (matcherTitle.find() || matcherContent.find() || matcherAuthor.find()) {
                    postsToShow.add(post);
                }

            }
            for (User user : users
                    ) {
                Matcher matcher = p.matcher(user.getUsername());
                if (matcher.find()) {
                    usersToShow.add(user);
                }
            }
        }

        model.addAttribute("posts", postsToShow);
        model.addAttribute("users", usersToShow);
        model.addAttribute("userRepo", userRepository);


        return "search";
    }
}
