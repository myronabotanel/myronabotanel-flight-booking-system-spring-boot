package com.example.demo.controller;

import com.example.demo.model.Stopover;
import com.example.demo.service.StopoverService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/stopover")
@Validated
public class StopoverController {

    private final StopoverService stopoverService;

    public StopoverController(StopoverService stopoverService) {
        this.stopoverService = stopoverService;
    }

    // Afișează toate stopover-urile
    @GetMapping()
    public String showStopovers(Model model) {
        List<Stopover> stopovers = stopoverService.getAllStopovers();
        model.addAttribute("stopovers", stopovers);
        return "stopovers/list-stopovers";
    }

    // Formular pentru adăugarea unui stopover nou
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("stopover", new Stopover());
        return "stopovers/create-stopover";
    }

    // Adaugă un nou stopover
    @PostMapping("/save")
    public String saveStopover(@Valid @ModelAttribute Stopover stopover) {
        stopoverService.addStopover(stopover);
        return "redirect:/stopover";
    }

    // Formular pentru editarea unui stopover
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Stopover stopover = stopoverService.getStopoverById(id);
            model.addAttribute("stopover", stopover);
            return "stopovers/edit-stopover";
        } catch (NoSuchElementException e) {
            return "redirect:/stopover";
        }
    }

    // Actualizează un stopover existent
    @PostMapping("/update")
    public String updateStopover(@Valid @ModelAttribute Stopover stopover) {
        stopoverService.updateStopover(stopover);
        return "redirect:/stopover";
    }

    // Șterge un stopover
    @GetMapping("/delete/{id}")
    public String deleteStopover(@PathVariable Long id) {
        stopoverService.deleteStopover(id);
        return "redirect:/stopover";
    }
    @GetMapping("/details/{id}")
    public String showStopoverDetails(@PathVariable Long id, Model model) {
        try {
            Stopover stopover = stopoverService.getStopoverById(id);
            model.addAttribute("stopover", stopover);
            return "stopovers/details-stopover"; // Trimite la pagina de detalii
        } catch (NoSuchElementException e) {
            return "redirect:/stopover"; // Dacă escală nu există, redirecționează
        }
    }

}
