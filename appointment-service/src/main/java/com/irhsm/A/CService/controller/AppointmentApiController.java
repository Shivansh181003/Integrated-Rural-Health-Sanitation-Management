package com.irhsm.A.CService.controller;

import com.irhsm.A.CService.dto.StatusUpdateDto;
import com.irhsm.A.CService.entity.Appointment;
import com.irhsm.A.CService.entity.AppointmentStatus;
import com.irhsm.A.CService.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    // PUT /api/appointments/{id}/status
    // Dashboard sends JSON {"status":"COMPLETED","remarks":"..."}; ?status=COMPLETED also works.
    @PutMapping("/{id}/status")
    public Appointment updateStatus(@PathVariable Long id,
                                    @RequestParam(required = false) String status,
                                    @RequestBody(required = false) StatusUpdateDto body) {
        String text = body != null && body.status() != null ? body.status() : status;
        if (text == null || text.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required");
        }
        AppointmentStatus newStatus;
        try {
            newStatus = AppointmentStatus.valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown status: " + text + " (use BOOKED, CHECKED_IN, COMPLETED or CANCELLED)");
        }
        return appointmentService.updateStatus(id, newStatus);
    }
}