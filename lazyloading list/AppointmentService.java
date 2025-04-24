package com.apptechnosoft.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.apptechnosoft.constant.AppointmentStatus;
import com.apptechnosoft.dto.AppointmentDTO;
import com.apptechnosoft.model.Appointment;
import com.apptechnosoft.model.Doctor;

public interface AppointmentService {
	
	Appointment addAppointment(Appointment appointment);
	List<Appointment> getAllAppointment();
	void deleteAppointmentById(int id);
	Appointment getAppointmentById(int id);
	void updateAppointment(Appointment appointment);
	
	List<Appointment> getByPatientId(int id);
	List<Appointment> getByDate(LocalDate date);
	List<Appointment> getByDoctorAndDateGreaterThan(Doctor doctor);
	List<Appointment> getByDateAndDoctor(LocalDate date, Doctor doctor);
	Appointment getByBookedAt(LocalDateTime datetime);
	List<Appointment> getByDoctor(Doctor doctor);
	List<Appointment> getByAppointmentStatusAndDate(AppointmentStatus appointmentStatus, LocalDate date);
	List<Appointment> getTop5ByDateDesc();
	List<Appointment> getByAppointmentStatus(AppointmentStatus appointmentStatus);
    List<Appointment> getByDoctorAndDateBefore(Doctor doctor, LocalDate date);
    List<Appointment> getByAppointmentStatusAndDateAndDoctor(AppointmentStatus status, LocalDate date, Doctor doctor);
	Page<Appointment> getAllOrderByStatusBookedOnTopAndDateDesc(Pageable pageable);
	List<AppointmentDTO> getAllAppointmentsDTO(List<Appointment> appointments);




}
