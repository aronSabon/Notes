package com.apptechnosoft.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.apptechnosoft.constant.AppointmentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int appointmentId;

	@ManyToOne
	private Doctor doctor;
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate date;
	private String time;
	private String shift;
	private LocalDateTime bookedAt;
	@Enumerated(EnumType.STRING)
	private AppointmentStatus appointmentStatus;
	@ManyToOne
	private Patient patient;
	
	
	@Override
	public String toString() {
		return "Appointment [apointmentId=" + appointmentId + ", date=" + date + ", time=" + time
				+ ", shift=" + shift + ", bookedAt=" + bookedAt + ", appointmentStatus=" + appointmentStatus + "]";
	}
}
