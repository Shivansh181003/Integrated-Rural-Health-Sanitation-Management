package com.irhsm.A.CService.client;

import com.irhsm.A.CService.dto.CentreDto;
import com.irhsm.A.CService.dto.WorkerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class CentreClient {

    private final RestTemplate restTemplate;
    private final String centreUrl;

    public CentreClient(RestTemplate restTemplate,
                        @Value("${services.centre.url}") String centreUrl) {
        this.restTemplate = restTemplate;
        this.centreUrl = centreUrl;
    }

    public List<CentreDto> getCentres() {
        try {
            CentreDto[] centres = restTemplate.getForObject(centreUrl + "/api/centres", CentreDto[].class);
            return centres == null ? List.of() : Arrays.asList(centres);
        } catch (RestClientException e) {
            throw new IllegalStateException("Health Centre service is not reachable");
        }
    }

    public CentreDto getCentre(Long id) {
        try {
            return restTemplate.getForObject(centreUrl + "/api/centres/" + id, CentreDto.class);
        } catch (RestClientException e) {
            throw new IllegalStateException("Centre " + id + " not found or Centre service is down");
        }
    }

    public List<WorkerDto> getWorkers(Long centreId) {
        try {
            WorkerDto[] workers = restTemplate.getForObject(
                    centreUrl + "/api/centres/" + centreId + "/workers", WorkerDto[].class);
            return workers == null ? List.of() : Arrays.asList(workers);
        } catch (RestClientException e) {
            throw new IllegalStateException("Could not load workers from Centre service");
        }
    }
}