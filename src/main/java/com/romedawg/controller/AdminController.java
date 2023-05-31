package com.romedawg.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class AdminController {

    @RequestMapping("/admin")
    public String home(){
        return "pages/admin";
    }
}
