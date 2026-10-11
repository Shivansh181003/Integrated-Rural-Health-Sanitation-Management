package com.irhsm.A.CService.repository;

import com.irhsm.A.CService.entity.Appointment;
import com.irhsm.A.CService.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByOrderByAppointmentDateDescTokenNoAsc();
    List<Appointment> findByStatusIn(List<AppointmentStatus> statuses);
    long countByStatus(AppointmentStatus status);
    long countByCentreIdAndAppointmentDate(Long centreId, LocalDate date);
}