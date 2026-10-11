package com.irhsm.HealthCentreService.controller;

import com.irhsm.HealthCentreService.dto.PendingItemDto;
import com.irhsm.HealthCentreService.dto.StatusUpdateDto;
import com.irhsm.HealthCentreService.entity.Facility;
import com.irhsm.HealthCentreService.entity.FacilityStatus;
import com.irhsm.HealthCentreService.entity.HealthCentre;
import com.irhsm.HealthCentreService.entity.HealthWorker;   // NEW
import com.irhsm.HealthCentreService.service.CentreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/centres")
@RequiredArgsConstructor
public class CentreApiController {

    private final CentreService centreService;

    @GetMapping
    public List<HealthCentre> getAll() {
        return centreService.getAll();
    }

    @GetMapping("/summary")
    public Map<String, Long> summary() {
        return centreService.getSummary();
    }

    // Team contract: facilities that are not AVAILABLE, in the shared 7-field format
    @GetMapping("/pending")
    public List<PendingItemDto> pending() {
        return centreService.getPendingFacilities().stream().map(PendingItemDto::from).toList();
    }

    // Team contract: PUT /api/centres/{id}/status  body {"status":"UNDER_REPAIR","remarks":"..."}
    // {id} = FACILITY id (the same id /pending gave out)
    @PutMapping("/{id}/status")
    public PendingItemDto updatePendingStatus(@PathVariable Long id, @RequestBody StatusUpdateDto body) {
        return PendingItemDto.from(centreService.updateFacilityStatus(id, body.status(), body.remarks()));
    }

    @GetMapping("/{id}")
    public HealthCentre getById(@PathVariable Long id) {
        return centreService.getById(id);
    }

    @GetMapping("/{id}/facilities")
    public List<Facility> facilities(@PathVariable Long id) {
        return centreService.getFacilities(id);
    }

    // NEW
    @GetMapping("/{id}/workers")
    public List<HealthWorker> workers(@PathVariable Long id) {
        return centreService.getActiveWorkers(id);
    }

    @PostMapping
    public HealthCentre create(@RequestBody HealthCentre centre) {
        return centreService.save(centre);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        centreService.delete(id);
    }

    @PutMapping("/facilities/{id}/status")
    public Facility updateStatus(@PathVariable Long id, @RequestParam FacilityStatus status) {
        return centreService.updateFacilityStatus(id, status);
    }
}