package com.irhsm.HealthCentreService.service;

import com.irhsm.HealthCentreService.entity.*;
import com.irhsm.HealthCentreService.repository.FacilityRepository;
import com.irhsm.HealthCentreService.repository.HealthCentreRepository;
import com.irhsm.HealthCentreService.repository.HealthWorkerRepository;   // NEW
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CentreService {

    private final HealthCentreRepository healthCentreRepository;
    private final FacilityRepository facilityRepository;
    private final HealthWorkerRepository healthWorkerRepository;   // NEW

    public List<HealthCentre> getAll() {
        return healthCentreRepository.findAll();
    }

    public HealthCentre getById(Long id) {
        return healthCentreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centre not found: " + id));
    }

    public List<HealthCentre> searchByName(String name) {
        return healthCentreRepository.findByNameContainingIgnoreCase(name);
    }

    // NEW
    public List<HealthCentre> searchByVillage(String village) {
        if (village == null || village.isBlank()) {
            return healthCentreRepository.findAll();
        }
        return healthCentreRepository.findByVillage(village);
    }

    public HealthCentre save(HealthCentre centre) {
        return healthCentreRepository.save(centre);
    }

    public void delete(Long id) {
        healthCentreRepository.deleteById(id);
    }

    public List<Facility> getFacilities(Long centreId) {
        return facilityRepository.findByCentreId(centreId);
    }

    public List<Facility> getPendingFacilities() {
        return facilityRepository.findByStatusNot(FacilityStatus.AVAILABLE);
    }

    // NEW
    public List<HealthWorker> getActiveWorkers(Long centreId) {
        return healthWorkerRepository.findByCentreIdAndActiveTrue(centreId);
    }

    public Facility updateFacilityStatus(Long id, FacilityStatus status) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facility not found: " + id));
        facility.setStatus(status);
        facility.setLastupdated(LocalDate.now());
        return facilityRepository.save(facility);
    }

    // Used by the dashboard: status comes as text, remarks are optional
    public Facility updateFacilityStatus(Long id, String statusText, String remarks) {
        FacilityStatus status;
        try {
            status = FacilityStatus.valueOf(statusText.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown status: " + statusText + " (use AVAILABLE, UNAVAILABLE or UNDER_REPAIR)");
        }
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Facility " + id + " not found"));
        facility.setStatus(status);
        if (remarks != null && !remarks.isBlank()) {
            facility.setRemarks(remarks);
        }
        facility.setLastupdated(LocalDate.now());
        return facilityRepository.save(facility);
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new HashMap<>();
        // "total" and "pending" are the two keys every service sends to the dashboard
        summary.put("total", healthCentreRepository.count());
        summary.put("pending", facilityRepository.countByStatusNot(FacilityStatus.AVAILABLE));
        summary.put("totalCentres", healthCentreRepository.count());
        summary.put("totalFacilities", facilityRepository.count());
        summary.put("active", healthCentreRepository.countByStatus(CentreStatus.ACTIVE));
        summary.put("facilityIssues", facilityRepository.countByStatusNot(FacilityStatus.AVAILABLE));
        return summary;
    }
}