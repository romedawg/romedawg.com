package com.romedawg.controller;

import com.romedawg.domain.customSchedules.HinsdaleSchedule;
import com.romedawg.domain.todo.Todo;
import com.romedawg.repository.Metra.TripRepository;
import com.romedawg.repository.Metra.customSchedules.HinsdaleRepository;
import com.romedawg.schedules.MetraUpdates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MetraController {

    private static final Logger log = LoggerFactory.getLogger(MetraController.class);

    @Autowired
    HinsdaleRepository hinsdaleRepository;

    @GetMapping("/metra")
    public String todo(Model model)  {

        // Works but shows up as a String, TODO how to split?
        List<String> hinsdaleSchedule = hinsdaleRepository.getScheduleByDepartureTime();

        model.addAttribute("stops", hinsdaleSchedule);
        return "pages/metra";
    }
}
