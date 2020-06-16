package com.tracking.corona.controller;


import com.tracking.corona.models.LocationState;
import com.tracking.corona.service.CoronaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private CoronaDataService coronaDataService;


    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<LocationState> allStats = coronaDataService.getAllStats();
        model.addAttribute("totalCases", allStats.stream().mapToInt(stat -> stat.getLastTotalCases()).sum());
        model.addAttribute("locationStats", allStats.stream()
                .sorted(Comparator.comparing(LocationState::getCountry))
                .collect(Collectors.toList()));

        model.addAttribute("totalnewCase",allStats.stream().mapToInt(stat->stat.getNewTotalCases()).sum());

        return "home";

    }


}
