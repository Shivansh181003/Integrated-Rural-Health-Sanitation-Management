package com.irhsm.A.CService.service;

import com.irhsm.A.CService.client.CentreClient;
import com.irhsm.A.CService.dto.CentreDto;
import com.irhsm.A.CService.dto.WorkerDto;
import com.irhsm.A.CService.entity.Appointment;
import com.irhsm.A.CService.entity.AppointmentStatus;
import com.irhsm.A.CService.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CentreClient centreClient;

    public List<Appointment> getAll() {
        return appointmentRepository.findAllByOrderByAppointmentDateDescTokenNoAsc();
    }

    public Appointment getById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));
    }

    public Appointment book(Appointment appointment) {
        CentreDto centre = centreClient.getCentre(appointment.getCentreId());

        WorkerDto worker = centreClient.getWorkers(appointment.getCentreId()).stream()
                .filter(w -> w.getId().equals(appointment.getWorkerId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("This worker is not active at the chosen centre"));

        long alreadyBooked = appointmentRepository.countByCentreIdAndAppointmentDate(
                appointment.getCentreId(), appointment.getAppointmentDate());

        appointment.setCentreName(centre.getName());
        appointment.setVillage(centre.getVillage());
        appointment.setWorkerName(worker.getName());
        appointment.setTokenNo((int) alreadyBooked + 1);
        appointment.setStatus(AppointmentStatus.BOOKED);
        return appointmentRepository.save(appointment);
    }

    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = getById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new HashMap<>();
        summary.put("total", appointmentRepository.count());
        summary.put("booked", appointmentRepository.countByStatus(AppointmentStatus.BOOKED));
        summary.put("completed", appointmentRepository.countByStatus(AppointmentStatus.COMPLETED));
        summary.put("cancelled", appointmentRepository.countByStatus(AppointmentStatus.CANCELLED));
        return summary;
    }

    public List<Map<String, Object>> getPending() {
        List<Map<String, Object>> pending = new ArrayList<>();
        for (Appointment a : appointmentRepository.findByStatusIn(
                List.of(AppointmentStatus.BOOKED, AppointmentStatus.CHECKED_IN))) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("source", "APPOINTMENT");
            item.put("id", a.getId());
            item.put("title", "Consultation - " + a.getResidentName() + " (token " + a.getTokenNo() + ")");
            item.put("village", a.getVillage());
            item.put("status", a.getStatus());
            item.put("priority", "MEDIUM");
            item.put("date", a.getAppointmentDate());
            pending.add(item);
        }
        return pending;
    }
}