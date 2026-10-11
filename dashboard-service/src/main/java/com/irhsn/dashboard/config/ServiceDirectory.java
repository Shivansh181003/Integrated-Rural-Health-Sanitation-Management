package com.irhsn.dashboard.config;

import com.irhsn.dashboard.dto.ServiceInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

// The "phone book": every service the dashboard reads from.
// To add/remove a service, change ONE line in the list.
@Component
public class ServiceDirectory {

    private final List<ServiceInfo> services;

    public ServiceDirectory(@Value("${services.residents.url}") String residents,
                            @Value("${services.centres.url}") String centres,
                            @Value("${services.medicines.url}") String medicines,
                            @Value("${services.sanitation.url}") String sanitation,
                            @Value("${services.water.url}") String water,
                            @Value("${services.camps.url}") String camps,
                            @Value("${services.appointments.url}") String appointments) {
        this.services = List.of(
                //              key             label                    url           api path             approve     reject
                new ServiceInfo("residents",    "Residents & Patients",  residents,    "/api/residents",    "ASSIGNED", "DONE"),
                new ServiceInfo("centres",      "Health Centres",        centres,      "/api/centres",      "UNDER_REPAIR", "AVAILABLE"),
                new ServiceInfo("medicines",    "Medicine & Inventory",  medicines,    "/api/medicines",    "APPROVED", "REJECTED"),
                new ServiceInfo("sanitation",   "Sanitation",            sanitation,   "/api/sanitation",   "ASSIGNED", "RESOLVED"),
                new ServiceInfo("water",        "Water & Public Health", water,        "/api/water",        "ASSIGNED", "RESOLVED"),
                new ServiceInfo("camps",        "Health Camps",          camps,        "/api/camps",        "REFERRED", "MISSED"),
                new ServiceInfo("appointments", "Appointments",          appointments, "/api/appointments", "COMPLETED", "CANCELLED")
        );
    }

    // All services
    public List<ServiceInfo> all() {
        return services;
    }

    // Find one by key, e.g. "medicines" (null if not found)
    public ServiceInfo find(String key) {
        return services.stream().filter(s -> s.key().equals(key)).findFirst().orElse(null);
    }
}