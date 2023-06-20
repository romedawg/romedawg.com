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

    @PostMapping("/viewblog")
    public ModelAndView viewPostSpecificBlog(@RequestParam("markdown") String markdown) throws IOException, URISyntaxException {
        log.info("WE ARE HITTING viewblog controller mapping");
        log.info("Value of markdown object: " + markdown);
        ModelAndView modelAndView = new ModelAndView("blog/view_blog");
        String htmlContent = htmlService.markdownDocument(markdown);
        modelAndView.addObject("htmlContent", htmlContent);
        return modelAndView;
    }

}
