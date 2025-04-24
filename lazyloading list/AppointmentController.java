package com.apptechnosoft.controller.backend;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apptechnosoft.constant.AppointmentStatus;
import com.apptechnosoft.constant.SlotStatus;
import com.apptechnosoft.constant.UserRole;
import com.apptechnosoft.model.Appointment;
import com.apptechnosoft.model.Doctor;
import com.apptechnosoft.model.DoctorSchedule;
import com.apptechnosoft.model.Patient;
import com.apptechnosoft.model.ShiftDetail;
import com.apptechnosoft.security.AppUser;
import com.apptechnosoft.security.AppUserService;
import com.apptechnosoft.service.AppointmentService;
import com.apptechnosoft.service.DoctorScheduleService;
import com.apptechnosoft.service.DoctorService;
import com.apptechnosoft.service.PatientService;
import com.apptechnosoft.service.ShiftDetailService;
import com.apptechnosoft.utils.MailUtils;

@Controller
public class AppointmentController {
	@Autowired
	private PatientService patientService;

	@Autowired
	ShiftDetailService shiftDetailService;

	@Autowired
	DoctorService doctorService;

	@Autowired
	DoctorScheduleService doctorScheduleService;

	@Autowired
	AppointmentService appointmentService;

	   @Autowired
	    private PasswordEncoder passwordEncoder;
	   
	   @Autowired
	   AppUserService appUserService;
	   @Autowired
	   MailUtils mailUtils;

	@GetMapping("/booking")
	public String booking(Model model, @RequestParam int shiftDetailId, @RequestParam int doctorId, @RequestParam int doctorScheduleId) {
		model.addAttribute("shiftDetailModel",shiftDetailService.getShiftDetailById(shiftDetailId));
		model.addAttribute("doctor",doctorService.getDoctorById(doctorId));
		model.addAttribute("doctorScheduleModel",doctorScheduleService.getDoctorScheduleById(doctorScheduleId));
		ShiftDetail shiftDetail = shiftDetailService.getShiftDetailById(shiftDetailId);
		//		shiftDetail.setSlotStatus(SlotStatus.PENDING);
		shiftDetailService.updateShiftDetail(shiftDetail);
		return "Booking3";

	}

	//	@RequestParam String transactionId,@RequestParam int doctorScheduleId,@RequestParam int shiftDetailId, Model model 
	//getmapping cannot handle model attribute(objects) and can only handle privitive parameters
	@PostMapping("/paymentMethod")
	public String payment(@ModelAttribute Patient patient,@RequestParam int doctorId, @RequestParam int shiftDetailId,
			@RequestParam int doctorScheduleId,
			Model model,
			@RequestParam LocalDate appointmentDate,
			@RequestParam String appointmentTime,
			@RequestParam String shift) {
		System.out.println(shiftDetailId);
		ShiftDetail shiftDetail = shiftDetailService.getShiftDetailById(shiftDetailId);
		if(shiftDetail.getSlotStatus().equals(SlotStatus.BOOKED)){
			LocalTime time = LocalTime.parse(shiftDetail.getStartTime().toString());
			String formattedTime = time.format(DateTimeFormatter.ofPattern("h:mm a"));
			return "redirect:/viewDoctor?doctorId=" + doctorId + "&bookingFailed=true" + "&time="+ URLEncoder.encode(formattedTime, StandardCharsets.UTF_8);		}

		Doctor doctor = doctorService.getDoctorById(doctorId);

		AppUser appUser = new AppUser();
		String fullName = patient.getName();
		String firstName = fullName.split(" ")[0];
//		String username = firstName + doctor.getNmcNo();
		appUser.setUsername(patient.getMobileNo());
		int randomNumber = (int)(Math.random() * 900000) + 100000; // Ensures a 6-digit number
		String password = firstName + randomNumber;
		appUser.setPassword(passwordEncoder.encode(password));
		appUser.setRole(UserRole.PATIENT);
		appUserService.addUser(appUser);
		patient.setAppUser(appUser);
		patientService.addPatient(patient);

String message = "Below are your Login credentials for the Kageshwori Hospital:\n" +
        "Username: " + patient.getMobileNo() + "\n" +
        "Password: " + password;
mailUtils.sendEmail(patient.getEmail(), "Login Details", message);
		


		
		//my old code
		Appointment appointment = new Appointment();
		appointment.setDate(appointmentDate); 
		appointment.setTime(appointmentTime); 
		appointment.setShift(shift);
		appointment.setBookedAt(LocalDateTime.now());
		appointment.setAppointmentStatus(AppointmentStatus.PENDING);
		appointment.setDoctor(doctor);
		appointment.setPatient(patient);
		
		
		
		
		appointmentService.addAppointment(appointment);
		
		if (patient.getAppointment() == null) {
			patient.setAppointment(new ArrayList<>());
		}
		patient.getAppointment().add(appointment);
		System.out.println(appointment);
		System.out.println(appointment);
		System.out.println(appointment);
		System.out.println(appointment);
		System.out.println(appointment);
		//		Patient savedPatient =patientService.addPatient(patient);
		//    DoctorSchedule doctorSchedule = doctorScheduleService.getDoctorScheduleById(doctorScheduleId);
		//        shiftDetail.setSlotStatus(SlotStatus.PENDING);
		//        doctorSchedule.setSlots(doctorSchedule.getSlots() -1);        
		//        shiftDetailService.updateShiftDetail(shiftDetail);
		//        doctorScheduleService.updateDoctorSchedule(doctorSchedule);
		//        System.out.println(patient);
		//        model.addAttribute("patient", patient);
		//      return "redirect:/TicketDownload?id=" + savedPatient.getId();
		//end of old code

		//code for payment page
		model.addAttribute("patientModel", patient);
		model.addAttribute("appointmentId", appointment.getAppointmentId());
		model.addAttribute("fee", doctor.getConsultationFee());
		model.addAttribute("doctorScheduleId",doctorScheduleId);
		model.addAttribute("shiftDetailId",shiftDetailId);
		model.addAttribute("doctorId", doctorId);


		return "PaymentMethod";
	}


	//	@PostMapping("/patient-save")
	//	public String savepatient(@ModelAttribute Patient patient,@RequestParam int doctorId, @RequestParam int shiftDetailId,
	//			@RequestParam int doctorScheduleId,Model model,
	//			@RequestParam LocalDate appointmentDate,
	//            @RequestParam String appointmentTime,
	//            @RequestParam String shift) {
	//		Appointment appointment = new Appointment();
	//	    appointment.setDate(appointmentDate); 
	//	    appointment.setTime(appointmentTime); 
	//	    appointment.setShift(shift);
	//	    appointment.setBookedAt(LocalDateTime.now());
	//	    appointment.setAppointmentStatus(AppointmentStatus.BOOKED);
	//	    appointment.setDoctor(doctorService.getDoctorById(doctorId));
	//	    appointment.setPatient(patient);
	//	    
	//	    if (patient.getAppointment() == null) {
	//	        patient.setAppointment(new ArrayList<>());
	//	    }
	//	    patient.getAppointment().add(appointment);
	//	    
	//	   
	//		Patient savedPatient =patientService.addPatient(patient);
	//	 // Retrieve the shiftDetail based on the shiftDetailId
	//    ShiftDetail shiftDetail = shiftDetailService.getShiftDetailById(shiftDetailId);
	//    DoctorSchedule doctorSchedule = doctorScheduleService.getDoctorScheduleById(doctorScheduleId);
	//  
	//        shiftDetail.setSlotStatus(SlotStatus.BOOKED);
	//        
	//        doctorSchedule.setSlots(doctorSchedule.getSlots() -1);        
	//        // Save the updated shiftDetail
	//        shiftDetailService.updateShiftDetail(shiftDetail);
	//        doctorScheduleService.updateDoctorSchedule(doctorSchedule);
	//        
	//        System.out.println(patient);
	//        
	////        model.addAttribute("patient", patient);
	//        return "redirect:/TicketDownload?id=" + savedPatient.getId();
	//	}





	@GetMapping("/TicketDownload")
	public String getTicket(@RequestParam int appointmentId, Model model) {
	
		model.addAttribute("appointment",appointmentService.getAppointmentById(appointmentId));
		
		return "TicketDownload";

	}

	@GetMapping("/appointmentList")
	public String appointmentLIst(Model model) {
//		model.addAttribute("appointmentList", appointmentService.getAllAppointment());
//		System.out.println(appointmentService.getAllAppointment());
		return "AppointmentList";
	}
	/*
	 * @GetMapping("/appointment-statistics") public String
	 * appointmentStatistics(Model model) { model.addAttribute("doctorsList",
	 * doctorService.getAllDoctor());
	 * 
	 * return "AppointmentStatistics"; }
	 */

	@GetMapping("/appointment-statistics")
	public String getAppointmentStatistics(@RequestParam(required = false) Integer doctorId, Model model) {
		// Add the list of doctors for the dropdown
		List<Doctor> doctorsList = doctorService.getAllDoctor();
		model.addAttribute("doctorsList", doctorsList);

		// If a doctor is selected, fetch appointments and calculate monthly counts
		if (doctorId != null) {
			Doctor doctor = doctorService.getDoctorById(doctorId);
			if (doctor != null) {
				model.addAttribute("doctorModel", doctor);
				List<Appointment> appointments = appointmentService.getByDoctor(doctor);
				List<Long> monthlyCounts = getMonthlyCountsList(appointments);
				model.addAttribute("monthlyCounts", monthlyCounts);
				int todayAppointmentSize=appointmentService.getByDateAndDoctor(LocalDate.now(), doctor).size();
				long yearlyAppointmentSize=appointments.stream().filter(a -> a.getDate().getYear() == LocalDate.now().getYear()).count();
				model.addAttribute("todayAppointments",todayAppointmentSize) ;
				model.addAttribute("todayRevenue", todayAppointmentSize * doctor.getConsultationFee());
				model.addAttribute("totalAppointments",appointments.size());
				model.addAttribute("totalRevenue",appointments.size()*doctor.getConsultationFee());
				model.addAttribute("yearAppointments",yearlyAppointmentSize); 
				model.addAttribute("yearlyRevenue",yearlyAppointmentSize* doctor.getConsultationFee());
			}

		}
		return "AppointmentStatistics"; // Thymeleaf template name
	}

	// Handle POST request from the form submission
	@PostMapping("/appointment-statistics")
	public String postAppointmentStatistics(@RequestParam("doctorId") int doctorId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("doctorId", doctorId);
		return "redirect:/appointment-statistics";
	}

	// Helper method to calculate monthly appointment counts for the current year
	private List<Long> getMonthlyCountsList(List<Appointment> appointments) {
		int currentYear = LocalDate.now().getYear();
		Map<Month, Long> countsByMonth = appointments.stream()
				.filter(a -> a.getDate() != null && a.getDate().getYear() == currentYear)
				.collect(Collectors.groupingBy(
						a -> a.getDate().getMonth(),
						Collectors.counting()
						));

		// Create a list of counts for all 12 months (January to December)
		List<Long> monthlyCounts = new ArrayList<>(12);
		for (Month month : Month.values()) {
			monthlyCounts.add(countsByMonth.getOrDefault(month, 0L));
		}
		return monthlyCounts;
	}

	@GetMapping("/today-appointment-list")
	public String todayList(Model model) {
		model.addAttribute("doctorList" ,doctorService.getByAppointments_Date(LocalDate.now()));
//		model.addAttribute("appointmentList", appointmentService.getAllAppointment());
		return "TodayAppointmentList";
	}
	
	@PostMapping("/today-appointment-list")
	public String postlist(Model model,@RequestParam int doctorId) {
		model.addAttribute("doctorList" ,doctorService.getByAppointments_Date(LocalDate.now()));
		model.addAttribute("appointmentList", appointmentService.getByAppointmentStatusAndDateAndDoctor(AppointmentStatus.BOOKED, LocalDate.now(), doctorService.getDoctorById(doctorId)));
		return "TodayAppointmentList";
	}
	
}

