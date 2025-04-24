package com.apptechnosoft.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.apptechnosoft.constant.AppointmentStatus;
import com.apptechnosoft.dto.AppointmentDTO;
import com.apptechnosoft.model.Appointment;
import com.apptechnosoft.model.Doctor;
import com.apptechnosoft.repository.AppointmentRepository;
import com.apptechnosoft.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Override
	public Appointment addAppointment(Appointment appointment) {
		
		return appointmentRepository.save(appointment);
	}

	@Override
	public List<Appointment> getAllAppointment() {
		return appointmentRepository.findAll();
	}

	@Override
	public void deleteAppointmentById(int id) {
		appointmentRepository.deleteById(id);
		
	}

	@Override
	public Appointment getAppointmentById(int id) {
		return appointmentRepository.findById(id).get();
	}

	@Override
	public void updateAppointment(Appointment appointment) {

		appointmentRepository.save(appointment);
	}

	@Override
	public List<Appointment> getByPatientId(int id) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByPatientId(id);
	}

	@Override
	public List<Appointment> getByDate(LocalDate date) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByDate(date);
	}

	@Override
	public List<Appointment> getByDoctorAndDateGreaterThan(Doctor doctor) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByDoctorAndDateGreaterThan(doctor,LocalDate.now());
	}

	@Override
	public List<Appointment> getByDateAndDoctor(LocalDate date, Doctor doctor) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByDateAndDoctor(date, doctor);
	}

	@Override
	public Appointment getByBookedAt(LocalDateTime datetime) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByBookedAt(datetime);
	}

	@Override
	public List<Appointment> getByDoctor(Doctor doctor) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByDoctor(doctor);
	}

	@Override
	public List<Appointment> getByAppointmentStatusAndDate(AppointmentStatus appointmentStatus,LocalDate date) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByAppointmentStatusAndDate(appointmentStatus, date);
	}

	@Override
	public List<Appointment> getTop5ByDateDesc() {
		// TODO Auto-generated method stub
		return appointmentRepository.findTop5ByOrderByDateDesc();
	}

	@Override
	public List<Appointment> getByAppointmentStatus(AppointmentStatus appointmentStatus) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByAppointmentStatus(appointmentStatus);
	}

	@Override
	public List<Appointment> getByDoctorAndDateBefore(Doctor doctor, LocalDate date) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByDoctorAndDateBefore(doctor, date);
	}

	@Override
	public List<Appointment> getByAppointmentStatusAndDateAndDoctor(AppointmentStatus status, LocalDate date,
			Doctor doctor) {
		// TODO Auto-generated method stub
		return appointmentRepository.findByAppointmentStatusAndDateAndDoctor(status, date, doctor);
	}

	@Override
	public Page<Appointment> getAllOrderByStatusBookedOnTopAndDateDesc(Pageable pageable) {
		// TODO Auto-generated method stub
		return appointmentRepository.findAllOrderByStatusBookedOnTopAndDateDesc(pageable);
	}
	
	public List<AppointmentDTO> getAllAppointmentsDTO(List<Appointment> appointments) {
	    return appointments.stream().map(appt -> {
	        AppointmentDTO dto = new AppointmentDTO();
	        dto.setAppointmentId(appt.getAppointmentId());
	        dto.setPatientName(appt.getPatient().getName());
	        dto.setPatientMobileNo(appt.getPatient().getMobileNo());
	        dto.setDoctorName(appt.getDoctor().getFullName());
	        dto.setDate(appt.getDate().toString());
	        dto.setTime(appt.getTime());
	        dto.setRoomNo(appt.getDoctor().getRoomNo());
	        dto.setStatus(appt.getAppointmentStatus().toString());
	        return dto;
	    }).toList();
	}

	@Override
	public Page<Appointment> getAllOrderByStatusBookedOnTopAndDateDescWithSearch(String keyword,Pageable pageable) {
		// TODO Auto-generated method stub
		return appointmentRepository.findAllOrderByStatusBookedOnTopAndDateDescWithSearch(keyword, pageable);
	}


}
