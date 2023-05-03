package com.romedawg.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexContoller {

    @RequestMapping("/")
    public String index(){
        return "pages/home";
    }

    @RequestMapping("/home")
    public String home(){
        return "pages/home";
    }

    @Timed()
    @RequestMapping("/prometheusTesting")
    public String prometheusTesting(){
        return "pages/promethes";
    }
}
