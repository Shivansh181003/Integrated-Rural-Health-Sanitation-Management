package com.irhsm.A.CService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Resident ID is required")
    private Long residentId;

    @NotBlank(message = "Resident name is required")
    private String residentName;

    @NotNull(message = "Choose a centre")
    private Long centreId;

    private String centreName;
    private String village;

    @NotNull(message = "Choose a health worker")
    private Long workerId;

    private String workerName;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date cannot be in the past")
    private LocalDate appointmentDate;

    @NotNull(message = "Time is required")
    private LocalTime timeSlot;

    private Integer tokenNo;

    @NotBlank(message = "Reason is required")
    private String reason;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}