package com.romedawg.controller;

import com.romedawg.domain.Metra.customSchedules.HinsdaleSchedule;
import com.romedawg.repository.Metra.customSchedules.HinsdaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MetraController {

    private static final Logger log = LoggerFactory.getLogger(MetraController.class);

    @Autowired
    HinsdaleRepository hinsdaleRepository;

    @GetMapping("/metra")
    public String metra(Model model)  {

        List<HinsdaleSchedule> hinsdaleSchedule = hinsdaleRepository.findAllByDepartureLocation("HINSDALE");
        model.addAttribute("destination", "Chicago Union Station");

        model.addAttribute("stops", hinsdaleSchedule);
        return "pages/metra";
    }

    @GetMapping("/metraToChicago")
    public String metraToChicago(Model model)  {

        List<HinsdaleSchedule> hinsdaleSchedule = hinsdaleRepository.findAllByDepartureLocation("HINSDALE");
        model.addAttribute("destination", "Chicago Union Station");

        model.addAttribute("stops", hinsdaleSchedule);
        return "pages/metra";
    }

    @GetMapping("/metraFromChicago")
    public String metraFromChicago(Model model)  {

        List<HinsdaleSchedule> hinsdaleSchedule = hinsdaleRepository.findAllByDepartureLocation("CUS");
        model.addAttribute("destination", "Hinsdale");


        model.addAttribute("stops", hinsdaleSchedule);
        return "pages/metra";
    }
}
