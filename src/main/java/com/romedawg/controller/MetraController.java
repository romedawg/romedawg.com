package com.romedawg.controller;

import com.romedawg.domain.todo.Todo;
import com.romedawg.schedules.MetraUpdates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MetraController {

    private static final Logger log = LoggerFactory.getLogger(MetraController.class);

    @GetMapping("/metra")
    public String todo()  {
        return "pages/metra";
    }
}
