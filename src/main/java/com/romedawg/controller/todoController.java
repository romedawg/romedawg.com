package com.romedawg.controller;

import com.romedawg.domain.todo.Todo;
import com.romedawg.repository.Metra.TodoRepository;
import com.romedawg.schedules.MetraUpdates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Collections;

@Controller
public class todoController {

    private static final Logger log = LoggerFactory.getLogger(MetraUpdates.class);

    private MetraUpdates metraUpdates;

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/todo")
    public String todo(Model model)  {
        model.addAttribute("todo", new Todo() );
        return "pages/todo";
    }

    @PostMapping(value = "/todo")
    public String todoPost(@ModelAttribute Todo todo){

        log.info("Post todo item");

        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        String taskDescription = todo.getTaskDescription();

        todo.setDate(date);
        todo.setTaskDescription(taskDescription.strip());
        todo.setActive(Boolean.TRUE);

        todoRepository.save(todo);
        return "pages/todo";
    }

    @PostMapping(value = "/todo/{id}")
    public String todoByID(Model model, @PathVariable Integer id){
        model.addAttribute("tasks", todoRepository.findAllById(Collections.singleton(id)));
        return "pages/todoByID";
    }



}
