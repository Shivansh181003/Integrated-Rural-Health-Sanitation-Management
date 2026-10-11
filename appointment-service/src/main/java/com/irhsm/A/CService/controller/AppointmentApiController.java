package com.irhsm.A.CService.controller;

import com.irhsm.A.CService.entity.Appointment;
import com.irhsm.A.CService.entity.AppointmentStatus;
import com.irhsm.A.CService.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentApiController {

    private final AppointmentService appointmentService;

    @GetMapping
    public List<Appointment> getAll() {
        return appointmentService.getAll();
    }

    @GetMapping("/summary")
    public Map<String, Long> summary() {
        return appointmentService.getSummary();
    }

    @GetMapping("/pending")
    public List<Map<String, Object>> pending() {
        return appointmentService.getPending();
    }

    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Long id) {
        return appointmentService.getById(id);
    }

    @PutMapping("/{id}/status")
    public Appointment updateStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        return appointmentService.updateStatus(id, status);
    }
}