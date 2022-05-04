package com.romedawg.controller;

import com.romedawg.domain.todo.Todo;
import com.romedawg.repository.Metra.TripRepository;
import com.romedawg.schedules.MetraUpdates;
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
    TripRepository tripRepository;

    @GetMapping("/metra")
    public String todo()  {
        List<String> BNSFIds = tripRepository.getTripIdByStopId("HINSDALE");
        // Chicago Union Station
        List<String> cusArrival = tripRepository.getArrivalByCUSTripID(BNSFIds);
        List<String> hinsdaleArriaval = tripRepository.getArrivalByHinsdaleTripID(BNSFIds);




//        System.out.println(BNSFIds);
        System.out.println("BNSFID length: " + BNSFIds.size());
        System.out.println("hinsdale length: " + hinsdaleArriaval.size() + " CUS length: " + cusArrival.size());
        System.out.println(hinsdaleArriaval);
        System.out.println(cusArrival);
        return "pages/metra";
    }
}
