package com.apptechnosoft.controller.frontend;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.apptechnosoft.webSocket.ContactUsHandler;
import com.apptechnosoft.webSocket.NotificationHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.apptechnosoft.constant.AppointmentStatus;
import com.apptechnosoft.constant.ContactUsStatus;
import com.apptechnosoft.constant.HomeServiceStatus;
import com.apptechnosoft.constant.Language;
import com.apptechnosoft.constant.NotificationStatus;
import com.apptechnosoft.constant.Shift;
import com.apptechnosoft.constant.SlotStatus;
import com.apptechnosoft.constant.VacancyStatus;
import com.apptechnosoft.controller.backend.DoctorController;
import com.apptechnosoft.controller.backend.MainController;
import com.apptechnosoft.model.Applicant;
import com.apptechnosoft.model.Appointment;
import com.apptechnosoft.model.Contact;
import com.apptechnosoft.model.Department;
import com.apptechnosoft.model.Doctor;
import com.apptechnosoft.model.DoctorSchedule;
import com.apptechnosoft.model.Employee;
import com.apptechnosoft.model.Enquiry;
import com.apptechnosoft.model.HealthPackage;
import com.apptechnosoft.model.HomeService;
import com.apptechnosoft.model.Notification;
import com.apptechnosoft.model.Patient;
import com.apptechnosoft.model.ShiftDetail;
import com.apptechnosoft.model.Specialization;
import com.apptechnosoft.service.ApplicantService;
import com.apptechnosoft.service.AppointmentService;
import com.apptechnosoft.service.CareerService;
import com.apptechnosoft.service.ContactService;
import com.apptechnosoft.service.DepartmentService;
import com.apptechnosoft.service.DoctorScheduleService;
import com.apptechnosoft.service.DoctorService;
import com.apptechnosoft.service.EmployeeService;
import com.apptechnosoft.service.EnquiryService;
import com.apptechnosoft.service.HealthPackageService;
import com.apptechnosoft.service.HomeServiceService;
import com.apptechnosoft.service.NewsService;
import com.apptechnosoft.service.NotificationService;
import com.apptechnosoft.service.PatientService;
import com.apptechnosoft.service.ShiftDetailService;
import com.apptechnosoft.service.SpecializationService;

@Controller
@RequestMapping("/user")
public class FrontEndController {


    private final DoctorController doctorController;

	@Autowired
	EmployeeService employeeService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	DoctorService doctorService;
	@Autowired
	SpecializationService specializationService;
	@Autowired
	HealthPackageService healthPackageService;
	@Autowired
	DoctorScheduleService doctorScheduleService;
	@Autowired
	ShiftDetailService shiftDetailService;
	@Autowired
	PatientService patientService;
	@Autowired
	AppointmentService appointmentService;
	@Autowired
	HomeServiceService homeServiceService;
	@Autowired
	NotificationService notificationService;
	@Autowired
	NotificationHandler notificationHandler;
	@Autowired
	CareerService careerService;
	@Autowired
	NewsService newsService;
	@Autowired
	ApplicantService applicantService;
	@Autowired
	EnquiryService enquiryService;
	@Autowired
	ContactService contactService;
	
	@Autowired
	ContactUsHandler contactUsHandler;

    FrontEndController(DoctorController doctorController) {
        this.doctorController = doctorController;
    }

	@GetMapping("/about")
	private String getAbout() {
		return "frontend/About";
	}
	@GetMapping("/contact")
	private String getContact() {
		return "frontend/contact";
	}
	@GetMapping("/department")
	private String getDepartment(Model model) {
		model.addAttribute("specializationsList",specializationService.getAllSpecialization());
		return "frontend/Department";
	}
	@GetMapping("view-specialization")
	private String viewSpecialization(Model model,@RequestParam int specializationId) {
		Specialization specialization = specializationService.getSpecializationById(specializationId);
		model.addAttribute("specializationModel",specialization);
		model.addAttribute("doctorsList", doctorService.getBySpecialization(specialization));
		return "frontend/Department-detail";
	}
	@GetMapping("/health-package")
	private String getHealthPackage(Model model) {
		model.addAttribute("healthPackagesList",healthPackageService.getAllHealthPackage());
		return "frontend/health-package";
	}
	@GetMapping("/opd-appointment")
	private String getOpdAppointment() {
		return "frontend/opd-appointment";
	}
	@GetMapping("/home")
	private String getIndex(Model model) {
		model.addAttribute("healthPackagesList",healthPackageService.getAllHealthPackage());
		model.addAttribute("specializationsList" , specializationService.getAllSpecialization());
		return "redirect:/";
	}
	//	@GetMapping("/find-doctor")
	//	private String getFindDoctor(Model model) {
	//		model.addAttribute("specializationsList", specializationService.getAllSpecialization());
	//		model.addAttribute("languagesList", Language.values());
	//		return "frontend/DoctorFind";
	//	}

	@GetMapping("/find-doctor")
	private String findDoctor(Model model,@RequestParam(required=false) Integer specializationId, 
			@RequestParam(required=false) String gender, @RequestParam(required=false) 
	Language language,@RequestParam(required = false) String name, 
	@RequestParam(required = false, defaultValue = "speciality") String activeTab) {
		if(specializationId!=null) {
			model.addAttribute("doctorsList", doctorService.getBySpecializationAndLanguageAndGender(specializationService.getSpecializationById(specializationId), language, gender));
		}
		if(name!=null) {
			model.addAttribute("doctorsList", doctorService.getBySimilarName(name));
			System.out.println("list" + doctorService.getBySimilarName(name));
			System.out.println("list" + doctorService.getBySimilarName(name));
			System.out.println("name" + name);
		}
		model.addAttribute("specializationsList", specializationService.getAllSpecialization());
		model.addAttribute("languagesList", Language.values());
		model.addAttribute("activeTab", activeTab);  // Pass the tab to the frontend

		return "frontend/DoctorFind";
	}
	@GetMapping("/career")
	private String getCareer(Model model) {
		model.addAttribute("careersList", careerService.getByVacancyStatus(VacancyStatus.ACTIVE));
		return "frontend/career";
	} 
	@GetMapping("/teamList")
	public String getEmployeeListByDepartment(@RequestParam("departmentId") int departmentId, Model model) {
		Department department = departmentService.getDepartmentById(departmentId);
		List<Employee> employeeList = employeeService.getByDepartment(department);
		List<Doctor> doctorList= doctorService.getByDepartment(department);
		List<Object> teamList = new ArrayList<>();
		teamList.addAll(doctorList);
		teamList.addAll(employeeList);
		model.addAttribute("teamList", teamList);
		model.addAttribute("department", department.getName());
		System.out.println("/teamlist called");
		System.out.println(employeeList);
		return "frontend/TeamList";
	}


	@GetMapping("/employeeProfile")
	public String getProfile(@RequestParam int id,Model model,@RequestParam String type) {
		if("doctor".equals(type)) {
			model.addAttribute("employeeModel", doctorService.getDoctorById(id));
		}
		else {
			model.addAttribute("employeeModel", employeeService.getEmployeeById(id));
		}

		return "frontend/EmployeeProfile";
	}

	@GetMapping("/heli-ambulance")
	private String getHeliAmbulance() {
		return "frontend/heliAmbulance";
	}
	//	@GetMapping("/view-doctor")
	//	private String getdoctorview(Model model, @RequestParam int doctorId) {
	//		model.addAttribute("doctorModel", doctorService.getDoctorById(doctorId));
	//
	//
	//		List<DoctorSchedule> weekScheduleList= doctorScheduleService.getWeekScheduleByDoctorId(doctorId);
	//		weekScheduleList.forEach(schedule -> {
	//			Map<Shift, List<ShiftDetail>> groupedShiftDetails = schedule.getShiftDetail().stream()
	//					.filter(shiftDetail -> shiftDetail.getSlotStatus() != SlotStatus.BOOKED && shiftDetail.getSlotStatus() !=SlotStatus.PENDING) 
	//					.collect(Collectors.groupingBy(ShiftDetail::getShift));
	//			schedule.setGroupedShiftDetails(groupedShiftDetails);
	//		});
	//		model.addAttribute("weekScheduleList",weekScheduleList);
	//		model.addAttribute("todayDate" , LocalDate.now());
	//		System.out.println(weekScheduleList);
	//
	//		return "frontend/Doctor-detail";
	//	}
	@GetMapping("/view-doctor")
	private String getdoctorview(Model model, @RequestParam int doctorId) {
		model.addAttribute("doctorModel", doctorService.getDoctorById(doctorId));

		List<DoctorSchedule> weekScheduleList = doctorScheduleService.getWeekScheduleByDoctorId(doctorId);

		List<Shift> shiftOrder = List.of(Shift.MORNING, Shift.AFTERNOON, Shift.EVENING, Shift.NIGHT); // Define order

		weekScheduleList.forEach(schedule -> {
			// Filter booked/pending slots and group by shift
			Map<Shift, List<ShiftDetail>> groupedShiftDetails = schedule.getShiftDetail().stream()
					.filter(shiftDetail -> shiftDetail.getSlotStatus() != SlotStatus.BOOKED 
					&& shiftDetail.getSlotStatus() != SlotStatus.PENDING)
					.collect(Collectors.groupingBy(ShiftDetail::getShift));

			// Sort the map based on predefined shift order
			Map<Shift, List<ShiftDetail>> sortedGroupedShiftDetails = groupedShiftDetails.entrySet().stream()
					.sorted(Comparator.comparingInt(e -> shiftOrder.indexOf(e.getKey()))) // Sort by predefined order
					.collect(Collectors.toMap(
							Map.Entry::getKey, 
							Map.Entry::getValue, 
							(oldValue, newValue) -> oldValue, 
							LinkedHashMap::new // Maintain insertion order
							));

			schedule.setGroupedShiftDetails(sortedGroupedShiftDetails); // Set sorted shifts
		});

		model.addAttribute("weekScheduleList", weekScheduleList);
		model.addAttribute("todayDate", LocalDate.now());

		return "frontend/Doctor-detail";
	}


	@GetMapping("/appointment-form")
	private String appointmentForm(Model model, @RequestParam int shiftDetailId, @RequestParam int doctorId, @RequestParam int doctorScheduleId) {
		model.addAttribute("shiftDetailModel",shiftDetailService.getShiftDetailById(shiftDetailId));
		model.addAttribute("doctor",doctorService.getDoctorById(doctorId));
		model.addAttribute("doctorScheduleModel",doctorScheduleService.getDoctorScheduleById(doctorScheduleId));
		return "frontend/appointment-form";
	}

	@PostMapping("/paymentMethod")
	public String payment(@ModelAttribute Patient patient,@RequestParam int doctorId, @RequestParam int shiftDetailId,
			@RequestParam int doctorScheduleId,
			Model model,
			@RequestParam LocalDate appointmentDate,
			@RequestParam String appointmentTime,
			@RequestParam String shift) {


		ShiftDetail shiftDetail = shiftDetailService.getShiftDetailById(shiftDetailId);
		if(shiftDetail.getSlotStatus().equals(SlotStatus.BOOKED)){
			LocalTime time = LocalTime.parse(shiftDetail.getStartTime().toString());
			String formattedTime = time.format(DateTimeFormatter.ofPattern("h:mm a"));
			return "redirect:/user/view-doctor?doctorId=" + doctorId + "&bookingFailed=true" + "&time="+ URLEncoder.encode(formattedTime, StandardCharsets.UTF_8);		}

		System.out.println(shiftDetailId);
		Doctor doctor = doctorService.getDoctorById(doctorId);



		//my old code
		Appointment appointment = new Appointment();
		appointment.setDate(appointmentDate); 
		appointment.setTime(appointmentTime); 
		appointment.setShift(shift);
		appointment.setBookedAt(LocalDateTime.now());
		appointment.setAppointmentStatus(AppointmentStatus.PENDING);
		appointment.setDoctor(doctor);
		appointment.setPatient(patient);
		if (patient.getAppointment() == null) {
			patient.setAppointment(new ArrayList<>());
		}
		patient.getAppointment().add(appointment);
		patientService.addPatient(patient);
		//		Patient savedPatient =patientService.addPatient(patient);
		//    ShiftDetail shiftDetail = shiftDetailService.getShiftDetailById(shiftDetailId);
		//    DoctorSchedule doctorSchedule = doctorScheduleService.getDoctorScheduleById(doctorScheduleId);
		//        shiftDetail.setSlotStatus(SlotStatus.PENDING);
		//        doctorSchedule.setSlots(doctorSchedule.getSlots() -1);        
		//        shiftDetailService.updateShiftDetail(shiftDetail);
		//        doctorScheduleService.updateDoctorSchedule(doctorSchedule);
		System.out.println(patient);
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


		return "frontend/PaymentMethod";
	}

	@GetMapping("/patient-already")
	private String alreadyAPatient(@RequestParam int doctorId, @RequestParam int shiftDetailId,
			@RequestParam int doctorScheduleId,Model model) {
		model.addAttribute("patientList",patientService.getAllPatient());
		model.addAttribute("bookingModel",shiftDetailService.getShiftDetailById(shiftDetailId));
		model.addAttribute("doctor",doctorService.getDoctorById(doctorId));
		model.addAttribute("bookingDate",doctorScheduleService.getDoctorScheduleById(doctorScheduleId));
		model.addAttribute("doctorScheduleId",doctorScheduleId);
		model.addAttribute("shiftDetailId",shiftDetailId);

		return "frontend/patient-already";
	}

	@PostMapping("/patient-already")
	private String postpatient(@RequestParam int doctorId, @RequestParam int shiftDetailId,
			@RequestParam int doctorScheduleId,@RequestParam int patientId,Model model)
	{
		System.out.println(patientId);
		System.out.println(patientId);
		System.out.println(patientId);
		System.out.println(patientId);
		System.out.println(patientId);
		System.out.println(patientId);
		System.out.println(patientId);
		System.out.println(patientId);
		System.out.println(patientId);
		System.out.println(patientId);

		ShiftDetail shiftDetail = shiftDetailService.getShiftDetailById(shiftDetailId);

		if(shiftDetail.getSlotStatus().equals(SlotStatus.BOOKED)){
			LocalTime time = LocalTime.parse(shiftDetail.getStartTime().toString());
			String formattedTime = time.format(DateTimeFormatter.ofPattern("h:mm a"));
			return "redirect:/user/view-doctor?doctorId=" + doctorId + "&bookingFailed=true" + "&time="+ URLEncoder.encode(formattedTime, StandardCharsets.UTF_8);		}

		Patient patient = patientService.getPatientById(patientId);
		Appointment appointment = new Appointment();
		appointment.setAppointmentStatus(AppointmentStatus.PENDING);
		appointment.setBookedAt(LocalDateTime.now());
		appointment.setDate(doctorScheduleService.getDoctorScheduleById(doctorScheduleId).getDate());
		appointment.setDoctor(doctorService.getDoctorById(doctorId));
		appointment.setPatient(patient);
		appointment.setShift(shiftDetail.getShift().toString());
		appointment.setTime(shiftDetail.getStartTime().toString());
		if (patient.getAppointment() == null) {
			patient.setAppointment(new ArrayList<>());
		}
		patient.getAppointment().add(appointment);
		patientService.updatePatient(patient);

		//		    DoctorSchedule doctorSchedule = doctorScheduleService.getDoctorScheduleById(doctorScheduleId);

		//		        shiftDetail.setSlotStatus(SlotStatus.PENDING);

		//		        doctorSchedule.setSlots(doctorSchedule.getSlots() -1);        
		// Save the updated shiftDetail
		//		        shiftDetailService.updateShiftDetail(shiftDetail);
		//		        doctorScheduleService.updateDoctorSchedule(doctorSchedule);

		//code for payment page


		model.addAttribute("patientModel", patient);
		model.addAttribute("appointmentId", appointmentService.getByBookedAt(appointment.getBookedAt()).getAppointmentId());
		model.addAttribute("fee", doctorService.getDoctorById(doctorId).getConsultationFee());
		model.addAttribute("doctorScheduleId",doctorScheduleId);
		model.addAttribute("shiftDetailId",shiftDetailId);
		model.addAttribute("doctorId", doctorId);

		//		return "redirect:/viewPatient?patientId=" + patientId;
		//		        return "redirect:/redirectWithNewTab?patientId=" + patientId;
		//        return "redirect:/TicketDownload?id=" + patientId;
		return "frontend/PaymentMethod";

	}
	@PostMapping("/patient-save")
	public String savepatient(@RequestParam int appointmentId, @RequestParam int shiftDetailId,@RequestParam int doctorScheduleId) {
		Appointment appointment = appointmentService.getAppointmentById(appointmentId);
		appointment.setAppointmentStatus(AppointmentStatus.BOOKED);
		appointmentService.updateAppointment(appointment);


		// Retrieve the shiftDetail based on the shiftDetailId
		ShiftDetail shiftDetail = shiftDetailService.getShiftDetailById(shiftDetailId);
		DoctorSchedule doctorSchedule = doctorScheduleService.getDoctorScheduleById(doctorScheduleId);

		shiftDetail.setSlotStatus(SlotStatus.BOOKED);

		doctorSchedule.setSlots(doctorSchedule.getSlots() -1);        
		// Save the updated shiftDetail
		shiftDetailService.updateShiftDetail(shiftDetail);
		doctorScheduleService.updateDoctorSchedule(doctorSchedule);
		System.out.println(appointment);
//		return "redirect:/user/TicketDownload?id=" + appointment.getPatient().getId();
		return "redirect:/user/TicketDownload?appointmentId=" + appointment.getAppointmentId();
	}


//	@GetMapping("/TicketDownload")
//	public String getTicket(@RequestParam int id, Model model) {
//		System.out.println(id);
//		model.addAttribute("patient",patientService.getPatientById(id));
//		System.out.println("from TicketDownoad");
//		System.out.println(patientService.getPatientById(id));
//		return "frontend/TicketDownload";
//
//	}
	@GetMapping("/TicketDownload")
	public String getTicket(@RequestParam int appointmentId, Model model) {
	
		model.addAttribute("appointment",appointmentService.getAppointmentById(appointmentId));
		System.out.println("from TicketDownoad");
	
		return "frontend/TicketDownload";

	}

	@GetMapping("/health-package-detail")
	public String healthPackageDetail(@RequestParam int healthPackageId,Model model) {

		model.addAttribute("healthPackageModel", healthPackageService.getHealthPackageById(healthPackageId));
		return "frontend/health-packageDetail";

	}



	@GetMapping("/package-checkout-form")
	public String packageCheckout(Model model, @RequestParam List<Integer> healthPackageId) {
		List<HealthPackage> healthPackageList = new ArrayList<>();
		for(Integer i : healthPackageId) {
			healthPackageList.add(healthPackageService.getHealthPackageById(i));
		}
		model.addAttribute("healthPackageList", healthPackageList);
		model.addAttribute("healthPackageIdList", healthPackageId);
		System.out.println(healthPackageId);
		double amount = 0;
		for(HealthPackage h : healthPackageList) {
			if(h.getDiscount() > 0) {
				amount+=h.getDiscountedPrice();
			}
			else {
				amount+=h.getPrice();
			}
		}
		model.addAttribute("total", amount);
		return "frontend/package-checkout-form"; // Adjust to your actual Thymeleaf template name
	}

	@GetMapping("/package-patient-already")
	private String alreadyAPatientPackage(@RequestParam List<Integer> healthPackageId,Model model) {
		System.out.println(healthPackageId);
		System.out.println(healthPackageId);
		System.out.println(healthPackageId);
		System.out.println(healthPackageId);
		List<HealthPackage> healthPackageList = new ArrayList<>();
		for(Integer i : healthPackageId) {
			healthPackageList.add(healthPackageService.getHealthPackageById(i));
		}
		model.addAttribute("healthPackageList", healthPackageList);
		model.addAttribute("healthPackageIdList", healthPackageId);
		model.addAttribute("patientList",patientService.getAllPatient());

		double amount = 0;
		for(HealthPackage h : healthPackageList) {
			if(h.getDiscount() > 0) {
				amount+=h.getDiscountedPrice();
			}
			else {
				amount+=h.getPrice();
			}
		}
		model.addAttribute("total", amount);

		return "frontend/package-patient-already";
	}


	@PostMapping("/package-payment-method")
	public String packagePaymentMethod(@ModelAttribute Patient patient,Model model,@RequestParam List<Integer> healthPackageId,@RequestParam(required=false) Integer patientId,@RequestParam(required=false) String alreadyAPatient) {
		//		System.out.println("healthPackageids"+healthPackageId);
		//
		//		if (patient.getHealthPackage() == null) {
		//			patient.setHealthPackage(new ArrayList<>());
		//		}
		//		double amount = 0;
		//		for(Integer i : healthPackageId) {
		//			HealthPackage healthPackage = healthPackageService.getHealthPackageById(i);
		//			if( healthPackage.getDiscount() > 0) {
		//				amount+=healthPackage.getDiscountedPrice();
		//			}
		//			else {
		//				amount+=healthPackage.getPrice();
		//			}
		//			patient.getHealthPackage().add(healthPackage);
		//		}
		//		patientService.addPatient(patient);
		//		model.addAttribute("fee",amount);


		double amount = 0;
		for(HealthPackage h : patient.getHealthPackage()) {
			if(h.getDiscount() > 0) {
				amount+=h.getDiscountedPrice();
			}
			else {
				amount+=h.getPrice();
			}
		}


		model.addAttribute("fee",amount);
		model.addAttribute("healthPackageId",healthPackageId);
		model.addAttribute("patientModel",patient);
		model.addAttribute("alreadyAPatient",alreadyAPatient);
		if(patientId!=null) {
			System.out.println(patient);
			Patient patient2 =patientService.getPatientById(patientId);
			System.out.println(patient2);
			model.addAttribute("patientModel",patient2);
		}
		return "frontend/package-payment-method";
	}

	@PostMapping("/package-patient-save")
	public String savepatientwithPackage(@ModelAttribute Patient patient,@RequestParam List<Integer> healthPackageId,@RequestParam(required=false) String alreadyAPatient,@RequestParam(required=false) Integer id) {
		//logic for already a patient

		if("true".equals(alreadyAPatient) && id != null) {
			Patient patient1 = patientService.getPatientById(id);
			//without health pacakages
			if(patient1.getHealthPackage() == null) {
				patient1.setHealthPackage(new ArrayList<>());
				for(int i : healthPackageId) {
					patient1.getHealthPackage().add(healthPackageService.getHealthPackageById(i));
					patientService.updatePatient(patient1);				

					addHomeService(patient,healthPackageService.getHealthPackageById(i));
				}
			}
			else {
				//with healthPackages
				if(patient1.getHealthPackage() !=null) {
					for(int i : healthPackageId) {
						patient1.getHealthPackage().add(healthPackageService.getHealthPackageById(i));
						patientService.updatePatient(patient1);
						addHomeService(patient,healthPackageService.getHealthPackageById(i));
					}
				}
			}
		}
		else {
			//below logic is for new patient
			patientService.addPatient(patient);
			for(Integer i : healthPackageId) {
				addHomeService(patient,healthPackageService.getHealthPackageById(i));
			}
		}
		//		clear the session storage after redirecting
		return "redirect:/user/home?bookingSuccess=true";
	}

	//	helper method for add home Service
	private void addHomeService(Patient patient, HealthPackage healthPackage) {
		HomeService homeService = new HomeService();
		homeService.setHealthPackage(healthPackage);
		homeService.setPatient(patient);
		homeService.setPurchaseDate(LocalDateTime.now());
		homeService.setStatus(HomeServiceStatus.PENDING);
		homeServiceService.addHomeService(homeService);

		Notification notification = new Notification();
		notification.setHomeService(homeService);
		notification.setMessage("A "+ healthPackage.getName() + " package has been purchased. Please assign a team." );
		notification.setStatus(NotificationStatus.UNSEEN);
		notificationService.addNotification(notification);
		//add notification logic here

		try {
			System.out.println("trying broadcast notification");
			notificationHandler.broadcastNotification(notification.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@GetMapping("/view-news")
	private String viewNews(@RequestParam int newsId, Model model) {
		model.addAttribute("newsModel", newsService.getNewsById(newsId));

		return "frontend/NewsView";
	}

	@GetMapping("/career-form")
	private String careerForm(Model model, @RequestParam int careerId) {
		model.addAttribute("careerId", careerId);
		return"frontend/career-form";

	}

	@PostMapping("/career-form")
	private String careerForm(Model model,@ModelAttribute Applicant applicant ,@RequestParam int careerId, @RequestParam(required=false) MultipartFile pdf) {
		if(!pdf.isEmpty()) {
			try {
				Files.copy(pdf.getInputStream(), 
						Path.of("src/main/resources/static/files/applicantsCV/"+ pdf.getOriginalFilename()), 
						StandardCopyOption.REPLACE_EXISTING);
				applicant.setFileName(pdf.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		applicant.setCareer(careerService.getCareerById(careerId));
		applicantService.addApplicant(applicant);
		return"redirect:/";

	}

	@PostMapping("/enquiry")
	private String enquiry(@ModelAttribute Enquiry enquiry) {
		enquiryService.addEnquiry(enquiry);
		return "redirect:/user/view-doctor?doctorId=" + enquiry.getDoctor().getDoctorId();

	}
	
	@PostMapping("/contact")
	private String contact(@ModelAttribute Contact contact) {
		contact.setContactUsDateTime(LocalDateTime.now());
		contact.setStatus(ContactUsStatus.UNSEEN);
		contactService.addContact(contact);
	
		try {
			System.out.println("trying  contact us broadcast notification");
			contactUsHandler.broadcastMessage(contact.getMessage(), contact.getFirstName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/user/contact";

	}
	
	
	
	@GetMapping("/view-cart")
	private String viewCart() {
		return"frontend/ViewCart";

	}



}
