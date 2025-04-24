package com.apptechnosoft.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apptechnosoft.model.Appointment;
import com.apptechnosoft.model.Doctor;
import com.apptechnosoft.model.Patient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.apptechnosoft.constant.AppointmentStatus;




public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{
	List<Appointment> findByPatientId(int id);
	
    List<Appointment> findByDate(LocalDate date);
    List<Appointment> findByDoctorAndDateGreaterThan(Doctor doctor, LocalDate date);
    List<Appointment> findByDoctorAndDateBefore(Doctor doctor, LocalDate date);
    List<Appointment> findByDateAndDoctor(LocalDate date, Doctor doctor);
Appointment  findByBookedAt(LocalDateTime bookedAt);
List<Appointment> findByDoctor(Doctor doctor);
List<Appointment> findByAppointmentStatusAndDate(AppointmentStatus appointmentStatus, LocalDate date);
List<Appointment> findTop5ByOrderByDateDesc();
List<Appointment> findByAppointmentStatus(AppointmentStatus appointmentStatus);
List<Appointment> findByAppointmentStatusAndDateAndDoctor(AppointmentStatus status, LocalDate date, Doctor doctor);
@Query("SELECT a FROM Appointment a ORDER BY " +
	       "CASE WHEN a.appointmentStatus = 'BOOKED' THEN 0 ELSE 1 END, " +
	       "a.date DESC")
	Page<Appointment> findAllOrderByStatusBookedOnTopAndDateDesc(Pageable pageable);


//@Query("SELECT a FROM Appointment a WHERE " +
//	       "LOWER(a.patient.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//	       "LOWER(a.doctor.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//	Page<Appointment> findAllOrderByStatusBookedOnTopAndDateDescWithSearch(@Param("keyword") String keyword, Pageable pageable);

@Query("SELECT a FROM Appointment a WHERE " +
	       "LOWER(a.patient.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
	       "LOWER(a.doctor.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
	       "ORDER BY " +
	       "CASE WHEN a.appointmentStatus = 'BOOKED' THEN 0 ELSE 1 END, " +
	       "a.date DESC")
	Page<Appointment> findAllOrderByStatusBookedOnTopAndDateDescWithSearch(@Param("keyword") String keyword, Pageable pageable);


}
