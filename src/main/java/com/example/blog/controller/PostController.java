package com.example.blog.controller;

import com.example.blog.dao.PostRepository;
import com.example.blog.dao.UserRepository;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    @Autowired
    public PostController(final PostRepository postRepository,
                          final UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @SuppressWarnings("SameReturnValue")
    @RequestMapping("/create")
    public String createPost() {
        return "createPost";
    }

    @SuppressWarnings("SameReturnValue")
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

    @RequestMapping("/create/upload")
    public void upLoad(@RequestParam("file") MultipartFile file,
                       HttpSession session) throws Exception {
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        User user = (User)session.getAttribute("CURRENT_USER");
        filename = "src/main/resources/static/upload/"+user.getId() + "_" + filename;
        File saveFile = new File(filename);
        boolean created = saveFile.createNewFile();
        if (created) {
            try (OutputStream outputStream = new FileOutputStream(filename)) {
                outputStream.write(bytes);
            }
        }

    }
}
