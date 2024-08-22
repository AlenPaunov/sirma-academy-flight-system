package com.academy.flightsystem.client.controller;

import com.academy.flightsystem.client.model.Flight;
import com.academy.flightsystem.client.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/")
    public String home(){
        return "home";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/flights")
    public String flights(Model model){
        List<Flight> flights = flightService.getAllFlights();
         model.addAttribute("flights", flights);
        return "flights";
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @GetMapping("/user")
//    public String userEndpoint(Model model){
//        return "user";
//    }
//
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("/admin")
//    public String adminEndpoint(Model model){
//        return "admin";
//    }



}
