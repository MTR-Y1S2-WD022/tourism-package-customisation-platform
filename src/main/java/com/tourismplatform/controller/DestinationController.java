package com.tourismplatform.controller;

import com.tourismplatform.model.Destination;
import com.tourismplatform.service.DestinationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/destinations")
    public String listDestinations(Model model) {
        model.addAttribute("destinations", destinationService.getAllActiveDestinations());
        return "destination/destination-list";
    }

    @GetMapping("/destinations/new")
    public String showAddDestinationForm(Model model) {
        model.addAttribute("destination", new Destination());
        return "destination/destination-form";
    }

    @PostMapping("/destinations/save")
    public String saveDestination(@ModelAttribute("destination") Destination destination) {
        destinationService.saveDestination(destination);
        return "redirect:/destinations";
    }

    @GetMapping("/destinations/edit/{destinationId}")
    public String showEditDestinationForm(@PathVariable int destinationId, Model model) {
        model.addAttribute("destination", destinationService.getDestinationById(destinationId));
        return "destination/destination-form";
    }

    @PostMapping("/destinations/update")
    public String updateDestination(@ModelAttribute("destination") Destination destination) {
        destinationService.updateDestination(destination);
        return "redirect:/destinations";
    }

    @GetMapping("/destinations/delete/{destinationId}")
    public String deactivateDestination(@PathVariable int destinationId) {
        destinationService.deactivateDestination(destinationId);
        return "redirect:/destinations";
    }
}
