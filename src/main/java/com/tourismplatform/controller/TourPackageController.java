package com.tourismplatform.controller;

import com.tourismplatform.model.TourPackage;
import com.tourismplatform.service.TourPackageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TourPackageController {

    private final TourPackageService tourPackageService;

    public TourPackageController(TourPackageService tourPackageService) {
        this.tourPackageService = tourPackageService;
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
}
