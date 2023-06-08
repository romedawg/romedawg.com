package com.romedawg.controller;

import com.romedawg.domain.todo.Todo;
import com.romedawg.service.HtmlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
//import org.springframework.web.servlet.ModelAndView;

@Controller
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(MetraController.class);

    @Autowired
    private HtmlService htmlService;

    @GetMapping("/edit")
    public String edit() {
        return "blog/edit";
    }

    @GetMapping("/view")
    public String viewGet() {
        return "blog/view";
    }

    @PostMapping("/view")
    public ModelAndView view(@RequestParam("markdown") String markdown) {
        log.info("view Controller page");
        log.info(markdown);
        ModelAndView modelAndView = new ModelAndView("blog/view");
        String htmlContent = htmlService.markdownToHtml(markdown);
        modelAndView.addObject("markdown", markdown);
        modelAndView.addObject("htmlContent", htmlContent);
        return modelAndView;
//        return "blog/view";
    }

    @GetMapping("/blog")
    public ModelAndView viewblog() throws IOException, URISyntaxException {
        ModelAndView modelAndView = new ModelAndView("blog/view_blog");
        String htmlContent = htmlService.markdownDocument("homepage");
        modelAndView.addObject("htmlContent", htmlContent);
        return modelAndView;
//        return "blog/view";
    }

//    @PostMapping(value = "/todo")
//    public String todoPost(@ModelAttribute Todo todo){
//
//        log.info("Post todo item");
//
//        long currentTime = System.currentTimeMillis();
//        Date date = new Date(currentTime);
//        String taskDescription = todo.getTaskDescription();
//
//        todo.setDate(date);
//        todo.setTaskDescription(taskDescription.strip());
//        todo.setActive(Boolean.TRUE);
//
//        todoRepository.save(todo);
//        return "pages/todo";
//    }
}
