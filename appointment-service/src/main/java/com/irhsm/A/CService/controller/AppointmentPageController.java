package com.irhsm.A.CService.controller;

import com.irhsm.A.CService.client.CentreClient;
import com.irhsm.A.CService.entity.Appointment;
import com.irhsm.A.CService.entity.AppointmentStatus;
import com.irhsm.A.CService.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentPageController {

    private final AppointmentService appointmentService;
    private final CentreClient centreClient;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("appointments", appointmentService.getAll());
        return "appointments/list";
    }

    @GetMapping("/new")
    public String newForm(@RequestParam(required = false) Long centreId, Model model) {
        Appointment appointment = new Appointment();
        appointment.setCentreId(centreId);
        model.addAttribute("appointment", appointment);
        loadOptions(centreId, model);
        return "appointments/form";
    }

    @PostMapping("/book")
    public String book(@Valid @ModelAttribute("appointment") Appointment appointment,
                       BindingResult result, Model model) {
        if (!result.hasErrors()) {
            try {
                appointmentService.book(appointment);
                return "redirect:/appointments";
            } catch (IllegalArgumentException | IllegalStateException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        loadOptions(appointment.getCentreId(), model);
        return "appointments/form";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        appointmentService.updateStatus(id, status);
        return "redirect:/appointments";
    }

    private void loadOptions(Long centreId, Model model) {
        try {
            model.addAttribute("centres", centreClient.getCentres());
            model.addAttribute("workers", centreId == null ? List.of() : centreClient.getWorkers(centreId));
        } catch (IllegalStateException e) {
            model.addAttribute("centres", List.of());
            model.addAttribute("workers", List.of());
            model.addAttribute("error", e.getMessage());
        }
    }
}