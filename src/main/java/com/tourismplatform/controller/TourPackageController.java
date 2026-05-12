package com.tourismplatform.controller;

import com.tourismplatform.model.TourPackage;
import com.tourismplatform.service.TourPackageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.tourismplatform.service.DestinationService;
import java.util.List;

@Controller
public class TourPackageController {

    private final TourPackageService tourPackageService;
    private final DestinationService destinationService;

    public TourPackageController(TourPackageService tourPackageService,
                                 DestinationService destinationService) {
        this.tourPackageService = tourPackageService;
        this.destinationService = destinationService;
    }

    @GetMapping("/packages")
    public String listPackages(Model model) {
        model.addAttribute("packages", tourPackageService.getAllActivePackages());
        return "package/package-list";
    }

    @GetMapping("/packages/new")
    public String showAddPackageForm(Model model) {
        model.addAttribute("tourPackage", new TourPackage());
        return "package/package-form";
    }

    @PostMapping("/packages/save")
    public String savePackage(@ModelAttribute("tourPackage") TourPackage tourPackage) {
        tourPackageService.saveTourPackage(tourPackage);
        return "redirect:/packages";
    }

    @GetMapping("/packages/view/{packageId}")
    public String viewPackageDetails(@PathVariable int packageId, Model model) {
        model.addAttribute("tourPackage", tourPackageService.getTourPackageById(packageId));
        return "package/package-details";
    }

    @GetMapping("/packages/edit/{packageId}")
    public String showEditPackageForm(@PathVariable int packageId, Model model) {
        model.addAttribute("tourPackage", tourPackageService.getTourPackageById(packageId));
        return "package/package-form";
    }

    @PostMapping("/packages/update")
    public String updatePackage(@ModelAttribute("tourPackage") TourPackage tourPackage) {
        tourPackageService.updateTourPackage(tourPackage);
        return "redirect:/packages";
    }

    @GetMapping("/packages/delete/{packageId}")
    public String deactivatePackage(@PathVariable int packageId) {
        tourPackageService.deactivateTourPackage(packageId);
        return "redirect:/packages";
    }
    @GetMapping("/packages/{packageId}/assign-destinations")
    public String showAssignDestinationsPage(@PathVariable int packageId, Model model) {
        model.addAttribute("tourPackage", tourPackageService.getTourPackageById(packageId));
        model.addAttribute("destinations", destinationService.getAllActiveDestinations());
        model.addAttribute("assignedDestinationIds", tourPackageService.getAssignedDestinationIds(packageId));

        return "package/assign-destinations";
    }

    @PostMapping("/packages/{packageId}/assign-destinations")
    public String saveAssignedDestinations(@PathVariable int packageId,
                                           @RequestParam(value = "destinationIds", required = false) List<Integer> destinationIds) {
        tourPackageService.updatePackageDestinations(packageId, destinationIds);

        return "redirect:/packages/view/" + packageId;
    }
}
